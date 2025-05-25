<template>
  <div class="level-selector">
    <h2 class="text-xl font-bold mb-4">选择关卡</h2>
    <div class="grid grid-cols-2 sm:grid-cols-3 gap-4">
      <button
          v-for="(level, index) in levels"
          :key="index"
          :class="{'bg-blue-500 text-white': selectedLevel === index, 'bg-gray-200': selectedLevel !== index}"
          @click="selectLevel(index)"
      >
        关卡 {{ index + 1 }}
      </button>
    </div>
    <div class="mt-6">
      <h3 class="text-lg font-semibold mb-2">关卡预览</h3>
      <div v-if="selectedLevel !== null" class="board-preview bg-gray-100 p-3 rounded">
        <div class="grid grid-cols-10 gap-1">
          <div
              v-for="(row, rowIndex) in levels[selectedLevel]"
              :key="rowIndex"
              class="flex flex-col"
          >
            <div
                v-for="(cell, colIndex) in row"
                :key="colIndex"
                :class="{
                'w-6 h-6 rounded-sm': true,
                'bg-cyan-500': cell === 0,
                'bg-magenta-500': cell === 1,
                'bg-yellow-500': cell === 2,
                'bg-black': cell === 3
              }"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';

const emits = defineEmits(['level-selected']);

const levels = ref([
  // 关卡1：渐变棋盘
  [
    [0, 1, 2, 3, 0, 1, 2, 3, 0, 1],
    [1, 2, 3, 0, 1, 2, 3, 0, 1, 2],
    [2, 3, 0, 1, 2, 3, 0, 1, 2, 3],
    [3, 0, 1, 2, 3, 0, 1, 2, 3, 0],
    [0, 1, 2, 3, 0, 1, 2, 3, 0, 1],
    [1, 2, 3, 0, 1, 2, 3, 0, 1, 2],
    [2, 3, 0, 1, 2, 3, 0, 1, 2, 3],
    [3, 0, 1, 2, 3, 0, 1, 2, 3, 0]
  ],
  // 关卡2：对角棋盘
  [
    [0, 1, 2, 3, 0, 1, 2, 3, 0, 1],
    [1, 0, 1, 2, 3, 0, 1, 2, 3, 0],
    [2, 1, 0, 1, 2, 3, 0, 1, 2, 3],
    [3, 2, 1, 0, 1, 2, 3, 0, 1, 2],
    [0, 3, 2, 1, 0, 1, 2, 3, 0, 1],
    [1, 0, 3, 2, 1, 0, 1, 2, 3, 0],
    [2, 1, 0, 3, 2, 1, 0, 1, 2, 3],
    [3, 2, 1, 0, 3, 2, 1, 0, 1, 2]
  ],
  // 关卡3：棋盘格
  [
    [0, 1, 0, 1, 0, 1, 0, 1, 0, 1],
    [1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
    [0, 1, 0, 1, 0, 1, 0, 1, 0, 1],
    [1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
    [0, 1, 0, 1, 0, 1, 0, 1, 0, 1],
    [1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
    [0, 1, 0, 1, 0, 1, 0, 1, 0, 1],
    [1, 0, 1, 0, 1, 0, 1, 0, 1, 0]
  ],
  // 关卡4：同心圆
  [
    [3, 3, 3, 3, 3, 3, 3, 3, 3, 3],
    [3, 2, 2, 2, 2, 2, 2, 2, 2, 3],
    [3, 2, 1, 1, 1, 1, 1, 1, 2, 3],
    [3, 2, 1, 0, 0, 0, 0, 1, 2, 3],
    [3, 2, 1, 0, 0, 0, 0, 1, 2, 3],
    [3, 2, 1, 0, 0, 0, 0, 1, 2, 3],
    [3, 2, 1, 1, 1, 1, 1, 1, 2, 3],
    [3, 3, 3, 3, 3, 3, 3, 3, 3, 3]
  ],
  // 关卡5：螺旋
  [
    [0, 0, 0, 0, 0, 1, 1, 1, 1, 1],
    [0, 2, 2, 2, 0, 1, 3, 3, 3, 1],
    [0, 2, 3, 2, 0, 1, 3, 0, 3, 1],
    [0, 2, 3, 2, 0, 1, 3, 0, 3, 1],
    [0, 2, 3, 2, 0, 1, 3, 0, 3, 1],
    [0, 2, 3, 2, 0, 1, 3, 0, 3, 1],
    [0, 2, 3, 2, 0, 1, 3, 0, 3, 1],
    [0, 2, 2, 2, 2, 2, 2, 2, 3, 1]
  ],
  // 关卡6：随机分布
  [
    [0, 1, 2, 3, 0, 1, 2, 3, 0, 1],
    [1, 2, 0, 3, 1, 2, 0, 3, 1, 2],
    [2, 3, 1, 0, 2, 3, 1, 0, 2, 3],
    [3, 0, 2, 1, 3, 0, 2, 1, 3, 0],
    [0, 1, 3, 2, 0, 1, 3, 2, 0, 1],
    [1, 2, 0, 3, 1, 2, 0, 3, 1, 2],
    [2, 3, 1, 0, 2, 3, 1, 0, 2, 3],
    [3, 0, 2, 1, 3, 0, 2, 1, 3, 0]
  ]
]);

const selectedLevel = ref(null);

const selectLevel = (index) => {
  selectedLevel.value = index;
  emits('level-selected', levels.value[index]);
};
</script>

<style scoped>
.level-selector {
  padding: 1rem;
  background-color: #6c79b8;
  border-radius: 0.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

button {
  padding: 0.75rem;
  border: none;
  border-radius: 0.375rem;
  background-color: #0bc5ea;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover:not([disabled]) {
  background-color: #4299e1;
}

.board-preview {
  max-width: 300px;
}

.bg-cyan-500 {
  background-color: #0bc5ea;
}

.bg-magenta-500 {
  background-color: #ed64a6;
}

.bg-yellow-500 {
  background-color: #ecc94b;
}
</style>
