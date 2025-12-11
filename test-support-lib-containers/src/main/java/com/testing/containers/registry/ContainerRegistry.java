package com.testing.containers.registry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import com.testing.containers.enums.ContainerType;
import com.testing.containers.exceptions.ContainerNotEnabledException;

public final class ContainerRegistry {
  private static final ConcurrentMap<ContainerType, GenericContainer<?>> containers = new ConcurrentHashMap<>();
  private static final ConcurrentMap<ContainerType, Boolean> enabled = new ConcurrentHashMap<>();

  private ContainerRegistry() {
    throw new IllegalStateException("You must not instantiate that class");
  }

  public static void cleanup() {
    containers.values().forEach(container -> {
      if (container.isRunning())
        container.stop();
    });
    containers.clear();
    enabled.clear();
  }

  public static GenericContainer<?> get(ContainerType containerType) {
    if(Boolean.FALSE.equals(enabled.getOrDefault(containerType, Boolean.FALSE))) {
      throw new ContainerNotEnabledException(containerType.getName() + " container is not enabled");
    }

    return containers.computeIfAbsent(containerType, t -> {
      throw new ContainerNotEnabledException(t.getName() + " container is registered as enabled but not found");
    });
  }

  public static ArtemisContainer getArtemis() {
    return (ArtemisContainer) get(ContainerType.ARTEMIS);
  }

  public static LocalStackContainer getDynamoDb() {
    return (LocalStackContainer) get(ContainerType.DYNAMODB);
  }

  public static PostgreSQLContainer getPostgres() {
    return (PostgreSQLContainer) get(ContainerType.POSTGRES);
  }

  public static LocalStackContainer getSqs() {
    return (LocalStackContainer) get(ContainerType.SQS);
  }

  public static WireMockContainer getWireMock() {
    return (WireMockContainer) get(ContainerType.WIREMOCK);
  }

  public static boolean isEnabled(ContainerType type) {
    return Boolean.TRUE.equals(enabled.get(type));
  }

  public static synchronized void register(ContainerType type, GenericContainer<?> container) {
    containers.put(type, container);
    enabled.put(type, Boolean.TRUE);
  }
}
