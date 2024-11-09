
<template>
  <div class="grid align-items-center justify-content-center">
    <div class="col-12 md:col-12 lg:col-10">
      <Menubar >
        <template #start>
            <a class='line-remove'>
            <router-link :to="{ name: 'admin' }" style="margin-left: 100px;">
              <span style="text-decoration: none; font-weight: bold;">Check-M8</span></router-link>
          </a>
        </template>
        <template #end>
          <a class='line-remove' style="margin-right: 50px;">
            <router-link :to="{ name: 'admin' }">
              <span style="text-decoration: none;">Home</span></router-link>
          </a>
          <a class='line-remove' style="margin-right: 50px;">
            <router-link :to="{ name: 'admin' }">
              <span style="text-decoration: none;font-weight: bold; ">Tournament</span></router-link>
          </a>
          <a class='line-remove' style="margin-right: 50px;">
            <router-link :to="{ name: 'login' }">
                <Button class="login-btn">Log In</Button>
            </router-link>
          </a>
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