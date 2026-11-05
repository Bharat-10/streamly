import LikeButton from "./LikeButton"
import { Link } from "react-router"
import Comments from "./Comments"
import { useState, useRef, useEffect } from "react"

function ReelCard({ reel, muted, onToggleMute }) {
    const [showComments, setShowComments] = useState(false)
    const videoRef = useRef(null)


    useEffect(() => {
        const video = videoRef.current
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                video.play()
            } else {
                video.pause()
            }
        }, { threshold: 0.6 })
        observer.observe(video)
        return () => observer.disconnect()
    }, [])

    return <div className="relative h-full aspect-[9/16] max-w-full mx-auto snap-start snap-always">
        <video ref={videoRef} src={reel.mediaUrl} poster={reel.thumbnailUrl} muted={muted} loop />
        <div className="absolute bottom-4 flex inset-x-4 items-end gap-4 justify-between">
            <div className="text-white text-sm min-w-0">
                <Link className="font-semibold" to={`/users/${reel.uploaderUsername}`}>{reel.uploaderUsername}</Link>
                <p className="line-clamp-2 break-words">{reel.title}</p>
            </div>
            <div className="text-white flex flex-col gap-2">
                <LikeButton post={reel} />
                <button onClick={() => setShowComments(true)}>Comments</button>
                <button onClick={onToggleMute}>{muted ? "Unmute" : "Mute"}</button>
            </div>
        </div>

        {showComments && (
            <div className="absolute inset-x-0 bottom-0 h-2/3 bg-white text-black rounded-t-xl p-4 overflow-y-auto overscroll-contain">
                <button onClick={() => setShowComments(false)}>Close</button>
                <Comments postId={reel.id} />
            </div>
        )}
    </div>

}

export default ReelCard