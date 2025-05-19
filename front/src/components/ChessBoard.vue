<template>
  <div class="chess-board" @click="handleClick">
    <div
        v-for="(row, rowIndex) in grid"
        :key="rowIndex"
        class="row"
    >
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

const COLORS = {
  0: 'cyan',
  1: 'magenta',
  2: 'yellow',
  3: 'black'
}

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

const emits = defineEmits(['cell-clicked'])

const grid = ref(props.initialGrid.length > 0 ? props.initialGrid : [])

// 监听 initialGrid 变化
watch(() => props.initialGrid, (newGrid) => {
  // 使用深拷贝确保响应式更新，同时保留原有随机生成逻辑
  if (newGrid && newGrid.length > 0) {
    console.log('收到新棋盘数据:', newGrid)
    grid.value = JSON.parse(JSON.stringify(newGrid))
  } else {
    console.log('initialGrid 为空，使用原有数据')
  }
}, { deep: true, immediate: true }) // immediate:true确保初始化时执行

// 初始化棋盘
const initGrid = () => {
  if (props.initialGrid.length > 0) {
    // 已通过watch处理初始化
    return
  }

  const newGrid = []
  const seed = Date.now()

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

// 基于种子的随机数生成器
const randomFromSeed = (seed) => {
  const x = Math.sin(seed++) * 10000
  return x - Math.floor(x)
}

// 获取颜色
const getColor = (state) => {
  return COLORS[state]
}

// 处理点击事件
const handleClick = (e) => {
  const cell = e.target.closest('.cell')
  if (!cell) return

  const colIndex = Array.from(cell.parentNode.children).indexOf(cell)
  const rowIndex = Array.from(cell.parentNode.parentNode.children).indexOf(cell.parentNode)

  // 如果有选中的颜色，使用选中的颜色，否则循环切换
  const newState = props.selectedColor !== null
      ? props.selectedColor
      : (grid.value[rowIndex][colIndex] + 1) % 4

  emits('cell-clicked', rowIndex, colIndex, grid.value[rowIndex][colIndex], newState)
}

onMounted(() => {
  initGrid()
})
</script>

<style scoped>
.chess-board {
  width: 500px;
  height: 400px;
  display: grid;
  grid-template-rows: repeat(8, 1fr);
  grid-template-columns: repeat(10, 1fr);
  gap: 2px;
  background-color: #ccc;
}

.row {
  display: contents;
}

.cell {
  transition: background-color 0.3s ease;
}

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
