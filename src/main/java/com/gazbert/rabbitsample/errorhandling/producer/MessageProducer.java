package com.gazbert.rabbitsample.errorhandling.producer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * The Message Producer.
 *
 * @author gazbert
 */
@Service
public class MessageProducer {

  private static final Logger LOG = LoggerFactory.getLogger(MessageProducer.class);
  private final RabbitTemplate rabbitTemplate;
  private MessagePostProcessor messagePostProcessor;

  /**
   * Initialise with Rabbit Template.
   *
   * @param rabbitTemplate template for publishing messages.
   */
  MessageProducer(final RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.messagePostProcessor = new TtlMessagePostProcessor(60 * 5);
  }

  /**
   * Sends a message to a named Rabbit Exchange and Routing Key.
   *
   * <p>The queue name is the Routing Key.
   */
  public void sendMessage() {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription("They mostly come at night... mostly.");

    LOG.info("Sending message: " + payload);

    rabbitTemplate.convertAndSend(MESSAGES_EXCHANGE, MESSAGES_QUEUE, payload, messagePostProcessor);
  }

  /**
   * Sets the Rabbit x-message-ttl header for all sent messages.
   *
   * @author gazbert
   */
  static class TtlMessagePostProcessor implements MessagePostProcessor {

    private final Integer ttl;

    /**
     * Creates the message post processor.
     *
     * @param ttl time to live in seconds.
     */
    TtlMessagePostProcessor(final Integer ttl) {
      this.ttl = ttl;
    }

    /**
     * For each message, set the expiration time.
     *
     * @param message the message.
     * @return the message with the expiration time set.
     * @throws AmqpException if anything unexpected happens.
     */
    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {
      message.getMessageProperties().getHeaders().put("expiration", ttl.toString());
      return message;
    }
  }
}
