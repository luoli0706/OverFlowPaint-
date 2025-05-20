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
    <div class="image-container">
      <img :src="currentImage" :class="getImageClass" alt="Chess Image" />
      <!-- 新增的图片 -->
      <img v-if="isMainPage" :src="extraImage" class="extra-image" alt="Extra Image" />
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted, defineEmits, defineProps, computed} from 'vue';

const props = defineProps(['components']);
const emit = defineEmits(['switch-component']);

const currentColorIndex = ref(0);
const COLORS = ['cyan', 'magenta', 'yellow', 'black'];
let colorInterval;

// 新增：当前显示的图片路径
const currentImage = ref(require('../public/Main.png'));
// 新增：额外图片的路径
const extraImage = ref(require('../public/Ning.png'));

// 每秒切换一次颜色
const startColorChange = () => {
  colorInterval = setInterval(() => {
    currentColorIndex.value = (currentColorIndex.value + 1) % COLORS.length;
  }, 1500); // 10s 切换一次颜色
};

const stopColorChange = () => {
  clearInterval(colorInterval);
};
// 计算属性判断是否为主页面
const isMainPage = computed(() => {
  return currentImage.value===require('../public/Main.png');
});
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
// 根据当前图片路径返回不同的类名
const getImageClass = () => {
  if (currentImage.value === require('../public/Random.png')) {
    return 'random-image';
  } else if (currentImage.value === require('../public/Level.png')) {
    return 'level-image';
  } else {
    return 'main-image';
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

.image-container {
  display: flex;
  align-items: start; /* 垂直对齐到顶部 */
  margin-top: 20px;
}

img {
  max-width: 100%;
  height: auto;
}

.main-image {
  transform: scale(0.7); /* 缩放图片为原来的 70% */
  transform-origin: top center; /* 设置缩放原点为顶部中心 */
}

.random-image {
  transform: scale(0.8); /* 缩放图片为原来的 80% */
  transform-origin: top left; /* 设置缩放原点为左上角 */
}

.level-image {
  transform: scale(0.9); /* 缩放图片为原来的 90% */
  transform-origin: top right; /* 设置缩放原点为右上角 */
}

.extra-image {
  transform: scale(0.6); /* 缩放额外图片为原来的 60% */
  transform-origin: top left; /* 设置缩放原点为左上角 */
  margin-left: 1px; /* 调整额外图片的左边距，使其紧贴当前图片 */
}
</style>
