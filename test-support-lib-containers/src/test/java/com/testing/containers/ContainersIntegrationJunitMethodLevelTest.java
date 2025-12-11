package com.testing.containers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.exceptions.ContainerNotEnabledException;
import com.testing.containers.registry.ContainerRegistry;

class ContainersIntegrationJunitMethodLevelTest {
  @Test
  @EnableTestContainers({ContainerType.ARTEMIS, ContainerType.DYNAMODB, ContainerType.POSTGRES, ContainerType.WIREMOCK, ContainerType.SQS})
  void shouldStartAllContainers() {
    assertNotNull(ContainerRegistry.getSqs());
    assertNotNull(ContainerRegistry.getArtemis());
    assertNotNull(ContainerRegistry.getDynamoDb());
    assertNotNull(ContainerRegistry.getPostgres());
    assertNotNull(ContainerRegistry.getWireMock());
  }

  @Test
  @EnableTestContainers({ContainerType.ARTEMIS})
  void shouldStartOnlyArtemisContainer() {
    assertNotNull(ContainerRegistry.getArtemis());
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getSqs, "SQS container is not enabled");
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getDynamoDb, "DYNAMODB container is not enabled");
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getPostgres, "POSTGRES container is not enabled");
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getWireMock, "WIREMOCK container is not enabled");
  }

  @Test
  @EnableTestContainers({ContainerType.SQS})
  void shouldStartOnlySqsContainer() {
    assertNotNull(ContainerRegistry.getSqs());
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getArtemis, "ARTEMIS container is not enabled");
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getDynamoDb, "DYNAMODB container is not enabled");
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getPostgres, "POSTGRES container is not enabled");
    assertThrowsExactly(ContainerNotEnabledException.class, ContainerRegistry::getWireMock, "WIREMOCK container is not enabled");
  }
}
