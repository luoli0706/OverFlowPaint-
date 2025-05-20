<template>
  <div>
    <!-- 使用 props.components 渲染按钮 -->
    <button
        v-for="(comp, index) in props.components"
        :key="index"
        :style="{ backgroundColor: getButtonColor(index) }"
        @click="handleSwitch(comp)"
    >
      切换到 {{ comp.name }}
    </button>
    <img :src="currentImage" alt="Chess Image" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, defineEmits, defineProps } from 'vue';

const props = defineProps(['components']);
const emit = defineEmits(['switch-component']);

const currentColorIndex = ref(0);
const COLORS = ['cyan', 'magenta', 'yellow', 'black'];
let colorInterval;

// 新增：当前显示的图片路径
const currentImage = ref(require('../public/Main.png'));

// 每秒切换一次颜色
const startColorChange = () => {
  colorInterval = setInterval(() => {
    currentColorIndex.value = (currentColorIndex.value + 1) % COLORS.length;
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

const handleSwitch = (comp) => {
  emit('switch-component', comp); // 触发切换事件
  // 根据切换的组件更新图片路径
  if (comp.name === 'RandomChess') {
    currentImage.value = require('../public/Random.png');
  } else if (comp.name === 'LevelChess') {
    currentImage.value = require('../public/Level.png');
  } else if (comp.name === 'CustomChess') {
    currentImage.value = require('../public/Custom.png');
  }
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

img {
  margin-top: 20px;
  max-width: 100%;
  height: auto;
}
</style>
