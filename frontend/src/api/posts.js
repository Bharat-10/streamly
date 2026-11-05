import { apiFetch } from "./client"

export function getFeed(page) {
    return apiFetch(`/api/posts?page=${page}&size=3`)
}

export function getPost(id) {
    return apiFetch(`/api/posts/${id}`)
}

export function likePost(id) {
    return apiFetch(`/api/posts/${id}/like`, {
        method: "POST"
    })
}

export function unlikePost(id) {
    return apiFetch(`/api/posts/${id}/like`, {
        method: "DELETE"
    })
}

export function createPost(form) {
    return apiFetch(`/api/posts`, {
        method: "POST",
        body: form
    })
}

export function getUserPosts(username, page) {
    return apiFetch(`/api/users/${username}/posts?page=${page}&size=3`)
}

export function getReels(page) {
    return apiFetch(`/api/posts/reels?page=${page}&size=3`)
}

export function getStories() {
    return apiFetch("/api/posts/stories")
}