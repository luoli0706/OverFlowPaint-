<template>
  <div class="custom-chess">
    <!-- 右上方的 Play Custom 按钮 -->
    <button
        class="play-custom-btn"
        @click="goToPlayCustom"
    >
      Play Custom
    </button>
    <div class="flex gap-8">
      <!-- 左侧棋盘 -->
      <div class="board-container">
        <ChessBoard
            :selectedColor="selectedColor"
            :initialGrid="grid"
            @cell-clicked="handleCellClicked"
        />
      </div>
      <!-- 右侧颜色选择按钮和记录按钮 -->
      <div class="controls-container">
        <div class="color-picker">
          <Button_CMYK @color-selected="handleColorSelected" />
        </div>
        <button
            class="record-btn"
            @click="recordBoard"
        >
          记录棋盘
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import ChessBoard from './ChessBoard.vue';
import Button_CMYK from './Buttons_CMYK.vue';

const emits = defineEmits(['go-to-play-custom']);

// 初始化 10*8 的棋盘
const grid = ref([
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
]);

const selectedColor = ref(null);

const handleColorSelected = (colorIndex) => {
  selectedColor.value = colorIndex;
};

const handleCellClicked = (row, col) => {
  if (selectedColor.value !== null) {
    grid.value[row][col] = selectedColor.value;
  }
};

const recordBoard = () => {
  const boardJson = JSON.stringify(grid.value);
  console.log('Recorded board JSON:', boardJson);
  localStorage.setItem('customBoard', boardJson);
};

const goToPlayCustom = () => {
  emits('go-to-play-custom');
};
</script>

<style scoped>
.custom-chess {
  padding: 2rem;
}

.play-custom-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 10px 20px;
  background-color: #6c79b8;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.flex {
  display: flex;
}

.gap-8 {
  gap: 2rem;
}

.board-container {
  flex-shrink: 0;
}

.controls-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.color-picker {
  margin-bottom: 20px;
}

.record-btn {
  padding: 10px 20px;
  background-color: #6c79b8;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
