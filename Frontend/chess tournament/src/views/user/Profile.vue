<template>
  <UserNavbar />
  <Toast />
  <div class="profile-container">
    <div class="profile-content" v-if="userData">
      <div class="profile-header">
        <h1>{{ userData.name }}</h1>
        <Tag :value="`Rank #${userData.rank}`" severity="info" />
        <p>Email: {{ userData.email }}</p>
        <p>ELO Rating: {{ userData.elo }}</p>
      </div>

      <div class="tournament-sections">
        <Card class="tournament-card">
          <template #title>
            <div class="section-header">
              <h2>Active Tournaments</h2>
              <Badge :value="activeTournaments.length" severity="info" />
            </div>
          </template>
          <template #content>
            <DataTable :value="activeTournaments" :paginator="true" :rows="5">
              <Column field="tournamentName" header="Tournament"></Column>
              <Column field="startDate" header="Start Date">
                <template #body="slotProps">
                  {{ formatDate(slotProps.data.startDate) }}
                </template>
              </Column>
              <Column field="status" header="Status"></Column>
              <Column header="Action">
                <template #body="slotProps">
                  <Button label="View" size="small" style="margin-right: 0.5rem;"
                    @click="navigateToTournament(slotProps.data.tournamentID)" />
                  <Button label="Quit" size="small" severity="danger" @click="handleQuit(slotProps.data)" />
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>

        <Card class="tournament-card">
          <template #title>
            <div class="section-header">
              <h2>Completed Tournaments</h2>
              <Badge :value="completedTournaments.length" severity="success" />
            </div>
          </template>
          <template #content>
            <DataTable :value="completedTournaments" :paginator="true" :rows="5">
              <Column field="tournamentName" header="Tournament"></Column>
              <Column field="endDate" header="End Date">
                <template #body="slotProps">
                  {{ formatDate(slotProps.data.endDate) }}
                </template>
              </Column>
              <Column field="rating" header="Winner">
                <template #body="slotProps">
                  <Tag :value="formatPlacement(slotProps.data.wonLastMatch)"
                    :severity="getPlacementSeverity(slotProps.data.wonLastMatch)" />
                </template>
              </Column>
              <Column header="Action">
                <template #body="slotProps">
                  <Button label="View" size="small" @click="navigateToTournament(slotProps.data.tournamentID)" />
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Card from 'primevue/card';
import Tag from 'primevue/tag';
import Badge from 'primevue/badge';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import axios from 'axios';
import UserNavbar from '../../components/UserNavbar.vue';
import Toast from 'primevue/toast'

const router = useRouter();
const userData = ref(null);
const activeTournaments = ref([]);
const completedTournaments = ref([]);
const token = ref('')
const uuid = ref('');
const formatDate = (date) => {
  return new Date(date).toLocaleDateString();
};

const formatPlacement = (placement) => {
  if (placement === 1) return '1st';
  if (placement === 2) return '2nd';
  if (placement === 3) return '3rd';
  return `${placement}th`;
};

const getPlacementSeverity = (placement) => {
  if (placement === 1) return 'success';
  if (placement === 2) return 'info';
  if (placement === 3) return 'warning';
  return null;
};

const navigateToTournament = (tournamentID) => {
  router.push(`tournaments/${tournamentID}`);
};

const handleQuit = async (tournament) => {
  console.log(uuid.value, tournament.tournamentID)
  try {
    const response = await axios.delete(
      import.meta.env.VITE_API_URL_PUBLIC_USER + `/tournaments/quit/${uuid.value}`,
      { data: { tournamentID: tournament.tournamentID } }
    );

    if (response.data.success) {
      activeTournaments.value = activeTournaments.value.filter(t => t.id !== tournament.tournamentID);
      console.log(response.data.message)
      
    } else {
      throw new Error('Failed to quit tournament');
    }
  } catch (error) {
    console.error('Error quitting tournament:', error);
  }
};

const fetchUserProfile = async () => {
  try {
    const response = await axios.post(import.meta.env.VITE_API_URL_USERS + `/profile`, {
      uuid: uuid.value
    });

    if (response.data.success) {
      userData.value = response.data.content;
      // Add rank property if it's not included in the API response
      userData.value.rank = calculateRank(userData.value.elo);
      console.log(userData.value, userData.value.elo)
      sessionStorage.setItem("elo", userData.value.elo)
    } else {
      console.error('Error fetching user profile:', response.data.message);
    }
  } catch (error) {
    console.error('Error fetching user profile:', error);
  }
};

const fetchPlayerTournaments = async () => {
  try {
    const response = await axios.get(import.meta.env.VITE_API_URL_PUBLIC_USER + `/player/${uuid.value}/tournaments`);
    console.log(response)
    if (response.data.success) {
      activeTournaments.value = response.data.content.filter(t => t.status === 'Upcoming');
      completedTournaments.value = response.data.content.filter(t => t.status === 'Completed');
    } else {
      console.error('Error fetching player tournaments:', response.data.message);
    }
  } catch (error) {
    console.error('Error fetching player tournaments:', error);
  }
};

const calculateRank = (elo) => {
  // Implement a simple ranking algorithm based on ELO
  // This is a placeholder and should be replaced with your actual ranking logic
  return Math.floor(elo / 100);
};
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


onMounted(async () => {
  token.value = sessionStorage.getItem('authToken')
  validateUser()
  uuid.value = sessionStorage.getItem('uuid')
  if (await validateUser()) {
    await fetchUserProfile();
    await fetchPlayerTournaments();
  }
});
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.profile-content {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.profile-header {
  text-align: center;
  margin-bottom: 2rem;
  padding: 2rem;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-header h1 {
  margin: 1rem 0;
}

.tournament-sections {
  display: grid;
  gap: 2rem;
}

.tournament-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.section-header h2 {
  margin: 0;
}
</style>