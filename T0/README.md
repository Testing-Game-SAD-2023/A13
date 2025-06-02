# T0 Service – JUnit 4 Test Generator
The T0 service is a standalone component of the system that automatically generates JUnit 4 tests for a target Java class using Randoop and EvoSuite.

This service is completely independent from the rest of the system and can be executed separately without affecting other components.

## Execution
The service is distributed as a Docker container and can be used via two scripts executed on the host machine:
- `generate.sh` for Linux systems
- `generate.bat` for Windows systems

Both scripts accept the following input parameters:
- `tool`: the generation tool to use (`randoop` or `evosuite`);
- `class_name`: the name of the target Java class to test;
- `package_name`: the package name of the target Java class. Optional;
- `class_path`: absolute path on the host to the directory containing the target class;
- `num_levels`: number of test difficulty levels to generate;
- `host_output_dir`: absolute path on the host where the generated tests will be saved;
