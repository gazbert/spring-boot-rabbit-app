package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.consumer.CustomDlqAmqpContainer.HEADER_X_RETRIES_COUNT;
import static org.assertj.core.api.Assertions.assertThat;
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
 * Test creation and behaviour of the CustomDlqAmqpContainer.
 *
 * @author gazbert
 */
public class TestCustomDlqAmqpContainer {

  private static final String FAILED_MESSAGE =
      "I say we take off and nuke the entire site from orbit... it's the only way to be sure.";
  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testCustomDlqAmqpContainerCreation() {
    assertThatCode(() -> new CustomDlqAmqpContainer(rabbitTemplateMock)).doesNotThrowAnyException();
  }

  @Test
  public void testProcessFailedMessageAndRetrySendingFirstTime() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    messageProperties.getHeaders().put(HEADER_X_RETRIES_COUNT, 0);

    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final CustomDlqAmqpContainer container = new CustomDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processFailedMessageAndRetrySendingXTimes(message))
        .doesNotThrowAnyException();
    assertThat(message.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT))
        .isEqualTo(1);
    Mockito.verify(rabbitTemplateMock)
        .send(
            eq(MESSAGES_EXCHANGE),
            eq(message.getMessageProperties().getReceivedRoutingKey()),
            eq(message));
  }

  @Test
  public void testProcessFailedMessageAndRetrySendingSecondTime() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    messageProperties.getHeaders().put(HEADER_X_RETRIES_COUNT, 1);

    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final CustomDlqAmqpContainer container = new CustomDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processFailedMessageAndRetrySendingXTimes(message))
        .doesNotThrowAnyException();
    assertThat(message.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT))
        .isEqualTo(2);
    Mockito.verify(rabbitTemplateMock)
        .send(
            eq(MESSAGES_EXCHANGE),
            eq(message.getMessageProperties().getReceivedRoutingKey()),
            eq(message));
  }

  @Test
  public void testProcessFailedMessageAndRetryCountExceeded() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    messageProperties.getHeaders().put(HEADER_X_RETRIES_COUNT, 3);

    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final CustomDlqAmqpContainer container = new CustomDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processFailedMessageAndRetrySendingXTimes(message))
        .doesNotThrowAnyException();
    assertThat(message.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT))
        .isEqualTo(3);
    Mockito.verifyNoInteractions(rabbitTemplateMock);
  }
}
