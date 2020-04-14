package com.gazbert.rabbitsample.publish.direct;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.junit.Test;

/**
 * Tests Direct consumer receives message as expected.
 *
 * @author gazbert
 */
public class TestDirectMessageConsumer {

  @Test
  public void testReceiveBroadcastMessage() {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription("Some direct message!");

    final DirectMessageConsumer consumer = new DirectMessageConsumer();
    assertThatCode(() -> consumer.receiveDirectMessage(payload))
        .doesNotThrowAnyException();
  }
}
