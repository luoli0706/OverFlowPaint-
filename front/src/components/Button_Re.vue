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

const emits = defineEmits(['reset']);

// 存储初始棋盘状态
const initialGrid = ref(null);
const hasInitialState = ref(false);

// 监听props变化，初始化或更新棋盘状态
watch(() => props.currentGrid, (newGrid) => {
  // 如果还没有初始状态，或者游戏已经获胜，则记录新状态
  if (!initialGrid.value || props.isVictory) {
    // 使用深拷贝确保状态隔离
    initialGrid.value = JSON.parse(JSON.stringify(newGrid));
    hasInitialState.value = true;
  }
}, { immediate: true });

// 基于种子的随机数生成器
const randomFromSeed = (seed) => {
  const x = Math.sin(seed++) * 10000
  return x - Math.floor(x)
}

// 重置棋盘方法
const resetBoard = () => {
  const newGrid = []
  for (let y = 0; y < 8; y++) {
    const row = []
    for (let x = 0; x < 10; x++) {
      const localSeed = props.seed + x * y + initialGrid.value
      const state = Math.floor(randomFromSeed(localSeed) * 4)
      row.push(state)
    }
    newGrid.push(row)
  }
  emits('reset', newGrid);
};
</script>

<style scoped>
button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
