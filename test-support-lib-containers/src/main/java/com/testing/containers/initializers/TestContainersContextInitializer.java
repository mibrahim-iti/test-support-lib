package com.testing.containers.initializers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.testing.containers.enums.ContainerType;
import com.testing.containers.registry.ContainerRegistry;

public class TestContainersContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
  private static void addArtemisProperties(Map<String, Object> properties) {
    if (ContainerRegistry.isEnabled(ContainerType.ARTEMIS)) {
      ArtemisContainer artemis = ContainerRegistry.getArtemis();
      properties.put("spring.artemis.mode", "native");
      properties.put("spring.artemis.host", artemis.getHost());
      properties.put("spring.artemis.port", artemis.getMappedPort(61616));
      properties.put("spring.artemis.user", artemis.getUser());
      properties.put("spring.artemis.password", artemis.getPassword());
      properties.put("spring.artemis.broker-url", artemis.getBrokerUrl());
    }
  }

  private static void addDynamoDbProperties(Map<String, Object> properties) {
    if (ContainerRegistry.isEnabled(ContainerType.DYNAMODB)) {
      LocalStackContainer dynamodb = ContainerRegistry.getDynamoDb();
      properties.put("dynamodb.region", dynamodb.getRegion());
      properties.put("dynamodb.endpoint", dynamodb.getEndpoint().toString());
    }
  }

  private static void addPostgresProperties(Map<String, Object> properties) {
    if (ContainerRegistry.isEnabled(ContainerType.POSTGRES)) {
      PostgreSQLContainer postgres = ContainerRegistry.getPostgres();
      properties.put("spring.datasource.url", postgres.getJdbcUrl());
      properties.put("spring.datasource.username", postgres.getUsername());
      properties.put("spring.datasource.password", postgres.getPassword());
    }
  }

  private static void addRedisProperties(Map<String, Object> properties) {
    if (ContainerRegistry.isEnabled(ContainerType.REDIS)) {
      GenericContainer<?> redis = ContainerRegistry.get(ContainerType.REDIS);
      properties.put("spring.redis.host", redis.getHost());
      properties.put("spring.redis.port", redis.getMappedPort(6379));
      properties.put("spring.data.redis.host", redis.getHost());
      properties.put("spring.data.redis.port", redis.getFirstMappedPort().toString());
    }
  }

  private static void addSqsProperties(Map<String, Object> properties) {
    if (ContainerRegistry.isEnabled(ContainerType.SQS)) {
      LocalStackContainer sqs = ContainerRegistry.getSqs();
      properties.put("sqs.region", sqs.getRegion());
      properties.put("sqs.endpoint", sqs.getEndpoint().toString());
    }
  }

  private static Map<String, Object> buildProperties() {
    Map<String, Object> properties = new HashMap<>();

    addSqsProperties(properties);
    addDynamoDbProperties(properties);
    addPostgresProperties(properties);
    addRedisProperties(properties);
    addArtemisProperties(properties);

    return properties;
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    applicationContext.getEnvironment()
        .getPropertySources()
        .addFirst(new MapPropertySource("testcontainers", buildProperties()));
  }
}
