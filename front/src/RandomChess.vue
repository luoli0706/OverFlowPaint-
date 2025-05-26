<template>
  <div class="app min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
    <!-- 游戏主容器，包含标题和主要游戏区域 -->
    <div class="game-container max-w-7xl mx-auto bg-red rounded-3xl shadow-2xl overflow-hidden">
      <!-- 游戏标题 -->
      <h1 class="title text-3xl font-bold text-center text-indigo-800 mb-8 mt-4">CMYK棋盘</h1>

      <!-- 游戏内容区：左侧棋盘，右侧控制面板 -->
      <div class="game-wrapper">
        <!-- 棋盘容器 -->
        <div class="board-container">
          <div class="chess-board w-[600px] h-[500px] bg-gray-50 border rounded-xl shadow-lg">
            <!-- 棋盘组件：渲染CMYK颜色网格，接收选中颜色和初始网格数据 -->
            <ChessBoard
                :selectedColor="selectedColor"
                :initialGrid="grid"
                @cell-clicked="handleCellClicked"
            />
          </div>
        </div>

        <!-- 右侧控制面板：包含颜色选择器、游戏操作按钮和统计信息 -->
        <div class="controls-container">
          <!-- 颜色选择器：选择CMYK四种颜色之一 -->
          <div class="color-picker bg-white p-5 rounded-lg shadow-md mb-6">
            <h2 class="text-xl font-bold text-gray-800 mb-4">选择颜色</h2>
            <Button_CMYK @color-selected="handleColorSelected" />
          </div>

          <!-- 游戏操作按钮：发送到服务器、重置游戏 -->
          <div class="action-buttons bg-white p-5 rounded-lg shadow-md mb-6">
            <h2 class="text-xl font-bold text-gray-800 mb-4">游戏操作</h2>
            <button
                class="send-btn w-full py-3 bg-green-500 hover:bg-green-600 text-white font-bold rounded-lg"
                @click="manualSendBoardToServer"
                :disabled="!boardChanged"
            >
              <i class="fa fa-paper-plane mr-2"></i>开始填充色彩
            </button>
            <!-- 重置按钮：根据当前状态重置棋盘 -->
            <Button_Re
                :currentGrid="grid"
                :isVictory="isBoardUniform"
                :seed="initSeed"
                @reset="handleResetBoard"
            />
          </div>

          <!-- 统计信息：步数计数器和最后操作信息 -->
          <div class="stats-container bg-white p-5 rounded-lg shadow-md">
            <div class="step-counter mb-4">
              <h2 class="text-xl font-bold text-gray-800 mb-4">步数统计</h2>
              <StepCounter v-model:steps="cn" />
            </div>

            <div class="status-bar bg-gray-100 p-4 rounded-lg">
              <!-- 显示最后点击的单元格信息 -->
              <p class="text-gray-700" v-if="lastClickedCell">
                <span class="font-medium">最后操作：</span>({{ lastClickedCell.row }}, {{ lastClickedCell.col }}) - {{ lastClickedCell.color }}
              </p>
              <p class="text-gray-500 italic" v-else>请点击棋盘开始游戏</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 胜利弹窗：当棋盘颜色统一时显示 -->
    <VictoryModal
        :steps="cn"
        :isVictory="isBoardUniform"
        @close="handleCloseGame"
        @next="handleNextLevel"
    />
    <!-- 退出按钮：返回到游戏选择界面 -->
    <Exit @exit="handleExit" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import ChessBoard from './components/ChessBoard.vue';
import Button_CMYK from './components/Buttons_CMYK.vue';
import Button_Re from './components/Button_Re.vue';
import StepCounter from './components/StepCounter.vue';
import VictoryModal from './components/VictoryModal.vue';
import Exit from './components_2/ExitButton.vue';
import axios from 'axios';

// 游戏状态管理
const cn = ref(0); // 步数计数器
axios.defaults.baseURL = 'http://localhost:8081'; // 后端API基础URL
axios.defaults.timeout = 5000; // 请求超时时间

