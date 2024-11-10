<template>
    <div class="login-container">
      <Toast />
      <div class="chess-board">
        <img src="../assets/Chessboard.png" class="chessboard-img" alt="Chessboard" />
      </div>
      
      <div class="login-box">
        <h1>Create your Check-M8 account</h1>
        <form @submit.prevent="handleSubmit" class="login-form">
          <div class="p-input-filled">
            <InputText 
              v-model="email" 
              type="text" 
              placeholder="Email" 
              class="w-full"
              :class="{ 'p-invalid': (submitted && !email) || (submitted && !isValidEmail) }" 
              @blur="validateEmail"
            />
            <small class="text-red-500" v-if="submitted && !email">
            Email is required
          </small>
          <small class="text-red-500" v-if="submitted && email && !isValidEmail">
            Please enter a valid email address
          </small>
          </div>
  
          <div class="p-input-filled p-input-icon-right">
            <i 
              :class="[passwordVisible ? 'pi pi-eye-slash' : 'pi pi-eye']" 
              @click="togglePassword"
              style="cursor: pointer" 
            ></i>
            <InputText 
              v-model="password" 
              :type="passwordVisible ? 'text' : 'password'" 
              placeholder="Password" 
              class="w-full"
              :class="{ 'p-invalid': (submitted && !password) || (submitted && !isValidPassword) }" 
              @blur="validatePassword"
            />
            <small class="text-red-500" v-if="submitted && !password">
            Password is required
          </small>
          <small class="text-red-500" v-if="submitted && password && !isValidPassword">
            {{ passwordError }}
          </small>
          </div>
          
          <Button 
            severity="info"
            label="Sign up" 
            class="w-full rounded" 
            :loading="loading" 
            style="background-color: #FBFBFB; color: #2D2D2D; margin-top: 2rem;"
            @click="handleSubmit"
          ></Button>

        </form>
      </div>

      
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  import { useToast } from 'primevue/usetoast'
  import Button from 'primevue/button'
  
  const toast = useToast()
  const email = ref('')
  const password = ref('')
  const passwordVisible = ref(false)
  const submitted = ref(false)
  const loading = ref(false)
  const passwordError = ref('')
  
  const isValidEmail = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email.value)
})

const isValidPassword = computed(() => {
  if (password.value.length < 8) {
    passwordError.value = 'Password must be at least 8 characters long'
    return false
  }
  if (!/(?=.*[A-Z])/.test(password.value)) {
    passwordError.value = 'Password must contain at least one uppercase letter'
    return false
  }
  if (!/(?=.*[0-9])/.test(password.value)) {
    passwordError.value = 'Password must contain at least one number'
    return false
  }
  if (!/(?=.*[!@#$%^&*])/.test(password.value)) {
    passwordError.value = 'Password must contain at least one special character'
    return false
  }
  return true
})

const validateEmail = () => {
  if (email.value && !isValidEmail.value) {
    toast.add({
      severity: 'warn',
      summary: 'Invalid Email',
      detail: 'Please enter a valid email address',
      life: 3000
    })
  }
}

const validatePassword = () => {
  if (password.value && !isValidPassword.value) {
    toast.add({
      severity: 'warn',
      summary: 'Invalid Password',
      detail: passwordError.value,
      life: 3000
    })
  }
}

const togglePassword = () => {
  passwordVisible.value = !passwordVisible.value
}

const handleSubmit = async () => {
  submitted.value = true

  if (!email.value || !password.value) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Please fill in all fields',
      life: 3000
    })
    return
  }

  if (!isValidEmail.value || !isValidPassword.value) {
    toast.add({
      severity: 'error',
      summary: 'Validation Error',
      detail: 'Please correct the form errors',
      life: 3000
    })
    return
  }

  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Account created successfully',
      life: 3000
    })
    
    // Navigate to login page after successful signup
    router.push({ name: 'login' })
  } 
  
  catch (error) {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: 'Registration failed',
      life: 3000
    })
  } finally {
    loading.value = false
  }
}
  </script>



<style scoped>
.login-container {
  display: flex;
  align-items: center;
  min-height: 100vh;
  gap: 2rem;
  padding: 2rem;
  background-color: white;
}


.login-box {
  background-color: #2D2D2D;
  padding: 2.5rem;
  border-radius: 10px;
  width: 600px;
  height: auto;
}

.signup-box{
  background-color: #2D2D2D;
  width: 490px;
  height: 81px;
  color: #FBFBFB;
  border-radius: 10px;
  
}

.login-box h1 {
  color: #FBFBFB;
  margin-bottom: 2rem;
  text-align: center;
  font-size: 2rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.p-input-filled .p-inputtext {
  background-color: #fff;
  border: 1px solid #ced4da;
  border-radius: 6px;
  padding: 0.75rem 1rem;
  width: 100%;
}

.p-input-filled .p-inputtext:hover {
  border-color: #93a3b8;
}

.p-input-filled .p-inputtext:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
}

.p-input-filled .p-inputtext.p-invalid {
  border-color: #ef4444;
}


@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }

  .login-box {
    width: 100%;
    max-width: 400px;
  }


}
</style>