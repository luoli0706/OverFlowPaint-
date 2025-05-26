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

    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { levels } from './levels.js';

const emits = defineEmits(['level-selected']);

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
