package com.gazbert.rabbitsample.publish.direct;

import static com.gazbert.rabbitsample.publish.direct.DirectExchangeConfiguration.QUEUE_NAME;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.gazbert.rabbitsample.domain.MessagePayload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Tests Direct producer sends message as expected.
 *
 * @author gazbert
 */
public class TestDirectMessageProducer {

  private static final String MESSAGE = "Direct message being sent!";
  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testSendingMessage() {
    final DirectMessageProducer producer = new DirectMessageProducer(rabbitTemplateMock);
    assertThatCode(() -> producer.sendMessage(MESSAGE)).doesNotThrowAnyException();
    Mockito.verify(rabbitTemplateMock).convertAndSend(eq(QUEUE_NAME), any(MessagePayload.class));
  }
}
