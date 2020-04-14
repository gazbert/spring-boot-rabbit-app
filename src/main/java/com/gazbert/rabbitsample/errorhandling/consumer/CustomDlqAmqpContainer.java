package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Convenience container for creating Rabbit Listeners that listen for error messages off the DLQ
 * and attempt to re-send them a configurable amount of times before discarding message.
 *
 * @author gazbert
 */
public class CustomDlqAmqpContainer {

  private static final Logger LOG = LoggerFactory.getLogger(CustomDlqAmqpContainer.class);
  private static final int MAX_RETRIES_COUNT = 3;

  static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
  private final RabbitTemplate rabbitTemplate;

  /**
   * Creates the container with given Rabbit template.
   *
   * @param rabbitTemplate the Rabbit template.
   */
  CustomDlqAmqpContainer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Creates Rabbit Listener for receiving failed message off the DLQ.
   *
   * <p>It tries to resend the message 3 times before discarding it.
   *
   * @param failedMessage the failed message.
   */
  @RabbitListener(queues = DLQ)
  void processFailedMessageAndRetrySendingXTimes(Message failedMessage) {
    Integer retryCount =
        (Integer) failedMessage.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT);
    if (retryCount == null) {
      retryCount = 0;
    }
    if (retryCount >= MAX_RETRIES_COUNT) {
      LOG.warn(String.format("Retry limit of %d exceeded - discarding message", MAX_RETRIES_COUNT));
      return;
    }

    LOG.info("Retrying message for the {} time", retryCount + 1);
    failedMessage.getMessageProperties().getHeaders().put(HEADER_X_RETRIES_COUNT, ++retryCount);
    rabbitTemplate.send(
        MESSAGES_EXCHANGE,
        failedMessage.getMessageProperties().getReceivedRoutingKey(),
        failedMessage);
  }
}
