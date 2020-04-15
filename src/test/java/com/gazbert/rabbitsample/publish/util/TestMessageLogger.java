package com.gazbert.rabbitsample.publish.util;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the MessageLogger behaves as expected.
 *
 * @author gazbert
 */
public class TestMessageLogger {

  private MessagePayload payload;

  /**
   * Setup for each test.x
   */
  @Before
  public void setup() {
    payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription("Some direct message!");
  }

  @Test
  public void testLoggingMessageForIntegrationTest() {
    final MessageLogger messageLogger = new MessageLogger();
    messageLogger.setActiveProfile("integration-test");
    assertThatCode(() -> messageLogger.logMessage(payload)).doesNotThrowAnyException();
  }

  @Test
  public void testLoggingMessageForNonIntegrationTest() {
    final MessageLogger messageLogger = new MessageLogger();
    assertThatCode(() -> messageLogger.logMessage(payload)).doesNotThrowAnyException();
  }
}
