package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_EXCHANGE;
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
 * Test creation and behaviour of the ParkingLotDlqAmqpContainer.
 *
 * @author gazbert
 */
public class TestParkingLotDlqAmqpContainer {

  private static final String FAILED_MESSAGE =
      "I say we take off and nuke the entire site from orbit... it's the only way to be sure.";
  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testParkingLotDlqAmqpContainerCreation() {
    assertThatCode(() -> new ParkingLotDlqAmqpContainer(rabbitTemplateMock))
        .doesNotThrowAnyException();
  }

  @Test
  public void testProcessFailedMessageAndRetrySendingFirstTime() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    messageProperties.getHeaders().put(HEADER_X_RETRIES_COUNT, 1);

    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final ParkingLotDlqAmqpContainer container = new ParkingLotDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processFailedMessagesRetryThenUseParkingLot(message))
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
  public void testProcessFailedMessageAndUseParkingLot() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    messageProperties.getHeaders().put(HEADER_X_RETRIES_COUNT, 3);

    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final ParkingLotDlqAmqpContainer container = new ParkingLotDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processFailedMessagesRetryThenUseParkingLot(message))
        .doesNotThrowAnyException();
    assertThat(message.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT))
        .isEqualTo(3);

    Mockito.verify(rabbitTemplateMock)
        .send(
            eq(PARKING_LOT_EXCHANGE),
            eq(message.getMessageProperties().getReceivedRoutingKey()),
            eq(message));
  }

  @Test
  public void testProcessingParkingLotQueue() {
    final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
    messageProperties.setReceivedRoutingKey(DLQ);
    messageProperties.getHeaders().put(HEADER_X_RETRIES_COUNT, 3);

    final Message message =
        new Message(FAILED_MESSAGE.getBytes(Charset.forName("UTF-8")), messageProperties);

    final ParkingLotDlqAmqpContainer container = new ParkingLotDlqAmqpContainer(rabbitTemplateMock);
    assertThatCode(() -> container.processParkingLotQueue(message)).doesNotThrowAnyException();

    Mockito.verifyNoInteractions(rabbitTemplateMock);
  }
}
