<template>
  <div class="level-chess">
    <!-- 关卡模式标题 -->
    <h1 class="text-3xl font-bold text-center mb-6">关卡模式</h1>
    <!-- 新增 target_success 组件 -->
    <target_success ref="targetSuccessRef" />
    <!-- 主内容区：左侧关卡选择器，右侧游戏区域 -->
    <div class="flex flex-col md:flex-row gap-6 max-w-6xl mx-auto">
      <!-- 关卡选择组件 -->
      <div class="w-full md:w-1/4">
        <LevelSelector @level-selected="handleLevelSelected" />
      </div>

      <!-- 游戏区域：根据选择的关卡动态渲染 -->
      <div class="w-full md:w-3/4" v-if="currentLevel">
        <div class="game-container bg-white rounded-xl shadow-lg overflow-hidden">
          <!-- 游戏头部：显示当前关卡信息 -->
          <div class="game-header p-4 bg-indigo-600 text-white">
            <h2 class="text-xl font-bold">关卡 {{ currentLevelIndex + 1 }}</h2>
          </div>

          <!-- 游戏内容：棋盘和控制面板 -->
          <div class="p-6">
            <!-- 棋盘容器 -->
            <div class="chess-board-container mb-6">
              <ChessBoard
                  :selectedColor="selectedColor"
                  :initialGrid="currentLevel"
                  @cell-clicked="handleCellClicked"
              />
            </div>

            <!-- 控制面板：分为颜色选择、操作按钮和统计信息三部分 -->
            <div class="controls-container grid grid-cols-1 md:grid-cols-3 gap-4">
              <!-- 颜色选择器 -->
              <div class="color-picker bg-gray-50 p-4 rounded-lg">
                <h3 class="text-lg font-semibold mb-3">选择颜色</h3>
                <Button_CMYK @color-selected="handleColorSelected" />
              </div>

              <!-- 操作按钮：发送到服务器和重置 -->
              <div class="action-buttons bg-gray-50 p-4 rounded-lg">
                <h3 class="text-lg font-semibold mb-3">游戏操作</h3>
                <button
                    class="w-full py-2 bg-green-500 hover:bg-green-600 text-white font-medium rounded-lg mb-3"
                    @click="manualSendBoardToServer"
                    :disabled="!boardChanged"
                >
                  开始填充色彩
                </button>
                <Button_Re
                    :currentGrid="currentLevel"
                    :isVictory="isBoardUniform"
                    :seed="initSeed"
                    @reset="handleResetBoard"
                />
              </div>

              <!-- 统计信息：步数和最后操作记录 -->
              <div class="stats-container rounded-lg">
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

    <!-- 胜利弹窗：当棋盘颜色统一时显示 -->
    <VictoryModal
        :steps="steps"
        :isVictory="isBoardUniform"
        @close="handleCloseGame"
        @next="handleNextLevel"
    />

    <!-- 退出按钮 -->
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
// 从关卡选择器导入预设关卡数据
import { levels } from './components_2/levels.js';

import axios from 'axios';
import Target_success from "./components/target_success.vue";

// API配置
axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL;
axios.defaults.timeout = 5000;

// 游戏状态管理
const currentLevel = ref(null); // 当前选中的关卡数据
let currentLevelIndex = ref(-1); // 当前关卡索引
const selectedColor = ref(null); // 当前选中的颜色
const lastClickedCell = ref(null); // 最后点击的单元格信息
const boardChanged = ref(false); // 棋盘是否有变化的标志
const steps = ref(0); // 步数计数器
const COLOR_NAMES = ['Cyan', 'Magenta', 'Yellow', 'Black']; // 颜色名称映射
const initSeed = ref(null); // 随机数种子（用于重置功能）
const targetSuccessRef = ref(null);

// 处理关卡选择：初始化关卡数据和游戏状态
const handleLevelSelected = (levelGrid, index) => {
  //console.log('index:value:',index);
  currentLevel.value = JSON.parse(JSON.stringify(levelGrid)); // 深拷贝关卡数据
  currentLevelIndex.value = index;
  steps.value = 0; // 重置步数
  lastClickedCell.value = null; // 清除最后点击信息
  boardChanged.value = false; // 重置棋盘变化标志
  targetSuccessRef.value.target_color=(Date.now() * 3 + 2) % 4;
};

// 处理颜色选择：更新选中的颜色并标记棋盘已更改
const handleColorSelected = (colorIndex) => {
  selectedColor.value = colorIndex;
  boardChanged.value = true;
};

// 处理单元格点击：记录点击信息并标记棋盘已更改
const handleCellClicked = (row, col, oldColor, newColor) => {
  lastClickedCell.value = {row, col, color: newColor};
  boardChanged.value = true;
};

// 发送棋盘状态到服务器：更新棋盘并增加步数
const manualSendBoardToServer = async () => {
  if (!lastClickedCell.value || !currentLevel.value) {
    alert('请先选择关卡并与棋盘交互');
    return;
  }

  try {
    const {row, col, color} = lastClickedCell.value;
    // 发送当前棋盘状态和点击信息到服务器
    const response = await axios.post('/board', {
      board: currentLevel.value,
      x: col,
      y: row,
      oldColor: currentLevel.value[row][col],
      newColor: color
    });

    if (response.data) {
      // 更新棋盘状态（深拷贝防止引用问题）
      currentLevel.value = JSON.parse(JSON.stringify(response.data));
      boardChanged.value = false;
      steps.value++; // 增加步数
    }
  } catch (error) {
    console.error('发送失败:', error);
    alert(`错误：${error.response?.data || '网络异常'}`);
  }
};

// 计算属性：判断棋盘是否已统一颜色（游戏胜利条件）
const isBoardUniform = computed(() => {
  if (!currentLevel.value || currentLevel.value.length === 0) return false;
  const firstColor = currentLevel.value[0][0];
  // 检查所有单元格是否与第一个单元格颜色相同
  return currentLevel.value.every(row => row.every(cell => cell === firstColor)) && firstColor===targetSuccessRef.value.target_color;
});

// 获取颜色名称：根据颜色索引返回对应的颜色名称
const getColorName = (colorIndex) => {
  return COLOR_NAMES[colorIndex] || '未知';
};

// 处理关闭游戏：重置当前关卡
const handleCloseGame = () => {
  currentLevel.value = null;
  currentLevelIndex.value = -1;
};

// 处理下一关：加载下一个关卡数据
const handleNextLevel = () => {
  console.log('currentLevelIndex value:',currentLevelIndex.value);
  console.log('level.value.length-1 value :',levels.value.length-1);
  if (currentLevelIndex.value < levels.value.length - 1) {
    // 加载下一关
    handleLevelSelected(levels.value[currentLevelIndex.value + 1], currentLevelIndex.value + 1);
  } else {
    alert('恭喜完成所有关卡！');
  }
};

// 处理重置棋盘：重新加载当前关卡
const handleResetBoard = () => {
  if (currentLevelIndex.value >= 0) {
    handleLevelSelected(levels.value[currentLevelIndex.value], currentLevelIndex.value);
  }
};

// 处理退出游戏：刷新页面
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

.stats-container {
  background-color: #6c79b8;
}

.bg-gray-50 {
  background-color: #f9fafb;
}

.color-picker {
  background-color: #6c79b8;
}

.text-sm {
  font-size: 0.875rem;
}

.mt-4 {
  background-color: #6c79b8;
}

.action-buttons {
  background-color: #6c79b8;
}

.text-lg {
  background-color: #6c79b8;
}

.flex {
  background-color: #6c79b8;
}
</style>
