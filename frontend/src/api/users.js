import { apiFetch } from "./client";

export function getMe() {
    return apiFetch("/api/users/me")
}

export function getProfile(username) {
    return apiFetch(`/api/users/${username}`)
}

export function getFollowList(username, type, page) {
    return apiFetch(`/api/users/${username}/${type}?page=${page}&size=20`)
}

export function followUser(username) {
    return apiFetch(`/api/users/${username}/follow`, {
        method: "POST"
    })
}

export function unfollowUser(username) {
    return apiFetch(`/api/users/${username}/follow`, {
        method: "DELETE"
    })
}