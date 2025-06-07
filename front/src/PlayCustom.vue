<template>
  <div class="app min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
    <div class="game-container max-w-7xl mx-auto bg-red rounded-3xl shadow-2xl overflow-hidden">
      <!-- 顶部标题 -->
      <h1 class="title text-3xl font-bold text-center text-indigo-800 mb-8 mt-4">CMYK棋盘</h1>
      <label>当前目标的获胜颜色是：{{colorMap[Record]}}</label>
      <!-- 主内容区 -->
      <div class="game-wrapper">
        <div class="board-container">
          <div class="chess-board w-[600px] h-[500px] bg-gray-50 border rounded-xl shadow-lg">
            <ChessBoard
                :selectedColor="selectedColor"
                :initialGrid="grid"
                @cell-clicked="handleCellClicked"
            />
          </div>
        </div>

        <div class="controls-container">
          <div class="color-picker bg-white p-5 rounded-lg shadow-md mb-6">
            <h2 class="text-xl font-bold text-gray-800 mb-4">选择颜色</h2>
            <Button_CMYK @color-selected="handleColorSelected" />
          </div>

          <div class="action-buttons bg-white p-5 rounded-lg shadow-md mb-6">
            <h2 class="text-xl font-bold text-gray-800 mb-4">游戏操作</h2>
            <button
                class="send-btn w-full py-3 bg-green-500 hover:bg-green-600 text-white font-bold rounded-lg"
                @click="manualSendBoardToServer"
                :disabled="!boardChanged"
            >
              <i class="fa fa-paper-plane mr-2"></i>发送到服务器
            </button>
            <Button_Re
                :currentGrid="grid"
                :isVictory="isBoardUniform"
                :seed="initSeed"
                @reset="handleResetBoard"
            />
          </div>

          <div class="stats-container bg-white p-5 rounded-lg shadow-md">
            <div class="step-counter mb-4">
              <h2 class="text-xl font-bold text-gray-800 mb-4">步数统计</h2>
              <StepCounter v-model:steps="cn" />
            </div>

            <div class="status-bar bg-gray-100 p-4 rounded-lg">
              <p class="text-gray-700" v-if="lastClickedCell">
                <span class="font-medium">最后操作：</span>({{ lastClickedCell.row }}, {{ lastClickedCell.col }}) - {{ lastClickedCell.color }}
              </p>
              <p class="text-gray-500 italic" v-else>请点击棋盘开始游戏</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 胜利弹窗 -->
    <VictoryModal
        :steps="cn"
        :isVictory="isBoardUniform"
        @close="handleCloseGame"
        @next="handleNextLevel"
    />
    <Exit @exit="handleExit" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, defineEmits } from 'vue';
import ChessBoard from './components/ChessBoard.vue';
import Button_CMYK from './components/Buttons_CMYK.vue';
import Button_Re from './components/Button_Re.vue';
import StepCounter from './components/StepCounter.vue';
import VictoryModal from './components/VictoryModal.vue';
import Exit from './components_2/ExitButton.vue';
import axios from 'axios';


const emits = defineEmits(['switch-component']);


const cn = ref(0);
axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL;
axios.defaults.timeout = 5000;

const grid = ref([]);
const selectedColor = ref(null);
const lastClickedCell = ref(null);
const boardChanged = ref(false);
const COLOR_NAMES = ['Cyan', 'Magenta', 'Yellow', 'Black'];
const initSeed = ref(null);

const colorMap={
  0:'青色Cyan',
  1:'品红Magenta',
  2:'黄色Yellow',
  3:'黑色Key plate'
}
let Record=ref(0);
onMounted(() => {
  const customBoard = localStorage.getItem('customBoard');
  if (customBoard) {
    grid.value = JSON.parse(customBoard);
  }
  const targetColor = localStorage.getItem('targetColor');
  console.log('LocalStorage targetColor:', targetColor); // 添加调试信息
  if (targetColor) {
    Record.value = parseInt(targetColor); // 修正赋值语句
  }
  console.log('The Record value:', Record.value);
});


const handleColorSelected = (colorIndex) => {
  selectedColor.value = colorIndex;
  boardChanged.value = true;
};

const handleCellClicked = (row, col, oldColor, newColor) => {
  lastClickedCell.value = { row, col, color: COLOR_NAMES[newColor] };
  boardChanged.value = true;
};

const manualSendBoardToServer = async () => {
  if (!lastClickedCell.value) {
    alert('请先与棋盘交互');
    return;
  }

  try {
    const { row, col, color } = lastClickedCell.value;
    const response = await axios.post('/board', {
      board: grid.value,
      x: col,
      y: row,
      oldColor: grid.value[row][col],
      newColor: COLOR_NAMES.indexOf(color)
    });

    if (response.data) {
      grid.value = JSON.parse(JSON.stringify(response.data));
      boardChanged.value = false;
      cn.value++;
      /*alert('发送成功！')*/
    }
  } catch (error) {
    console.error('发送失败:', error);
    alert(`错误：${error.response?.data || '网络异常'}`);
  }
};

const isBoardUniform = computed(() => {
  if (!grid.value || grid.value.length === 0) return false;
  const firstColor = grid.value[0][0];
  console.log('target:',Record.value);
  return grid.value.every(row => row.every(cell => cell === firstColor))&&firstColor===Record.value;
});

const handleCloseGame = () => alert('游戏已退出');
const handleNextLevel = () => {
  cn.value = 0;
  // 这里可以根据需要重新初始化棋盘
  const customBoard = localStorage.getItem('customBoard');
  if (customBoard) {
    grid.value = JSON.parse(customBoard);
  }
};
const handleResetBoard = (newGrid) => {
  grid.value = newGrid;
  cn.value = 0;
  lastClickedCell.value = null;
};

const handleExit = () => {
  emits('switch-component', null);
};
</script>

<style scoped>
/* 旧版本样式复现 */
.app {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #a8d0e6, #6c79b8);
}

.game-container {
  width: 100%;
  height: 50%;
  max-height: 1000px;
  max-width: 1200px;
  background: #ccceff;
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin: 20px;
}

.title {
  color: #6c79b8;
  margin-bottom: 30px;
}

.game-wrapper {
  display: flex;
  justify-content: space-between;
  gap: 40px;
}

.board-container {
  flex-shrink: 0;
}

.chess-board {
  border: 2px solid #ddd;
  background: white;
}

.controls-container {
  width: 100%;
  max-width: 400px;
}

.color-picker,
.action-buttons,
.stats-container {
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.05);
}

.send-btn {
  width: 100%;
  padding: 10px 20px;
  border: none;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
}

.send-btn:hover {
  background: #45a049;
}

.status-bar {
  margin-top: 20px;
  padding: 15px;
  border-radius: 8px;
}

/* 响应式调整 */
@media (max-width: 900px) {
  .game-wrapper {
    flex-direction: column;
    align-items: center;
  }

  .board-container {
    margin-bottom: 40px;
  }
}
</style>
