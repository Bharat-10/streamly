import { useMutation } from "@tanstack/react-query"
import { likePost, unlikePost } from "./api/posts"

function LikeButton({ post }) {

    const { mutate, data, isPending } = useMutation({
        mutationFn: (liked) => liked ? unlikePost(post.id) : likePost(post.id)
    })

    const likedByMe = data ? data.likedByMe : post.likedByMe
    const likeCount = data ? data.likeCount : post.likeCount

    return <button disabled={isPending} onClick={() => mutate(likedByMe)} className="disabled:opacity-50 border border-gray-300 rounded px-3 py-1 text-sm">
        {likedByMe ? "♥" : "♡"} {likeCount}
    </button>

}

export default LikeButton