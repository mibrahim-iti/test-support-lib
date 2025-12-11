package com.testing.containers.services;

import org.testcontainers.containers.ContainerState;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

final class PropertyConfigurationService {
  private PropertyConfigurationService() {
    throw new IllegalStateException("You must not instantiate that class");
  }

  public static void configureArtemis(ContainerState container) {
    System.setProperty("spring.artemis.mode", "native");
    System.setProperty("spring.artemis.host", container.getHost());
    System.setProperty("spring.artemis.port", container.getMappedPort(61616).toString());
  }

  public static void configureDynamoDB(LocalStackContainer container) {
    System.setProperty("spring.cloud.aws.dynamodb.endpoint", container.getEndpoint().toString());
  }

  public static void configureLocalStack(LocalStackContainer container) {
    System.setProperty("aws.accessKeyId", container.getAccessKey());
    System.setProperty("aws.secretKey", container.getSecretKey());
    System.setProperty("aws.region", container.getRegion());
  }

  public static void configurePostgres(PostgreSQLContainer container) {
    System.setProperty("spring.datasource.url", container.getJdbcUrl());
    System.setProperty("spring.datasource.username", container.getUsername());
    System.setProperty("spring.datasource.password", container.getPassword());
  }

  public static void configureRedis(ContainerState container) {
    System.setProperty("spring.redis.host", container.getHost());
    System.setProperty("spring.redis.port", String.valueOf(container.getMappedPort(6379)));
  }

  public static void configureSQS(LocalStackContainer container) {
    System.setProperty("spring.cloud.aws.sqs.endpoint", container.getEndpoint().toString());
  }

  public static void configureWireMock(ContainerState container) {
    System.setProperty("wiremock.server.port", String.valueOf(container.getMappedPort(8080)));
  }
}
