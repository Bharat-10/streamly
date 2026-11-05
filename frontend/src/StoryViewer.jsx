import { useState } from "react"

function StoryViewer({ groups, startIndex, onClose }) {
    const [groupIndex, setGroupIndex] = useState(startIndex)
    const [storyIndex, setStoryIndex] = useState(0)
    const group = groups[groupIndex]
    const story = group.stories[storyIndex]

    const next = () => {
        if (storyIndex < group.stories.length - 1) {
            setStoryIndex(storyIndex + 1)
        } else if (groupIndex < groups.length - 1) {
            setGroupIndex(groupIndex + 1)
            setStoryIndex(0)
        } else {
            onClose()
        }
    }

    return <div className="fixed inset-0 bg-black text-white flex flex-col">
        <div className="flex justify-between items-center p-4">
            <p className="font-semibold">{group.username}</p>
            <button onClick={onClose}>Close</button>
        </div>
        <div onClick={next} className="flex-1 flex items-center justify-center">
            {story.contentType.startsWith("video/")
                ? <video className="max-h-[80vh]" src={story.mediaUrl} autoPlay playsInline onEnded={next} />
                : <img className="max-h-[80vh]" src={story.mediaUrl} />}
        </div>
    </div>
}

export default StoryViewer