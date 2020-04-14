package com.gazbert.rabbitsample.publish.topic;

import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_EXCHANGE_NAME;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Produces and sends Topic messages.
 *
 * <p>When we send a message to the topic exchange, we need to pass a routing key. Based on this
 * routing key the message will be delivered to specific queues.
 *
 * @author gazbert
 */
@Component
public class TopicMessageProducer {

  private static final Logger LOG = LoggerFactory.getLogger(TopicMessageProducer.class);

  static final String ROUTING_KEY_ALERT_HIGH_WARN = "alert.high.warn";
  static final String ROUTING_KEY_ALERT_HIGH_ERROR = "alert.high.error";

  private RabbitTemplate rabbitTemplate;

  @Autowired
  public TopicMessageProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Sends a warning message to a Topic exchange.
   *
   * @param message the message.
   */
  public void sendWarningMessage(String message) {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("WARN");
    payload.setDescription(message);

    LOG.info("Sending high priority warn message to Topic exchange...");
    rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY_ALERT_HIGH_WARN, payload);
  }

  /**
   * Sends an error message to a Topic exchange.
   *
   * @param message the message.
   */
  public void sendErrorMessage(String message) {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ERROR");
    payload.setDescription(message);

    LOG.info("Sending high priority error message to Topic exchange...");
    rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY_ALERT_HIGH_ERROR, payload);
  }
}
