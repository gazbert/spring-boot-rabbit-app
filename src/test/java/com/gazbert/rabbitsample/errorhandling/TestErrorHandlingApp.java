package com.gazbert.rabbitsample.errorhandling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.errorhandling.producer.MessageProducer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the Error Handling Boot app is created and initialised as expected.
 *
 * @author gazbert
 */
public class TestErrorHandlingApp {

  private MessageProducer messageProducer;

  @Before
  public void setupMocks() {
    messageProducer = Mockito.mock(MessageProducer.class);
  }

  @Test
  public void testCreatingAppSuccessfully() {
    final ErrorHandlingApp app = new ErrorHandlingApp(messageProducer);
    assertThat(app).isNotNull();
  }

  @Test
  public void testWhenApplicationReadyEvent() {
    final ErrorHandlingApp app = new ErrorHandlingApp(messageProducer);
    app.whenApplicationReady();
    Mockito.verify(messageProducer).sendMessage();
  }
}
