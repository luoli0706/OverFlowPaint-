<template>
  <!-- 胜利弹窗组件 -->
  <div
      v-if="isOpen"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 transition-opacity duration-300"
      :class="{ 'opacity-0 pointer-events-none': !isOpen }"
  >
    <!-- 弹窗内容容器 -->
    <div
        class="bg-white rounded-lg shadow-xl p-6 max-w-md w-full transform transition-all duration-300"
        :class="{ 'scale-95 opacity-0': !isOpen, 'scale-100 opacity-100': isOpen }"
    >
      <div class="text-center">
        <!-- 胜利图标 -->
        <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-green-100 mb-4">
          <i class="fa fa-trophy text-3xl text-yellow-500"></i>
        </div>
        <h3 class="text-xl font-bold text-gray-900 mb-2">恭喜你获得胜利！</h3>
        <p class="text-gray-600 mb-4">你花费的步数为：{{ steps }}</p>
        <!-- 倒计时提示 -->
        <p class="text-sm text-gray-500 mb-6">将在 <span class="font-semibold text-red-500">{{ countdown }}</span> 秒后关闭并刷新界面</p>

        <!-- 操作按钮组 -->
        <div class="flex flex-col sm:flex-row gap-3">
          <button
              @click="closeModal"
              class="flex-1 px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 transition-colors"
          >
            退出游戏
          </button>
          <button
              @click="nextLevel"
              class="flex-1 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
          >
            进入下一关
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch,defineProps, defineEmits } from 'vue';

// 定义组件接收的props
const props = defineProps({
  steps: {
    type: Number,
    required: true
  },
  isVictory: {
    type: Boolean,
    default: false
  }
});

// 定义组件触发的事件
const emits = defineEmits(['close', 'next']);

// 弹窗状态
const isOpen = ref(false);
// 倒计时秒数
const countdown = ref(5);
// 倒计时定时器
let countdownTimer = null;

/**
 * 监听胜利状态变化
 * 当游戏胜利状态变为true时自动打开弹窗
 */
watch(() => props.isVictory, (newVal) => {
  if (newVal) {
    openModal();
  }
});

/**
 * 打开弹窗并启动倒计时
 */
const openModal = () => {
  isOpen.value = true;
  startCountdown();
};

/**
 * 关闭弹窗
 */
const closeModal = () => {
  stopCountdown();
  isOpen.value = false;
  emits('close');
};

/**
 * 进入下一关
 */
const nextLevel = () => {
  stopCountdown();
  isOpen.value = false;
  emits('next');
};

/**
 * 开始倒计时
 */
const startCountdown = () => {
  countdown.value = 5;
  countdownTimer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      nextLevel();
    }
  }, 1000);
};

/**
 * 停止倒计时
 */
const stopCountdown = () => {
  if (countdownTimer) {
    clearInterval(countdownTimer);
    countdownTimer = null;
  }
};

/**
 * 组件挂载时检查胜利状态
 */
onMounted(() => {
  if (props.isVictory) {
    openModal();
  }
});

/**
 * 组件卸载时清理定时器
 */
onUnmounted(() => {
  stopCountdown();
});
</script>

<style scoped>
/* 背景淡入淡出动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* 弹窗缩放动画 */
.scale-enter-active, .scale-leave-active {
  transition: transform 0.3s ease, opacity 0.3s ease;
}
.scale-enter-from, .scale-leave-to {
  transform: scale(0.95);
  opacity: 0;
}
</style>
