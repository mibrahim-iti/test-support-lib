package com.testing.containers.services;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import com.testing.containers.common.CommonTestContainers;
import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.ContainerRegistry;

public final class DockerContainersManager {
  private static final EnumMap<ContainerType, Runnable> INIT_ACTIONS = new EnumMap<>(ContainerType.class);
  private static final ReentrantLock lock = new ReentrantLock(true);

  static {
    INIT_ACTIONS.put(ContainerType.ARTEMIS, DockerContainersManager::initArtemis);
    INIT_ACTIONS.put(ContainerType.POSTGRES, DockerContainersManager::initPostgres);
    INIT_ACTIONS.put(ContainerType.REDIS, DockerContainersManager::initRedis);
    INIT_ACTIONS.put(ContainerType.WIREMOCK, DockerContainersManager::initWireMock);
    INIT_ACTIONS.put(ContainerType.DYNAMODB, () -> {
    }); // handled in initLocalStack
    INIT_ACTIONS.put(ContainerType.SQS, () -> {
    });      // handled in initLocalStack
  }

  private DockerContainersManager() {
    throw new IllegalStateException("You must not instantiate that class");
  }

  public static void init(Set<ContainerType> containerTypes) {
    lock.lock();
    try {
      // Initialize single containers
      containerTypes.forEach(containerType -> {
        if (containerType != ContainerType.DYNAMODB && containerType != ContainerType.SQS) {
          Runnable action = INIT_ACTIONS.get(containerType);
          if (Objects.nonNull(action)) {
            action.run();
          }
        }
      });

      // Initialize LocalStack if either DYNAMODB or SQS is containerTypes
      boolean dynamodb = containerTypes.contains(ContainerType.DYNAMODB);
      boolean sqs = containerTypes.contains(ContainerType.SQS);
      if (dynamodb || sqs) {
        initLocalStack(dynamodb, sqs);
      }

    } finally {
      lock.unlock();
    }
  }

  private static void initArtemis() {
    ArtemisContainer container = CommonTestContainers.artemis();
    container.start();
    ContainerRegistry.register(ContainerType.ARTEMIS, container);
    PropertyConfigurationService.configureArtemis(container);
  }

  private static void initLocalStack(boolean dynamodb, boolean sqs) {
    List<String> services = new ArrayList<>();
    if (dynamodb) {
      services.add("dynamodb");
    }
    if (sqs) {
      services.add("sqs");
    }

    LocalStackContainer container = CommonTestContainers.localStack(services.toArray(new String[0]));
    container.start();

    PropertyConfigurationService.configureLocalStack(container);

    if (dynamodb) {
      ContainerRegistry.register(ContainerType.DYNAMODB, container);
      PropertyConfigurationService.configureDynamoDB(container);
    }
    if (sqs) {
      ContainerRegistry.register(ContainerType.SQS, container);
      PropertyConfigurationService.configureSQS(container);
    }
  }

  private static void initPostgres() {
    PostgreSQLContainer container = CommonTestContainers.postgres();
    container.start();
    ContainerRegistry.register(ContainerType.POSTGRES, container);
    PropertyConfigurationService.configurePostgres(container);
  }

  private static void initRedis() {
    GenericContainer<?> container = CommonTestContainers.redis();
    container.start();
    ContainerRegistry.register(ContainerType.REDIS, container);
    PropertyConfigurationService.configureRedis(container);
  }

  private static void initWireMock() {
    WireMockContainer container = CommonTestContainers.wiremock();
    container.start();
    ContainerRegistry.register(ContainerType.WIREMOCK, container);
    PropertyConfigurationService.configureWireMock(container);
  }
}
