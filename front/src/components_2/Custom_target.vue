<template>
  <div>
    <label for="target-color">选择获胜目标颜色 (C, M, Y, K):</label>
    <input type="text" id="target-color" v-model="inputColor" @input="handleInput">
    <p v-if="target_color!== null">当前目标颜色索引: {{ target_color }}</p>
  </div>
</template>

<script setup>
import { ref ,defineExpose} from 'vue';

const inputColor = ref('');
const target_color = ref(null);
defineExpose({target_color});
const colorMap = {
  'C': 0,
  'M': 1,
  'Y': 2,
  'K': 3
};

const handleInput = () => {
  const upperCaseValue = inputColor.value.toUpperCase();
  if (colorMap[upperCaseValue]) {
    target_color.value = colorMap[upperCaseValue];
  } else {
    target_color.value = null;
  }
};
</script>

<style scoped>
/* 可以添加自定义样式 */
</style>
