import { AUTH_URL } from "./base";
import { fetchJson } from "./base";

export async function login(payload) {
  return fetchJson(`${AUTH_URL}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(payload),
  });
}

export async function signup(payload) {
  return fetchJson(`${AUTH_URL}/signup`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(payload),
  });
}

export async function getUserInfo() {
  try {
    const data = await fetchJson(`${AUTH_URL}/me`, {
      method: "GET",
      credentials: "include",
    });

    return {
      id: data.id,
      username: data.username,
      email: data.email,
      phoneNumber: data.phoneNumber,
    };
  } catch (err) {
    return null;
  }
}

export async function logoutUser() {
  try {
    await fetchJson(`${AUTH_URL}/logout`, {
      method: "POST",
    });

    localStorage.removeItem("seenWelcome");
    return true;
  } catch (err) {
    return false;
  }
}

export async function refreshAuth() {
  try {
    await fetchJson(`${AUTH_URL}/refresh`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    });

    return true;
  } catch {
    return false;
  }
}