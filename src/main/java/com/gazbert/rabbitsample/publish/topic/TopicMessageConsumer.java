package com.gazbert.rabbitsample.publish.topic;

import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.BINDING_PATTERN_ERROR;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.BINDING_PATTERN_HIGH_PRIORITY;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_QUEUE_1_NAME;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_QUEUE_2_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazbert.rabbitsample.domain.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes Topic messages.
 *
 * <p>We configure consumers using the @RabbitListener annotation. The only arguments passed here is
 * the queues' name. Consumers (by design) are not aware of exchanges or routing keys.
 */
@Component
class TopicMessageConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(TopicMessageConsumer.class);

  /**
   * Creates Rabbit listener for Topic queue 1.
   *
   * @param message the message received.
   */
  @RabbitListener(queues = {TOPIC_QUEUE_1_NAME})
  void receiveMessageFromTopicQueue1(MessagePayload message) throws Exception {
    LOG.info("Received topic 1 (" + BINDING_PATTERN_HIGH_PRIORITY + ") message: " + message);

    // Hack for integration testing
    if (LOG.isDebugEnabled()) {
      final ObjectMapper objectMapper = new ObjectMapper();
      System.out.println(objectMapper.writeValueAsString(message));
    }
  }

  /**
   * Creates Rabbit listener for Topic queue 2.
   *
   * @param message the message received.
   */
  @RabbitListener(queues = {TOPIC_QUEUE_2_NAME})
  void receiveMessageFromTopicQueue2(MessagePayload message) throws  Exception {
    LOG.info("Received topic 2 (" + BINDING_PATTERN_ERROR + ") message: " + message);

    // Hack for integration testing
    if (LOG.isDebugEnabled()) {
      final ObjectMapper objectMapper = new ObjectMapper();
      System.out.println(objectMapper.writeValueAsString(message));
    }
  }
}
