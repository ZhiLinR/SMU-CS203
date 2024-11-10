<script>
import UserSignup from '../../components/Signup.vue'
import UserNavbar from '../../components/UserNavbar.vue';

export default {
  name: 'login',
  components: {
    UserSignup,
    UserNavbar,

  }
}

</script>
<template>
  <UserNavbar/>
    <div class="outer-box">

      <div class="signup-box">
      <UserSignup/>
      </div>
    </div>

</template>
<style scoped>
@import '../../css/main.css';




</style>