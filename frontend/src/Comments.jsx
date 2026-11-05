import { useInfiniteQuery, useMutation, useQueryClient, useQuery } from "@tanstack/react-query"
import { Link } from "react-router"
import { getComments, addComment, deleteComment } from "./api/comment"
import LoadMore from "./LoadMore"
import { useState } from "react"
import { getMe } from "./api/users"
function Comments({ postId }) {
    const queryClient = useQueryClient()
    const [text, setText] = useState("")
    const { data: me } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
        staleTime: Infinity
    })
    const { data, isPending, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery({
        queryKey: ["comments", postId],
        queryFn: ({ pageParam }) => getComments(postId, pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.page + 1 : undefined
    })
    const { mutate: postComment, isPending: isPosting, error: postError } = useMutation({
        mutationFn: () => addComment(postId, text),
        onSuccess: () => {
            setText("")
            queryClient.invalidateQueries({ queryKey: ["comments", postId] })
        }
    })
    const { mutate: removeComment } = useMutation({
        mutationFn: (commentId) => deleteComment(postId, commentId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["comments", postId] })
        }
    })
    const handleSubmit = (e) => {
        e.preventDefault()
        postComment()
    }
    if (isPending) return <p>Loading comments...</p>
    if (isError) return <p>{error.message}</p>
    const comments = data.pages.flatMap((page) => page.comments)

    return <div className="mt-6">
        <h2 className="font-semibold">Comments</h2>
        <form onSubmit={handleSubmit} className="flex gap-2 mt-2">
            <input value={text} onChange={(e) => setText(e.target.value)} maxLength={500} required placeholder="Add a comment" className="min-w-0 border rounded border-gray-300 py-1 px-2" />
            <button disabled={isPosting} className="disabled:opacity-50 bg-blue-600 text-white rounded px-3 py-1 text-sm">Post</button>
        </form>
        {postError && <p className="text-sm text-red-600">{postError.message}</p>}
        {comments.length === 0 && <p className="mt-2 text-sm text-gray-600">No comments yet.</p>}
        {comments.map((comment) => <div className="mt-3 flex gap-2" key={comment.id}>
            <Link className="font-semibold" to={`/users/${comment.username}`}>{comment.username}</Link> {comment.text}
            {me && comment.userId === me.id && <button className="ml-2 text-gray-500 hover:underline" onClick={() => removeComment(comment.id)}>Delete</button>}
        </div>)}
        <LoadMore hasNextPage={hasNextPage} isFetchingNextPage={isFetchingNextPage} fetchNextPage={fetchNextPage} />
    </div>
}

export default Comments