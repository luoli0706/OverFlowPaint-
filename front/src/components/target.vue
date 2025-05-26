<script setup>
import { ref, computed, onMounted } from "vue";

// 初始化为null，等待初始化信号
const target_color = ref(null);

// 颜色映射表
const colorMap = {
  0: { english: "Cyan", chinese: "青色" },
  1: { english: "Magenta", chinese: "品红" },
  2: { english: "Yellow", chinese: "黄色" },
  3: { english: "Black", chinese: "黑色" }
};

// 计算显示文本
const displayText = computed(() => {
  if (target_color.value === null) return "等待初始化...";
  const color = colorMap[target_color.value];
  return `将全部色彩染成：${color.chinese}(${color.english})`;
});

// 初始化函数（接收外部信号调用）
const initialize = () => {
  target_color.value = (Date.now() * 3 + 2) % 4;
  console.log("初始化完成:", target_color.value);
};

// 刷新函数（接收外部信号调用）
const refresh = () => {
  if (target_color.value !== null) {
    target_color.value = (Date.now() * 5 + 3) % 4; // 使用不同算法确保值变化
    console.log("刷新完成:", target_color.value);
  } else {
    console.warn("请先初始化!");
  }
};

// 接收事件示例（使用自定义事件总线或props）
onMounted(() => {

});
</script>

<template>
  <div>
    <input type="text" :value="displayText" readonly/>

  </div>
</template>

<style scoped>
</style>
