<template>
  <!-- 棋盘组件：渲染可交互的CMYK颜色格子 -->
  <div class="chess-board" @click="handleClick">
    <!-- 渲染棋盘行 -->
    <div
        v-for="(row, rowIndex) in grid"
        :key="rowIndex"
        class="row"
    >
      <!-- 渲染棋盘列 -->
      <div
          v-for="(cell, colIndex) in row"
          :key="colIndex"
          :class="`cell state-${cell}`"
          :style="{ backgroundColor: getColor(cell) }"
      ></div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, defineProps, defineEmits, watch} from 'vue'

// 定义CMYK颜色映射表
const COLORS = {
  0: 'cyan',
  1: 'magenta',
  2: 'yellow',
  3: 'black'
}

// 定义组件接收的props
const props = defineProps({
  rows: {
    type: Number,
    default: 8
  },
  cols: {
    type: Number,
    default: 10
  },
  selectedColor: {
    type: Number,
    default: null
  },
  initialGrid: {
    type: Array,
    default: () => []
  }
})

// 定义组件触发的事件
const emits = defineEmits(['cell-clicked'])

// 棋盘数据，初始化为props传入的初始值或空数组
const grid = ref(props.initialGrid.length > 0 ? props.initialGrid : [])

/**
 * 监听 initialGrid 变化，更新棋盘数据
 * 使用深拷贝确保响应式更新，同时保留原有随机生成逻辑
 */
watch(() => props.initialGrid, (newGrid) => {
  // 使用深拷贝确保响应式更新，同时保留原有随机生成逻辑
  if (newGrid && newGrid.length > 0) {
    console.log('收到新棋盘数据:', newGrid)
    grid.value = JSON.parse(JSON.stringify(newGrid))
  } else {
    console.log('initialGrid 为空，使用原有数据')
  }
}, {deep: true, immediate: true}) // immediate:true确保初始化时执行

/**
 * 初始化棋盘数据
 * 如果有初始数据则使用，否则随机生成
 */
const initGrid = () => {
  if (props.initialGrid.length > 0) {
    // 已通过watch处理初始化
    return
  }

  const newGrid = []
  const seed = Date.now()

  // 基于种子生成随机棋盘
  for (let y = 0; y < props.rows; y++) {
    const row = []
    for (let x = 0; x < props.cols; x++) {
      const localSeed = seed + x + y
      const state = Math.floor(randomFromSeed(localSeed) * 4)
      row.push(state)
    }
    newGrid.push(row)
  }

  grid.value = newGrid
}

/**
 * 基于种子的伪随机数生成器
 * 相同的种子将生成相同的随机数序列
 * @param {number} seed - 随机数种子
 * @returns {number} 0-1之间的随机数
 */
const randomFromSeed = (seed) => {
  const x = Math.sin(seed++) * 10000
  return x - Math.floor(x)
}

/**
 * 根据状态值获取对应的颜色
 * @param {number} state - 颜色状态值(0-3)
 * @returns {string} 颜色名称
 */
const getColor = (state) => {
  return COLORS[state]
}

/**
 * 处理棋盘点击事件
 * @param {MouseEvent} e - 鼠标事件
 */
const handleClick = (e) => {
  // 获取点击的单元格元素
  const cell = e.target.closest('.cell')
  if (!cell) return

  // 计算点击位置的行列索引
  const colIndex = Array.from(cell.parentNode.children).indexOf(cell)
  const rowIndex = Array.from(cell.parentNode.parentNode.children).indexOf(cell.parentNode)

  // 如果有选中的颜色，使用选中的颜色，否则循环切换
  const newState = props.selectedColor !== null
      ? props.selectedColor
      : (grid.value[rowIndex][colIndex] + 1) % 4

  // 触发事件通知父组件
  emits('cell-clicked', rowIndex, colIndex, grid.value[rowIndex][colIndex], newState)
}

// 组件挂载后初始化棋盘
onMounted(() => {
  initGrid()
})
</script>

<style scoped>
/* 棋盘容器样式 */
.chess-board {
  width: 500px;
  height: 400px;
  display: grid;
  grid-template-rows: repeat(8, 1fr);
  grid-template-columns: repeat(10, 1fr);
  gap: 2px;
  background-color: #ccc;
}

/* 行样式 */
.row {
  display: contents;
}

/* 单元格基础样式 */
.cell {
  transition: background-color 0.3s ease;
}

/* 四种颜色状态的样式 */
.state-0 {
  background-color: cyan;
}

.state-1 {
  background-color: magenta;
}

.state-2 {
  background-color: yellow;
}

.state-3 {
  background-color: black;
}
</style>
