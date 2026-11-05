import { useQuery, useInfiniteQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import { useParams, Link } from "react-router"
import { getProfile, getMe, followUser, unfollowUser } from "./api/users"
import { getUserPosts } from "./api/posts"
import LoadMore from "./LoadMore"
import PostCard from "./PostCard"

function Profile() {
    const { username } = useParams()
    const queryClient = useQueryClient()
    const { data: me } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
        staleTime: Infinity
    })
    const { data: profile, isPending, isError, error } = useQuery({
        queryKey: ["profile", username],
        queryFn: () => getProfile(username)
    })

    const { data, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery({
        queryKey: ["userPosts", username],
        queryFn: ({ pageParam }) => getUserPosts(username, pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.page + 1 : undefined
    })
    const { mutate, isPending: isFollowPending } = useMutation({
        mutationFn: (followed) => followed ? unfollowUser(username) : followUser(username),
        onSuccess: (follow) => {
            queryClient.setQueryData(["profile", username], (old) => ({ ...old, ...follow }))
        }
    })

    if (isPending) return <p className="mt-8 text-center text-sm">Loading profile...</p>
    if (isError) return <p className="mt-8 text-center text-sm text-red-600">{error.message}</p>


    const posts = data ? data.pages.flatMap((page) => page.posts) : []

    return <div className="mt-6 max-w-xl mx-auto px-4">
        <h1 className="text-2xl font-bold">{profile.username}</h1>
        <div className="flex gap-4">
            <span className="mt-1 text-sm text-gray-600" >{profile.postCount} posts</span>
            <Link className="mt-1 text-sm text-gray-600 hover:underline" to={`/users/${profile.username}/followers`}>{profile.followerCount} followers</Link>
            <Link className="mt-1 text-sm text-gray-600 hover:underline" to={`/users/${profile.username}/following`}>{profile.followingCount} following</Link>
        </div>
        {me && me.username !== profile.username && <button className="mt-3 disabled:opacity-50 border border-gray-300 rounded px-4 py-1 text-sm" disabled={isFollowPending} onClick={() => mutate(profile.followedByMe)}>
            {profile.followedByMe ? "Unfollow" : "Follow"}
        </button>}
        {data && posts.length === 0 && <p>No posts yet.</p>}
        <div className="flex mt-6 flex-col gap-6">
            {posts.map((post) => <PostCard key={post.id} post={post} />)}
        </div>
        <LoadMore hasNextPage={hasNextPage} isFetchingNextPage={isFetchingNextPage} fetchNextPage={fetchNextPage} />
    </div>
}

export default Profile