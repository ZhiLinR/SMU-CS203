<template>
    <AdNavbar/>
    <div class="tournament-details">
      <!-- Left side - Chess board visualization -->
      <div class="chess-board">
        <img src="/layout/chessboard.png" alt="Chess board visualization" class="object-contain" />
      </div>
  
      <!-- Right side - Tournament information -->
      <div class="tournament-info">
        <!-- Tournament header -->
        <div class="header">
          <h2 class="tournament-name">{{ tournament.name }}</h2>
        </div>
        <div class="status-badge" >
            {{ tournament.status }}
          </div>
        <!-- Tournament details -->
        <div class="details-section">
          <div class="detail-row">
            <span class="label">Player Limit:</span>
            <span class="value">{{ tournament.playerLimit || '---' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">Location:</span>
            <span class="value">{{ tournament.location || '---' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">Start Date:</span>
            <span class="value">{{ formatDate(tournament.startDate) }}</span>
          </div>
          <div class="detail-row">
            <span class="label">End Date:</span>
            <span class="value">{{ formatDate(tournament.endDate) }}</span>
          </div>
        </div>
  
        <!-- Participants section -->
        <div class="participants-section">
          <div class="section-header">
            <h3>Current Participants({{ participants.length }})</h3>
          </div>
          <div class="participants-list">
            <DataTable :value="participants" :paginator="true" :rows="5">
              <Column field="name" header="Player Name"></Column>
              <Column field="rating" header="Rating"></Column>
            </DataTable>
          </div>
        </div>
  
        <!-- Join tournament button -->
        <Button 
          label="Join Tournament" 
          severity="success" 
          class="join-button"
          @click="joinTournament"
        />
      </div>
  
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute } from 'vue-router';
  import { useToast } from 'primevue/usetoast'
  import axios from 'axios'
  
  // Components
  import Button from 'primevue/button'
  import DataTable from 'primevue/datatable'
  import Column from 'primevue/column'
import AdNavbar from '../../components/AdNavbar.vue';
  
  const toast = useToast()
  const route = useRoute();
  
  // State
  const tournament = ref({})
  const participants = ref([])
  const list = ref([])
  
  // Fetch tournament details
  const fetchTournamentDetails = async (tournamentId) => {
    try {
      const response = await axios.get( import.meta.env.VITE_API_URL_TOURNAMENT + `/tournaments/${tournamentId}`)
      if (response.data.success) {
        tournament.value = response.data.content
        console.log(tournament.value)
      }
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch tournament details' })
    }
  }
  
  // Fetch participants
  const fetchParticipants = async (tournamentId) => {
    try {
      const response = await axios.get(import.meta.env.VITE_API_URL_TOURNAMENT + `/matchups/participants/${tournamentId}`)
      if (response.data.success) {
        list.value = response.data.content
        const participantsResponse = await axios.post(import.meta.env.VITE_API_URL_USERS + `/namelist`, list)
        if (participantsResponse.data.success){
            console.log(participantsResponse)
            participants.value = response.data.content
        }
      }
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch participants' })
    }
  }
  
  
  const joinTournament = async () => {
    try {
      // Implementation would depend on your backend API structure
      const response = await axios.post( import.meta.env.VITE_API_URL_PUBLIC_USER + `/tournaments/signup/${uuid}`)
      if (response.data.success){
        toast.add({ severity: 'success', summary: 'Success', detail: 'Tournament joined successfully' })
      }
    } catch (error) {
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to join tournament' })
    }
  }
  
  // Utilities
  const formatDate = (date) => {
    if (!date) return '---'
    return new Date(date).toLocaleDateString()
  }
  
  // Lifecycle
  onMounted(() => {
      const tournamentId = route.params.tournamentId;
      console.log(tournamentId);
      if (tournamentId) {
        fetchTournamentDetails(tournamentId);
        fetchParticipants(tournamentId);
      } else {
        console.error('Tournament ID is missing in route params');
      }
    });
  </script>
  
  <style scoped>
  .tournament-details {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 2rem;
    padding: 2rem;
    max-width: 1400px;
    margin: 0 auto;
  }
  
  .chess-board {
    aspect-ratio: 1;
    display: flex;
    padding-left: 20px;
    overflow: hidden;
    height: 500px;
  }
  
  .tournament-info {
    display: flex;
    flex-direction: column;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #333;
    padding: 1rem;
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
  }
  
  .tournament-name {
    color: white;
    margin: 0;
    font-size: 1.5rem;
  }
  
  .status-badge {
    padding: 0.5rem 1rem;
    font-weight: 500;
    text-transform: capitalize;
    background: #dc2626;
    color: white;
  }
  
  .details-section {
    background: #f5f5f5;
    padding: 1.5rem;
    border-radius: 4px;
  }
  
  .detail-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.5rem;
  }
  
  .label {
    font-weight: 500;
    color: #666;
  }
  
  .participants-section {
    background: #f5f5f5;
    padding: 1.5rem;
    border-radius: 4px;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }
  
  .section-header h3 {
    margin: 0;
  }
  
  .join-button {
    margin-top: auto;
  }
  
  @media (max-width: 768px) {
    .tournament-details {
      grid-template-columns: 1fr;
    }
  }
  </style>