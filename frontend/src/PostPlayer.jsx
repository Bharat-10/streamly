import { useQuery } from "@tanstack/react-query"
import { useParams, Link } from "react-router"
import { getPost } from "./api/posts"
import Comments from "./Comments"

function PostPlayer() {
    const { id } = useParams()
    const { data: post, isPending, isError, error } = useQuery({
        queryKey: ["post", id],
        queryFn: () => getPost(id)
    })

    if (isPending) return <p className="mt-8 text-center text-sm">Loading...</p>
    if (isError) return <p className="mt-8 text-center text-sm text-red-600">{error.message}</p>

    return <div className="mt-6 max-w-3xl mx-auto px-4">
        {post.type === "IMAGE"
            ? <img className="w-full" src={post.mediaUrl} alt={post.title} />
            : <video className="w-full rounded max-h-[75vh] bg-black" src={post.mediaUrl} poster={post.thumbnailUrl} controls />}
        <h1 className="mt-4 text-2xl font-bold">{post.title}</h1>
        <p className="mt-1 text-sm text-gray-500"><Link to={`/users/${post.uploaderUsername}`}>{post.uploaderUsername}</Link></p>
        {post.description && <p className="mt-4">{post.description}</p>}
        <Comments postId={post.id} />
    </div>
}

export default PostPlayer