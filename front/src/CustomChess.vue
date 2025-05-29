<template>
  <div>
    <h1>Custom Chess</h1>
    <Custom_target ref="customTargetRef" />
    <LevelSelector @target_to_play="handleGoToPlayCustom" @level-selected="handleLevelSelected" @go-to-play-custom="handleGoToPlayCustom" />
    <Exit @exit="handleExit" />
  </div>
</template>

<script setup>
import {defineEmits, ref} from 'vue';
import Exit from './components_2/ExitButton.vue';
import LevelSelector from './components/LevelSelector.vue';
import PlayCustom from "./PlayCustom.vue";
import Custom_target from "@/components_2/Custom_target.vue";

const emits = defineEmits(['switch-component'],['target']);
const customTargetRef = ref(null);
const handleLevelSelected = (levelGrid) => {
  // 这里可以添加处理选择关卡的逻辑
  console.log('Selected level grid:', levelGrid);
};

const handleGoToPlayCustom = () => {
  if (!customTargetRef.value) {
    console.error('Custom_target 组件引用错误');
    return;
  }
  const targetColor = customTargetRef.value.target_color;
  if (targetColor === null) {
    alert('请输入有效的颜色代码 (C, M, Y, K)');
    return;
  }
  localStorage.setItem('targetColor', targetColor);
  setTimeout(() => {
    emits('switch-component', { name: 'PlayCustom', component: PlayCustom});
    emits('target',targetColor);
    console.log('target:', targetColor);
  }, 100); // 延迟 100ms
};



const handleExit = () => {
  emits('switch-component', null);
};
</script>

<style scoped>
/* 可以添加自定义样式 */
</style>
