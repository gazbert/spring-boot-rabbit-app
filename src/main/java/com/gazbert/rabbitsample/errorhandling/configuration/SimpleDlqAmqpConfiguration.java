package com.gazbert.rabbitsample.errorhandling.configuration;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;

import com.gazbert.rabbitsample.configuration.BaseRabbitConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Simple Dead Letter Queue (DLQ) handling.
 *
 * <p>NOTE: Choosing non-durable queues for demo purposes only. This is so the Rabbit Docker
 * container restart will remove them before running the different demos.
 *
 * @author gazbert
 */
@Configuration
@ConditionalOnProperty(value = "amqp.configuration.current", havingValue = "simple-dlq")
class SimpleDlqAmqpConfiguration extends BaseRabbitConfiguration {

  /**
   * Create a Direct Exchange.
   *
   * @return the Direct Exchange.
   */
  @Bean
  DirectExchange createMessagesExchange() {
    return new DirectExchange(MESSAGES_EXCHANGE);
  }

  /**
   * Bean binds message queue to an exchange with a given routing key.
   *
   * @return the queue Binding.
   */
  @Bean
  Binding createBinding() {
    return BindingBuilder.bind(createMessagesQueue())
        .to(createMessagesExchange())
        .with(MESSAGES_QUEUE);
  }

  // --------------------------------------------------------------------------
  // Uncomment To demo error thrown in queue + infinite automatic retry to
  // head of queue. This shows what the DLQ seeks to fix.
  // --------------------------------------------------------------------------

  //  /**
  //   * Creates a durable message queue.
  //   *
  //   * @return the queue.
  //   */
  //  @Bean
  //  Queue createMessagesQueue() {
  //    return QueueBuilder.nonDurable(MESSAGES_QUEUE).build();
  //  }

  // --------------------------------------------------------------------------
  // Uncomment to demo error thrown + Dead Letter Queue handling of message.
  // --------------------------------------------------------------------------

  /**
   * The empty string value for the x-dead-letter-exchange option tells the broker to use the
   * default exchange.
   *
   * <p>The x-dead-letter... args are reserved Rabbit identifiers...
   *
   * @return the queue configured to use DLQ.
   */
  @Bean
  Queue createMessagesQueue() {
    return QueueBuilder.nonDurable(MESSAGES_QUEUE)
        .withArgument("x-dead-letter-exchange", "") // use default exchange
        .withArgument(
            "x-dead-letter-routing-key",
            DLQ) // changes the routing key of the message to use the DLQ
        .build();
  }

  /**
   * The second argument is as equally important as setting routing keys for helloworld messages.
   * This option changes the initial routing key of the message for further routing by DLX.
   *
   * @return the DLQ.
   */
  @Bean
  Queue createDeadLetterQueue() {
    return QueueBuilder.nonDurable(DLQ).build();
  }
}