const grid = ref([]); // 当前棋盘状态
const selectedColor = ref(null); // 当前选中的颜色
const lastClickedCell = ref(null); // 最后点击的单元格信息
const boardChanged = ref(false); // 棋盘是否有变化
const COLOR_NAMES = ['Cyan', 'Magenta', 'Yellow', 'Black']; // 颜色名称数组
const initSeed = ref(null); // 随机数种子，用于生成初始棋盘

// 初始化棋盘：使用种子生成随机棋盘
const initGrid = () => {
  initSeed.value = Date.now(); // 使用当前时间戳作为种子
  const newGrid = [];
  // 生成8行10列的棋盘，每个格子随机分配一种CMYK颜色
  for (let y = 0; y < 8; y++) {
    const row = [];
    for (let x = 0; x < 10; x++) {
      // 基于种子生成伪随机数，确保每次使用相同种子生成相同的棋盘
      const localSeed = initSeed.value + x * y + ( x + 1 ) * y+ ( y + 1 ) * x + initSeed.value;
      const state = Math.floor(randomFromSeed(localSeed) * 4);
      row.push(state);
    }
    newGrid.push(row);
  }
  grid.value = newGrid;
};

// 基于种子的伪随机数生成器
const randomFromSeed = (seed) => {
  const x = Math.sin(seed++) * 10000;
  return x - Math.floor(x);
};

// 处理颜色选择：更新选中的颜色并标记棋盘已更改
const handleColorSelected = (colorIndex) => {
  selectedColor.value = colorIndex;
  boardChanged.value = true;
};

// 处理单元格点击：记录最后点击的单元格信息并标记棋盘已更改
const handleCellClicked = (row, col, oldColor, newColor) => {
  lastClickedCell.value = { row, col, color: COLOR_NAMES[newColor] };
  boardChanged.value = true;
};

// 手动发送棋盘状态到服务器：更新棋盘并增加步数
const manualSendBoardToServer = async () => {
  if (!lastClickedCell.value) {
    alert('请先与棋盘交互');
    return;
  }

  try {
    const { row, col, color } = lastClickedCell.value;
    // 发送当前棋盘状态和点击信息到服务器
    const response = await axios.post('/board', {
      board: grid.value,
      x: col,
      y: row,
      oldColor: grid.value[row][col],
      newColor: COLOR_NAMES.indexOf(color)
    });

    if (response.data) {
      // 更新棋盘状态
      grid.value = JSON.parse(JSON.stringify(response.data));
      boardChanged.value = false;
      cn.value++; // 增加步数
      /*alert('发送成功！')*/
    }
  } catch (error) {
    console.error('发送失败:', error);
    alert(`错误：${error.response?.data || '网络异常'}`);
  }
};

// 计算属性：判断棋盘是否已统一颜色（游戏胜利条件）
const isBoardUniform = computed(() => {
  if (!grid.value || grid.value.length === 0) return false;
  const firstColor = grid.value[0][0];
  // 检查所有单元格是否与第一个单元格颜色相同
  return grid.value.every(row => row.every(cell => cell === firstColor));
});

// 处理游戏关闭：显示提示信息
const handleCloseGame = () => alert('游戏已退出');

// 处理下一关：重置步数并生成新棋盘
const handleNextLevel = () => {
  cn.value = 0;
  initGrid();
};

// 处理重置棋盘：使用新的随机种子生成棋盘
const handleResetBoard = (newGrid) => {
  grid.value = newGrid;
  cn.value = 0;
  lastClickedCell.value = null;
};

// 处理退出游戏：刷新页面返回到初始状态
const handleExit = () => {
  // 返回到SwitchView
  window.location.reload();
};

// 组件挂载时初始化棋盘
onMounted(initGrid);
</script>

<style scoped>
/* 应用整体样式 */
.app {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #a8d0e6, #6c79b8);
}

/* 游戏容器样式 */
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

/* 游戏内容布局 */
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

/* 控制面板样式 */
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

/* 响应式布局：在小屏幕上垂直排列元素 */
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
