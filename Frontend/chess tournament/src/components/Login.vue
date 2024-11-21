<template>
  <div class="login-container">
    <Toast />
    <div class="chess-board">
      <img src="../assets/Chessboard.png" class="chessboard-img" alt="Chessboard" />
      <div style="margin-left: 10rem;">
        <router-link :to="{ name: 'signup' }">
          <Button class="signup-box" label="New User? Sign up - and start playing chess!" style="margin: 0;">
          </Button>
        </router-link>
      </div>
    </div>

    <div class="login-box">
      <h1>Log In</h1>
      <form @submit.prevent="handleSubmit" class="login-form">
        <div class="p-input-filled">
          <InputText v-model="email" type="text" placeholder="Email" class="w-full"
            :class="{ 'p-invalid': submitted && !email }" />
        </div>

        <div class="p-input-filled p-input-icon-right">
          <i :class="[passwordVisible ? 'pi pi-eye-slash' : 'pi pi-eye']" @click="togglePassword"
            style="cursor: pointer"></i>
          <InputText v-model="password" :type="passwordVisible ? 'text' : 'password'" placeholder="Password"
            class="w-full" :class="{ 'p-invalid': submitted && !password }" />
        </div>

        <div
          style="display: flex; justify-content: space-between; align-items: center; width: 100%; margin-top: 1rem;margin-bottom: 1rem;">
          <div style="display: flex; align-items: center;">
            <Checkbox v-model="checked" binary variant="filled" label="Remember me" />&nbsp;
            <p style="color: #FBFBFB; margin: 0;">Remember Me?</p>
          </div>
          <div>
            <p style="color: #FBFBFB; font-style: oblique; margin: 0;">Forgot Password?</p>
          </div>
        </div>


        <Button severity="info" label="Submit" type="submit" class="w-full rounded" :loading="loading"
          style="background-color: #FBFBFB; color: #2D2D2D"></Button>
      </form>
    </div>


  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useToast } from 'primevue/usetoast'

const router = useRouter()
const toast = useToast()


// Form data
const email = ref('')
const uuid = ref('')
const isAdmin = ref(false)
const password = ref('')
const passwordVisible = ref(false)
const submitted = ref(false)
const loading = ref(false)
const checked = ref(false)


// Toggle password visibility
const togglePassword = () => {
  passwordVisible.value = !passwordVisible.value
}


// Handle form submission
const handleSubmit = async () => {
  submitted.value = true

  // Check if required fields are filled
  if (!email.value || !password.value) {
    toast.add({
      severity: 'error',
      summary: 'Login Failed',
      detail: 'Please fill in all required fields',
      life: 3000
    })
    return
  }

  loading.value = true

  try {
    const response = await axios.post(import.meta.env.VITE_API_URL_USERS + `/login`, {
      email: email.value,
      password: password.value,
    })


    // Show success message
    if (response.data.success) {
      const { token, email } = response.data.content

      // Store token in sessionStorage
      sessionStorage.setItem('authToken', token)

      // Store user info
      sessionStorage.setItem('userEmail', email)

      // Set axios default authorization header for future requests
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

      console.log('Login successful', token, email)

      toast.add({
        severity: 'success',
        summary: 'Login Successful',
        detail: 'Your account has logged in successfully!',
        life: 3000
      })

      if (await checkAdminAuthStatus == true) {
        // Redirect after successful login
        setTimeout(() => {
          router.push('/admin')
        }, 1000)
      }
      // Redirect after successful login
      setTimeout(() => {
        router.push('/user')
      }, 1000)
    }

  } catch (error) {

    const errorMessage = error.response?.data?.message || 'An error occurred during login'

    toast.add({
      severity: 'error',
      summary: 'Login Failed',
      detail: errorMessage,
      life: 3000
    })
  } finally {
    loading.value = false
  }
}

// Check for admin authentication status
const checkAdminAuthStatus = async () => {
  const response = await axios.get(import.meta.env.VITE_API_URL_MIDDLEWARE + "/jwt", {
    headers: { Authorization: token.value, Origin: import.meta.env.VITE_API_URL_ORIGIN }
  })
  if (response.data.success) {

    isAdmin.value = response.data.content.isAdmin
    if (isAdmin.value === 1) {
      uuid.value = response.data.content.uuid
      localStorage.setItem('uuid', uuid)
      localStorage.setItem('isAdmin', isAdmin)
      console.log(token.value, uuid.value)
      return true
    }
    return false
  }
  // //name of token to be replaced when arsh change login
  // const token = sessionStorage.getItem('adminAuthToken')
  // //name of token to be replaced when arsh change login
  // const email = sessionStorage.getItem('adminEmail')
  // isAdminLoggedIn.value = !!token
  // adminEmail.value = email || ''
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

.signup-box {
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