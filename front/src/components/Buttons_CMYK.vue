<template>
  <div class="color-buttons">
    <button
        v-for="(color, index) in colors"
        :key="index"
        :class="{ 'selected': selectedColor === index }"
        :style="{ backgroundColor: color.value }"
        @click="selectColor(index)"
    >
      {{ color.name }}
    </button>
  </div>
</template>


<script setup>
import { ref, defineEmits } from 'vue'

// 定义组件向外触发的事件
const emits = defineEmits(['color-selected'])

// 定义CMYK颜色模型的四种基础颜色
const colors = [
  { name: 'Cyan', value: 'cyan' },
  { name: 'Magenta', value: 'magenta' },
  { name: 'Yellow', value: 'yellow' },
  { name: 'Black', value: 'black' }
]

// 存储当前选中的颜色索引
const selectedColor = ref(null)

/**
 * 处理颜色选择事件
 * @param {number} index - 被选中的颜色索引
 */
const selectColor = (index) => {
  // 更新选中的颜色索引
  selectedColor.value = index
  // 触发自定义事件通知父组件
  emits('color-selected', index)
}
</script>

<style scoped>
/* 颜色选择器容器样式 */
.color-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-left: 20px;
}

/* 颜色按钮基础样式 */
button {
  width: 100px;
  height: 40px;
  border: none;
  border-radius: 4px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s; /* 悬停动画效果 */
}

/* 按钮悬停状态样式 */
button:hover {
  transform: scale(1.05); /* 轻微放大效果 */
}

/* 当前选中按钮的样式 */
button.selected {
  box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.3); /* 添加高亮边框 */
}

/* 根据颜色对比度设置文本颜色，确保可读性 */
button:nth-child(1) { color: #333; } /* Cyan颜色较亮，使用深色文本 */
button:nth-child(2) { color: white; } /* Magenta颜色较暗，使用白色文本 */
button:nth-child(3) { color: #333; } /* Yellow颜色较亮，使用深色文本 */
button:nth-child(4) { color: white; } /* Black颜色为黑色，使用白色文本 */
</style>