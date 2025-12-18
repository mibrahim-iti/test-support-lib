package com.testing.containers.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface WireMockConfig {
  String path() default "/";

  int status() default 200;
}
