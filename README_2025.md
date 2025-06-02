# TestingRobotChallenge
TestingRobotChallenge is a web application designed to support the learning of software testing through gamification, where players compete against preconfigured opponents. The goal for each player is to write JUnit test code for a given Java class, aiming to outperform — or at least match — the score achieved by the selected opponent.

## Key Features
- Role-based access: users can register either as administrators or players. Administrators — typically instructors — can upload new Java classes to be tested and configure opponents (called robots). Players — typically students — can then challenge these robots;
- Experience points and achievements**: players earn experience points for each defeated robot, which contribute to their level progression. Completing specific challenges also unlocks achievements;
- Match history: a dedicated section allows players to review their past matches, showing which were won or lost;
- Multiple game modes: the platform currently supports two gameplay modes:
  - Practice: players can freely test their skills on a Java class without competing against a robot and without being scored.
  - Single Match: players compete against a robot by trying to match or exceed its instruction-level bytecode coverage. Matches are time-limited, with the duration chosen by the player before starting.

## System Design and Implementation
The platform is implemented as a browser-accessible web application based on a microservices architecture. Each component is containerized using Docker and exposes REST APIs, making the system easy to deploy, scale, and maintain.

The platform consists of the following microservices:
1. T1, which manages administrator-related functionalities, including class and robot management;
2. T23, which handles login and registration for players;
3. T4, which tracks match results and maintains player profiles and progress;
4. T5, which manages gameplay logic and user interfaces for the player role.
5. T7, which compiles the user-provided test code and computes coverage metrics using JaCoCo;
6. T8, which compiles the user-provided test code and computes EvoSuite-based metrics.

### Data Persistence
The system uses Docker volumes and containerized databases to persist data. Specifically:
- T23 communicates with a PostgreSQL database for player authentication and registration;
- T4 communicates with a MySQL database to store match data and player statistics;
- T1 communicates with a MongoDB database to manage administrator accounts and to store references to the file paths of Java classes uploaded for testing.
- The shared Docker volume VolumeT0 is used to store a copy of each Java class to be tested along with its associated robots, as well as a copy of each player’s compilation output, including the corresponding metrics.

### Support Services
Two standalone components complement the system:
1. T0, a self-contained component that can automatically generate JUnit 4 tests for a given Java class using Randoop and EvoSuite;
2. db-backup, a utility service that performs regular backups (as dumps) of the PostgreSQL and MySQL databases used by T23 and T4.

These services can be invoked directly from the host machine when needed.

## System Deployment
TestingRobotChallenge includes a suite of scripts to simplify the build and deployment process:
- `build.bat` / `build.sh`: Windows/Linux scripts to build and create Docker images for all microservices and components;
- `deploy.bat` / `deploy.sh`: Windows/Linux scripts to deploy all containers, create necessary Docker volumes and networks, and start services. Local images will be used if available; otherwise, images from DockerHub will be pulled;
- `selective_build_and_deploy.bat` / `selective_build_and_deploy.sh`: Windows/Linux scripts that allow selective build and deployment of specific microservices, mainly used during development;
- `uninstaller.bat` / `uninstaller.sh`: Windows/Linux scripts that purge all Docker containers, images, networks, and volumes on the host. Use with caution.
