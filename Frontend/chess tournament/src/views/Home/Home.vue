<script>

import HomeComponent from '../../components/Home.vue'
import UserNavbar from '../../components/UserNavbar.vue';

export default {
  name: 'Home',
  components: {
    HomeComponent,
    UserNavbar
  }
}
</script>
<template>
    <div id="Home">
      <UserNavbar/>
    <HomeComponent />
  </div>
 

</template>

<style>
  .upcoming-tournament {
    background-color: #2D2D2D;
    padding: 0.5rem;
    margin: 0;
  }

</style>