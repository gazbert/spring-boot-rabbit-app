package com.gazbert.rabbitsample.publish.fanout;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.publish.util.MessageLogger;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests Fanout consumer receives messages as expected.
 *
 * @author gazbert
 */
public class TestFanoutMessageConsumer {

  private static final String MESSAGE = "Fanout message being sent!";
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
    payload.setDescription(MESSAGE);


    final FanoutMessageConsumer consumer = new FanoutMessageConsumer(messageLogger);
    assertThatCode(() -> consumer.receiveMessageFromFanout1(payload)).doesNotThrowAnyException();
    assertThatCode(() -> consumer.receiveMessageFromFanout2(payload)).doesNotThrowAnyException();
  }
}
