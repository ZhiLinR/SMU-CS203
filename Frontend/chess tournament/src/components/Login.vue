<script>


const onFormSubmit = (data) => {
  console.log('Form submitted:', data)
}

</script>
<template>
    <div class="login-container">
    <div class="chess-board">
       <img src = "../assets/Chessboard.png" class = "chessboard-img" alt = "Chessboard"/>
    </div>
    <div class="login-outer-box">
      <div class="login-header">
        <h1>Log In</h1>
      </div>
      <div class="form-login">
        <!-- <Form :resolver @submit="onFormSubmit" class="gap-4 w-full">
            <FormField v-slot="$field" as="section" name="username" initialValue="" class="flex flex-col gap-2">
            <InputText 
                type="text" 
                placeholder="Username" 
                class="input-box"  
            />
            <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                {{ $field.error?.message }}
            </Message>
            </FormField>
            <FormField v-slot="$field" asChild name="password" initialValue="">
            <section class="flex flex-col gap-2">
                <Password 
                type="text" 
                placeholder="Password" 
                :feedback="false" 
                toggleMask 
                fluid 
                class="input-box"  
                />
                <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                {{ $field.error?.message }}
                </Message>
            </section>
            </FormField>

          <Button type="submit" severity="secondary" label="Log In" class="w-full" style="background-color: #FBFBFB; color: #2D2D2D; border-radius: 5px;margin-top:15 px;">
          </Button>

        </Form> -->
        <Form :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56">
    <FormField v-slot="$field" as="section" name="username" initialValue="" class="flex flex-col gap-2">
        <InputText type="text" placeholder="Username" />
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
    </FormField>
    <FormField v-slot="$field" asChild name="password" initialValue="">
        <section class="flex flex-col gap-2">
            <Password type="text" placeholder="Password" :feedback="false" toggleMask fluid />
            <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
        </section>
    </FormField>
    <Button type="submit" severity="secondary" label="Submit" />
</Form>
      </div>
    </div>
</div>
</template>
<style>
h1{
    color: #FBFBFB
}

.login-container{
    display:flex;
}


.login-outer-box{
    padding: 2rem;
    background-color: #2D2D2D;
    margin: 50px;
    border-radius: 10px;
}

.form-login {
    justify-content: center;
    align-items: center;
}

.input-box {
    width: 553px;
    height: 46px;
    box-sizing: border-box; /* Ensure padding/border don’t affect size */
}



</style>