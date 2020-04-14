package com.gazbert.rabbitsample.errorhandling.configuration;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLX;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_QUEUE;

import com.gazbert.rabbitsample.configuration.BaseRabbitConfiguration;
import com.gazbert.rabbitsample.errorhandling.errorhandler.CustomFatalExceptionStrategy;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

/**
 * Sometimes a message may require manual processing or we might need to audit messages that failed
 * more than n times.
 *
 * <p>For situations like this, there is a concept of a Parking Lot Queue. We can forward all
 * messages from the DLQ that failed more than the configurable number of times, to the Parking Lot
 * Queue for further processing.
 *
 * <p>See:
 * https://docs.spring.io/autorepo/docs/spring-cloud-stream-binder-rabbit-docs/1.1.1.RELEASE/reference/html/rabbit-dlq-processing.html
 *
 * <p>A global error handling strategy is also configured at the bottom of the file.
 *
 * <p>NOTE: Choosing non-durable queues for demo purposes only. This is so the Rabbit Docker *
 * container restart will remove them before running the different demos.
 *
 * @author gazbert
 */
@Configuration
@ConditionalOnProperty(value = "amqp.configuration.current", havingValue = "parking-lot-dlx")
public class ParkingLotDlxAmqpConfiguration extends BaseRabbitConfiguration {

  /**
   * Creates a Fanout Exchange for the Parking Lot.
   *
   * <p>Messages are publish to all queues bound to the Fanout exchange.
   *
   * @return the exchange.
   */
  @Bean
  FanoutExchange createParkingLotExchange() {
    return new FanoutExchange(PARKING_LOT_EXCHANGE);
  }

  /**
   * Creates the Parking Lot queue.
   *
   * @return the Parking Lot queue.
   */
  @Bean
  Queue createParkingLotQueue() {
    return QueueBuilder.nonDurable(PARKING_LOT_QUEUE).build();
  }

  /**
   * Binds the Parking Lot queue to the Parking Lot Fanout exchange.
   *
   * @return the Binding.
   */
  @Bean
  Binding createParkingLotBinding() {
    return BindingBuilder.bind(createParkingLotQueue()).to(createParkingLotExchange());
  }

  // --------------------------------------------------------------------------
  // The DLX, DLQ, and Bindings
  // --------------------------------------------------------------------------

  /**
   * Creates the DLX Fanout Exchange.
   *
   * @return the exchange.
   */
  @Bean
  FanoutExchange createDlx() {
    return new FanoutExchange(DLX);
  }

  /**
   * Creates the DLQ.
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
   * Creates the Business message Direct Exchange.
   *
   * @return the exchange.
   */
  @Bean
  DirectExchange createMessagesExchange() {
    return new DirectExchange(MESSAGES_EXCHANGE);
  }

  /**
   * Creates the Business message queue.
   *
   * <p>Tells Rabbit to use DLX for failed messages.
   *
   * @return the queue.
   */
  @Bean
  Queue createMessagesQueue() {
    return QueueBuilder.nonDurable(MESSAGES_QUEUE)
        .withArgument("x-dead-letter-exchange", DLX)
        .build();
  }

  /**
   * Binds the Business message queue to the Business message exchange.
   *
   * @return the Binding.
   */
  @Bean
  Binding createMessagesBinding() {
    return BindingBuilder.bind(createMessagesQueue())
        .to(createMessagesExchange())
        .with(MESSAGES_QUEUE);
  }

  // --------------------------------------------------------------------------
  // Global error handling strategy.
  // --------------------------------------------------------------------------

  /**
   * Creates a SimpleRabbitListenerContainerFactory with a custom FatalExceptionStrategy.
   *
   * @param connectionFactory the Rabbit Connection factory.
   * @param configurer the Rabbit Listener container factory configurer.
   * @return the Rabbit Listener factory.
   */
  @Bean
  SimpleRabbitListenerContainerFactory createSimpleRabbitListenerContainerFactory(
      ConnectionFactory connectionFactory,
      SimpleRabbitListenerContainerFactoryConfigurer configurer) {

    final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    factory.setErrorHandler(createErrorHandler());
    return factory;
  }

  /**
   * Create custom Error Handler using our custom exception strategy.
   *
   * @return the error handler.
   */
  @Bean
  ErrorHandler createErrorHandler() {
    return new ConditionalRejectingErrorHandler(createFatalExceptionStrategy());
  }

  /**
   * Creates our custom Fatal Exception Strategy.
   *
   * @return the fatal exception strategy.
   */
  @Bean
  FatalExceptionStrategy createFatalExceptionStrategy() {
    return new CustomFatalExceptionStrategy();
  }
}
