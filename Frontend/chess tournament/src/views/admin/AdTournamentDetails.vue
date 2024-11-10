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
      <div class="status-badge">
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
        <div class="detail-row">
          <span class="label">Description:</span>
          <span class="value">{{ tournament.descOID }}</span>
        </div>
      </div>

      <!-- Participants section -->
      <div class="participants-section">
        <div class="section-header">
          <h3>Current Participants({{ participants.length }})</h3>
        </div>
        <div class="participants-list">
          <DataTable :value="participants" :paginator="true" :rows="5">
            <Column field="name" header="Player Name"> </Column>
            <Column field="rating" header="Elo Rating"> </Column>
          </DataTable>
        </div>
      </div>

      <!-- Conditional buttons based on tournament status -->
      <Button v-if="tournament.status === 'Upcoming'" label="Match Players" severity="success" class="action-button"
        @click="matchPlayers" />
      <Button v-if="tournament.status === 'Completed'" label="Generate Results" severity="success" class="action-button"
        @click="generateResults" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router';
import { useToast } from 'primevue/usetoast'
import axios from 'axios'
import AdNavbar from '../../components/AdNavbar.vue';
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'

const toast = useToast()
const route = useRoute();

// State
const tournament = ref({})
const participants = ref([])
const list = ref([])

// Fetch tournament details
const fetchTournamentDetails = async (tournamentId) => {
  try {
    const response = await axios.get(import.meta.env.VITE_API_URL_TOURNAMENT + `/tournaments/${tournamentId}`)
    if (response.data.success) {
      tournament.value = response.data.content
    }
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch tournament details' })
  }
}

// Fetch participants
const fetchParticipants = async (tournamentId) => {
  try {
    const response = await axios.get(import.meta.env.VITE_API_URL_TOURNAMENT + `/matchups/participants/${tournamentId}`);
    if (response.data.success) {
      list.value = response.data.content;
      console.log(list);  // For debugging purposes

      const participantsProfiles = await Promise.all(
        list.value.map(async (uuid) => {
          try {
            const res = await axios.post(import.meta.env.VITE_API_URL_USERS + `/profile`, { uuid });
            return res.data.success ? res.data.content : null;  // Return null if not successful
          } catch (err) {
            console.error(`Failed to fetch profile for UUID: ${uuid}`, err);
            return null;  // Handle each failure gracefully
          }
        })
      );

      participants.value = participantsProfiles
        .filter(profile => profile !== null)  // Filter out null values (failed requests)
        .map(profile => ({
          name: profile.name,
          rating: profile.elo
        }));

      if (participantsProfiles.includes(null)) {
        toast.add({ severity: 'warn', summary: 'Warning', detail: 'Some participant profiles could not be fetched' });
      }
    }
  } catch (error) {
    console.error('Error fetching participants:', error);
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch participants' });
  }
};


const matchPlayers = async () => {
  try {
    const response = await axios.get(import.meta.env.VITE_API_URL_USERS + `/matchmaking/${tournament.value.id}`);

    if (response.data.success) {
      console.log('Players matched successfully');
      toast.add({ severity: 'success', summary: 'Success', detail: 'Players matched successfully' })
    } else {
      console.error('Error matching players:', response.data.message);
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to match players' })
    }
  } catch (error) {
    console.error('Error fetching matchmaking data:', error.message);
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to match players' })
  }
};

const generateResults = async () => {
  try {
    const response = await axios.get(import.meta.env.VITE_API_URL_TOURNAMENT + `/ranking/${tournament.value.id}`);

    if (response.data.success) {
      console.log('Results generated successfully:', response.data.content.results);
      toast.add({ severity: 'success', summary: 'Success', detail: 'Tournament results generated successfully' })
      // You can handle the ranking results here, e.g., display them in a modal or update the component state
    } else {
      console.error('Error generating results:', response.data.message);
      toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to generate results' })
    }
  } catch (error) {
    console.error('Error generating results:', error.message);
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to generate results' })
  }
};

// Utilities
const formatDate = (date) => {
  if (!date) return '---'
  return new Date(date).toLocaleDateString()
}

// Lifecycle
onMounted(() => {
  // Get the tournament ID from the route params
  const tournamentId = route.params.tournamentId;
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

.action-button {
  margin-top: 1rem;
}

@media (max-width: 768px) {
  .tournament-details {
    grid-template-columns: 1fr;
  }
}

.participants-list {
  color: black;
}
</style>