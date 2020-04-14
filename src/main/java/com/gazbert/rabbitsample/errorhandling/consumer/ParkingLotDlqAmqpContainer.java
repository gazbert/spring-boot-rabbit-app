package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Convenience container for creating Rabbit Listeners that listen for error messages off the DLQ
 * and attempt to re-send them a configurable amount of times.
 *
 * <p>If the retry limit is exceeded, the message is sent to the Parking Lot queue for manual
 * intervention / audit logging / saved to database etc...
 *
 * @author gazbert
 */
public class ParkingLotDlqAmqpContainer {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParkingLotDlqAmqpContainer.class);
  private static final String HEADER_X_RETRIES_COUNT = "x-retries-count";

  private final RabbitTemplate rabbitTemplate;
  private static final int MAX_RETRIES_COUNT = 3;

  /**
   * Creates the container with given Rabbit template.
   *
   * @param rabbitTemplate the Rabbit template.
   */
  ParkingLotDlqAmqpContainer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Tries to resent the message a configurable number of times. If the retry limit is exceeded,
   * message is sent to Parking Lot queue.
   *
   * @param failedMessage the failed message.
   */
  @RabbitListener(queues = DLQ)
  void processFailedMessagesRetryThenUseParkingLot(Message failedMessage) {

    Integer retriesCnt =
        (Integer) failedMessage.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT);
    if (retriesCnt == null) {
      retriesCnt = 0;
    }

    if (retriesCnt >= MAX_RETRIES_COUNT) {
      LOGGER.warn(String.format("Retry limit of %d exceeded!", MAX_RETRIES_COUNT));
      LOGGER.info("Sending message to the parking lot queue...");
      rabbitTemplate.send(
          PARKING_LOT_EXCHANGE,
          failedMessage.getMessageProperties().getReceivedRoutingKey(),
          failedMessage);
      return;
    }

    LOGGER.info("Retrying message for the {} time", retriesCnt + 1);
    failedMessage.getMessageProperties().getHeaders().put(HEADER_X_RETRIES_COUNT, ++retriesCnt);
    rabbitTemplate.send(
        MESSAGES_EXCHANGE,
        failedMessage.getMessageProperties().getReceivedRoutingKey(),
        failedMessage);
  }

  /**
   * Listens to failed messages that arrive on the Parking Lot queue.
   *
   * <p>Just logs the message, but could be sent to database for manual investigation etc...
   *
   * <p>If this Listener is commented out, you can view the message in the Parking Lot queue using
   * the Rabbit UI: http://localhost:15672 (until it expires after 5 minutes).
   *
   * @param failedMessage the failed message.
   */
  @RabbitListener(queues = PARKING_LOT_QUEUE)
  void processParkingLotQueue(Message failedMessage) {
    LOGGER.info("Received message in parking lot queue {}", failedMessage.toString());
  }
}
