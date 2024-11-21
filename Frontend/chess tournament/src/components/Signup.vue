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
          />
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
            :class="{ 'p-invalid': submitted && !isValidEmail }" 
          />
          <small class="text-red-500" v-if="submitted && !email">
            Email is required
          </small>
          <small class="text-red-500" v-if="submitted && email && !isValidEmail">
            Please enter a valid email address
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
            :class="{ 'p-invalid': submitted && !isValidPassword }" 
          />
          <small class="text-red-500" v-if="submitted && !password">
            Password is required
          </small>
          <small class="text-red-500" v-if="submitted && password && !isValidPassword">
            Password must be at least 8 characters long and contain at least one number
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
        />
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
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

// Computed properties for validation
const isValidEmail = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email.value)
})

const isValidPassword = computed(() => {
  const passwordRegex = /^(?=.*[0-9]).{8,}$/
  return passwordRegex.test(password.value)
})

// Toggle password visibility
const togglePassword = () => {
  passwordVisible.value = !passwordVisible.value
}

// Validation function
const validateForm = () => {
  if (!name.value) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Name is required',
      life: 3000
    })
    return false
  }
  
  if (!email.value) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Email is required',
      life: 3000
    })
    return false
  }

  if (!isValidEmail.value) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Please enter a valid email address',
      life: 3000
    })
    return false
  }

  if (!password.value) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Password is required',
      life: 3000
    })
    return false
  }

  if (!isValidPassword.value) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Password must be at least 8 characters long and contain at least one number',
      life: 3000
    })
    return false
  }

  return true
}

// Handle form submission
const handleSubmit = async () => {
  submitted.value = true
  
  // Validate form before submission
  if (!validateForm()) {
    return
  }

  loading.value = true

  try {
    const signup = await axios.post(import.meta.env.VITE_API_URL_USERS + `/register`, {
      email: email.value,
      password: password.value,
      name: name.value,
      isAdmin: 0,
    })
    
    if (signup.data.success) {
      toast.add({
        severity: 'success',
        summary: 'Registration Successful',
        detail: 'Your account has been created successfully!',
        life: 3000
      })
      
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    }
  } catch (error) {
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