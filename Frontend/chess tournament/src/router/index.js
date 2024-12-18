import { createRouter, createWebHistory } from 'vue-router';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/Home/Home.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/admin/Admin.vue')
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('../views/user/UserTournaments.vue')
    },
    {
      path: '/admin/tournaments/:tournamentId',
      name: 'adminTournamentDetails',
      component: () => import('../views/admin/AdTournamentDetails.vue')
    },
    {
      path: '/user/tournaments/:tournamentId',
      name: 'userTournamentDetails',
      component: () => import('../views/user/UserJoinTournament.vue')
    },

    {
      path: '/user/profile',
      name: 'userProfile',
      component: () => import('../views/user/Profile.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login/Login.vue')
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('../views/signup/Signup.vue')
    },
    {
      path: '/public/tournaments',
      name: 'publicTournaments',
      component: () => import('../views/public/PublicTournaments.vue')
    },
    {
      path: '/:tournamentId',
      name: 'publicTournament',
      component: () => import('../views/public/PublicTournaments.vue')
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    // If the hash exists in the URL, scroll to the hash
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth',
      };
    }

    // Otherwise, scroll to top
    return { top: 0, behavior: 'smooth' };
  },
})

export default router
