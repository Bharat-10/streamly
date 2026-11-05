import { Link } from "react-router"
import LikeButton from "./LikeButton"

function PostCard({ post }) {

    return <div className="border border-gray-300 rounded overflow-hidden">
        <Link to={`/posts/${post.id}`}>
            <img className="w-full aspect-video object-cover" src={post.thumbnailUrl ?? post.mediaUrl} />
            <p className="pl-3 pt-2 font-semibold">{post.title}</p>
        </Link>

        <div className="px-3 py-2">
            <p className="text-sm text-gray-500"><Link to={`/users/${post.uploaderUsername}`}>{post.uploaderUsername}</Link></p>
            <LikeButton post={post} />
        </div>
    </div>
}

export default PostCard