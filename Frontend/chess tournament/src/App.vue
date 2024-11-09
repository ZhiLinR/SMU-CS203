<template>
  <div id="app" :style="{ backgroundColor: '#FBFBFB' }">
    <Navbar/>
    <router-view />

  </div>
</template>

<script>
import Navbar from '../src/components/AdNavbar.vue'
import Home from './views/Home/Home.vue'
import UpcomingTournaments from '../src/components/UpcomingTournaments.vue'


export default {
  name: 'App',
  components: {
    Navbar,
    Home,
    UpcomingTournaments,

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