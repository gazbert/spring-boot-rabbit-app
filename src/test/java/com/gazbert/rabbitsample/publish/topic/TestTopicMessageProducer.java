package com.gazbert.rabbitsample.publish.topic;

import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_EXCHANGE_NAME;
import static com.gazbert.rabbitsample.publish.topic.TopicMessageProducer.ROUTING_KEY_ALERT_HIGH_ERROR;
import static com.gazbert.rabbitsample.publish.topic.TopicMessageProducer.ROUTING_KEY_ALERT_HIGH_WARN;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.gazbert.rabbitsample.domain.MessagePayload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Tests Topic producer sends messages as expected.
 *
 * @author gazbert
 */
public class TestTopicMessageProducer {

  private static final String WARNING_MESSAGE = "A warning message!";
  private static final String ERROR_MESSAGE = "An error message!";
  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testSendingWarningMessage() {
    final TopicMessageProducer producer = new TopicMessageProducer(rabbitTemplateMock);
    assertThatCode(() -> producer.sendWarningMessage(WARNING_MESSAGE)).doesNotThrowAnyException();

    Mockito.verify(rabbitTemplateMock)
        .convertAndSend(
            eq(TOPIC_EXCHANGE_NAME), eq(ROUTING_KEY_ALERT_HIGH_WARN), any(MessagePayload.class));
  }

  @Test
  public void testSendingErrorMessage() {
    final TopicMessageProducer producer = new TopicMessageProducer(rabbitTemplateMock);
    assertThatCode(() -> producer.sendErrorMessage(ERROR_MESSAGE)).doesNotThrowAnyException();

    Mockito.verify(rabbitTemplateMock)
        .convertAndSend(
            eq(TOPIC_EXCHANGE_NAME), eq(ROUTING_KEY_ALERT_HIGH_ERROR), any(MessagePayload.class));
  }
}
