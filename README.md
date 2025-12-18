# Test Support Library

A lightweight library that provides a modular testing infrastructure framework that unifies Testcontainers, WireMock, JMS test utilities, and other test-time
integrations under a single, extensible annotation model. The library automatically enables seamless initialization, startup, and cleanup of test resources.

## Features

- **Unified Annotation Model**: Single `@EnableTestContainers` annotation to manage multiple container types
- **Automatic Lifecycle Management**: Containers are automatically started before tests and cleaned up after
- **Selective Container Activation**: Enable only the containers you need for specific test classes
- **Spring Boot Integration**: Seamless integration with Spring Boot test framework
- **Container Registry**: Centralized access to running containers through `ContainerRegistry`
- **Exception Safety**: Proper error handling for disabled containers

## Supported Container Types

The library currently supports the following container types:

- **PostgreSQL** - Relational database testing
- **Redis** - In-memory data structure store
- **Artemis** - JMS message broker
- **DynamoDB** - NoSQL database (via LocalStack)
- **SQS** - Message queuing service (via LocalStack)
- **WireMock** - HTTP service mocking

## Installation

Add the following dependency to your `pom.xml`:

```xml

<dependency>
  <groupId>com.test.support.lib</groupId>
  <artifactId>test-support-lib-containers</artifactId>
  <version>${test-support-lib-containers.version}</version>
  <scope>test</scope>
</dependency>
```

## Usage

### Basic Usage

Use the `@EnableTestContainers` annotation on top of your `@SpringBootTest` class:

```java

@SpringBootTest
@EnableTestContainers({ContainerType.ARTEMIS, ContainerType.DYNAMODB})
class DemoApplicationTests {

  @Test
  void testContainersTest() {
    // Access running containers through ContainerRegistry
    Assertions.assertNotNull(ContainerRegistry.getArtemis());
    Assertions.assertNotNull(ContainerRegistry.getDynamoDb());

    // Attempting to access disabled containers throws exception
    Assertions.assertThrows(ContainerNotEnabledException.class,
        ContainerRegistry::getSqs);
  }
}
```

### Container Registry

Access running containers through the `ContainerRegistry` class:

```java
// Get specific container types
ArtemisContainer artemis = ContainerRegistry.getArtemis();
LocalStackContainer dynamodb = ContainerRegistry.getDynamoDb();
PostgreSQLContainer postgres = ContainerRegistry.getPostgres();
LocalStackContainer sqs = ContainerRegistry.getSqs();
WireMockContainer wiremock = ContainerRegistry.getWireMock();

// Generic container access
GenericContainer<?> container = ContainerRegistry.get(ContainerType.POSTGRES);

// Check if container is enabled
boolean isEnabled = ContainerRegistry.isEnabled(ContainerType.REDIS);
```

### Available Container Types

```java
public enum ContainerType {
  POSTGRES,   // PostgreSQL database
  REDIS,      // Redis cache
  ARTEMIS,    // ActiveMQ Artemis JMS broker
  DYNAMODB,   // DynamoDB via LocalStack
  SQS,        // SQS via LocalStack
  WIREMOCK    // WireMock HTTP service mock
}
```

### Specialized Annotations

For convenience, the library also provides specialized annotations for single container types:

```java

@EnablePostgresOnlyTest
class PostgresOnlyTest {
  // Only PostgreSQL container will be started
}

@EnableDynamodbOnlyTest
class DynamoDbOnlyTest {
  // Only DynamoDB container will be started
}
```

## Architecture

The library is built around several key components:

- **Annotations**: `@EnableTestContainers` and specialized single-container annotations
- **Container Registry**: Centralized container management and access
- **Context Initializer**: Spring context initialization for container setup
- **Test Execution Listener**: Manages container lifecycle during test execution
- **Container Manager**: Handles Docker container operations
- **Property Configuration**: Manages container configuration properties

### Test Execution Lifecycle

![Lifecycle](test-support-lib-containers/src/main/resources/lifecycle.png)

<details>
<summary>Click to view PlantUML source code</summary>

[View PlantUML lifecycle](test-support-lib-containers/src/main/resources/lifecycle.puml)

</details>

## Error Handling

The library provides proper error handling through:

- `ContainerNotEnabledException`: Thrown when attempting to access a container that wasn't enabled
- Automatic cleanup of containers on test completion
- Safe container state management

## Future Enhancements

The following features are planned for future releases:

### Short Term

- **More Container Types**: Additional container support as needed
- **Method Level Support**: Enable containers at the method level
- **Annotation Validation**: Prevent using class and method level annotations simultaneously
- **Custom Configuration**: Support for custom container configuration files
- **WireMock Integration**: Support for WireMock
- **JMS Integration**: Support for JMS

### Medium Term

- **Test Suite Caching**: Container reuse across multiple test classes for improved performance
- **JUnit Extension Auto-registration**: Automatic registration of JUnit extensions
- **Maven Plugin**: Automatic dependency addition based on annotation detection

### Long Term

- **Enhanced WireMock Integration**: Array-based configuration with request verification
- **Enhanced JMS Testing Utilities**: Comprehensive JMS testing support
- **Performance Optimizations**: Advanced caching and container sharing strategies

## Requirements

- Java 21+
- Spring Boot 3.x
- Maven 3.6+
- Docker (for running containers)

## Contributing

For contributions and issues, please feel free to suggest new features or make a pull request.

## License

Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0);
