<template>
  <UserNavbar/>
    <div class="tournament-dashboard">
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
          <Column header="Actions">
            <template #body="slotProps">
              <Button label="View Details" @click="viewTournamentDetails(slotProps.data)" severity="info" text />
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
  import Dialog from 'primevue/dialog'
  import InputText from 'primevue/inputtext'
  import InputNumber from 'primevue/inputnumber'
  import Calendar from 'primevue/calendar'
  import { useRouter } from 'vue-router'
  import UserNavbar from '../../components/UserNavbar.vue'
  
  
  
  export default {
    name: 'TournamentDashboard',
    components: {
      DataTable,
      Column,
      Button,
      Menubar,
      Toast,
      InputText,
      InputNumber,
      Calendar,
      Dialog,
      useRouter,
      UserNavbar
    },
    setup() {
      const toast = useToast()
      const upcomingTournaments = ref([])
      const ongoingTournaments = ref([])
      const loading = ref(false)
      const token = ref('')
      const uuid = ref('')
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
    
      const viewTournamentDetails = (tournament) => {
        sessionStorage.setItem("tournamentId",tournament.tournamentID)
        router.push(`/user/tournaments/${tournament.tournamentID}`)
      }
  
      const formatDate = (dateString) => {
        return new Date(dateString).toLocaleDateString()
      }

      const validateUser = async () => {
        const response = await axios.get(import.meta.env.VITE_API_URL_MIDDLEWARE + "/jwt" , {
          headers: { jwt: token.value, Origin: import.meta.env.VITE_API_URL_ORIGIN }
        }) 
        if (response.data.success) {
          uuid.value = response.data.content.uuid
          localStorage.setItem('uuid', uuid)
          console.log(token.value,uuid.value)
          return true
        } 
      }
      onMounted(async () => {
        fetchTournaments()
        token.value = sessionStorage.getItem('authToken')
        validateUser()
        uuid.value = sessionStorage.getItem('uuid')
      })

  
      return {
        upcomingTournaments,
        ongoingTournaments,
        loading,
        fetchTournaments,
        formatDate,
        viewTournamentDetails,
        validateUser,
        token,
        uuid
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