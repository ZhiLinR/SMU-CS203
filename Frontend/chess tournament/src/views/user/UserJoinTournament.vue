<template>
  <UserNavbar />
  <Toast/>
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
        <!-- Join tournament button -->
      <Button label="Join Tournament" severity="success" class="join-button" @click="joinTournament" />
      </div>

      <!-- Participants section
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
      </div> -->

      
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router';
import { useToast } from 'primevue/usetoast'
import axios from 'axios'
import UserNavbar from '../../components/UserNavbar.vue';
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'


const toast = useToast()
const route = useRoute();
const token = ref('')
const uuid = ref('')
const elo = sessionStorage.getItem("elo")
// State
const tournament = ref({})
const participants = ref([])
const list = ref([])

const userData = ref(null);
const tournamentId = sessionStorage.getItem("tournamentId");
onMounted(async () => {
  if (tournamentId) {
    fetchTournamentDetails(tournamentId);
   // fetchParticipants(tournamentId);
    token.value = sessionStorage.getItem('authToken')
    if (await validateUser()) {
      await fetchUserProfile();
    }
  } else {
    console.error('Tournament ID is missing in route params');
  }
});

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

// // Fetch participants
// const fetchParticipants = async (tournamentId) => {
//   try {
//     const response = await axios.get(import.meta.env.VITE_API_URL_PUBLIC_USER + `/tournaments/players/${tournamentId}`);
//     if (response.data.success) {
//       list.value = response.data.content;
//       console.log(list);  // For debugging purposes

//       const participantsProfiles = await Promise.all(
//         list.value.map(async (uuid) => {
//           try {
//             const res = await axios.post(import.meta.env.VITE_API_URL_USERS + `/profile`, { uuid });
//             return res.data.success ? res.data.content : null;  // Return null if not successful
//           } catch (err) {
//             console.error(`Failed to fetch profile for UUID: ${uuid}`, err);
//             return null;  // Handle each failure gracefully
//           }
//         })
//       );

//       participants.value = participantsProfiles
//         .filter(profile => profile !== null)  // Filter out null values (failed requests)
//         .map(profile => ({
//           name: profile.name,
//           rating: profile.elo
//         }));

//       if (participantsProfiles.includes(null)) {
//         toast.add({ severity: 'warn', summary: 'Warning', detail: 'Some participant profiles could not be fetched' });
//       }
//     }
//   } catch (error) {
//     console.error('Error fetching participants:', error);
//     toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch participants' });
//   }
// };


const joinTournament = async () => {
  try {
    const response = await axios.post(
      import.meta.env.VITE_API_URL_PUBLIC_USER +`/tournaments/signup/${uuid.value}`,
      { tournamentID: tournamentId, elo: elo }
    );
    console.log(response)
    if (response.data.success) {
      toast.add({ severity: 'success', summary: 'Success', detail: 'Tournament joined successfully' });
    }
  } catch (error) {
    toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to join tournament' });
  }
};

// Utilities
const formatDate = (date) => {
  if (!date) return '---'
  return new Date(date).toLocaleDateString()
}

const validateUser = async () => {
  const response = await axios.get(import.meta.env.VITE_API_URL_MIDDLEWARE + "/jwt", {
    headers: { Authorization: token.value, Origin: import.meta.env.VITE_API_URL_ORIGIN }
  })
  if (response.data.success) {
    uuid.value = response.data.content.uuid
    localStorage.setItem('uuid', uuid)
    console.log(token.value, uuid.value)
    return true
  }
}

const fetchUserProfile = async () => {
    console.log(uuid.value)
    try {
      const response = await axios.post(import.meta.env.VITE_API_URL_USERS + `/profile`, {
        uuid: uuid.value 
      });
  
      if (response.data.success) {
        userData.value = response.data.content;
        // Add rank property if it's not included in the API response
       // userData.value.rank = calculateRank(userData.value.elo);
       console.log(userData.value,userData.value.elo)
       sessionStorage.setItem("elo", userData.value.elo)
      } else {
        console.error('Error fetching user profile:', response.data.message);
      }
    } catch (error) {
      console.error('Error fetching user profile:', error);
    }
  };

// Lifecycle

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