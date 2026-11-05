import { Link } from "react-router"

function UserRow({ user }) {
    return <Link to={`/users/${user.username}`} className="flex items-center gap-3 px-3 py-2 hover:bg-gray-50">
        <span className="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center font-semibold uppercase">{user.username[0]}</span>
        <span>{user.username}</span>
    </Link>
}

export default UserRow