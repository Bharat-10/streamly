import { apiFetch } from "./client"

export function login(credentials) {
    return apiFetch("/api/auth/login", {
        method: "POST",
        body: JSON.stringify(credentials)
    })
}

export function signup(credentials) {
    return apiFetch("/api/auth/signup", {
        method: "POST",
        body: JSON.stringify(credentials)
    })
}