package com.testing.containers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import com.testing.containers.enums.ContainerType;
import com.testing.containers.initializers.TestContainersContextInitializer;
import com.testing.containers.listeners.SelectiveContainersListener;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@TestExecutionListeners( // Register the listener for any test class annotated with this
    listeners = SelectiveContainersListener.class,
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@ContextConfiguration(initializers = TestContainersContextInitializer.class)
public @interface EnableTestContainers {
  ContainerType[] value() default {};
}
