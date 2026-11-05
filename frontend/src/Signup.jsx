import { useMutation } from "@tanstack/react-query"
import { Link, useNavigate } from "react-router"
import { signup } from "./api/auth"

function Signup() {
    const navigate = useNavigate()

    const { mutate, isPending, error } = useMutation({
        mutationFn: signup,
        onSuccess: () => {
            navigate("/login?signup=success")
        }
    })

    const handleSubmit = (e) => {
        e.preventDefault()
        const form = new FormData(e.target)
        mutate({ email: form.get("email"), username: form.get("username"), password: form.get("password") })
    }

    return <div className="mt-24 mx-auto max-w-xl px-4">
        <h1 className="text-3xl font-bold">Sign up</h1>
        <form onSubmit={handleSubmit} className="mt-6 flex flex-col gap-2">
            <input
                className="border rounded border-gray-300 py-2 px-3"
                name="email"
                type="email"
                required
                placeholder="Email"
            />
            <input
                className="border rounded border-gray-300 py-2 px-3"
                name="username"
                type="text"
                required
                placeholder="Username"
            />
            <input
                className="border rounded border-gray-300 py-2 px-3"
                name="password"
                type="password"
                required
                placeholder="Password"
            />
            <button disabled={isPending} className="disabled:opacity-50 bg-blue-600 text-white rounded px-4 py-2">{isPending ? "Signing up..." : "Sign up"}</button>
        </form>
        {error && <p className="mt-2 text-sm text-red-600">{error.message}</p>}
        <p className="mt-4 text-sm">Already have an account? <Link to="/login" className="text-blue-600 underline">Log in</Link></p>
    </div>
}

export default Signup