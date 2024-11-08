<template>
    <AdNavbar />
    <div class="tournament-dashboard">
        <div class="action-buttons">
        <Button class="rounded" severity="danger">
            Create New Tournament
        </Button>
    </div>
        <div class="tournament-section">
      <h2 class="section-title">Upcoming Tournaments</h2>
      <DataTable :value="upcomingTournaments" :loading="loading" responsiveLayout="stack" class="tournament-table">
        <Column field="name" header="Tournament Name"></Column>
        <Column field="playerLimit" header="Player Limit"></Column>
        <Column field="startDate" header="Start Date">
          <template #body="slotProps">
            {{ formatDate(slotProps.data.startDate) }}
          </template>
        </Column>
        <Column field="endDate" header="End Date">
          <template #body="slotProps">
            {{ formatDate(slotProps.data.endDate) }}
          </template>
        </Column>
        <Column field="location" header="Location"></Column>
        <Column header="Actions" class="actions-column">
          <template #body="slotProps">
            <div class="action-buttons">
              <Button 
                label="Edit" 
                severity="secondary" 
                text 
                @click="editTournament(slotProps.data)" 
                class="edit-btn"
              />
              <Button 
                label="Delete" 
                severity="danger" 
                text 
                @click="deleteTournament(slotProps.data.tournamentID)" 
                class="delete-btn"
              />
            </div>
          </template>
        </Column>
      </DataTable>
    </div>

    <!-- Ongoing Tournaments -->
    <div class="tournament-section">
      <h2 class="section-title">Ongoing Tournaments</h2>
      <DataTable :value="ongoingTournaments" :loading="loading" responsiveLayout="stack" class="tournament-table">
        <Column field="name" header="Tournament Name"></Column>
        <Column field="startDate" header="Start Date">
          <template #body="slotProps">
            {{ formatDate(slotProps.data.startDate) }}
          </template>
        </Column>
        <Column field="endDate" header="End Date">
          <template #body="slotProps">
            {{ formatDate(slotProps.data.endDate) }}
          </template>
        </Column>
        <Column field="location" header="Location"></Column>
        <Column field="playerLimit" header="Player Limit"></Column>
      </DataTable>
    </div>

    <Toast />
    </div>

</template>
<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useToast } from 'primevue/usetoast'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import Menubar from 'primevue/menubar'
import Toast from 'primevue/toast'
import AdNavbar from '../../components/AdNavbar.vue'



export default {
  name: 'TournamentDashboard',
  components: {
    DataTable,
    Column,
    Button,
    Menubar,
    Toast,
    AdNavbar
  },
  setup() {
    const toast = useToast()
    const upcomingTournaments = ref([])
    const ongoingTournaments = ref([])
    const loading = ref(false)

    const fetchTournaments = async () => {
      loading.value = true
      try {
        const response = await axios.get('http://localhost:8080/api/tournaments')
        if (response.data.success) {
          const allTournaments = response.data.data
          upcomingTournaments.value = allTournaments.filter(t => t.status === 'Upcoming')
          ongoingTournaments.value = allTournaments.filter(t => t.status === 'Ongoing')
        } else {
          throw new Error(response.data.message)
        }
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch tournaments', life: 3000 })
      } finally {
        loading.value = false
      }
    }

    const editTournament = (tournament) => {
      // Implement edit functionality
      console.log('Edit tournament:', tournament)
    }

    const deleteTournament = async (tournamentId) => {
      try {
        const response = await axios.delete(`http://localhost:8080/api/tournaments/${tournamentId}`)
        if (response.data.success) {
          toast.add({ severity: 'success', summary: 'Success', detail: 'Tournament deleted successfully', life: 3000 })
          fetchTournaments()
        } else {
          throw new Error(response.data.message)
        }
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete tournament', life: 3000 })
      }
    }

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString()
    }

    const nextPage = () => {
      // Implement pagination logic
      console.log('Navigate to next page')
    }

    onMounted(() => {
      fetchTournaments()
    })

    return {
      upcomingTournaments,
      ongoingTournaments,
      loading,
      fetchTournaments,
      editTournament,
      deleteTournament,
      formatDate,
      nextPage
    }
  }
}
</script>
<style>
@import url('https://cdn.jsdelivr.net/npm/primevue@^3/resources/themes/lara-light-blue/theme.css');
@import url('https://cdn.jsdelivr.net/npm/primevue@^3/resources/primevue.min.css');
@import url('https://cdn.jsdelivr.net/npm/primeicons@^6/primeicons.css');
.tournament-dashboard {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}


.action-buttons {
  display: flex;
  justify-content: flex-end;
  
}

@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
  }
}

.all-tournaments {
  background-color: #333;
  border: none;
}


.section-title {
  background-color: #333;
  color: white;
  padding: 0.75rem 1.5rem;
  display: inline-block;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.tournament-section {
  margin-bottom: 3rem;
}

.tournament-table {
  border: 1px solid #dee2e6;
  border-radius: 4px;
}

.actions-column {
  display: flex;
  gap: 0.5rem;
}

.edit-btn {
  color: #333;
}

.delete-btn {
  color: #dc3545;
}




</style>