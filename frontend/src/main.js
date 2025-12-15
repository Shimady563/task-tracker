import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'

async function tryRefreshAuth() {
  try {
    return await refreshAuth();
  } catch (e) {
    return false;
  }
}

function setupFetchInterceptor() {
  const originalFetch = window.fetch;

  window.fetch = async (input, init = {}) => {
    const url = typeof input === 'string' ? input : input.url;

    const options = {
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        ...(init.headers || {}),
      },
      ...init,
    };

    let response = await originalFetch(input, options);

    if (response.status === 401 && !/\/(login|refresh|logout)/.test(url)) {
      const refreshed = await tryRefreshAuth();

      if (refreshed) {
        response = await originalFetch(input, options);
      } else {
        await logoutUser();
        router.push({ name: 'Login' }).catch(() => {});
        throw new Error('Сессия истекла, требуется повторный вход');
      }
    }

    return response;
  };
}

(async () => {
  await tryRefreshAuth();
  setupFetchInterceptor();

  const app = createApp(App);
  app.use(router);
  app.mount('#app');
})();