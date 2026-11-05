import { useInfiniteQuery } from "@tanstack/react-query"
import { useParams } from "react-router"
import { getFollowList } from "./api/users"
import UserRow from "./UserRow"
import LoadMore from "./LoadMore"

function FollowList({ type }) {
    const { username } = useParams()
    const { data, isPending, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery({
        queryKey: [type, username],
        queryFn: ({ pageParam }) => getFollowList(username, type, pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.page + 1 : undefined
    })

    if (isPending) return <p className="mt-8 text-center text-sm">Loading...</p>
    if (isError) return <p className="mt-8 text-center text-sm text-red-600">{error.message}</p>
    const users = data.pages.flatMap((page) => page.users)

    return <div className="mt-6 max-w-xl mx-auto px-4">
        <h1 className="text-2xl font-bold">{username}</h1>
        <p className="text-sm text-gray-500">{type === "followers" ? "Followers" : "Following"}</p>
        {users.length === 0 && <p>{type === "followers" ? "No followers yet." : "Not following anyone yet."}</p>}
        <div className=" mt-4 border border-gray-300 rounded divide-y divide-gray-200 overflow-hidden">
            {users.map((user) => <UserRow key={user.id} user={user} />)}
        </div>
        <LoadMore hasNextPage={hasNextPage} isFetchingNextPage={isFetchingNextPage} fetchNextPage={fetchNextPage} />
    </div>
}

export default FollowList