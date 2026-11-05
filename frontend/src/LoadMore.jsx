import { useRef, useEffect } from "react"

function LoadMore({ hasNextPage, isFetchingNextPage, fetchNextPage }) {
    const bottomRef = useRef(null)
    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
                fetchNextPage()
            }
        })
        if (bottomRef.current) {
            observer.observe(bottomRef.current)
        }
        return () => observer.disconnect()
    }, [hasNextPage, isFetchingNextPage])

    return <div ref={bottomRef}>
        {isFetchingNextPage && <p className="mt-4 text-center text-sm">Loading more...</p>}
    </div>


}

export default LoadMore