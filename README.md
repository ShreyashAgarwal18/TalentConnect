# TalentConnect

A production-grade freelance marketplace REST API built with Spring Boot 3, Java 21, and MySQL.

## Tech Stack

- **Java 21** + **Spring Boot 3.3.5**
- **Spring Security** with JWT authentication and refresh tokens
- **MySQL** + **Spring Data JPA / Hibernate**
- **Lombok** for boilerplate reduction
- **Jakarta Validation** for request validation
- **SpringDoc OpenAPI** for Swagger UI

## Features

- **Auth** — Register, login, JWT access tokens, refresh tokens, logout
- **Role-based access control** — `CLIENT` and `FREELANCER` roles with method-level security (`@PreAuthorize`)
- **Gigs** — CRUD, pagination, search, category filter, per-gig average rating
- **Orders** — Place, view, update status, delete with ownership checks
- **Reviews** — One review per completed order, auto-computed average rating on gig responses

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
└── services/         # Business logic
```

## Getting Started

### Prerequisites
- Java 21
- MySQL 8+
- Maven

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
   ```

3. Run the app:
   ```bash
   ./mvnw spring-boot:run
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

## Security Notes

- Passwords hashed with BCrypt
- JWT secrets and DB credentials loaded from `.env` (never committed)
- CORS configured for `localhost:3000` and `localhost:5173`
- Stateless sessions (no server-side session storage)
