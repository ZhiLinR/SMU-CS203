<script>



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
                <Button type="submit" severity="secondary" label="Submit"></Button>
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
    max-width: 620px;
    max-height: 770px;
    margin: 50px;
}

</style>