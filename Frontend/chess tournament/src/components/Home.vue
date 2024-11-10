  
  <script>
 import { ref, onMounted } from 'vue'

export default {
  name: 'Home',
  setup() {
    const tournaments = ref([])
    const stats = ref({
      totalTournaments: 0,
      totalUsers: 0
    })

    const fetchTournaments = async () => {
      try {
        const response = await fetch('http://localhost:3000/tournaments')
        const data = await response.json()
        if (data.success) {
          tournaments.value = data.content[0]
          stats.value.totalTournaments = tournaments.value.length
        }
      } catch (error) {
        console.error('Error fetching tournaments:', error)
      }
    }

    onMounted(() => {
      fetchTournaments()
    })

    return {
      tournaments,
      stats
    }
  }
}
  </script>

<template>
    <div class="chess-board-container">
      <div class="chess-board">
       <img src = "../assets/Chessboard.png" class = "chessboard-img" alt = "Chessboard"/>
      </div>
      <div class="home-text-main">
        <h1>#1 Chess Site,even Prof Vincent Goh Played It.</h1>
        <div class="inner-home-text-main">
      <div class="statistics">
        <div class="stat-item">
            <div class="game-tournament-stat" label="Secondary" severity="secondary">
                <h3 class="stat-header" style="font-weight: bold;" disabled>4000</h3>&nbsp; 
                <p class="stat-body">Game tournaments</p> 
            </div>
        </div>
        <div class="stat-item">
            <div class="game-tournament-stat" label="Secondary" severity="secondary">
                <h2 class="stat-header"  style="font-weight: bold;" disabled>10000+</h2>&nbsp;
                <p class="stat-body">Users</p>
            </div>
        </div>
        </div>
        <div><img src = "../assets/chess-pieces.png" class = "chesspieces" alt = "Chesspieces-king"/></div>
        </div>
        <div><button class="play-button">
            <h2 style="color: #FDFDFD; font-weight: bold;line-height: 60px;">Play Online Now</h2>
        </button>
    </div>
      </div>
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
    height: 60px;
    font-size: 1rem;
    cursor: pointer;
    width: 500px;
    max-height: 60px;
    border-radius: 5px;
    vertical-align: middle;
    margin-top: 1rem;
  }
  </style>