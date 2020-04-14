package com.gazbert.rabbitsample.errorhandling.producer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.errorhandling.producer.MessageProducer.TtlMessagePostProcessor;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Tests Message producer sends message as expected.
 *
 * @author gazbert
 */
public class TestMessageProducer {

  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testSendingMessage() {
    final MessageProducer messageProducer = new MessageProducer(rabbitTemplateMock);
    assertThatCode(messageProducer::sendMessage).doesNotThrowAnyException();
    Mockito.verify(rabbitTemplateMock)
        .convertAndSend(
            eq(MESSAGES_EXCHANGE),
            eq(MESSAGES_QUEUE),
            any(MessagePayload.class),
            any(MessagePostProcessor.class));
  }

  @Test
  public void testPostProcessor() {
    final byte[] payload = "The message".getBytes(Charset.forName("UTF-8"));
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(MESSAGES_QUEUE);
    final Message message = new Message(payload, messageProperties);

    assertThat(message.getMessageProperties().getHeaders().get("expiration")).isNull();

    final TtlMessagePostProcessor postProcessor = new TtlMessagePostProcessor(60);
    final Message processedMessage = postProcessor.postProcessMessage(message);

    assertThat(processedMessage.getMessageProperties().getHeaders().get("expiration"))
        .isEqualTo(Integer.toString(60));
  }
}
