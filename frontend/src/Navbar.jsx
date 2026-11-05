import { getMe } from "./api/users"
import { useQuery, useQueryClient } from "@tanstack/react-query"
import { Link, useNavigate } from "react-router"
import { clearTokens } from "./api/token"
function Navbar() {

    const queryClient = useQueryClient()
    const navigate = useNavigate()
    const { data } = useQuery({
        queryKey: ["me"],
        queryFn: getMe
    })
    const handleLogout = () => {
        clearTokens()
        queryClient.clear()
        navigate("/login")
    }

    return <nav className="border-b border-gray-300">
        <div className="max-w-3xl mx-auto flex justify-between items-center px-4 py-3">
            <Link to="/" className="text-xl font-bold hover:underline">Streamly</Link>
            <div className="flex gap-3 items-center">
                <Link to="/reels" className="text-sm hover:underline">Reels</Link>
                <Link to="/upload" className="text-sm hover:underline">Upload</Link>
                <Link to="/search" className="text-sm hover:underline">Search</Link>
                {data && <Link to={`/users/${data.username}`} className="text-sm font-semibold hover:underline">{data?.username}</Link>}
                <button onClick={handleLogout} className="border border-gray-300 rounded px-3 py-1 text-sm">Log out</button>
            </div>
        </div>
    </nav>

}

export default Navbar