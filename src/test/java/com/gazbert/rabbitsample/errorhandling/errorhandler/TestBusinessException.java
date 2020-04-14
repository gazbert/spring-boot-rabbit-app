package com.gazbert.rabbitsample.errorhandling.errorhandler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests the BusinessException is created successfully.
 *
 * @author gazbert
 */
public class TestBusinessException {

  private static final String ERROR_MESSAGE = "Everything's shiny, Cap'n. Not to fret.";

  @Test
  public void testExceptionCreation() {
    final BusinessException exception = new BusinessException(ERROR_MESSAGE);
    assertThat(exception.getMessage()).isEqualTo(ERROR_MESSAGE);
  }
}
