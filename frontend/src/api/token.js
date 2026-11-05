export function saveTokens(tokens) {
    localStorage.setItem("accessToken", tokens.accessToken)
    localStorage.setItem("refreshToken", tokens.refreshToken)
}

export function getAccessToken() {
    return localStorage.getItem("accessToken")
}

export function getRefreshToken() {
    return localStorage.getItem("refreshToken")
}

export function clearTokens() {
    localStorage.removeItem("accessToken")
    localStorage.removeItem("refreshToken")
}