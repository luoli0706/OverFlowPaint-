<template>
  <div class="app min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
    <SwitchView :components="[
      { name: 'RandomChess', component: RandomChess },
      { name: 'CustomChess', component: CustomChess },
      { name: 'LevelChess', component: LevelChess },
      { name: 'PlayCustom', component: PlayCustom }
    ]" @switch-component="handleSwitchComponent" />
    <component :is="currentComponent" />
  </div>
</template>
<!--主页面入口，存放四个核心模式的页面组件以及入口管理的组件-->
<script setup>
import {ref} from 'vue';
import SwitchView from './SwitchView.vue';
import RandomChess from './RandomChess.vue';
import CustomChess from './CustomChess.vue';
import LevelChess from './LevelChess.vue';
import PlayCustom from './PlayCustom.vue';

const currentComponent = ref(null);

const handleSwitchComponent = (comp) => {
  if (comp) {
    currentComponent.value = comp.component;
  } else {
    currentComponent.value = null;
  }
};
</script>

<style scoped>
.app {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #a8d0e6, #6c79b8);
}
</style>
