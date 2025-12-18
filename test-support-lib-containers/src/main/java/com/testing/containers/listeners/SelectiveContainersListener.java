package com.testing.containers.listeners;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.annotations.WireMockConfig;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.AnnotationConfigRegistry;
import com.testing.containers.registry.ContainerRegistry;
import com.testing.containers.services.DockerContainersManager;

public class SelectiveContainersListener implements TestExecutionListener {
  private static final String INFLUX_DB_NAME = "influxTestDatabase";

  private static void stubInfluxDbApi() {
    WireMock.stubFor(WireMock.post("/write?db=" + INFLUX_DB_NAME + "&precision=ns&rp=30days").willReturn(WireMock.ok()));
    WireMock.stubFor(WireMock.post("/write?consistency=one&precision=ms&db=" + INFLUX_DB_NAME + "&rp=30days").willReturn(WireMock.ok()));
    WireMock.stubFor(WireMock.post("/query?q=CREATE+DATABASE+%22" + INFLUX_DB_NAME + "%22+WITH+NAME+30days").willReturn(WireMock.ok()));
    WireMock.stubFor(WireMock.post("/events/").willReturn(WireMock.ok()));
  }

  @Override
  public void afterTestClass(TestContext testContext) {
    ContainerRegistry.cleanup();
  }

  @Override
  public void afterTestMethod(TestContext testContext) {
    ContainerRegistry.cleanup();
  }

  @Override
  public void beforeTestClass(TestContext testContext) {
    EnableTestContainers annotation = AnnotationUtils.findAnnotation(testContext.getTestClass(), EnableTestContainers.class);

    if (Objects.nonNull(annotation)) {
      DockerContainersManager.init(EnumSet.copyOf(Arrays.asList(annotation.value())));

      if (ContainerRegistry.isEnabled(ContainerType.WIREMOCK)) {
        AnnotationConfigRegistry.registerWireMockConfig(annotation.wireMockConfig());
        stubApis(annotation.wireMockConfig());
      }
    }
  }

  @Override
  public void beforeTestMethod(TestContext testContext) {
    EnableTestContainers annotation = AnnotationUtils.findAnnotation(testContext.getTestMethod(), EnableTestContainers.class);

    if (Objects.nonNull(annotation)) {
      DockerContainersManager.init(EnumSet.copyOf(Arrays.asList(annotation.value())));
    }
  }

  private void stubApis(WireMockConfig... wireMockConfigs) {
    WireMockContainer wireMockContainer = ContainerRegistry.getWireMock();
    WireMock.configureFor(wireMockContainer.getHost(), wireMockContainer.getMappedPort(8080));

    stubInfluxDbApi();
    for (WireMockConfig wireMockConfig : wireMockConfigs) {
      WireMock.stubFor(WireMock.any(WireMock.urlEqualTo(wireMockConfig.path()))
          .willReturn(WireMock.aResponse().withStatus(wireMockConfig.status())));
    }
  }
}
