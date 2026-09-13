# TalentConnect

A production-grade freelance marketplace REST API built with Spring Boot 3, Java 21, and MySQL.

## Tech Stack

- **Java 21** + **Spring Boot 3.3.5**
- **Spring Security** with JWT authentication and refresh tokens
- **MySQL** + **Spring Data JPA / Hibernate**
- **Lombok** for boilerplate reduction
- **Jakarta Validation** for request validation
- **SpringDoc OpenAPI** for Swagger UI
- **Google Gemini API** for AI-generated gig descriptions
- **Docker** + **Docker Compose** for containerized deployment

## Features

- **Auth** — Register, login, JWT access tokens, refresh tokens, logout
- **Role-based access control** — `CLIENT` and `FREELANCER` roles with method-level security (`@PreAuthorize`)
- **Gigs** — CRUD, pagination, search, category filter, per-gig average rating
- **Orders** — Place, view, update status, delete with ownership checks
- **Reviews** — One review per completed order, auto-computed average rating on gig responses
- **AI gig description generator** — Freelancers submit a title/category/skills and get back an AI-generated, ready-to-use gig description (powered by Gemini)

## Project Structure

```
src/main/java/com/Project/TalentConnect/
├── configs/          # Security, CORS, beans
├── controllers/      # REST endpoints
├── DTO/              # Request / Response DTOs
├── entity/           # JPA entities
├── exception/        # Global exception handler
├── repository/       # Spring Data JPA repositories
├── security/         # JWT filter & utility
└── services/         # Business logic (including AI integration)
```

## Getting Started

### Prerequisites
- Java 21
- MySQL 8+
- Maven
- (Optional) Docker + Docker Compose, for containerized setup
- (Optional) A free Gemini API key from [Google AI Studio](https://aistudio.google.com), for the AI gig description feature

### Setup

1. Clone the repo and create the database:
   ```sql
   CREATE DATABASE talentconnect;
   ```

2. Create a `.env` file in the project root:
   ```
   DB_USERNAME=root
   DB_PASSWORD=your_password
   JWT_SECRET=your_256bit_secret
   JWT_EXPIRY_MS=3600000
   GEMINI_API_KEY=your_gemini_api_key
   CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
   ```
   `GEMINI_API_KEY` is optional — the app runs fine without it, but the AI description endpoint will return an error until it's set.

3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```

   Or, run the full stack (app + MySQL) with Docker:
   ```bash
   docker compose up --build
   ```

The server starts on `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## API Overview

| Module  | Endpoint              | Auth          |
|---------|-----------------------|---------------|
| Auth    | `POST /api/auth/login` | Public       |
| Users   | `POST /api/users/register` | Public   |
| Gigs    | `GET /api/gigs`       | Public        |
| Gigs    | `POST /api/gigs/create` | FREELANCER  |
| Orders  | `POST /api/orders`    | CLIENT        |
| Orders  | `PATCH /api/orders/{id}/status` | CLIENT |
| Reviews | `POST /api/reviews`   | CLIENT        |
| Reviews | `GET /api/reviews/gig/{gigId}` | Public |
| AI      | `POST /api/ai/generate-description` | FREELANCER |

## Security Notes

- Passwords hashed with BCrypt
- JWT secrets, DB credentials, and the Gemini API key loaded from `.env` (never committed)
- CORS origins configurable via `CORS_ALLOWED_ORIGINS` (defaults to `localhost:3000`/`localhost:5173`)
- Stateless sessions (no server-side session storage)
