package com.gazbert.rabbitsample.publish.topic;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.publish.util.MessageLogger;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests Topic consumer receives messages as expected.
 *
 * @author gazbert
 */
public class TestTopicMessageConsumer {

  private static final String MESSAGE = " payload is published!";
  private MessagePayload payload;
  private MessageLogger messageLogger;

  /**
   * Setup MessagePayload and mocks before each test.
   */
  @Before
  public void setUp() {
    payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("WARN");
    payload.setDescription(MESSAGE);

    messageLogger = Mockito.mock(MessageLogger.class);
  }

  @Test
  public void testReceivingWarningMessage() {
    final TopicMessageConsumer consumer = new TopicMessageConsumer(messageLogger);
    assertThatCode(() -> consumer.receiveMessageFromTopicQueue1(payload))
        .doesNotThrowAnyException();
  }

  @Test
  public void testReceivingErrorMessage() {
    final TopicMessageConsumer consumer = new TopicMessageConsumer(messageLogger);
    assertThatCode(() -> consumer.receiveMessageFromTopicQueue2(payload))
        .doesNotThrowAnyException();
  }
}
