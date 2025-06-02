# T8 Service - MVN Compiler and EvoSuite Metrics
The T8 service handles the compilation of provided Java code and the calculation of the metrics of EvoSuite.
It can be invoked by:
- T1, to calculate the missing EvoSuite metrics of a robot uploaded by the administrator;
- T5, to calculate the EvoSuite metrics achieved by the JUnit code written by the user during a match.

## Available REST Endpoints

| HTTP Method | Endpoint       | Function                                                                           |
| ----------- |----------------|------------------------------------------------------------------------------------|
| POST        | `/coverage/randoop` | Calculates the missing EvoSuite metrics for a robot uploaded by the administrator. |
| POST        | `/api/VolumeT0` | Calculate the EvoSuite metrics for the code written by the user during a match.    |

## Executor and Task Queue

To handle multiple compilation requests from players within a limited time frame, T8 uses a system based on task queues and an executor.

Each received request is tagged with a timestamp indicating its arrival time and is added to a queue waiting to be processed. A periodic check of the queue removes all requests that have been waiting beyond a certain time threshold. In such cases, the calling service (T5) is notified and invited to retry later.

During the execution of a compilation task, a timeout timer is started: if the compilation and the EvoSuite metrics analysis do not complete within this limit, the operation is stopped, and the calling service is informed of the timeout. The system will then automatically move on to the next task.

This configuration allows:
- Limiting the number of concurrent compilations;
- Avoiding long wait times for users to receive compilation results when the system is under heavy load.
