import { createStore } from 'vuex';

export default createStore({
  state: {
    activeLink: null,
  },
  mutations: {
    setActiveLink(state, index) {
      state.activeLink = index;
    },
  },
});