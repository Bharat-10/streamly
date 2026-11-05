# Streamly

A full-stack social media platform for photos, videos, reels and 24-hour stories — users can follow each other, like and comment on posts, and search users and posts with typo-tolerant search.

**Backend:** Java 25, Spring Boot 4, Spring Security (JWT), Spring Data JPA, MySQL, Elasticsearch, AWS S3
**Frontend:** React 19, Vite, TanStack Query, React Router, Tailwind CSS

## Features

- **Auth** — signup/login with BCrypt-hashed passwords; 15-minute JWT access tokens and 7-day refresh tokens that rotate on every use. Reusing an already-used refresh token revokes all of that user's sessions.
- **Posts** — four post types: `IMAGE`, `VIDEO`, `REEL`, `STORY`. Media is uploaded to AWS S3 under random UUID keys and served through 1-hour presigned URLs, so the bucket stays private.
- **Stories** — expire 24 hours after posting; grouped per user for people you follow.
- **Feed & reels** — paginated home feed from followed users and a reels feed; reels autoplay as they scroll into view.
- **Social graph** — follow/unfollow, follower/following lists, profile counts. Likes and follows are idempotent via unique DB constraints.
- **Comments** — paginated, deletable only by their author.
- **Search** — users and posts indexed in Elasticsearch with fuzzy matching (`fuzziness: AUTO`), so small typos still find results. Indexes are re-synced from MySQL on startup.

## API overview (22 endpoints)

| Area | Endpoints |
|---|---|
| Auth | `POST /api/auth/signup`, `/login`, `/refresh` |
| Posts | `POST /api/posts`, `GET /api/posts`, `GET /api/posts/{id}`, `GET /api/posts/reels`, `GET /api/posts/stories`, `POST/DELETE /api/posts/{id}/like` |
| Comments | `GET/POST /api/posts/{postId}/comments`, `DELETE /api/posts/{postId}/comments/{commentId}` |
| Users | `GET /api/users/me`, `GET /api/users/{username}`, `GET /api/users/{username}/posts`, `/followers`, `/following`, `POST/DELETE /api/users/{username}/follow` |
| Search | `GET /api/search/users?q=`, `GET /api/search/posts?q=` |

## Running locally

**Prerequisites:** Java 25, Node 20+, Docker, and an AWS S3 bucket with credentials configured (`aws configure` or `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY`).

```bash
# 1. Start MySQL and Elasticsearch
docker compose up -d

# 2. Backend (http://localhost:8080)
cd backend
export DB_PASSWORD=root
export S3_BUCKET=<your-bucket-name>
./mvnw spring-boot:run

# 3. Frontend (http://localhost:5173)
cd ../frontend
cp .env.example .env
npm install
npm run dev
```

Tables are created automatically by Hibernate on first run.

| Env variable | Default |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/streamly` |
| `DB_USERNAME` / `DB_PASSWORD` | `root` / *(empty)* |
| `ES_URI` | `http://localhost:9200` |
| `JWT_SECRET` | dev-only value — set your own |
| `AWS_REGION` / `S3_BUCKET` | `ap-south-1` / — |
