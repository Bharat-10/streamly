import { Navigate } from "react-router"
import { getAccessToken } from "./api/token"
import Navbar from "./Navbar"

function ProtectedRoute({ children }) {
    if (!getAccessToken()) {
        return <Navigate to="/login" replace />
    }
    return <>
        <Navbar />
        {children}
    </>
}

export default ProtectedRoute