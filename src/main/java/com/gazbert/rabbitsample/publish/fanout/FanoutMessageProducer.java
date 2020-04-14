package com.gazbert.rabbitsample.publish.fanout;

import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_EXCHANGE_NAME;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Produces and sends Fanout messages.
 *
 * <p>When we send a message to a fanout exchange, the routingKey is ignored, and the message is
 * passed to all bound queues. No need to set a routingKey.
 *
 * @author gazbert
 */
@Component
public class FanoutMessageProducer {

  private static final Logger LOG = LoggerFactory.getLogger(FanoutMessageProducer.class);
  private RabbitTemplate rabbitTemplate;

  @Autowired
  public FanoutMessageProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Sends a message to a Fanout exchange.
   *
   * @param message the message.
   */
  public void broadcastMessage(String message) {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription(message);

    LOG.info("Sending message to Fanout exchange...");
    rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", payload);
  }
}
