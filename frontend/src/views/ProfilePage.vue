<template>
  <div class="profile-page" v-if="user">
    <div class="profile-card">
      <div class="profile-header">
        <div class="avatar">
          {{ user.username.charAt(0).toUpperCase() }}
        </div>

        <div class="user-main">
          <h2>{{ user.username }}</h2>
          <p class="email">{{ user.username }}</p>
        </div>
      </div>

      <div class="profile-info">
        <div class="info-item">
          <span class="label">Username</span>
          <span class="value">{{ user.username }}</span>
        </div>

        <div class="info-item">
          <span class="label">Email</span>
          <span class="value">{{ user.email }}</span>
        </div>

        <div class="info-item">
          <span class="label">Phone</span>
          <span class="value">
            {{ user.phoneNumber || "—" }}
          </span>
        </div>
      </div>

      <div class="profile-actions">
        <button class="logout-btn" @click="logout">
          Logout
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { getUserInfo, logoutUser } from "../js/auth";

export default {
  data() {
    return {
      user: null,
    };
  },

  async created() {
    this.user = await getUserInfo();
  },

  methods: {
    async logout() {
      await logoutUser();
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
/* ===== Page ===== */
.profile-page {
  display: flex;
  justify-content: center;
  margin-top: 40px 16px;
}

/* ===== Card ===== */
.profile-card {
  background: white;
  width: 100%;
  max-width: 480px;
  min-width: 320px;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.08);
}

/* ===== Header ===== */
.profile-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  font-weight: 600;
}

.user-main h2 {
  margin: 0;
}

.email {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 14px;
}

/* ===== Info ===== */
.profile-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 12px;
  background: #f9fafb;
  border-radius: 8px;
}

.label {
  color: #6b7280;
  font-size: 13px;
}

.value {
  font-weight: 500;
  color: #6b7280;
}

/* ===== Actions ===== */
.profile-actions {
  margin-top: 24px;
}

.logout-btn {
  width: 100%;
  padding: 10px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
}

.logout-btn:hover {
  background: #dc2626;
}
</style>
