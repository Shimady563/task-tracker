<template>
  <div class="page">
    <h1>Edit Task</h1>

    <form v-if="loaded" class="form" @submit.prevent="onUpdate">
      <input v-model="title" placeholder="Title" required />
      <textarea v-model="description" placeholder="Description" required />

      <select v-model="status">
        <option value="TODO">TODO</option>
        <option value="IN_PROGRESS">IN PROGRESS</option>
        <option value="DONE">DONE</option>
      </select>

      <input v-model="deadline" type="datetime-local" required />
      <button type="submit">Update Task</button>
    </form>

    <p class="message" v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import { getTasks, updateTask } from '../js/task';

export default {
  props: ["id"],

  data() {
    return {
      title: "",
      description: "",
      status: "TODO",
      deadline: "",
      message: "",
      loaded: false,
    };
  },

  async created() {
    try {
      const data = await getTasks();
      const task = data.content.find(t => t.id == this.id);

      if (!task) {
        this.message = "Task not found";
        return;
      }

      this.title = task.title;
      this.description = task.description;
      this.status = task.status;
      this.deadline = task.deadline.slice(0, 16);
      this.loaded = true;
    } catch (err) {
      this.message = err.message || "Failed to load task";
    }
  },

  methods: {
    async onUpdate() {
      try {
        await updateTask(this.id, {
          title: this.title,
          description: this.description,
          status: this.status,
          deadline: new Date(this.deadline).toISOString(),
        });

        this.message = "Task updated successfully!";
      } catch (err) {
        this.message = err.message || "Failed to update task";
      }
    },
  },
};
</script>
