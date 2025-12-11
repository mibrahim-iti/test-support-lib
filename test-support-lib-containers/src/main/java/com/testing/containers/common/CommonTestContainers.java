package com.testing.containers.common;

import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.time.Duration;

public final class CommonTestContainers {
  private static final String LATEST = "latest";
  private static final String SHARED_SERVICES_DOCKER_PUBLIC = ""; //CHANGE ME if you must use a private registery for your company, example: docker.io/

  private CommonTestContainers() {
  }

  @SuppressWarnings("resource")
  public static ArtemisContainer artemis() {
    return new ArtemisContainer(getDockerImageName("apache/activemq-artemis").withTag(LATEST))
        .withEnv("ANONYMOUS_LOGIN", "true")
        .withReuse(true)
        .withStartupTimeout(Duration.ofMinutes(1));
  }

  @SuppressWarnings("resource")
  public static LocalStackContainer localStack(String... services) {
    return new LocalStackContainer(getDockerImageName("localstack/localstack").withTag(LATEST))
        .withServices(services)
        .withReuse(true);
  }

  @SuppressWarnings("resource")
  public static PostgreSQLContainer postgres() {
    return new PostgreSQLContainer(getDockerImageName("postgres:16-alpine").withTag(LATEST))
        .withReuse(true);
  }

  @SuppressWarnings({"resource", "rawtypes"})
  public static GenericContainer redis() {
    return new GenericContainer<>(getDockerImageName("valkey/valkey").withTag(LATEST))
        .withExposedPorts(6379)
        .withReuse(true);
  }

  @SuppressWarnings("resource")
  public static WireMockContainer wiremock() {
    return new WireMockContainer(getDockerImageName("wiremock/wiremock").withTag("latest-alpine")).withReuse(true);
  }

  private static DockerImageName getDockerImageName(String imageName) {
    return DockerImageName.parse(SHARED_SERVICES_DOCKER_PUBLIC + imageName).asCompatibleSubstituteFor(imageName);
  }
}
