import { useMutation } from "@tanstack/react-query"
import { login } from "./api/auth"
import { saveTokens } from "./api/token"
import { Link, useNavigate, useSearchParams } from "react-router"

function Login() {
    const navigate = useNavigate()
    const [searchParams] = useSearchParams()
    const { mutate, isPending, error } = useMutation({
        mutationFn: login,
        onSuccess: (data) => {
            saveTokens(data)
            navigate("/", { replace: true })
        }
    })

    const handleSubmit = (e) => {
        e.preventDefault()
        const form = new FormData(e.target)
        mutate({ email: form.get("email"), password: form.get("password") })
    }

    return <div className="mt-24 mx-auto max-w-xl px-4">
        <h1 className="text-3xl font-bold ">Log in</h1>
        {searchParams.get("signup") && <p className="mt-2 text-sm text-green-600">Account created. Please log in.</p>}
        <form onSubmit={handleSubmit} className="mt-6 flex gap-2 flex-col">
            <input className="border rounded border-gray-300 py-2 px-3" name="email" type="email" required placeholder="Email" />
            <input className="border rounded border-gray-300 py-2 px-3" name="password" type="password" required placeholder="Password" />
            <button disabled={isPending} className="disabled:opacity-50  bg-blue-600 text-white rounded px-4 py-2">{isPending ? "Logging in..." : "Log in"}</button>
        </form>
        {error && <p className="mt-2 text-sm text-red-600">{error.message}</p>}
        <p className="mt-4 text-sm">New here? <Link to="/signup" className="text-blue-600 underline">Sign up</Link></p>
    </div >
}

export default Login