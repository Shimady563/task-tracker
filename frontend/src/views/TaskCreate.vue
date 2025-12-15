<template>
  <div class="page">
    <h1>Create Task</h1>

    <form class="form" @submit.prevent="onCreate">
      <input v-model="title" placeholder="Title" required />
      <textarea v-model="description" placeholder="Description" required />
      <input v-model="deadline" type="datetime-local" required />

      <button type="submit">Create Task</button>
    </form>

    <p class="message" v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import { createTask } from '../js/task';

export default {
  data() {
    return {
      title: "",
      description: "",
      deadline: "",
      message: "",
    };
  },

  methods: {
    async onCreate() {
      try {
        await createTask({
          title: this.title,
          description: this.description,
          deadline: new Date(this.deadline).toISOString(),
        });

        this.message = "Task created successfully!";
        this.title = "";
        this.description = "";
        this.deadline = "";
      } catch (err) {
        this.message = err.message || "Failed to create task";
      }
    },
  },
};
</script>
