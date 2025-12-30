<template>
  <div class="app">
    <header class="header">
      <div class="header-inner">
        <h2 class="logo">Task Tracker</h2>

        <nav class="nav">
          <router-link to="/">Home</router-link>

          <template v-if="isAuth">
            <router-link to="/tasks">Tasks</router-link>
            <router-link to="/tasks/create">Create</router-link>
            <router-link to="/profile">Profile</router-link>
            <button class="link-btn" @click="logout">Logout</button>
          </template>

          <template v-else>
            <router-link to="/login">Login</router-link>
            <router-link to="/signup">Signup</router-link>
          </template>
        </nav>
      </div>
    </header>

    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script>
import { getUserInfo, logoutUser } from "./js/auth";

export default {
  data() {
    return { isAuth: false };
  },

  async created() {
    this.isAuth = !!(await getUserInfo());
  },

  methods: {
    async logout() {
      await logoutUser();
      this.isAuth = false;
      this.$router.push("/login");
    },
  },
};
</script>

<style>
/* ===== Base ===== */
body {
  margin: 0;
  background: #f3f4f6;
}

.app {
  font-family: Inter, Arial, sans-serif;
}

/* ===== Header ===== */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: #111827;
  color: white;
  z-index: 1000;
}

.header-inner {
  max-width: 1200px;
  height: 100%;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  margin: 0;
  font-size: 20px;
  color: white;
}

/* ===== Nav ===== */
.nav {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav a {
  color: #d1d5db;
  text-decoration: none;
  font-weight: 500;
}

.nav a.router-link-active {
  color: white;
  border-bottom: 2px solid #3b82f6;
}

.nav a:hover {
  color: white;
}

.link-btn {
  background: none;
  border: none;
  color: #ef4444;
  cursor: pointer;
  font-weight: 500;
}

/* ===== Content ===== */
.content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 96px 24px 24px; /* ВАЖНО: отступ под fixed header */
}
</style>
