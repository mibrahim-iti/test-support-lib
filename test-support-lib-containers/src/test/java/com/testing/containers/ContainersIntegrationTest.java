package com.testing.containers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.ContainerRegistry;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableTestContainers({ContainerType.ARTEMIS, ContainerType.DYNAMODB})
class ContainersIntegrationTest {
    @Test
    void shouldStartContainers() {
        assertNotNull(ContainerRegistry.getArtemis());
        assertNotNull(ContainerRegistry.getDynamoDb());
    }
}
