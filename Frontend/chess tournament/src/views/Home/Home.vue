<script>

import HomeComponent from '../../components/Home.vue'

export default {
  name: 'Home',
  components: {
    HomeComponent,

  }
}
</script>
<template>
    <div id="Home">
    <HomeComponent />
  </div>
  <Divider align="center" style="font-weight: bold;">
    <div class="upcoming-tournament">
      <h2 style="color:#FBFBFB">Upcoming Tournaments</h2>
    </div>
</Divider>

</template>

<style>
  .upcoming-tournament {
    background-color: #2D2D2D;
    padding: 0.5rem;
    margin: 0;
  }

</style>