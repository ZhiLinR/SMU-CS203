<script>
import UserNavbar from '../../components/UserNavbar.vue';
import PublicTournaments from '../../components/PublicTournaments.vue';


export default {
  name: 'publicTournaments',
  components: {
    UserNavbar,
    PublicTournaments
    
  }
}
</script>
<template>
    <div id="publicTournaments">
      <UserNavbar/>
    <PublicTournaments />
  </div>
 

</template>