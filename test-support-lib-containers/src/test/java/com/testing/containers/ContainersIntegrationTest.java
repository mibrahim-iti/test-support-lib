package com.testing.containers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.ContainerRegistry;

@SpringBootTest
@EnableTestContainers({ContainerType.ARTEMIS, ContainerType.DYNAMODB, ContainerType.POSTGRES, ContainerType.WIREMOCK, ContainerType.SQS})
class ContainersIntegrationTest {
  @Test
  void shouldStartAllContainers() {
    assertNotNull(ContainerRegistry.getSqs());
    assertNotNull(ContainerRegistry.getArtemis());
    assertNotNull(ContainerRegistry.getDynamoDb());
    assertNotNull(ContainerRegistry.getPostgres());
    assertNotNull(ContainerRegistry.getWireMock());
  }
}
