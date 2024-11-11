<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const isLoggedIn = ref(false)
const userEmail = ref('')

onMounted(() => {
  checkAuthStatus()
})

// Check authentication status if user is logged in
const checkAuthStatus = () => {
  const token = sessionStorage.getItem('authToken')
  const email = sessionStorage.getItem('userEmail')
  isLoggedIn.value = !!token
  userEmail.value = email || ''
}

// Logout when user is logged in
const handleLogout = () => {
  sessionStorage.removeItem('authToken')
  sessionStorage.removeItem('userEmail')
  delete axios.defaults.headers.common['Authorization']
  isLoggedIn.value = false
  //then go login
  router.push('/login')
}
</script>

<template>
  <div class="grid align-items-center justify-content-center">
    <div class="col-12 md:col-12 lg:col-10">
      <Menubar>
        <template #start>
          <a class='line-remove'>
            <router-link :to="{ name: 'user' }" style="margin-left: 100px;">
              <span style="text-decoration: none; font-weight: bold;">Check-M8</span>
            </router-link>
          </a>
        </template>
        
        <template #end>
          <!-- Show these items only when logged out -->
          <template v-if="!isLoggedIn">
            <a class='line-remove' style="margin-right: 50px;">
              <router-link :to="{ name: 'Home' }">
                <span style="text-decoration: none;">Home</span>
              </router-link>
            </a>
            <a class='line-remove' style="margin-right: 50px;">
              <router-link :to="{ name: 'login' }">
                <Button class="login-btn">Log In</Button>
              </router-link>
            </a>
          </template>

          <!-- Show these items only when logged in -->
          <template v-else>
            <a class='line-remove' style="margin-right: 50px;">
              <router-link :to="{ name: 'user' }">
                <span style="text-decoration: none;">Home</span>
              </router-link>
            </a>
            <a class='line-remove' style="margin-right: 50px;">
              <router-link :to="{ name: 'userProfile' }">
                <span style="text-decoration: none; font-weight: bold;">Profile</span>
              </router-link>
            </a>
            <Button 
              class="logout-btn" 
              style="margin-right: 50px;"
              @click="handleLogout"
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

  .logout-btn{
    border-radius: 10px;
    background-color: red;
    max-height: 50px;
    max-width: 100px;
  }
  
  
  
  </style>