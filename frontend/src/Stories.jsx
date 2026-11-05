import { useState } from "react"
import { useQuery } from "@tanstack/react-query"
import { getStories } from "./api/posts"
import { getMe } from "./api/users"
import StoryViewer from "./StoryViewer"

function Stories() {
    const [openIndex, setOpenIndex] = useState(null)
    const { data: me } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
        staleTime: Infinity
    })
    const { data: groups } = useQuery({
        queryKey: ["stories"],
        queryFn: getStories
    })

    if (!groups || groups.length === 0) return null
    const mine = groups.filter((group) => group.userId === me?.id)
    const others = groups.filter((group) => group.userId !== me?.id)
    const sorted = [...mine, ...others]

    return <div className="mb-6 flex gap-4 overflow-x-auto">
        {sorted.map((group, index) => <button key={group.userId} onClick={() => setOpenIndex(index)} className="flex flex-col items-center gap-1">
            <span className="w-14 h-14 rounded-full border-2 border-pink-500 bg-gray-200 flex items-center justify-center font-semibold uppercase">{group.username[0]}</span>
            <span className="text-xs">{group.username}</span>
        </button>)}
        {openIndex !== null && <StoryViewer groups={sorted} startIndex={openIndex} onClose={() => setOpenIndex(null)} />}
    </div>
}

export default Stories