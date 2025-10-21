# T-shared Module

`T_shared` is a shared module of the Test Robot Challenge project. It contains reusable components, classes, and
utilities
for other modules, such as common DTOs, internal domain classes, score helpers, enumerations, and constants.

This module does not provide standalone business logic but offers common tools to ensure consistency and maintainability
across the project.

The `T_shared` module aims to:

* Centralize shared classes to reduce duplication;
* Provide a consistent model for scores, metrics, and DTOs;
* Facilitate integration between microservices.

## How to Use

1. Add the `T_shared` module as a dependency in modules that need DTOs or shared classes.
2. Import the necessary classes or enumerations:
   ```java
   import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
   import testrobotchallenge.commons.models.score.Coverage;
   import testrobotchallenge.commons.util.ExtractScore;
   ```

## Notes

* All **score** classes (`JacocoScore`, `EvosuiteScore`, `Coverage`) are designed as **internal value objects**, not
  DTOs for external exposure.
* All DTOs are contained in `models.dto.*` and are intended for communication between modules or with external
  clients.**