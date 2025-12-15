# Task T23 – Player Progress and Auth Service

Spring Boot backend service for gaming platform, handling secure authentication, player progress tracking, admin management, and multi-language notifications.

## Core Features

Service manages user authentication flows, player game progress CRUD, admin oversight, and social notifications.

- JWT authentication with refresh tokens, password resets, and role-based access
- Player progress tracking including achievements, game stats, and updates
- Admin functions for user summaries, registrations, and system oversight
- Email notifications and internationalization support (EN/IT)
- Custom exceptions for business rules like invalid tokens or incompatible roles

## Key Workflows

Authentication and progress management follow structured flows with validation.

### Authentication Flow

- Login/register via AuthController with JWT generation and refresh handling
- Password reset using time-limited tokens stored in database
- Interceptors enforce authenticated access across endpoints

### Player Progress Flow

- Create/update progress records with DTOs like CreateGameProgressDTO
- Retrieve detailed stats via PlayerProgressController, mapped to DTOs
- Service layer handles persistence, validation, and business logic

## API Endpoints

RESTful endpoints secured by JWT and roles.

| Category | Examples                                              | Purpose                    |
| -------- | ----------------------------------------------------- | -------------------------- |
| Auth     | POST /auth/login, /auth/refresh, /auth/reset-password | User sessions and recovery |
| Players  | GET/POST /players/progress/{id}, /players/follow      | Progress CRUD and social   |
| Admin    | GET /admin/summary, POST /admin/register              | Oversight and management   |
| Language | GET /language/switch                                  | Locale handling            |

## External Integrations

- Email service for notifications and resets using configurable SMTP
- Database initialization via init.sql for entities like PlayerProgress, RefreshToken
- HTTP logging and RestTemplate for external calls if needed

## Error Handling

Custom exceptions map to structured API errors via ApiErrorDTO.

- Token issues: InvalidRefreshTokenException, PasswordResetTokenNotFoundException
- Business rules: IncompatibleRoleException, PlayerProgressNotFoundException
- Centralized handling ensures consistent HTTP responses

## Technology Stack

| Component | Technology                           |
| --------- | ------------------------------------ |
| Framework | Spring Boot, JPA/Hibernate           |
| Security  | Spring Security, JWT custom provider |
| Database  | SQL (MySQL/PostgreSQL via init.sql)  |
| Build     | Maven with mvnw wrapper              |
| Config    | Multi-profile (dev/prod properties)  |

## Deployment and Setup

1. Run mvnw clean install to build

2. Configure database and JWT secrets in application.properties

3. Execute init.sql for schema

4. Start with mvnw spring-boot:run --spring.profiles.active=dev

5. Access APIs at http://localhost:8080 (adjust port in properties)
