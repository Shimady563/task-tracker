<template>
  <div class="form">
    <h2>Signup</h2>

    <input v-model="username" placeholder="Username" required />
    <input v-model="email" placeholder="Email" type="email" required />
    <input v-model="password" placeholder="Password" type="password" required />
    <input v-model="phoneNumber" placeholder="Phone Number" />
    <button type="submit" @click="signup">Sign Up</button>
    <p class="error" v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import { signup } from '../js/auth';

export default {
  data() {
    return {
      username: '',
      email: '',
      password: '',
      phoneNumber: '',
      message: ''
    };
  },
  methods: {
    async signup() {
      try {
          await signup({
            username: this.username,
            email: this.email,
            password: this.password,
            phoneNumber: this.phoneNumber,
          });

          this.$router.push("/").then(() => window.location.reload());
        } catch (err) {
          this.errorMessage = err.message || "Ошибка регистрации";
        }
    }
  }
};
</script>
