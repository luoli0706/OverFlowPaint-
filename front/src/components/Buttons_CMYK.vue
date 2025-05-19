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

const emits = defineEmits(['color-selected'])

const colors = [
  { name: 'Cyan', value: 'cyan' },
  { name: 'Magenta', value: 'magenta' },
  { name: 'Yellow', value: 'yellow' },
  { name: 'Black', value: 'black' }
]

const selectedColor = ref(null)

const selectColor = (index) => {
  selectedColor.value = index
  emits('color-selected', index)
}
</script>

<style scoped>
.color-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-left: 20px;
}

button {
  width: 100px;
  height: 40px;
  border: none;
  border-radius: 4px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s;
}

button:hover {
  transform: scale(1.05);
}

button.selected {
  box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.3);
}

button:nth-child(1) { color: #333; } /* Cyan */
button:nth-child(2) { color: white; } /* Magenta */
button:nth-child(3) { color: #333; } /* Yellow */
button:nth-child(4) { color: white; } /* Black */
</style>