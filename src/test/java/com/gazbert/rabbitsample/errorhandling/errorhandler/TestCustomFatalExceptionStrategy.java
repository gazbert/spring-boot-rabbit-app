package com.gazbert.rabbitsample.errorhandling.errorhandler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.amqp.AmqpException;

/**
 * Tests the CustomFatalExceptionStrategy is created successfully.
 *
 * @author gazbert
 */
public class TestCustomFatalExceptionStrategy {

  @Test
  public void testCustomFatalStrategyRecognisesFatalError() {
    final Exception fatalError = new RuntimeException("It's dead Jim!");
    final AmqpException amqpException = new AmqpException(fatalError);
    final CustomFatalExceptionStrategy strategy = new CustomFatalExceptionStrategy();
    assertThat(strategy.isFatal(amqpException)).isEqualTo(true);
  }

  @Test
  public void testCustomFatalStrategyRecognisesNonFatalError() {
    final BusinessException nonFatalError =
        new BusinessException("Everything's shiny, Cap'n. Not to fret.");
    final AmqpException amqpException = new AmqpException(nonFatalError);
    final CustomFatalExceptionStrategy strategy = new CustomFatalExceptionStrategy();
    assertThat(strategy.isFatal(amqpException)).isEqualTo(false);
  }
}
