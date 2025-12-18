package com.testing.containers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.testing.containers.enums.ContainerType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@EnableSpringTestContainers   // Works only when Spring TestContext is active
@EnableJupiterTestContainers // Works only when JUnit 5 is active
public @interface EnableTestContainers {
  ContainerType[] value() default {};

  WireMockConfig[] wireMockConfig() default {};
}
