package com.testing.containers.exceptions;

import java.io.Serial;

public class ContainerNotEnabledException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public ContainerNotEnabledException(String message) {
    super(message);
  }
}
