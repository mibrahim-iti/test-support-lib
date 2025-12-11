package com.testing.containers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.exceptions.ContainerNotEnabledException;
import com.testing.containers.registry.ContainerRegistry;

@EnableTestContainers({ContainerType.ARTEMIS, ContainerType.DYNAMODB, ContainerType.POSTGRES, ContainerType.SQS})
class ContainersIntegrationJunitClassLevelTest {
  @Test
  void shouldStartOnlyArtemisContainer() {
    assertNotNull(ContainerRegistry.getArtemis());
  }

  @Test
  void shouldStartOnlyDynamoDbContainer() {
    assertNotNull(ContainerRegistry.getDynamoDb());
  }

  @Test
  void shouldStartOnlyPostgresContainer() {
    assertNotNull(ContainerRegistry.getSqs());
  }

  @Test
  void shouldStartOnlySqsContainer() {
    assertNotNull(ContainerRegistry.getSqs());
  }

  @Test
  void shouldStartOnlyWireMockContainer() {
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getWireMock, "WIREMOCK container is not enabled");
  }
}
