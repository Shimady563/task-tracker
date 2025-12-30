<template>
  <div class="form">
    <h2>Login</h2>

    <input v-model="email" type="email" placeholder="Email" />
    <input v-model="password" type="password" placeholder="Password" />

    <button @click="login">Login</button>
    <p class="error" v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import { login } from '../js/auth';

export default {
  data() {
    return {
      email: '',
      password: '',
      message: ''
    };
  },
  methods: {
    async login() {
      try {
        await login({ email: this.email, password: this.password });
        this.$router.push("/").then(() => window.location.reload());
      } catch (err) {
        this.errorMessage = err.message || "Ошибка при входе";
      }
    }
  }
};
</script>

<style>
h2 {
  font-size: 2em;
  line-height: 1.1;
  color: black;
}
</style>