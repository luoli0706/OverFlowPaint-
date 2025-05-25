<template>
  <div class="level-chess">
    <h1 class="text-3xl font-bold text-center mb-6">关卡模式</h1>

    <div class="flex flex-col md:flex-row gap-6 max-w-6xl mx-auto">
      <!-- 左侧：关卡选择器 -->
      <div class="w-full md:w-1/4">
        <LevelSelector @level-selected="handleLevelSelected" />
      </div>

      <!-- 右侧：游戏区域 -->
      <div class="w-full md:w-3/4" v-if="currentLevel">
        <div class="game-container bg-white rounded-xl shadow-lg overflow-hidden">
          <div class="game-header p-4 bg-indigo-600 text-white">
            <h2 class="text-xl font-bold">关卡 {{ currentLevelIndex + 1 }}</h2>
          </div>

          <div class="p-6">
            <div class="chess-board-container mb-6">
              <ChessBoard
                  :selectedColor="selectedColor"
                  :initialGrid="currentLevel"
                  @cell-clicked="handleCellClicked"
              />
            </div>

            <div class="controls-container grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="color-picker bg-gray-50 p-4 rounded-lg">
                <h3 class="text-lg font-semibold mb-3">选择颜色</h3>
                <Button_CMYK @color-selected="handleColorSelected" />
              </div>

              <div class="action-buttons bg-gray-50 p-4 rounded-lg">
                <h3 class="text-lg font-semibold mb-3">游戏操作</h3>
                <button
                    class="w-full py-2 bg-green-500 hover:bg-green-600 text-white font-medium rounded-lg mb-3"
                    @click="manualSendBoardToServer"
                    :disabled="!boardChanged"
                >
                  发送到服务器
                </button>
                <Button_Re
                    :currentGrid="currentLevel"
                    :isVictory="isBoardUniform"
                    :seed="initSeed"
                    @reset="handleResetBoard"
                />
              </div>

              <div class="stats-container bg-gray-50 p-4 rounded-lg">
                <h3 class="text-lg font-semibold mb-3">游戏统计</h3>
                <StepCounter v-model:steps="steps" />
                <div class="mt-4 p-3 bg-gray-100 rounded-md">
                  <p class="text-sm" v-if="lastClickedCell">
                    <span class="font-medium">最后操作：</span>
                    ({{ lastClickedCell.row }}, {{ lastClickedCell.col }}) - {{ getColorName(lastClickedCell.color) }}
                  </p>
                  <p class="text-sm text-gray-500 italic" v-else>点击棋盘开始游戏</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 胜利弹窗 -->
    <VictoryModal
        :steps="steps"
        :isVictory="isBoardUniform"
        @close="handleCloseGame"
        @next="handleNextLevel"
    />

    <Exit @exit="handleExit" class="mt-6" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import ChessBoard from './components/ChessBoard.vue';
import Button_CMYK from './components/Buttons_CMYK.vue';
import Button_Re from './components/Button_Re.vue';
import StepCounter from './components/StepCounter.vue';
import VictoryModal from './components/VictoryModal.vue';
import Exit from './components_2/ExitButton.vue';
import LevelSelector from './components_2/LevelSelector.vue';
// 从 LevelSelector 导入关卡数据
import { levels } from './components_2/LevelSelector.vue';

import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081';
axios.defaults.timeout = 5000;

const currentLevel = ref(null);
const currentLevelIndex = ref(-1);
const selectedColor = ref(null);
const lastClickedCell = ref(null);
const boardChanged = ref(false);
const steps = ref(0);
const COLOR_NAMES = ['Cyan', 'Magenta', 'Yellow', 'Black'];
const initSeed = ref(null);

const handleLevelSelected = (levelGrid, index) => {
  currentLevel.value = JSON.parse(JSON.stringify(levelGrid));
  currentLevelIndex.value = index;
  steps.value = 0;
  lastClickedCell.value = null;
  boardChanged.value = false;
};

const handleColorSelected = (colorIndex) => {
  selectedColor.value = colorIndex;
  boardChanged.value = true;
};

const handleCellClicked = (row, col, oldColor, newColor) => {
  lastClickedCell.value = {row, col, color: newColor};
  boardChanged.value = true;
};

const manualSendBoardToServer = async () => {
  if (!lastClickedCell.value || !currentLevel.value) {
    alert('请先选择关卡并与棋盘交互');
    return;
  }

  try {
    const {row, col, color} = lastClickedCell.value;
    const response = await axios.post('/board', {
      board: currentLevel.value,
      x: col,
      y: row,
      oldColor: currentLevel.value[row][col],
      newColor: color
    });

    if (response.data) {
      currentLevel.value = JSON.parse(JSON.stringify(response.data));
      boardChanged.value = false;
      steps.value++;
    }
  } catch (error) {
    console.error('发送失败:', error);
    alert(`错误：${error.response?.data || '网络异常'}`);
  }
};

const isBoardUniform = computed(() => {
  if (!currentLevel.value || currentLevel.value.length === 0) return false;
  const firstColor = currentLevel.value[0][0];
  return currentLevel.value.every(row => row.every(cell => cell === firstColor));
});

const getColorName = (colorIndex) => {
  return COLOR_NAMES[colorIndex] || '未知';
};

const handleCloseGame = () => {
  currentLevel.value = null;
  currentLevelIndex.value = -1;
};

const handleNextLevel = () => {
  if (currentLevelIndex.value < 5) {
    handleLevelSelected(levels.value[currentLevelIndex.value + 1], currentLevelIndex.value + 1);
  } else {
    alert('恭喜完成所有关卡！');
  }
};

const handleResetBoard = () => {
  if (currentLevelIndex.value >= 0) {
    handleLevelSelected(levels.value[currentLevelIndex.value], currentLevelIndex.value);
  }
};

const handleExit = () => {
  window.location.reload();
};
</script>

<style scoped>
.level-chess {
  padding: 2rem;
}

.game-container {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.chess-board-container {
  max-width: 600px;
  margin: 0 auto;
}

.controls-container {
  margin-top: 2rem;
}

.bg-gray-50 {
  background-color: #f9fafb;
}

.text-sm {
  font-size: 0.875rem;
}
</style>
