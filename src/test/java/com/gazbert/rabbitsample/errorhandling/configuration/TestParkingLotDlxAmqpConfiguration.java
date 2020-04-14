package com.gazbert.rabbitsample.errorhandling.configuration;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLX;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.PARKING_LOT_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.util.ErrorHandler;

/**
 * Tests the ParkingLotDlxAmqpConfiguration exchange config is created as expected.
 *
 * @author gazbert
 */
public class TestParkingLotDlxAmqpConfiguration {

  @Test
  public void testParkingLotExchangeCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final FanoutExchange exchange = configuration.createParkingLotExchange();
    assertThat(exchange.getType()).isEqualTo("fanout");
    assertThat(exchange.getName()).isEqualTo(PARKING_LOT_EXCHANGE);
    assertThat(exchange.getArguments().isEmpty()).isTrue();
  }

  @Test
  public void testParkingLotQueueCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final Queue queue = configuration.createParkingLotQueue();
    assertThat(queue.getName()).isEqualTo(PARKING_LOT_QUEUE);
    assertThat(queue.isDurable()).isFalse();
  }

  @Test
  public void testParkingLotBindingCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final Binding binding = configuration.createParkingLotBinding();
    assertThat(binding.getExchange()).isEqualTo(PARKING_LOT_EXCHANGE);
    assertThat(binding.getDestination()).isEqualTo(PARKING_LOT_QUEUE);
    assertThat(binding.getRoutingKey()).isEqualTo("");
    assertThat(binding.isDestinationQueue()).isTrue();
    assertThat(binding.getDestinationType().name()).isEqualTo("QUEUE");
  }

  // --------------------------------------------------------------------------
  // The DLX, DLQ, and Bindings
  // --------------------------------------------------------------------------

  @Test
  public void testDlxCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final FanoutExchange exchange = configuration.createDlx();
    assertThat(exchange.getType()).isEqualTo("fanout");
    assertThat(exchange.getName()).isEqualTo(DLX);
    assertThat(exchange.getArguments().isEmpty()).isTrue();
  }

  @Test
  public void testDlqCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final Queue queue = configuration.createDlq();
    assertThat(queue.getName()).isEqualTo(DLQ);
    assertThat(queue.isDurable()).isFalse();
  }

  @Test
  public void testDeadLetterBindingCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final Binding binding = configuration.createDeadLetterBinding();
    assertThat(binding.getExchange()).isEqualTo(DLX);
    assertThat(binding.getDestination()).isEqualTo(DLQ);
    assertThat(binding.getRoutingKey()).isEqualTo("");
    assertThat(binding.isDestinationQueue()).isTrue();
    assertThat(binding.getDestinationType().name()).isEqualTo("QUEUE");
  }

  // --------------------------------------------------------------------------
  // The Business messaging Queues, Exchange, and Bindings
  // --------------------------------------------------------------------------

  @Test
  public void testMessagesExchangeCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final DirectExchange exchange = configuration.createMessagesExchange();
    assertThat(exchange.getType()).isEqualTo("direct");
    assertThat(exchange.getName()).isEqualTo(MESSAGES_EXCHANGE);
    assertThat(exchange.getArguments().isEmpty()).isTrue();
  }

  @Test
  public void testMessagesQueueCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final Queue queue = configuration.createMessagesQueue();
    assertThat(queue.getName()).isEqualTo(MESSAGES_QUEUE);
    assertThat(queue.isDurable()).isFalse();
    assertThat(queue.getArguments().get("x-dead-letter-exchange")).isEqualTo(DLX);
  }

  @Test
  public void testMessagesBindingCreation() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final Binding binding = configuration.createMessagesBinding();
    assertThat(binding.getExchange()).isEqualTo(MESSAGES_EXCHANGE);
    assertThat(binding.getDestination()).isEqualTo(MESSAGES_QUEUE);
    assertThat(binding.getRoutingKey()).isEqualTo(MESSAGES_QUEUE);
    assertThat(binding.isDestinationQueue()).isTrue();
    assertThat(binding.getDestinationType().name()).isEqualTo("QUEUE");
  }

  // --------------------------------------------------------------------------
  // Global error handling strategy
  // --------------------------------------------------------------------------

  @Test
  public void testCreatingFatalExceptionStrategy() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final FatalExceptionStrategy strategy = configuration.createFatalExceptionStrategy();
    assertThat(strategy).isNotNull();
  }

  @Test
  public void testCreatingErrorHandler() {
    final ParkingLotDlxAmqpConfiguration configuration = new ParkingLotDlxAmqpConfiguration();
    final ErrorHandler errorHandler = configuration.createErrorHandler();
    assertThat(errorHandler).isNotNull();
  }
}
