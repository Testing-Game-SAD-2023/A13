# T56 - Package Game

The **Game package** is dedicated to managing the logic of the various game modes within the application.
It represents the core of the **GameEngine**, providing structures and classes to implement, manage, and monitor games,
turns, and rounds.

## Structure and Role

The Game package is organized to allow the creation of multiple game types, all based on a common logic and consistent
interface.
This approach facilitates the implementation of new games and modification of existing ones without impacting the rest
of the application.

The workflow revolves around the **GameController**, whose main objective is to manage the lifecycle of a game in a
smooth
and coherent way, ensuring a balanced and engaging experience for players.
The modular and extendable design of the controller allows easy extension of the logic to new games, enhancing the
flexibility of the game architecture.

## Class Diagram: Game

![game_package.png](images/game_package.png)

### Components

1. **GameController:** Acts as an intermediary between the client and the backend, handling routing related to games,
   coordinating the game lifecycle, and interacting with external services.
   It integrates a **ServiceManager** (see Interface package), as games often require communication with external
   services (e.g., saving results or retrieving new challenges). This integration provides ready-to-use HTTP calls and
   resource management for the user.
   Specifically:
    * Tracks active games in a map (`activeGames`), associating each game with a `playerId`.
    * Receives client requests (e.g., starting a game or executing a turn) via methods annotated with `@PostMapping` and
      `@GetMapping`.
    * Provides support methods, such as `getUserData` or `getRobotScore`, which retrieve required information from
      external service calls.

2. **GameLogic:** Defines game concepts such as matches, rounds, and turns. Provides methods to create, play, and end a
   game.
   GameLogic defines the structure and rules of games, while subclasses extending GameLogic implement the specifics of
   each game mode.
   It does not directly interact with the client or HTTP requests; it handles internal game logic only.
   Subclasses must implement methods to define the specific game logic:
    * `playTurn()`: Defines the logic executed during each turn.
    * `isGameEnd()`: Defines the conditions for the end of the game.
    * `getScore()`: Calculates the player’s score based on specific metrics (e.g., code coverage).

3. **TurnBasedGameLogic:** Extends GameLogic and provides a ready-made implementation managing turn transitions and
   victory/defeat conditions while maintaining a consistent game state.
   Implements the game logic specific to the **challenge** mode.

## Use Case: Creating a New Game Mode

The system uses the abstract class GameLogic as a base for game logic management. To create a new game mode, derive a
new class from GameLogic and implement the three abstract methods: `playTurn()`, `isGameEnd()`, and `getScore()`, as
well
as the `createGame()` method.

### Steps

1. **Create a New Class Extending GameLogic**: This class represents the new game mode.

2. **Implement the `playTurn()` Method**: Responsible for the logic executed each turn. May include creating new turns,
   collecting scores, and transitioning between game states.

3. **Implement the `isGameEnd()` Method**: Returns a boolean indicating whether the game has ended, based on the logic
   of the specific game mode.

4. **Implement the `getScore()` Method**: Calculates the player’s score according to a specific metric (e.g., code
   coverage, robot performance). The score is often influenced by the number of rounds played or performance during a
   turn.

5. **Integrate the New Mode into the System**: After creating the new game mode, integrate it into the system by
   registering it within the GameController or other components managing game types.

## Sequence Diagram: CreateGame

This diagram illustrates the process through which an authenticated user starts a new game.
After selecting game parameters (class and robot), the system sends the information to the backend, which creates the
game and stores its details.
The system then returns the newly created game ID to the user.
![package_game_sequence.png](images/package_game_sequence.png)
