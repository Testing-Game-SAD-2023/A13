# T5 Service - User and Game Service
The T5 service manages the user-facing side of the system, allowing players to start and play matches, view their current status as a player, and analyze the history of completed games.

The component implements a Redis cache to allow a player who has left a match to resume it later. For each player, the system maintains one active session per game mode.

## User Interface Features
The graphical interface provides a home page where the user can:
- Start a new match by selecting from the available game modes;
- Change the current language from the available options;
- View their current player status, including experience points and earned achievements;
- Review a history of completed matches, including information such as the class under test, the chosen opponent, the achieved code coverage score, and whether the match was won or lost.

## Game Flow
After selecting a game mode, the user chooses:
- The class under test they want to work on;
- The opponent to face, based on:
  - Type (i.e., the tool or entity that generated the opponent);
  - Difficulty, chosen among `Easy`, `Medium`, or `Hard`.

In the current version, only for the `Single Match` mode, the user can specify a maximum duration (default: 120 minutes) to complete the match. Once the timer expires the user can either submit the code written up to that point (without compiling) or be automatically evaluated based on the last compilation attempt.

### Match progression
During the game, there are no compilation limits or penalties. Players may compile their code at any time and as many times as needed.

Each compilation triggers:
- A request to the T7 service, which compiles the code and returns JaCoCo code coverage metrics;
- A request to the T8 service, only if the T7 compilation succeeds, to obtain EvoSuite metrics;
- Logging of the test code and metrics to the shared volume `VolumeT0`, for traceability and auditing.

For every compilation, the user receives:
- The result of the Maven compilation;
- If the compilation is successful, the coverage and EvoSuite metrics, along with the corresponding results of the selected opponent.

### Submitting the Solution
At any time, the player may submit their solution to end the match. The outcome (win or loss) is determined based on their score and the selected game mode. The evaluation is based on the latest compilation attempt.


