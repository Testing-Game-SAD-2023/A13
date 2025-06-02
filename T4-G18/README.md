# T4 Service - Game Repository
The T4 service is responsible for tracking and maintaining all information related to games and the player’s status, including:
- Games played, with related victories and defeats;
- Available robots (opponents) for each test class;
- Experience points earned, unlocked achievements, and defeated robots.

---

## Robots data model and Rest endpoints
The available robots (opponents) for the various classes under test are described by the **Robots** entity.

![robots.png](documentation/robots.png)

### Robot - `/robots`

| HTTP Method | Endpoint                   | Function                                                          |
| ----------- | -------------------------- | ----------------------------------------------------------------- |
| GET         | `/robots/`                 | Returns the available robots with all their information.          |
| GET         | `/robots/all`              | Returns the available robots.                                     |
| GET         | `/robots/evosuitecoverage` | Returns the EvoSuite metrics associated with the specified robot. |
| POST        | `/robots/`                 | Adds one or more new robots.                                      |
| DELETE      | `/robots/`                 | Removes one or more robots.                                       |

---

## Player status data model and Rest endpoints
The player’s game status is described by the following entities:
- **Experience**: tracks the experience points accumulated by the player;
- **GlobalAchievementProgress**: tracks achievements unlocked by the player that are not tied to a specific game mode. A player can have 0, 1, or multiple `GlobalAchievementProgress` entries, each representing a global unlocked achievement;
- **UserGameProgress**: tracks the opponents faced by the player, identified by robot-game mode pairs, and whether they have been defeated;
- **AchievementProgress**: tracks the achievements unlocked by the player related to a specific opponent. A player can have 0, 1, or multiple `AchievementProgress` entries, each corresponding to a specific unlocked achievement.

![player_status.png](documentation/player_status.png)

### Defeated Opponents and Achievements - `/progress`

| HTTP Method | Endpoint                                                                          | Function                                                                                                                                                                 |
| ----------- | --------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| GET         | `/progress/{playerId}/{gameMode}/{classUT}/{robotType}/{difficulty}`              | Returns the player's status against a specific opponent (identified by game mode and robot), including unlocked achievements and whether the opponent has been defeated. |
| POST        | `/progress/`                                                                      | Initializes a new progress state (no achievements unlocked, opponent not yet defeated) for a player against a specific opponent.                                         |
| PUT         | `/progress/state/{playerId}/{gameMode}/{classUT}/{robotType}/{difficulty}`        | Updates the player's state to indicate the opponent has been defeated.                                                                                                   |
| PUT         | `/progress/achievements/{playerId}/{gameMode}/{classUT}/{robotType}/{difficulty}` | Updates the player's state with newly unlocked achievements related to the opponent.                                                                                     |
| GET         | `/progress/{playerId}`                                                            | Returns the player's progress for all faced opponents.                                                                                                                   |
| GET         | `/progress/global-achievements/{playerId}`                                        | Returns the player's unlocked achievements not related to specific opponents.                                                                                            |
| PUT         | `/progress/global-achievements/{playerId}`                                        | Updates the player's global (non-opponent-related) achievements.                                                                                                         |

### User Experience Points - `/experience`

| HTTP Method | Endpoint                 | Function                                                                              |
| ----------- | ------------------------ | ------------------------------------------------------------------------------------- |
| GET         | `/experience/{playerId}` | Returns the experience points accumulated by the player identified by `playerId`.     |
| POST        | `/experience/`           | Initializes the experience points (set to 0) for a newly registered player.           |
| PUT         | `/experience/{playerId}` | Increases the player's experience points by the amount specified in the request body. |

---

## Games data model and Rest endpoints
Games are managed using the following entities:
- **Game**: describes an ongoing or completed game for a specific player;
- **Round**: represents a round within a game;
- **Turn**: represents a turn within a round.

![game.png](documentation/game.png)

### Games - `/games`

| HTTP Method | Endpoint              | Function                                                          |
| ----------- | --------------------- | ----------------------------------------------------------------- |
| GET         | `/games/{id}`         | Returns information about a specific game by its `id`.            |
| GET         | `/games/player/{pid}` | Returns all games associated with the player identified by `pid`. |
| GET         | `/games/`             | Returns all games stored in the system.                           |
| POST        | `/games/`             | Creates a new game.                                               |
| PUT         | `/games/{id}`         | Updates the state of an ongoing game identified by `id`.          |
| DELETE      | `/games/{id}`         | Deletes the game identified by `id`.                              |

### Game Rounds - `/rounds`

| HTTP Method | Endpoint       | Function                                       |
| ----------- | -------------- | ---------------------------------------------- |
| GET         | `/rounds/{id}` | Returns information about the round with `id`. |
| GET         | `/rounds/`     | Returns all saved rounds.                      |
| POST        | `/rounds/`     | Creates a new round.                           |
| PUT         | `/rounds/{id}` | Updates an existing round.                     |
| DELETE      | `/rounds/{id}` | Deletes an existing round.                     |

### Turns within Rounds - `/turns`

| HTTP Method | Endpoint            | Function                                               |
| ----------- | ------------------- | ------------------------------------------------------ |
| GET         | `/turns/{id}`       | Returns a turn by its `id`.                            |
| GET         | `/turns/`           | Returns all turns associated with the specified round. |
| POST        | `/turns/`           | Creates a new turn and associates it with a round.     |
| PUT         | `/turns/{id}`       | Updates the state of a turn identified by its `id`.    |
| DELETE      | `/turns/{id}`       | Deletes a turn identified by its `id`.                 |
| GET         | `/turns/{id}/files` | Deprecated.                                            |
| PUT         | `/turns/{id}/files` | Deprecated.                                            |

---





