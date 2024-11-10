<script>
import Login from '../../components/Login.vue'
import UserNavbar from '../../components/UserNavbar.vue';

export default {
  name: 'login',
  components: {
    Login,
    UserNavbar

  }
}

</script>
<template>
  <UserNavbar/>
    <div class="outer-box">

      <div class="login-box">
      <Login/>
      </div>
    </div>

</template>
<style scoped>
@import '../../css/main.css';




</style>