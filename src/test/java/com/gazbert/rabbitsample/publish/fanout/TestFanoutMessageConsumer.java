package com.gazbert.rabbitsample.publish.fanout;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.junit.Test;

/**
 * Tests Fanout consumer receives messages as expected.
 *
 * @author gazbert
 */
public class TestFanoutMessageConsumer {

  private static final String MESSAGE = "Fanout message being sent!";

  @Test
  public void testReceiveBroadcastMessage() {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription(MESSAGE);

    final FanoutMessageConsumer consumer = new FanoutMessageConsumer();
    assertThatCode(() -> consumer.receiveMessageFromFanout1(payload)).doesNotThrowAnyException();
    assertThatCode(() -> consumer.receiveMessageFromFanout2(payload)).doesNotThrowAnyException();
  }
}
