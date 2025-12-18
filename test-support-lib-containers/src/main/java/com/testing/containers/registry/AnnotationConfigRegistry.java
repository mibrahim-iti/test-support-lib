package com.testing.containers.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.testing.containers.annotations.WireMockConfig;

public final class AnnotationConfigRegistry {
  private static final List<WireMockConfig> wireMockConfigs = new ArrayList<>();

  private AnnotationConfigRegistry() {
    throw new IllegalStateException("You must not instantiate that class");
  }

  public static List<WireMockConfig> getWireMockConfigs() {
    return Collections.unmodifiableList(wireMockConfigs);
  }

  public static void registerWireMockConfig(WireMockConfig... configs) {
    if (configs.length > 0) {
      wireMockConfigs.addAll(List.of(configs));
    }
  }

  public static void verifyRequests() {
    for (WireMockConfig wireMockConfig : wireMockConfigs) {
      WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(wireMockConfig.path())));
    }
  }
}
