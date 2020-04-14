package com.gazbert.rabbitsample.publish.topic;

import com.gazbert.rabbitsample.configuration.BaseRabbitConfiguration;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration and beans for creating a Topic exchange.
 *
 * @author gazbert
 */
@Configuration
class TopicExchangeConfiguration extends BaseRabbitConfiguration {

  static final boolean IS_DURABLE = false;
  static final boolean AUTO_DELETE = false;

  static final String TOPIC_EXCHANGE_NAME = "com.gazbert.rabbitsample.publish.topic.exchange";
  static final String TOPIC_QUEUE_1_NAME = "com.gazbert.rabbitsample.publish.topic.queue1";
  static final String TOPIC_QUEUE_2_NAME = "com.gazbert.rabbitsample.publish.topic.queue2";

  static final String BINDING_PATTERN_HIGH_PRIORITY = "*.high.*";
  static final String BINDING_PATTERN_ERROR = "#.error";

  /**
   * Topic exchanges use a routing key for passing messages to a particular queue(s).
   *
   * <p>A Topic exchange allows queues to be bound to it with different key patterns. This allows
   * multiple queues to be bound with the same pattern. Or even multiple patterns bound to the same
   * queue.
   *
   * <p>When a message's routing key matches the pattern, it will be placed in the queue. If a queue
   * has multiple bindings which match, only 1 copy of the message is placed on the queue.
   *
   * <p>Binding patterns can use an asterisk "*" to match a word in a specific position or a hash
   * sign "#" to match zero or more words.
   *
   * <p>Below, topicQueue1 will receive messages which have routing keys having a three-word pattern
   * with the middle word being "high" – for example: "alert.high.error" or
   * "alert.high.warn".
   *
   * <p>And topicQueue2 will receive messages which have routing keys ending in the word error;
   * matching examples are "error", "alert.low.error" or "alert.high.error".
   *
   * @return declarables containing Fanout queues, Fanout exchange, and Bindings.
   */
  @Bean
  Declarables createTopicBindings() {

    final Queue topicQueue1 = new Queue(TOPIC_QUEUE_1_NAME, IS_DURABLE);
    final Queue topicQueue2 = new Queue(TOPIC_QUEUE_2_NAME, IS_DURABLE);

    final TopicExchange topicExchange =
        new TopicExchange(TOPIC_EXCHANGE_NAME, IS_DURABLE, AUTO_DELETE);

    return new Declarables(
        topicQueue1,
        topicQueue2,
        topicExchange,
        BindingBuilder.bind(topicQueue1).to(topicExchange).with(BINDING_PATTERN_HIGH_PRIORITY),
        BindingBuilder.bind(topicQueue2).to(topicExchange).with(BINDING_PATTERN_ERROR));
  }
}
