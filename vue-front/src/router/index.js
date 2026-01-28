import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import SocialCallbackView from "../views/SocialCallbackView.vue";

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", name: "home", component: HomeView },
    { path: "/oauth/callback", name: "oauth-callback", component: SocialCallbackView },
  ],
});
