<template>
  <div class="tasks-page">
    <div class="tasks-header">
      <h1>My Tasks</h1>
      <router-link class="btn-primary" to="/tasks/create">
        + New Task
      </router-link>
    </div>

    <div v-if="tasks.length" class="tasks-grid">
      <div class="task-card" v-for="task in tasks" :key="task.id">
        <div class="task-top">
          <h3 class="task-title">{{ task.title }}</h3>
          <span :class="['status', task.status.toLowerCase()]">
            {{ task.status }}
          </span>
        </div>

        <p class="task-desc">
          {{ task.description }}
        </p>

        <div class="task-bottom">
          <span class="deadline">
            ⏰ {{ formatDate(task.deadline) }}
          </span>

          <router-link
            class="edit-link"
            :to="`/tasks/${task.id}/edit`"
          >
            Edit →
          </router-link>
        </div>
      </div>
    </div>

    <p v-else class="empty">No tasks yet. Create your first one 🚀</p>
  </div>
</template>

<script>
import { getTasks } from "../js/task";

export default {
  data() {
    return { tasks: [] };
  },

  async created() {
    const data = await getTasks();
    this.tasks = data.content || [];
  },

  methods: {
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
  },
};
</script>

<style scoped>
/* ===== Page ===== */
.tasks-page {
  max-width: 1100px;
  margin: 0 auto;
}

/* ===== Header ===== */
.tasks-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  color: black;
}

.tasks-header h1 {
  margin: 0;
  font-size: 28px;
}

/* ===== Button ===== */
.btn-primary {
  background: #3b82f6;
  color: white;
  padding: 10px 16px;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
}

.btn-primary:hover {
  background: #2563eb;
}

/* ===== Grid ===== */
.tasks-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* ===== Card ===== */
.task-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.06);
  display: flex;
  flex-direction: column;
  gap: 12px;
  transition: transform .15s ease, box-shadow .15s ease;
}

.task-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 35px rgba(0,0,0,0.08);
}

/* ===== Card content ===== */
.task-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.task-title {
  margin: 0;
  font-size: 18px;
}

.task-desc {
  color: #6b7280;
  font-size: 14px;
  line-height: 1.4;
}

/* ===== Status ===== */
.status {
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

.status.todo {
  background: #e5e7eb;
  color: #374151;
}

.status.in_progress {
  background: #fef3c7;
  color: #92400e;
}

.status.done {
  background: #dcfce7;
  color: #166534;
}

/* ===== Bottom ===== */
.task-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.deadline {
  color: #6b7280;
}

.edit-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 500;
}

.edit-link:hover {
  text-decoration: underline;
}

/* ===== Empty ===== */
.empty {
  text-align: center;
  color: #6b7280;
  margin-top: 60px;
}
</style>
