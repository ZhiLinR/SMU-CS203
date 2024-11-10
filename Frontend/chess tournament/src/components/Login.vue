<template>
    <div class="login-container">
      <Toast />
      <div class="chess-board">
        <img src="../assets/Chessboard.png" class="chessboard-img" alt="Chessboard" />
      </div>
      <div class="login-box">
        <h1>Log In</h1>
        <form @submit.prevent="handleSubmit" class="login-form">
          <div class="p-input-filled">
            <InputText 
              v-model="username" 
              type="text" 
              placeholder="Username" 
              class="w-full"
              :class="{ 'p-invalid': submitted && !username }" 
            />
          </div>
  
          <div class="p-input-filled p-input-icon-right">
            <i 
              :class="[passwordVisible ? 'pi pi-eye-slash' : 'pi pi-eye']" 
              @click="togglePassword"
              style="cursor: pointer" 
            />
            <InputText 
              v-model="password" 
              :type="passwordVisible ? 'text' : 'password'" 
              placeholder="Password" 
              class="w-full"
              :class="{ 'p-invalid': submitted && !password }" 
            />
          </div>
  
          <Button 
            severity="info"
            label="Submit" 
            class="w-full rounded" 
            :loading="loading" 
          />
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useToast } from 'primevue/usetoast'
  import Button from 'primevue/button';
  
  const toast = useToast()
  const username = ref('')
  const password = ref('')
  const passwordVisible = ref(false)
  const submitted = ref(false)
  const loading = ref(false)
  
  const togglePassword = () => {
    passwordVisible.value = !passwordVisible.value
  }
  
  const handleSubmit = async () => {
    submitted.value = true
  
    if (!username.value || !password.value) {
      toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Please fill in all fields',
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
        detail: 'Login successful',
        life: 3000
      })
    } catch (error) {
      toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Login failed',
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
  width: 400px;
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