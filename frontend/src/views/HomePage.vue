<template>
  <div class="page">
    <h1>Welcome 👋</h1>

    <p v-if="!isAuth">
      Please login or signup to manage your tasks.
    </p>

    <p v-else>
      Welcome back! Go to your tasks and stay productive 🚀
    </p>
  </div>
</template>

<script>
import { getUserInfo } from "../js/auth";

export default {
  data() {
    return {
      isAuth: false,
    };
  },

  async created() {
    this.isAuth = !!(await getUserInfo());
  },
};
</script>

<style>
h1, p {
 color: black;
}
</style>