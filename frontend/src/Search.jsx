import { useEffect, useState } from "react"
import { useInfiniteQuery, useQuery } from "@tanstack/react-query"
import { useSearchParams } from "react-router"
import { searchPosts, searchUsers } from "./api/search"
import PostCard from "./PostCard"
import UserRow from "./UserRow"
import LoadMore from "./LoadMore"

function Search() {
    const [searchParams, setSearchParams] = useSearchParams()
    const q = searchParams.get("q") ?? ""
    const [text, setText] = useState(q)

    useEffect(() => {
        if (text.trim() === q) {
            return
        }
        const timer = setTimeout(() => {
            setSearchParams({ q: text.trim() }, { replace: true })
        }, 400)
        return () => clearTimeout(timer)
    }, [text, q, setSearchParams])

    const { data: userPage } = useQuery({
        queryKey: ["searchUsers", q],
        queryFn: () => searchUsers(q),
        enabled: q !== ""
    })
    const { data, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery({
        queryKey: ["searchPosts", q],
        queryFn: ({ pageParam }) => searchPosts(q, pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.page + 1 : undefined,
        enabled: q !== ""
    })
    const posts = data ? data.pages.flatMap((page) => page.posts) : []

    return <div className="mt-6 max-w-xl mx-auto px-4">
        <input className="w-full border rounded border-gray-300 py-2 px-3" value={text} onChange={(e) => setText(e.target.value)} placeholder="Search posts and people" />
        {isError && <p className="mt-4 text-sm text-red-600">{error.message}</p>}
        {userPage && <div className="mt-6">
            <h2 className="text-sm font-semibold text-gray-500">People</h2>
            {userPage.users.length === 0 && <p className="mt-2 text-sm text-gray-500">No people found.</p>}
            {userPage.users.length > 0 && <div className="mt-2 border border-gray-300 rounded divide-y divide-gray-200 overflow-hidden">
                {userPage.users.map((user) => <UserRow key={user.id} user={user} />)}
            </div>}
        </div>}
        {data && <div className="mt-6">
            <h2 className="text-sm font-semibold text-gray-500">Posts</h2>
            {posts.length === 0 && <p className="mt-2 text-sm text-gray-500">No posts found.</p>}
            <div className="mt-2 flex flex-col gap-6">
                {posts.map((post) => <PostCard key={post.id} post={post} />)}
            </div>
            <LoadMore hasNextPage={hasNextPage} isFetchingNextPage={isFetchingNextPage} fetchNextPage={fetchNextPage} />
        </div>}
    </div>
}

export default Search