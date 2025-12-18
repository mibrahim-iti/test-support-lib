package com.testing.containers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.testing.containers.enums.ContainerType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableTestContainers({ContainerType.DYNAMODB})
public @interface EnableDynamodbOnlyTest {
}
