<template>
  <!-- 这里可以添加退出按钮的具体内容 -->
  <button
      :style="{ backgroundColor: getButtonColor(3) }"
      @click="handleExit"
  >
    退出
  </button>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { defineEmits } from 'vue';

const emit = defineEmits(['exit']);

const currentColorIndex = ref(0);
const COLORS = ['cyan', 'magenta', 'yellow', 'black'];
let colorInterval;

// 每秒切换一次颜色
const startColorChange = () => {
  colorInterval = setInterval(() => {
    currentColorIndex.value = (currentColorIndex.value + 3) % COLORS.length;
  }, 1500); // 10s 切换一次颜色
};

const stopColorChange = () => {
  clearInterval(colorInterval);
};

// 根据索引获取当前按钮的颜色
const getButtonColor = (index) => {
  const offset = index % COLORS.length;
  const colorIndex = (currentColorIndex.value + offset) % COLORS.length;
  return COLORS[colorIndex];
};

const handleExit = () => {
  emit('exit');
};

onMounted(() => {
  startColorChange();
});

onUnmounted(() => {
  stopColorChange();
});
</script>

<style scoped>
button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 10s ease; /* 10s 渐变效果 */
  margin: 5px;
}

button:hover {
  transform: scale(1.05);
}
</style>
