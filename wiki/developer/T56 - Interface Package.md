# T56 - Interface Package

The **Interface package** manages communication between the application and external services via REST calls. It is
responsible for abstracting API interactions and handling HTTP requests, enabling smooth and centralized communication
between the application and external resources.

## Structure and Role

The Interface package acts as a service layer for communication between the application components and remote services.
It provides classes and methods that simplify the construction and sending of HTTP requests, while also managing data
formatting,
response handling, and error control. This allows other packages to interact with external APIs without dealing with the
complexity
of HTTP calls directly.

**ServiceManager** is the core component of the package, designed as a **dispatcher** for REST service calls in a Java
Spring
application. It aggregates and orchestrates requests to various endpoints, streamlining integration and communication
between
different microservices.

## Objectives

The main objective of the ServiceManager is to improve the scalability and flexibility of the application architecture,
making it easier to add and modify services. Thanks to its modular design, developers can focus on business logic
without
having to manage the complexities of service calls.

## Key Features

* **Unified Service Management**: ServiceManager provides a common interface for accessing multiple services, reducing
  code complexity and improving maintainability.
* **Use of RestTemplate**: a Spring-provided class that simplifies communication with external web services, supporting
  HTTP methods such as GET, POST, PUT, and DELETE. It acts as an HTTP client that facilitates RESTful API integration.
* **Standardized HTTP Calls**: Methods are implemented to handle GET, POST, PUT, and DELETE requests, ensuring
  consistency across all API interactions. These methods support parameter passing, JSON payload handling, and response
  management.
* **Support for Generic Methods**: Enables the creation of generic methods for handling different types of requests and
  responses, making it possible to implement REST operations on any user-defined type.
* **Integration with UI Components**: Seamlessly integrates with user interface components, ensuring efficient data
  handling and a smooth user experience.

## Class Diagram: Interfaces

![interface_package.png](images/interface_package.png)

1. **BaseService:**
   An abstract implementation serving as the foundation for services communicating with REST APIs, providing a shared
   infrastructure for operations and HTTP call handling.
    * Allows the registration of available actions through the `registerAction()` method.
    * Implements `handleRequest()` to check the existence of the requested action and execute the related operation.
    * Provides methods for GET, POST, PUT, and DELETE HTTP calls, handling both URI construction and response
      processing.
    * Handles exceptions during REST calls, delivering clear and understandable error messages.

2. **ServiceActionDefinition:**
   An immutable class defining actions by encapsulating a function and parameter specifications. An action represents an
   operation exposed by a microservice through an endpoint.

3. **ServiceInterface:**
   An interface that all services must implement to be encapsulated into a single dispatcher class.

4. **ServiceManager:**
   The central service manager, enabling the registration and handling of service instances implementing
   ServiceInterface via the `registerService()` method.
   For request handling, it uses `handleRequest()` to check service availability and route requests to the appropriate
   action, managing exceptions as well.

5. **ServiceManagerLogger:**
   Responsible for logging ServiceManager activity, providing monitoring for service operations.

6. **T1Service:**
   Extends BaseService and interacts with an API managing test classes. It registers actions such as `getClasses` and
   `getClassUnderTest`, mapping each to a specific function for retrieving either a list of classes or a specific class.

7. **T4Service:**
   Extends BaseService and handles interactions with an API for game and turn management. It registers actions like
   `getLevels`, `createGame`, `endGame`, etc., enabling creation and management of games and turns via REST calls.

8. **T7Service:**
   Extends BaseService and communicates with an API for compiling code and analyzing code coverage. Implements the
   `CompileCoverage` action, which sends a POST request with code details to be compiled and analyzed.

9. **T23Service:**
   Extends BaseService and manages user authentication and access for registered users.

## Use Case: Registering a New Action

If a service class already exists, you can add a new action to it; otherwise, you need to create a new service class
extending
**BaseService**.

> Example: Suppose we want to add a new generic action called *“newAction”* to **T1Service**. Below is a practical
> example (though no fixed pattern exists, the method can be implemented as deemed appropriate).

```java
private String performNewAction(String param1, int param2) {
   return "Action executed with " + param1 + " and " + param2;
}
```

In the service class constructor, use the `_registerAction()` method to register the newly defined action with a *
*ServiceActionDefinition** instance.

> The first `params` call specifies the function signature,
> while the second indicates the number and type of required parameters.

```java
registerAction("newAction", new ServiceActionDefinition(
        params -> performNewAction((String) params[0], (Integer) params[1]),
String.class, Integer.class
));
```

Once registered and implemented, the action can be invoked through the **ServiceManager**,
specifying the service name, action name, and required parameters.

```java
Object result = serviceManager.handleRequest("T1", "newAction", "testParam", 10);
logger.info(result);
```

It is also possible to create an action without parameters:

```java
registerAction("newAction", new ServiceActionDefinition(
    params -> newAction() // Method without arguments
));
```

### Use Case: Registering a New Service

To create a new service, define a new class extending **BaseService**, then register it in **ServiceManager** using the
`registerService()` method.

> Note: You must pass the `RestTemplate` to each service, as it is a singleton provided by Spring. Otherwise, multiple
> unnecessary instances would be created.

```java
// Example snippet from Interfaces/ServiceManager.java 
public ServiceManager(RestTemplate restTemplate) {
    this.logger = new ServiceManagerLogger();
    // Dynamic service registration
    registerService("T1", T1Service.class, restTemplate);
    registerService("T23", T23Service.class, restTemplate);
    registerService("T4", T4Service.class, restTemplate);
    registerService("T7", T7Service.class, restTemplate);

    registerService("NewService", NewService.class, restTemplate);
}
```