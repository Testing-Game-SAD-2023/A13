# T7 Service - MVN Compiler and JaCoCo Coverage

The T7 service handles the compilation of provided Java code and the calculation of code coverage using JaCoCo.
It can be invoked by:
- T1, to calculate the missing JaCoCo coverage of a robot uploaded by the administrator;
- T5, to verify the compilability and the coverage achieved by the JUnit code written by the user during a match.

## Available REST Endpoints

| HTTP Method | Endpoint                    | Function                                                                                          |
| ----------- | --------------------------- | ------------------------------------------------------------------------------------------------- |
| POST        | `/coverage/evosuite`        | Calculates the missing JaCoCo coverage for a robot uploaded by the administrator.                 |
| POST        | `/compile-and-codecoverage` | Verifies the compilability and the coverage of the JUnit code written by the user during a match. |

## Executor and Task Queue

To handle multiple compilation requests from players within a limited time frame, T7 uses a system based on task queues and an executor.

Each received request is tagged with a timestamp indicating its arrival time and is added to a queue waiting to be processed. A periodic check of the queue removes all requests that have been waiting beyond a certain time threshold. In such cases, the calling service (T5) is notified and invited to retry later.

During the execution of a compilation task, a timeout timer is started: if the compilation and the JaCoCo coverage analysis do not complete within this limit, the operation is stopped, and the calling service is informed of the timeout. The system will then automatically move on to the next task.

This configuration allows:
- Limiting the number of concurrent compilations;
- Avoiding long wait times for users to receive compilation results when the system is under heavy load.
