package com.gazbert.rabbitsample.publish.fanout;

import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_EXCHANGE_NAME;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.gazbert.rabbitsample.domain.MessagePayload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Tests Fanout producer sends messages as expected.
 *
 * @author gazbert
 */
public class TestFanoutMessageProducer {

  private static final String MESSAGE = "Fanout message being sent!";
  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testBroadcast() {
    final FanoutMessageProducer producer = new FanoutMessageProducer(this.rabbitTemplateMock);
    assertThatCode(() -> producer.broadcastMessage(MESSAGE)).doesNotThrowAnyException();

    // Not Routing Key required.
    Mockito.verify(rabbitTemplateMock)
        .convertAndSend(eq(FANOUT_EXCHANGE_NAME), eq(""), any(MessagePayload.class));
  }
}
