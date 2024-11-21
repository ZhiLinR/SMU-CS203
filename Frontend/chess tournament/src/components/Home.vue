<script>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useToast } from 'primevue/usetoast'

export default {
  name: 'Home',
  setup() {
    const toast = useToast()
    const loading = ref(false)
    const ongoingTournaments = ref([])
    const showAllOngoing = ref(false);
    const stats = ref({
      totalTournaments: 0,
      totalUsers: 0
    })

    const fetchTournaments = async () => {
      loading.value = true
      try {
        const response = await axios.get(import.meta.env.VITE_API_URL_PUBLIC + `/events`)
        if (response.data.success) {
          ongoingTournaments.value = response.data.content[0] || []

          stats.value.totalTournaments = ongoingTournaments.value.length
          
        } else {
          throw new Error(response.data.message)
        }
      } catch (error) {
        console.error('Failed to fetch tournaments:', error)
        toast.add({ 
          severity: 'error', 
          summary: 'Error', 
          detail: error.message || 'Failed to fetch tournaments', 
          life: 3000 
        })
      } finally {
        loading.value = false
      }
    }

    const limitedOngoingTournaments = computed(() => 
    (showAllOngoing.value ? ongoingTournaments.value : ongoingTournaments.value.slice(0, 4)));
   

    onMounted(() => {
      fetchTournaments()
    })

    return {
      loading,
      ongoingTournaments,
      limitedOngoingTournaments,
      showAllOngoing,
      stats,
    }
  }
}
</script>

<template>
  <div class="chess-board-container">
    <div class="chess-board">
      <img src="../assets/Chessboard.png" class="chessboard-img" alt="Chessboard"/>
    </div>
    <div class="home-text-main">
      <h1>#1 Chess Site, even Prof Vincent Goh Played It.</h1>
      <div class="inner-home-text-main">
        <div class="statistics">
          <div class="stat-item">
            <div class="game-tournament-stat" label="Secondary" severity="secondary">
              <h3 class="stat-header" style="font-weight: bold;" disabled>{{ stats.totalTournaments || 4000 }}</h3>&nbsp; 
              <p class="stat-body">Game tournaments</p> 
            </div>
          </div>
          <div class="stat-item">
            <div class="game-tournament-stat" label="Secondary" severity="secondary">
              <h2 class="stat-header" style="font-weight: bold;" disabled>10000+</h2>&nbsp;
              <p class="stat-body">Users</p>
            </div>
          </div>
        </div>
        <div><img src="../assets/chess-pieces.png" class="chesspieces" alt="Chesspieces-king"/></div>
      </div>
      <div>
        <button class="play-button">
          <h2 style="color: #FDFDFD; font-weight: bold;line-height: 60px;">Play Online Now</h2>
        </button>
      </div>
     

    </div>
  </div>

<!---HOME PAGE BREAK-->

  <div>
    <Divider align="center" style="font-weight: bold;">
        <div class="upcoming-tournament">
          <h2 style="color:#FBFBFB">Ongoing Tournaments</h2>
        </div>
    </Divider>
    <div>
      <!-- Tournament Sections -->
      <div v-if="!loading" class="tournaments-section" >

      <!-- Ongoing Tournaments -->
<div v-if="limitedOngoingTournaments.length > 0" >
  <Card>
    <template #content>
      <div class="each-card" >
        <Card 
          v-for="tournament in limitedOngoingTournaments" 
          :key="tournament.tournamentID" 
          style="min-width: 250px; flex: 0 0 auto; background-color: #FBFBFB;"
        >
          <template #title>
            <h3>{{ tournament.name }}</h3>
          </template>
          <template #content>
            <div class="tournament-details">
              <p><i class="pi pi-map-marker"></i> {{ tournament.location }}</p>
              <p><i class="pi pi-clock"></i> {{ tournament.status }}</p>
              <p><i class="pi pi-users"></i> Players: {{ tournament.playerLimit }}</p>
            </div>
          </template>
        </Card>
      </div>
      <div style="text-align: center; margin-top: 1rem; ">
        <router-link to ="/public/tournaments">
        <Button 
          v-if="ongoingTournaments.length > 4 && !showAllOngoing" 
          @click="showAllOngoing = true" 
          style="color:#FBFBFB; margin: 1rem; background-color: #2D2D2D; border-radius: 10px;"
        >
          View More
        </Button>
      </router-link>
      </div>
    </template>
  </Card>
</div>

</div>

        <!-- No Tournaments Message -->
        <Card v-if="ongoingTournaments.length === 0">
          <template #content>
            <p class="text-center">No tournaments available at the moment.</p>
          </template>
        </Card>
      </div>

      <!-- Loading State -->
      <Card v-if="loading">
        <template #content>
          <div class="text-center">
            <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
            <p>Loading tournaments...</p>
          </div>
        </template>
      </Card>
    </div>

</template>
  <style scoped>
  @import '../css/main.css';

  *{
     color: #2D2D2D;
  }

  h1{
    font-weight: bold;
  }

  .chess-board-container {
    display: flex;
    padding: 2rem;
  }

  .home-text-main{
    max-width: 500px;
    margin-left: 10rem;
  }

  .inner-home-text-main{
    display: flex;
    
  }

  .statistics {
    flex: 1;
    text-align: center;
  margin-top: 2rem;
  }

  .stat-header{
    justify-content: left;
    display: inline;
  }

  .stat-body{
    justify-content: right;
  }
  
  .stat-item {
    margin-bottom: 1rem;
    text-align: center;
  }
  .game-tournament-stat{
    background-color: #D9D9D9;
    width: 245px;
    height: 67px;
    
  }
  
  .stat-item h3 {
    font-size: 2rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
  }
  
  .play-button {
    background-color: #333;
    border: none;
    font-size: 1rem;
    cursor: pointer;
    width: 500px;
    max-height: 60px;
    border-radius: 5px;
    vertical-align: middle;
    margin-top: 1rem;
  }

  .each-card{
    display: flex; 
    flex-direction: row; 
    gap: 1rem; 
    overflow-x: auto; 
    padding: 1rem; 
    justify-content: center;
    background-color: #FBFBFB;
  }

  .tournaments-section{
    background-color: #FBFBFB;
  }

  Card{
    background-color: #FBFBFB;
  }

  

  </style>