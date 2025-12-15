import { createRouter, createWebHistory } from 'vue-router'
import { getUserInfo } from '../js/auth'; 
import HomePage from '../views/HomePage.vue'
import LoginPage from '../views/LoginPage.vue';
import RegistrationPage from '../views/RegistrationPage.vue';
import ProfilePage from '../views/ProfilePage.vue';
import TasksList from '../views/TasksList.vue';
import TaskCreate from '../views/TaskCreate.vue';
import TaskEdit from '../views/TaskEdit.vue';

const routes = [
  { path: '/', component: HomePage },
  { path: '/login', component: LoginPage },
  { path: '/signup', component: RegistrationPage },
  { path: '/profile', component: ProfilePage },
   { path: '/tasks', component: TasksList, meta: { requiresAuth: true } },
  { path: '/tasks/create', component: TaskCreate },
  { path: '/tasks/:id/edit', component: TaskEdit, props: true },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach(async (to) => {
  const isAuth = !!(await getUserInfo());

  if (!isAuth && to.meta.requiresAuth) {
    return "/login";
  }
});

export default router;