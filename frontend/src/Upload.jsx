import { useMutation } from "@tanstack/react-query"
import { createPost } from "./api/posts"
import { useState, useEffect } from "react"
import { useNavigate } from "react-router"
function Upload() {
    const navigate = useNavigate()
    const [type, setType] = useState("VIDEO")
    const [preview, setPreview] = useState(null)
    const { mutate, isPending, error } = useMutation({
        mutationFn: createPost,
        onSuccess: (post) => {
            navigate(post.type === "STORY" ? "/" : `/users/${post.uploaderUsername}`)
        }
    })

    useEffect(() => {
        return () => {
            if (preview) {
                URL.revokeObjectURL(preview.url)
            }
        }
    }, [preview])

    const handleSubmit = (e) => {
        e.preventDefault()
        const form = new FormData(e.target)
        mutate(form)
    }
    const handleMediaChange = (e) => {
        const file = e.target.files[0]
        if (file) {
            setPreview({ url: URL.createObjectURL(file), isVideo: file.type.startsWith("video/") })
        } else {
            setPreview(null)
        }
    }

    return <div className="mt-6 mx-auto max-w-xl px-4">
        <h1 className="text-3xl font-bold">Upload</h1>
        <form className="flex flex-col gap-3 mt-6" onSubmit={handleSubmit}>
            <select className="border rounded border-gray-300 py-2 px-3" value={type} onChange={(e) => setType(e.target.value)} name="type" >
                <option value="VIDEO">Video</option>
                <option value="IMAGE">Photo</option>
                <option value="REEL">Reel</option>
                <option value="STORY">Story</option>
            </select>
            <input className="border rounded border-gray-300 py-2 px-3" name="title" placeholder="Title" />
            <textarea className="border rounded border-gray-300 py-2 px-3" name="description" placeholder="Description" />
            <label>Media <input className="file:border file:border-gray-300 file:rounded file:px-3 file:py-1 file:cursor-pointer file:hover:bg-gray-100" type="file" name="media" accept="image/*, video/*" required onChange={handleMediaChange} /></label>
            {preview && (preview.isVideo ? <video className="max-h-[75vh]" src={preview.url} controls /> : <img className="max-h-[75vh]" src={preview.url} />)}
            {(type === "VIDEO" || type === "REEL") && <label>Thumbnail <input className="file:border file:border-gray-300 file:rounded file:px-3 file:py-1 file:cursor-pointer file:hover:bg-gray-100" name="thumbnail" type="file" accept="image/*" /></label>}
            <button className="disabled:opacity-50  bg-blue-600 text-white rounded px-4 py-2" disabled={isPending}>{isPending ? "Uploading..." : "Upload"}</button>
        </form>
        {error && <p className="mt-2 text-sm text-red-600">{error.message}</p>}
    </div>
}

export default Upload