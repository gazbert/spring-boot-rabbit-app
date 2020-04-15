package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Convenience container for creating Rabbit Listeners that listen for error messages on the DLQ.
 *
 * @author gazbert
 */
public class SimpleDlqAmqpContainer {

  private static final Logger LOG = LoggerFactory.getLogger(SimpleDlqAmqpContainer.class);
  private final RabbitTemplate rabbitTemplate;

  /**
   * Creates the container with given Rabbit template.
   *
   * @param rabbitTemplate the Rabbit template.
   */
  SimpleDlqAmqpContainer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  // --------------------------------------------------------------------------
  // Uncomment to Listener that just logs error message
  // --------------------------------------------------------------------------

  //  /**
  //   * Creates Listener for receiving error messages off the DLQ and just logs it.
  //   *
  //   * @param failedMessage the incoming error message.
  //   */
  //  @RabbitListener(queues = DLQ)
  //  public void processFailedMessages(Message failedMessage) {
  //    LOG.warn("Received failed message: {}", failedMessage);
  //  }

  // --------------------------------------------------------------------------
  // Uncomment to Listener that logs error message and resends it forever!
  // --------------------------------------------------------------------------

  /**
   * Create Listener receiving error messages off the DLQ and requeue the message to the original
   * destination.
   *
   * @param message the failed message.
   */
  @RabbitListener(queues = DLQ)
  void processFailedMessage(Message message) {

    LOG.warn("Received failed message, re-queueing message: {}", message);
    LOG.warn(
        "Received failed message, re-queueing routing key id: {}",
        message.getMessageProperties().getReceivedRoutingKey());

    rabbitTemplate.send(
        MESSAGES_EXCHANGE, message.getMessageProperties().getReceivedRoutingKey(), message);
  }
}
