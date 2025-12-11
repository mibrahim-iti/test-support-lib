package com.testing.containers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import com.testing.containers.enums.ContainerType;
import com.testing.containers.extensions.JupiterContainersExtension;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(JupiterContainersExtension.class)
public @interface EnableJupiterTestContainers {
  ContainerType[] value() default {};
}
