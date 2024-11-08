<template>
  <AdNavbar />
  <div class="tournament-dashboard">
    <div class="action-buttons">
      <Button class="rounded" severity="danger" @click="openCreateDialog()">
        Create New Tournament
      </Button>
    </div>
    <div class="tournament-section">
      <div class="tournament-header">
        <div class="line"></div>
        <div class="text">Upcoming Tournaments</div>
        <div class="line"></div>
      </div>
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
              <Button label="View Details" @click="viewTournamentDetails(slotProps.data)" severity="info" text class="rounded" />
              <Button label="Edit" severity="success" text @click="openEditDialog(slotProps.data)" class="rounded" />
              <Button label="Delete" severity="danger" text @click="deleteTournament(slotProps.data.tournamentID)"
                class="rounded" />
            </div>
          </template>
        </Column>
      </DataTable>
    </div>

    <!-- Ongoing Tournaments -->
    <div class="tournament-section">
      <div class="tournament-header">
        <div class="line"></div>
        <div class="text">Ongoing Tournaments</div>
        <div class="line"></div>
      </div>
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
        <Column header="Actions">
          <template #body="slotProps">
            <Button label="View Details" @click="viewTournamentDetails(slotProps.data)" severity="info" text />
          </template>
        </Column>
      </DataTable>
    </div>
    <Toast />

    <!-- Create Tournament Dialog -->
    <Dialog v-model:visible="createDialogVisible" header="Create Tournament" :style="{ width: '50vw' }" :modal="true">
      <div class="p-fluid">
        <div class="p-field">
          <label for="name">Tournament Name</label>
          <InputText id="name" v-model="creatingTournament.name" required autofocus />
        </div>
        <div class="p-field">
          <label for="startDate">Start Date</label>
          <Calendar id="startDate" v-model="creatingTournament.startDate" dateFormat="yy-mm-dd" />
        </div>
        <div class="p-field">
          <label for="endDate">End Date</label>
          <Calendar id="endDate" v-model="creatingTournament.endDate" dateFormat="yy-mm-dd" />
        </div>
        <div class="p-field">
          <label for="playerLimit">Player Limit</label>
          <InputNumber id="playerLimit" v-model="creatingTournament.playerLimit" />
        </div>
        <div class="p-field">
          <label for="location">Location</label>
          <InputText id="location" v-model="creatingTournament.location" required />
        </div>
        <div class="p-field">
          <label for="descOID">Description</label>
          <InputText id="descOID" v-model="creatingTournament.descOID" required />
        </div>
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" @click="closeCreateDialog" class="p-button-text" />
        <Button label="Create" icon="pi pi-check" @click="createTournament" autofocus />
      </template>
    </Dialog>

    <!-- Edit Tournament Dialog -->
    <Dialog v-model:visible="editDialogVisible" header="Edit Tournament" :style="{ width: '50vw' }" :modal="true">
      <div class="p-fluid">
        <div class="p-field">
          <label for="name">Name</label>
          <InputText id="name" v-model="editingTournament.name" required autofocus />
        </div>
        <div class="p-field">
          <label for="startDate">Start Date</label>
          <Calendar id="startDate" v-model="editingTournament.startDate" dateFormat="yy-mm-dd" />
        </div>
        <div class="p-field">
          <label for="endDate">End Date</label>
          <Calendar id="endDate" v-model="editingTournament.endDate" dateFormat="yy-mm-dd" />
        </div>
        <div class="p-field">
          <label for="playerLimit">Player Limit</label>
          <InputNumber id="playerLimit" v-model="editingTournament.playerLimit" />
        </div>
        <div class="p-field">
          <label for="location">Location</label>
          <InputText id="location" v-model="editingTournament.location" required />
        </div>
        <div class="p-field">
          <label for="descOID">Description</label>
          <InputText id="descOID" v-model="editingTournament.descOID" required />
        </div>
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" @click="closeEditDialog" class="p-button-text" />
        <Button label="Save" icon="pi pi-check" @click="saveTournament" autofocus />
      </template>
    </Dialog>
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
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Calendar from 'primevue/calendar'
import { useRouter } from 'vue-router'



export default {
  name: 'TournamentDashboard',
  components: {
    DataTable,
    Column,
    Button,
    Menubar,
    Toast,
    AdNavbar,
    InputText,
    InputNumber,
    Calendar,
    Dialog,
    useRouter,
  },
  setup() {
    const toast = useToast()
    const upcomingTournaments = ref([])
    const ongoingTournaments = ref([])
    const loading = ref(false)
    const editDialogVisible = ref(false)
    const createDialogVisible = ref(false)
    const editingTournament = ref({})
    const creatingTournament = ref({})
    const router = useRouter();


    const fetchTournaments = async () => {
      loading.value = true
      try {
        const response = await axios.get(import.meta.env.VITE_API_URL_TOURNAMENT + `/tournaments`)
        if (response.data.success) {
          const allTournaments = response.data.content
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

    const openEditDialog = (tournament) => {
      editingTournament.value = { ...tournament }
      editDialogVisible.value = true
    }

    const closeEditDialog = () => {
      editingTournament.value = {}
      editDialogVisible.value = false
    }

    const openCreateDialog = (tournament) => {
      creatingTournament.value = { ...tournament }
      createDialogVisible.value = true
    }

    const closeCreateDialog = () => {
      creatingTournament.value = {}
      createDialogVisible.value = false
    }

    const createTournament = async () => {
      try {
        const response = await axios.post(import.meta.env.VITE_API_URL_TOURNAMENT + `/tournaments`, creatingTournament.value)
        console.log(response)
        if (response.data.success) {
          toast.add({ severity: 'success', summary: 'Success', detail: 'Tournament created successfully', life: 3000 })
          fetchTournaments()
          closeEditDialog()
        } else {
          throw new Error(response.data.message)
        }
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to create tournament', life: 3000 })
      }
    }

    const saveTournament = async () => {
      try {
        const response = await axios.put(import.meta.env.VITE_API_URL_TOURNAMENT + `/tournaments/${editingTournament.value.tournamentID}`, editingTournament.value)
        if (response.data.success) {
          toast.add({ severity: 'success', summary: 'Success', detail: 'Tournament updated successfully', life: 3000 })
          fetchTournaments()
          closeEditDialog()
        } else {
          throw new Error(response.data.message)
        }
      } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to update tournament', life: 3000 })
      }
    }

    const deleteTournament = async (tournamentId) => {
      try {
        const response = await axios.delete(import.meta.env.VITE_API_URL_TOURNAMENT + `/tournaments/${tournamentId}`)
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

    const viewTournamentDetails = (tournament) => {
      router.push(`/admin/tournaments/${tournament.tournamentID}`)
    }

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString()
    }

    onMounted(() => {
      fetchTournaments()
    })
    onMounted(() => {
      fetchTournaments()
    })

    return {
      upcomingTournaments,
      ongoingTournaments,
      loading,
      fetchTournaments,
      deleteTournament,
      formatDate,
      editDialogVisible,
      editingTournament,
      openEditDialog,
      closeEditDialog,
      saveTournament,
      createTournament,
      creatingTournament,
      createDialogVisible,
      openCreateDialog,
      closeCreateDialog,
      viewTournamentDetails
    }
  }
}
</script>
<style>
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


.tournament-header {
  display: flex;
  align-items: center;
  width: 100%;
  margin: 20px 0;
}

.line {
  flex-grow: 1;
  height: 2px;
  background-color: #121111;
}

.text {
  padding: 15px;
  background-color: #333;
  color: white;
  border-radius: 15px;
  font-size: large;
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
</style>