package com.gazbert.rabbitsample.publish.direct;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.publish.util.MessageLogger;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests Direct consumer receives message as expected.
 *
 * @author gazbert
 */
public class TestDirectMessageConsumer {

  private MessageLogger messageLogger;

  /**
   * Setup before each test.
   */
  @Before
  public void setUp() {
    messageLogger = Mockito.mock(MessageLogger.class);
  }

  @Test
  public void testReceiveBroadcastMessage() {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription("Some direct message!");

    final DirectMessageConsumer consumer = new DirectMessageConsumer(messageLogger);
    assertThatCode(() -> consumer.receiveDirectMessage(payload))
        .doesNotThrowAnyException();
  }
}
