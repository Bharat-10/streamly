import { Routes, Route } from "react-router"
import Login from "./Login"
import Signup from "./Signup"
import ProtectedRoute from "./ProtectedRoute"
import Feed from "./Feed"
import PostPlayer from "./PostPlayer"
import Upload from "./Upload"
import Profile from "./Profile"
import FollowList from "./FollowList"
import Search from "./Search"
import Reels from "./Reels"
function App() {
  return <Routes>
    <Route path="/" element={<ProtectedRoute><Feed /></ProtectedRoute>} />
    <Route path="/reels" element={<ProtectedRoute><Reels /></ProtectedRoute>} />
    <Route path="/upload" element={<ProtectedRoute><Upload /></ProtectedRoute>} />
    <Route path="/posts/:id" element={<ProtectedRoute><PostPlayer /></ProtectedRoute>} />
    <Route path="/users/:username" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
    <Route path="/search" element={<ProtectedRoute><Search /></ProtectedRoute>} />
    <Route path="/users/:username/followers" element={<ProtectedRoute><FollowList type="followers" /></ProtectedRoute>} />
    <Route path="/users/:username/following" element={<ProtectedRoute><FollowList type="following" /></ProtectedRoute>} />
    <Route path="/login" element={<Login />} />
    <Route path="/signup" element={<Signup />} />
    <Route path="*" element={<p className="mt-24 text-center">Page not found</p>} />
  </Routes>
}

export default App