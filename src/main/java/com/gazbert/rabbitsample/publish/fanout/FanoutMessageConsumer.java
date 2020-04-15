package com.gazbert.rabbitsample.publish.fanout;

import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_QUEUE_1_NAME;
import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_QUEUE_2_NAME;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.publish.util.MessageLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumes Fanout messages.
 *
 * <p>We configure consumers using the @RabbitListener annotation. The only arguments passed here
 * is
 * the queues' name. Consumers (by design) are not aware of exchanges or routing keys.
 *
 * @author gazbert
 */
@Component
class FanoutMessageConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(FanoutMessageConsumer.class);
  private MessageLogger messageLogger;

  @Autowired
  FanoutMessageConsumer(MessageLogger messageLogger) {
    this.messageLogger = messageLogger;
  }

  /**
   * Creates Rabbit listener for Fanout queue 1.
   *
   * @param message the message received.
   */
  @RabbitListener(queues = {FANOUT_QUEUE_1_NAME})
  void receiveMessageFromFanout1(MessagePayload message) {
    LOG.info("Received fanout 1 message: {}", message);
    messageLogger.logMessage(message);
  }

  /**
   * Creates Rabbit listener for Fanout queue 2.
   *
   * @param message the message received.
   */
  @RabbitListener(queues = {FANOUT_QUEUE_2_NAME})
  void receiveMessageFromFanout2(MessagePayload message) {
    LOG.info("Received fanout 2 message: {}", message);
    messageLogger.logMessage(message);
  }
}
