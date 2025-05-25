<template>
  <div>
    <h1>Custom Chess</h1>
    <LevelSelector @level-selected="handleLevelSelected" @go-to-play-custom="handleGoToPlayCustom" />
    <Exit @exit="handleExit" />
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';
import Exit from './components_2/ExitButton.vue';
import LevelSelector from './components/LevelSelector.vue';
import PlayCustom from "./PlayCustom.vue";

const emits = defineEmits(['switch-component']);

const handleLevelSelected = (levelGrid) => {
  // 这里可以添加处理选择关卡的逻辑
  console.log('Selected level grid:', levelGrid);
};

const handleGoToPlayCustom = () => {
  emits('switch-component', {name: 'PlayCustom', component: PlayCustom});
};

const handleExit = () => {
  emits('switch-component', null);
};
</script>

<style scoped>
/* 可以添加自定义样式 */
</style>
