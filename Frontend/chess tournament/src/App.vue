<template>

  <div id="app" :style="{ backgroundColor: '#FBFBFB' }">
    
    <router-view />

  </div>
</template>

<script>

import Home from './views/Home/Home.vue'


export default {
  name: 'App',
  components: {

    Home,

  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  background-color: #FBFBFB;

  }
</style>