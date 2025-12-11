package com.testing.containers.listeners;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.registry.ContainerRegistry;
import com.testing.containers.services.DockerContainersManager;

public class SelectiveContainersListener implements TestExecutionListener {
  @Override
  public void afterTestClass(TestContext testContext) {
    ContainerRegistry.cleanup();
  }

  @Override
  public void beforeTestClass(TestContext testContext) {
    EnableTestContainers annotation = AnnotationUtils.findAnnotation(testContext.getTestClass(), EnableTestContainers.class);

    if (Objects.nonNull(annotation)) {
      DockerContainersManager.init(EnumSet.copyOf(Arrays.asList(annotation.value())));
    }
  }
}
