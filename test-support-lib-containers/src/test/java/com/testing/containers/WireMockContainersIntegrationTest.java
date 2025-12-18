package com.testing.containers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.annotations.WireMockConfig;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.AnnotationConfigRegistry;
import com.testing.containers.registry.ContainerRegistry;

@SpringBootTest
@EnableTestContainers(value = {ContainerType.WIREMOCK},
    wireMockConfig = {
        @WireMockConfig(path = "/api/test1"),
        @WireMockConfig(path = "/api/test2", status = 404)
    }
)
class WireMockContainersIntegrationTest {
  @Test
  void shouldVerifyRequests() {
    TestHttpClient client = new TestHttpClient();

    int status1 = client.getStatus(ContainerRegistry.getWireMock().getBaseUrl() + "/api/test1");
    int status2 = client.getStatus(ContainerRegistry.getWireMock().getBaseUrl() + "/api/test2");

    assertEquals(200, status1);
    assertEquals(404, status2);
    AnnotationConfigRegistry.verifyRequests();
  }
}
