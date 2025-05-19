<template>
  <div class="step-counter">
    <div class="relative">
      <input
          type="text"
          v-model="displayText"
          class="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-lg text-lg font-medium text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
          readonly
      />
      <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        <i class="fa fa-shoe-prints text-blue-500 text-xl"></i>
      </div>
    </div>
    <button
        @click="incrementSteps"
        class="mt-4 w-full py-3 bg-blue-500 hover:bg-blue-600 text-white font-medium rounded-lg transition-all transform hover:scale-105 active:scale-95 flex items-center justify-center gap-2"
    >
      <i class="fa fa-plus-circle"></i>
      <span>增加步数</span>
    </button>
  </div>
</template>

<script setup>
import {  computed, defineProps, defineEmits } from 'vue';

// 移除错误的导入，StepCounter不需要导入其他组件
// 之前的错误导入：
// import ChessBoard from './components/ChessBoard.vue'
// import Button_CMYK from './components/Buttons_CMYK.vue'
// 等...

// 定义接收的props
const props = defineProps({
  steps: {
    type: Number,
    default: 0
  }
});

// 定义触发的事件
const emits = defineEmits(['update:steps']);

// 计算属性用于格式化显示文本
const displayText = computed(() => `当前使用的步数为：${props.steps}`);

// 增加步数的方法
const incrementSteps = () => {
  emits('update:steps', props.steps + 1);
};
</script>

<style scoped>
/* 文本框样式 */
input {
  padding-left: 3rem;
}
</style>

<style scoped>
/* 全局动画效果 */
.fade-in {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>