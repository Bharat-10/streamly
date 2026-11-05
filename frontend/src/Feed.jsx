import { getFeed } from "./api/posts"
import PostCard from "./PostCard"
import { useInfiniteQuery } from "@tanstack/react-query"
import { useRef, useEffect } from "react"
import LoadMore from "./LoadMore"
import Stories from "./Stories"
function Feed() {
    const bottomRef = useRef(null)
    const { data, isPending, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery({
        queryKey: ["feed"],
        queryFn: ({ pageParam }) => getFeed(pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.page + 1 : undefined
    })


    if (isPending) return <p className="mt-8 text-center text-sm">Loading feed...</p>
    if (isError) return <p className="mt-8 text-center text-sm text-red-600">{error.message}</p>
    const posts = data.pages.flatMap((page) => page.posts)

    return <div className="mt-6 max-w-xl mx-auto pb-8">
        <Stories />
        {posts.length === 0 && <p className="mt-8 text-center text-sm text-gray-500">Your feed is empty. Follow people to see their posts here.</p>}
        <div className="flex flex-col gap-6">
            {posts.map((post) => <PostCard key={post.id} post={post} />)}
        </div>
        <LoadMore hasNextPage={hasNextPage} isFetchingNextPage={isFetchingNextPage} fetchNextPage={fetchNextPage} />
    </div>


}

export default Feed