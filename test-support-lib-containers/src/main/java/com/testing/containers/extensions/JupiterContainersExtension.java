package com.testing.containers.extensions;

import java.util.Arrays;
import java.util.EnumSet;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;

import com.testing.containers.annotations.EnableTestContainers;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.ContainerRegistry;
import com.testing.containers.services.DockerContainersManager;

public class JupiterContainersExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
  private boolean classLevelUsed = false;

  @Override
  public void afterAll(@NonNull ExtensionContext extensionContext) {
    if (classLevelUsed) {
      ContainerRegistry.cleanup();
    }
  }

  @Override
  public void afterEach(@NonNull ExtensionContext extensionContext) {
    if (!classLevelUsed) {
      ContainerRegistry.cleanup();
    }
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    Class<?> testClass = extensionContext.getRequiredTestClass();
    EnableTestContainers classAnnotations = testClass.getAnnotation(EnableTestContainers.class);

    // Check if any method also has the annotation
    boolean methodLevelUsed = Arrays.stream(testClass.getDeclaredMethods())
        .anyMatch(method -> method.isAnnotationPresent(EnableTestContainers.class));

    if (classAnnotations != null && methodLevelUsed) {
      throw new IllegalStateException(
          "@EnableTestContainers must be applied either on the class or methods, but not both in class: " + testClass.getName()
      );
    }

    if (classAnnotations != null && !testClass.isAnnotationPresent(SpringBootTest.class)) {
      classLevelUsed = true;
      EnumSet<ContainerType> containerTypes = EnumSet.copyOf(Arrays.asList(classAnnotations.value()));
      DockerContainersManager.init(containerTypes);
    }
  }

  @Override
  public void beforeEach(@NonNull ExtensionContext extensionContext) {
    // Check if any method also has the annotation
    Class<?> testClass = extensionContext.getRequiredTestClass();
    boolean methodLevelUsed = Arrays.stream(testClass.getDeclaredMethods())
        .anyMatch(method -> method.isAnnotationPresent(EnableTestContainers.class));

    if (testClass.isAnnotationPresent(SpringBootTest.class) && methodLevelUsed) {
      throw new IllegalStateException(
          "It's not possible to use @EnableTestContainers annotation in the method level for a class annotated with @SpringBootTest");
    }

    if (!classLevelUsed) {
      EnableTestContainers methodAnnotations = extensionContext.getRequiredTestMethod().getAnnotation(EnableTestContainers.class);

      if (methodAnnotations != null) {
        EnumSet<ContainerType> containerTypes = EnumSet.copyOf(Arrays.asList(methodAnnotations.value()));
        DockerContainersManager.init(containerTypes);
      }
    }
  }
}
