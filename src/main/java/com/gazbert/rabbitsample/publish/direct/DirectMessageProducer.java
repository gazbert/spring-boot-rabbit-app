package com.gazbert.rabbitsample.publish.direct;

import static com.gazbert.rabbitsample.publish.direct.DirectExchangeConfiguration.QUEUE_NAME;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Produces and sends a direct message.
 *
 * <p>When we send a message to a direct exchange, we only pass the name of queue to send to.
 *
 * @author gazbert
 */
@Component
public class DirectMessageProducer {

  private static final Logger LOG = LoggerFactory.getLogger(DirectMessageProducer.class);
  private RabbitTemplate rabbitTemplate;

  @Autowired
  DirectMessageProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Sends a message directly to a named queue.
   *
   * @param message the message.
   */
  public void sendMessage(String message) {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription(message);

    LOG.info("Sending direct message to exchange: {}", payload);
    rabbitTemplate.convertAndSend(QUEUE_NAME, payload);
  }
}
