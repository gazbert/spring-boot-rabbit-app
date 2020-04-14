package com.gazbert.rabbitsample.errorhandling.configuration;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLX;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;

import com.gazbert.rabbitsample.configuration.BaseRabbitConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for creating a custom DLX for routing error messages.
 *
 * <p>More here: https://www.rabbitmq.com/dlx.html
 *
 * <p>NOTE: Choosing non-durable queues for demo purposes only. This is so the Rabbit Docker *
 * container restart will remove them before running the different demos.
 *
 * @author gazbert
 */
@Configuration
@ConditionalOnProperty(value = "amqp.configuration.current", havingValue = "custom-dlx")
public class CustomDlxAmqpConfiguration extends BaseRabbitConfiguration {

  /**
   * Creates a Fanout DLX.
   *
   * <p>Using Fanout type so messages will be sent to all bounded queues (DLQs).
   *
   * @return the DLX.
   */
  @Bean
  FanoutExchange createDlx() {
    return new FanoutExchange(DLX);
  }

  /**
   * Creates a DLQ.
   *
   * @return the DLQ.
   */
  @Bean
  Queue createDlq() {
    return QueueBuilder.nonDurable(DLQ).build();
  }

  /**
   * Binds the DLQ to the DLX.
   *
   * @return the Binding.
   */
  @Bean
  Binding createDeadLetterBinding() {
    return BindingBuilder.bind(createDlq()).to(createDlx());
  }

  // --------------------------------------------------------------------------
  // The Business messaging Queues, Exchange, and Bindings
  // --------------------------------------------------------------------------

  /**
   * Creates the main Exchange.
   *
   * @return a Direct Exchange.
   */
  @Bean
  DirectExchange createMessagesExchange() {
    return new DirectExchange(MESSAGES_EXCHANGE);
  }

  /**
   * Creates the main messaging queue for Business messages.
   *
   * <p>Arg tells Rabbit to send any error messages to the DLX.
   *
   * @return the DLQ.
   */
  @Bean
  Queue createMessagesQueue() {
    return QueueBuilder.nonDurable(MESSAGES_QUEUE)
        .withArgument("x-dead-letter-exchange", DLX)
        .build();
  }

  /**
   * Binds the messages Queue to the Exchange.
   *
   * @return the Binding.
   */
  @Bean
  Binding createMessagesBinding() {
    return BindingBuilder.bind(createMessagesQueue())
        .to(createMessagesExchange())
        .with(MESSAGES_QUEUE);
  }
}
