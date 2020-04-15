package com.gazbert.rabbitsample.publish.direct;

import static com.gazbert.rabbitsample.publish.direct.DirectExchangeConfiguration.QUEUE_NAME;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.publish.util.MessageLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumes Direct messages.
 *
 * <p>We configure consumers using the @RabbitListener annotation. The only arguments passed here
 * is
 * the queues' name. Consumers (by design) are not aware of exchanges or routing keys.
 *
 * @author gazbert
 */
@Component
class DirectMessageConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(DirectMessageConsumer.class);
  private MessageLogger messageLogger;

  @Autowired
  DirectMessageConsumer(MessageLogger messageLogger) {
    this.messageLogger = messageLogger;
  }

  /**
   * Creates Rabbit listener for a Direct queue.
   *
   * @param message the message received.
   */
  @RabbitListener(queues = {QUEUE_NAME})
  void receiveDirectMessage(MessagePayload message) {
    LOG.info("Received direct message: {}", message);
    messageLogger.logMessage(message);
  }
}
