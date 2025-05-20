<template>
  <div class="app min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
    <SwitchView :components="[
      { name: 'RandomChess', component: RandomChess },
      { name: 'CustomChess', component: CustomChess },
      { name: 'LevelChess', component: LevelChess }
    ]" @switch-component="handleSwitchComponent" />
    <component :is="currentComponent" />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import SwitchView from './SwitchView.vue';
import RandomChess from './RandomChess.vue';
import CustomChess from './CustomChess.vue';
import LevelChess from './LevelChess.vue';

const currentComponent = ref(null);

const handleSwitchComponent = (comp) => {
  currentComponent.value = comp.component;
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
