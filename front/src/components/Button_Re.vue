<template>
  <button
      class="w-full py-3 bg-red-500 text-white rounded-md hover:bg-red-600 transition-colors flex items-center justify-center gap-2"
      @click="resetBoard"
      :disabled="!hasInitialState"
  >
    <i class="fa fa-refresh"></i>
    <span>重置棋盘</span>
  </button>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';

// 定义组件接收的props
const props = defineProps({
  currentGrid: {
    type: Array,
    required: true
  },
  isVictory: {
    type: Boolean,
    default: false
  },
  seed: {
    type: Number,
    required: true
  }
});

// 定义组件触发的事件
const emits = defineEmits(['reset']);

// 存储棋盘的初始状态，用于重置
const initialGrid = ref(null);
// 标识是否已初始化棋盘状态
const hasInitialState = ref(false);

/**
 * 监听当前棋盘变化，初始化或更新棋盘状态
 * 当首次加载或游戏胜利时，记录当前棋盘状态
 */
watch(() => props.currentGrid, (newGrid) => {
  // 如果还没有初始状态，或者游戏已经获胜，则记录新状态
  if (!initialGrid.value || props.isVictory) {
    // 使用深拷贝确保状态隔离，避免引用问题
    initialGrid.value = JSON.parse(JSON.stringify(newGrid));
    hasInitialState.value = true;
  }
}, { immediate: true });

/**
 * 基于种子的伪随机数生成器
 * 相同的种子将生成相同的随机数序列
 * @param {number} seed - 随机数种子
 * @returns {number} 0-1之间的随机数
 */
const randomFromSeed = (seed) => {
  // 使用正弦函数生成伪随机数
  const x = Math.sin(seed++) * 10000
  // 返回小数部分作为随机结果
  return x - Math.floor(x)
}

/**
 * 重置棋盘方法
 * 基于种子生成与初始状态相似但不完全相同的新棋盘
 */
const resetBoard = () => {
  const newGrid = []
  // 遍历生成8行10列的新棋盘
  for (let y = 0; y < 8; y++) {
    const row = []
    for (let x = 0; x < 10; x++) {
      // 基于种子、坐标和初始状态计算局部种子
      const localSeed = props.seed + x * y + ( x + 1 ) * y+ ( y + 1 ) * x + initialGrid.value
      // 生成0-3之间的随机状态值（对应四种颜色）
      const state = Math.floor(randomFromSeed(localSeed) * 4)
      row.push(state)
    }
    newGrid.push(row)
  }
  // 触发reset事件，将新棋盘传递给父组件
  emits('reset', newGrid);
};
</script>

<style scoped>
button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
