import { getReels } from "./api/posts"
import LoadMore from "./LoadMore"
import { useInfiniteQuery } from "@tanstack/react-query"
import ReelCard from "./ReelCard"
import { useState } from "react"
import { Link } from "react-router"
function Reels() {
    const [muted, setMuted] = useState(true)
    const { data, isPending, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery({
        queryKey: ["reels"],
        queryFn: ({ pageParam }) => getReels(pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.page + 1 : undefined
    })
    if (isPending) return <p>Loading reels...</p>
    if (isError) return <p>{error.message}</p>
    const reels = data.pages.flatMap((page) => page.posts)

    return <div className="fixed inset-0 overflow-y-scroll bg-black snap-y snap-mandatory">
        <Link to="/" className="text-white fixed top-4 left-4 text-sm z-11">← Back</Link>
        {reels.length === 0 && <p>No reels yet.</p>}
        {reels.map((reel) => <ReelCard key={reel.id} reel={reel} muted={muted} onToggleMute={() => setMuted(!muted)} />)}
        <LoadMore hasNextPage={hasNextPage} isFetchingNextPage={isFetchingNextPage} fetchNextPage={fetchNextPage} />
    </div>
}

export default Reels