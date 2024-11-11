<script>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useToast } from 'primevue/usetoast'

export default {
  name: 'PublicTournaments',
  setup() {
    const router = useRouter()
    const toast = useToast()

    const selectedTournaments = ref(null)
    const Filtertournaments = ref([
      { name: 'All Tournaments', endpoint: '/tournaments' },
      { name: 'Ongoing Tournaments', endpoint: '/events',  status: 'Ongoing' },
      { name: 'Upcoming Tournaments', endpoint: '/upcoming-events', status: 'Upcoming'  },
      { name: 'Past Tournaments', endpoint: '/past-events', status: 'Completed' },
    ])

    const loading = ref(true)
    const tournaments = ref([])
    const filteredTournaments = ref([]) 

    const fetchTournaments = async () => {
      loading.value = true
      try {
        const endpoint = selectedTournaments.value ? selectedTournaments.value.endpoint : '/tournaments'
        const response = await axios.get(import.meta.env.VITE_API_URL_PUBLIC + endpoint)
        tournaments.value = Array.isArray(response.data.content) && response.data.content[0] 
          ? response.data.content[0] 
          : []
        filterTournaments() 
        console.log(tournaments.value)
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

    const filterTournaments = () => {
    if (!selectedTournaments.value || selectedTournaments.value.name === 'All Tournaments') {
        filteredTournaments.value = tournaments.value
    } else {
        filteredTournaments.value = tournaments.value.filter(
        tournament => tournament.status === selectedTournaments.value.status
        )
    }
    }

    const viewTournament = (tournamentId) => {
      router.push(`/${tournamentId}`)
    }

    watch(selectedTournaments, () => {
      fetchTournaments()
    })

    onMounted(() => {
      fetchTournaments()
    })

    return {
      loading,
      selectedTournaments,
      Filtertournaments,
      filteredTournaments,
      viewTournament,
      fetchTournaments
    }
  }
}
</script>

<template>
  <div class="tournament-container">
    <!-- Dropdown Filter -->
    <Dropdown v-model="selectedTournaments" 
        :options="Filtertournaments" 
        optionLabel="name" 
        placeholder="Select a Tournament Type" 
        checkmark 
        :highlightOnSelect="false" 
        class="w-full md:w-20rem" 
        style="margin: 10px;"
        @change="fetchTournaments"
    ></Dropdown>
    
    <!-- Title -->
    <Divider align="center" class="font-bold">
      <div class="upcoming-tournament">
        <h2 class="text-white">
          {{ selectedTournaments ? selectedTournaments.name : 'All Tournaments' }}
        </h2>
      </div>
    </Divider>

    <!-- Loading State -->
    <div v-if="loading" class="loading-state">
      Loading tournaments...
    </div>

    <!-- tournament list -->
    <div v-else>
      <Card>
        <template #content>
          <div class="each-card">
            <Card
              v-for="tournament in filteredTournaments"
              :key="tournament.tournamentID"
              style="min-width: 250px; flex: 0 0 auto; background-color: #FBFBFB; display: block"
              @click="viewTournament(tournament.tournamentID)"
            >
              <template #title>
                <h3>{{ tournament.name }}</h3>
              </template>
              <template #content>
                <div class="tournament-details">
                  <p><i class="pi pi-map-marker"></i> {{ tournament.location }}</p>
                  <p><i class="pi pi-clock"></i> {{ tournament.status }}</p>
                  <p><i class="pi pi-users"></i> Players: {{ tournament.playerLimit }}</p>
                  <p><i class="pi pi-calendar"></i> Start: {{ new Date(tournament.startDate).toLocaleDateString() }}</p>
                  <p><i class="pi pi-calendar-times"></i> End: {{ new Date(tournament.endDate).toLocaleDateString() }}</p>
                </div>
              </template>
            </Card>
          </div>
        </template>
      </Card>
    </div>

    <!-- No Results Message -->
    <div v-if="!loading && filteredTournaments.length === 0" class="no-results">
      No tournaments found
    </div>
  </div>
</template>

<style>
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


  .each-card {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* Exactly 4 columns */
  gap: 1rem;
  padding: 1rem;
  background-color: #2D2D2D;
  border-radius: 10px;
}


</style>