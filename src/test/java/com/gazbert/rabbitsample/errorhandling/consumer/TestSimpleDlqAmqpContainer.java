package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;

import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Test creation and behaviour of the SimpleDlqAmqpContainer.
 *
 * @author gazbert
 */
public class TestSimpleDlqAmqpContainer {

  private static final String FAILED_MESSAGE =
      "I say we take off and nuke the entire site from orbit...it's the only way to be sure.";

  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testSimpleDlqAmqpContainerCreation() {
    assertThatCode(() -> new SimpleDlqAmqpContainer(rabbitTemplateMock)).doesNotThrowAnyException();
  }

  @Test
  public void testProcessingFailedMessage() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final SimpleDlqAmqpContainer container = new SimpleDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processFailedMessage(message)).doesNotThrowAnyException();
    Mockito.verify(rabbitTemplateMock)
        .send(
            eq(MESSAGES_EXCHANGE),
            eq(message.getMessageProperties().getReceivedRoutingKey()),
            eq(message));
  }
}
