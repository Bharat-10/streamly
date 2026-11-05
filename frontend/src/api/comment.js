import { apiFetch } from "./client"

export function getComments(postId, page) {
    return apiFetch(`/api/posts/${postId}/comments?page=${page}&size=10`)
}

export function addComment(postId, text) {
    return apiFetch(`/api/posts/${postId}/comments`, {
        method: "POST",
        body: JSON.stringify({ text })
    })
}

export function deleteComment(postId, commentId) {
    return apiFetch(`/api/posts/${postId}/comments/${commentId}`, {
        method: "DELETE"
    })
}