<script>

import ChessBoard from '../../components/ChessBoard.vue'

export default {
  name: 'Home',
  components: {
    ChessBoard,

  }
}
</script>
<template>
    <div id="Home">
    <ChessBoard />
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