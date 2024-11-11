<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const isAdminLoggedIn = ref(false)
const adminEmail = ref('')

onMounted(() => {
  checkAdminAuthStatus()
})

// Check for admin authentication status
const checkAdminAuthStatus = () => {

  //name of token to be replaced when arsh change login
  const token = sessionStorage.getItem('adminAuthToken')
   //name of token to be replaced when arsh change login
  const email = sessionStorage.getItem('adminEmail')
  isAdminLoggedIn.value = !!token
  adminEmail.value = email || ''
}

// Logout for admin
const handleAdminLogout = () => {
  sessionStorage.removeItem('adminAuthToken')
  sessionStorage.removeItem('adminEmail')
  delete axios.defaults.headers.common['Authorization']
  isAdminLoggedIn.value = false
  // Redirect to login page
  router.push('/login')
}
</script>
  
<template>
  <div class="grid align-items-center justify-content-center">
    <div class="col-12 md:col-12 lg:col-10">
      <Menubar>
        <!-- header -->
        <template #start>
          <template v-if = "!isAdminLoggedIn">
            <a class='line-remove'>
            <router-link :to="{ name: 'Home' }" style="margin-left: 100px;">
              <span style="text-decoration: none; font-weight: bold;">Check-M8</span>
            </router-link>
          </a>
          </template>
          <template v-else>
            <a class='line-remove'>
            <router-link :to="{ name: 'admin' }" style="margin-left: 100px;">
              <span style="text-decoration: none; font-weight: bold;">Check-M8</span>
            </router-link>
            </a>
          </template>
        </template>  

        <template #end>
          <!-- Show these items only when admin is logged out -->
          <template v-if="!isAdminLoggedIn">
            <a class="line-remove" style="margin-right: 50px;">
              <router-link :to="{ name: 'Home' }">
                <span style="text-decoration: none;">Home</span>
              </router-link>
            </a>
            <a class="line-remove" style="margin-right: 50px;">
              <router-link :to="{ name: 'login' }">
                <Button class="login-btn">Log In</Button>
              </router-link>
            </a>
          </template>

          <!-- Show these items only when admin is logged in -->
          <template v-else>
            <!-- redundant -->
            <!-- <a class="line-remove" style="margin-right: 50px;">
              <router-link :to="{ name: 'admin' }">
                <span style="text-decoration: none;">Home</span>
              </router-link>
            </a> -->
            <a class="line-remove" style="margin-right: 50px;">
              <router-link :to="{ name: 'adminTournament' }">
                <span style="text-decoration: none; font-weight: bold;">Tournaments</span>
              </router-link>
            </a>
            <Button 
              class="logout-btn" 
              style="margin-right: 50px;"
              @click="handleAdminLogout"
              label="Log Out"
            >
            </Button>
          </template>
        </template>
      </Menubar>
    </div>
  </div>
</template>
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap');


a {
  text-decoration: none !important;
  color: black !important;
  font-size: large;
  font-family: "Noto Sans", sans-serif;
  margin-top: 20px;
  
}

.login-btn{
  border-radius: 10px;
  background-color: #2D2D2D;
  max-height: 50px;
  max-width: 100px;
}




</style>