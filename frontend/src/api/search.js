import { apiFetch } from "./client"

export function searchUsers(q) {
    return apiFetch(`/api/search/users?q=${encodeURIComponent(q)}`)
}

export function searchPosts(q, page) {
    return apiFetch(`/api/search/posts?q=${encodeURIComponent(q)}&page=${page}&size=3`)
}