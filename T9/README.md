# T9 Service – Player Profile and Social Service

The T9 service manages the player profile and social-facing side of the system, allowing players to view and edit their personal profile, control visibility and preferences, browse other profiles through search, and automatically generate a short bio using an LLM powered by Groq.

## User Interface Features

The graphical interface connected to T9 provides sections where the player can:

- View and edit their personal profile, including name, surname, email, nickname, bio, and profile picture.​
- Customize preferences such as language, theme color, accent color, and whether the profile is public or private.​
- Browse and search for other players’ profiles using a search bar, with results that never include the currently authenticated player when using “Find friends”.​
- View aggregated game statistics on the profile, such as matches played, matches won, and the time of the last match, retrieved from the T4 service.​
- Generate or regenerate a short, Italian, gamer-style bio using an LLM (Groq) based on profile and gameplay data.​

## Profile Management Flow

When the frontend or another service queries T9, the component is responsible for ensuring that each player has a consistent profile entry:

- On first access, if a profile does not exist for the given user/player ID, T9 creates a default profile with placeholder values for name, surname, email, nickname, bio, avatar, and visibility.​
- The client can initialize the profile using an InitProfileRequest, which:
- Updates an existing profile only for the fields provided (name, surname, email, nickname), applying sensible fallbacks;
- Or creates a new profile, generating a default nickname and placeholder email if not explicitly provided.​

Subsequent updates can be performed via dedicated operations:
- UpdateUserProfileRequest to upsert basic information (name, surname, email, nickname, bio);
- UpdateAvatarRequest to modify only the profile picture path;
- A preferences update request to change theme color, accent color, language, and public visibility;
- A dedicated nickname update that enforces uniqueness, throwing a DuplicateNicknameException if the nickname is already taken by another user;
- A dedicated bio update to change only the textual biography.​

### Search and Discovery

The T9 service provides search capabilities to help players discover other profiles while preserving privacy and avoiding accidental exposure of the full user base:

- The UserProfileService exposes methods to search profiles using a free-text searchTerm, delegating to UserProfileRepository methods such as search and searchExcluding.​
- When the search term is null or blank, the service returns an empty page instead of all profiles, avoiding listing the entire database.​
- For friend discovery, a variant of the search excludes the currently authenticated user (excludeUserId), ensuring that “Find friends” never returns the player’s own profile.​
- Search results are returned as paginated DTOs like UserProfileResponseDTO and UserProfileSearchDTO, carrying both profile details and, where applicable, aggregate game statistics.​

## External Integrations
The T9 service integrates with other components of the system to present a rich profile experience:

- Game statistics are obtained from the T4 service and attached to the profile response, exposing fields such as matchesPlayed, matchesWon, and lastMatchAt on UserProfileResponseDTO.​
- An LLM integration with Groq, implemented by GroqLlmClient, uses an OpenAI-compatible HTTP endpoint (/openai/v1/chat/completions) configured via properties llm.api-url, llm.api-key, and llm.model.​
- GroqLlmClient builds an Italian prompt using PlayerBioData (name, surname, nickname, matches played, matches won, last match date), calls the model with a configurable temperature, and extracts the generated bio from the response.​
- In case of API errors or missing content, the client logs the issue and falls back to a default Italian gamer-style bio, ensuring a consistent user experience even when the LLM is unavailable.​
- For development and testing scenarios, a MockLLMClient provides a deterministic, local-only bio generator that implements the same LlmClient interface without external calls.

## Error Handling and Nickname Uniqueness

The service centralizes error handling and enforces business rules around identity and uniqueness:

- The custom DuplicateNicknameException is thrown when a player attempts to set a nickname already associated with a different user, preventing collisions in the social and search features.​
- RestExceptionHandler intercepts domain-specific exceptions and translates them into structured HTTP responses, mapping nickname conflicts to status 409 (Conflict) with a clear error payload.
- Generic runtime errors related to missing profiles or invalid operations are also handled centrally, allowing clients to rely on consistent status codes and error formats across the T9 API surface.​

## Technology and Deployment

The T9 service is implemented as a Spring Boot application running on Java 17 and backed by a MySQL database.  
It is packaged as a Docker image and can be run together with its database through the provided docker-compose.yml, which configures the `t9-db` MySQL container and the `t9-app` application container, wiring database credentials and LLM-related environment variables such as GROQ_API_KEY, LLM_API_URL, and LLM_MODEL.
