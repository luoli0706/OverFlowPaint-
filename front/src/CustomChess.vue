<template>
  <div>
    <h1>Custom Chess</h1>
    <Exit @exit="handleExit" />
  </div>
</template>

<script setup>
import Exit from './components_2/ExitButton.vue';

const handleExit = () => {
  // 返回到SwitchView
  window.location.reload();
};
</script>

<style scoped>
/* 可以添加自定义样式 */
</style>
