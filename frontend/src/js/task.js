import { TRACKER_URL } from "./base";
import { fetchJson } from "./base";

export async function getTasks({
  limit = 10,
  offset = 0,
  sort = "ID_ASC",
} = {}) {
  const params = new URLSearchParams({
    limit,
    offset,
    sort,
  });

  return fetchJson(`${TRACKER_URL}/tasks?${params.toString()}`, {
    method: "GET",
    credentials: "include",
  });
}

export async function createTask(payload) {
  return fetchJson(`${TRACKER_URL}/tasks`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      title: payload.title,
      description: payload.description,
      createdAt: payload.createdAt ?? new Date().toISOString(),
      deadline: payload.deadline,
    }),
  });
}

export async function updateTask(id, payload) {
  return fetchJson(`${TRACKER_URL}/tasks/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      title: payload.title,
      description: payload.description,
      status: payload.status,
      deadline: payload.deadline,
    }),
  });
}