package com.testing.containers.enums;

public enum ContainerType {
  POSTGRES("postgres"), REDIS("redis"), ARTEMIS("artemis"),
  DYNAMODB("dynamodb"), SQS("sqs"), WIREMOCK("wiremock");

  private final String name;

  ContainerType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
