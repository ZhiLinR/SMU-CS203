<template>
  <div class="signup-container">
    <Toast />
    <div class="chess-board">
      <img src="../assets/Chessboard.png" class="chessboard-img" alt="Chessboard" />
    </div>
    
    <div class="login-box">
      <h1>Create your Check-M8 account</h1>
      <form @submit.prevent="handleSubmit" class="signup-form">
        <!-- name -->
        <div class="p-input-filled">
          <InputText 
            v-model="name" 
            type="text" 
            placeholder="Your account Name" 
            class="w-full"
            :class="{ 'p-invalid': submitted && !name }" 
          ></InputText>
          <small class="text-red-500" v-if="submitted && !name">
            Name is required
          </small>
        </div>

        <!-- email -->
        <div class="p-input-filled">
          <InputText 
            v-model="email" 
            type="text" 
            placeholder="Email" 
            class="w-full"
            :class="{ 'p-invalid': submitted && !email }" 
          />
          <small class="text-red-500" v-if="submitted && !email">
            Email is required
          </small>
        </div>

        <!-- password -->
        <div class="p-input-filled p-input-icon-right">
          <i 
            :class="[passwordVisible ? 'pi pi-eye-slash' : 'pi pi-eye']" 
            @click="togglePassword"
            style="cursor: pointer;"
          />
          <InputText 
            v-model="password" 
            :type="passwordVisible ? 'text' : 'password'" 
            placeholder="Password" 
            class="w-full"
            :class="{ 'p-invalid': submitted && !password }" 
          ></InputText>
          <small class="text-red-500" v-if="submitted && !password">
            Password is required
          </small>
        </div>
        
        <!-- sign up button -->
        <Button 
          severity="info"
          label="Sign up" 
          class="w-full rounded" 
          :loading="loading" 
          style="background-color: #FBFBFB; color: #2D2D2D; margin-top: 2rem;"
          type="submit"
        ></Button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useToast } from 'primevue/usetoast'

const router = useRouter()
const toast = useToast()

// Form data
const name = ref('')
const email = ref('')
const password = ref('')
const passwordVisible = ref(false)
const submitted = ref(false)
const loading = ref(false)

// Toggle password visibility
const togglePassword = () => {
  passwordVisible.value = !passwordVisible.value
}

// Handle form submission
const handleSubmit = async () => {
  submitted.value = true
  
  // Check if required fields are filled
  if (!name.value || !email.value || !password.value) {
    return
  }

  loading.value = true

  try {
    const signup = await axios.post(import.meta.env.VITE_API_URL_USER + `/register`, {
      email: email.value,
      password: password.value,
      name: name.value,
      isAdmin: 0,
    })

    
    // Show success message
    if (signup.data.success){
      toast.add({
      severity: 'success',
      summary: 'Registration Successful',
      detail: 'Your account has been created successfully!',
      life: 3000
    })
     // Redirect to login page after successful registration
     setTimeout(() => {
      router.push('/login')
    }, 2000)

    }
    
  } catch (error) {
    // Handle different types of errors
    const errorMessage = error.response?.data?.message || 'An error occurred during registration'
    
    toast.add({
      severity: 'error',
      summary: 'Registration Failed',
      detail: errorMessage,
      life: 3000
    })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@import url('../css/signup.css');
</style>