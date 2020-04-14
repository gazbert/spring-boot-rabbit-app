package com.gazbert.rabbitsample.errorhandling.configuration;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLX;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

/**
 * Tests the CustomDlqAmqpConfiguration exchange config is created as expected.
 *
 * @author gazbert
 */
public class TestCustomDlqAmqpConfiguration {

  @Test
  public void testDlxCreation() {
    final CustomDlxAmqpConfiguration configuration = new CustomDlxAmqpConfiguration();
    final FanoutExchange exchange = configuration.createDlx();
    assertThat(exchange.getType()).isEqualTo("fanout");
    assertThat(exchange.getName()).isEqualTo(DLX);
    assertThat(exchange.getArguments().isEmpty()).isTrue();
  }

  @Test
  public void testDlqCreation() {
    final CustomDlxAmqpConfiguration configuration = new CustomDlxAmqpConfiguration();
    final Queue queue = configuration.createDlq();
    assertThat(queue.getName()).isEqualTo(DLQ);
    assertThat(queue.isDurable()).isFalse();
  }

  @Test
  public void testDeadLetterBindingCreation() {
    final CustomDlxAmqpConfiguration configuration = new CustomDlxAmqpConfiguration();
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
    final CustomDlxAmqpConfiguration configuration = new CustomDlxAmqpConfiguration();
    final DirectExchange exchange = configuration.createMessagesExchange();
    assertThat(exchange.getType()).isEqualTo("direct");
    assertThat(exchange.getName()).isEqualTo(MESSAGES_EXCHANGE);
    assertThat(exchange.getArguments().isEmpty()).isTrue();
  }

  @Test
  public void testMessagesQueueCreation() {
    final CustomDlxAmqpConfiguration configuration = new CustomDlxAmqpConfiguration();
    final Queue queue = configuration.createMessagesQueue();
    assertThat(queue.getName()).isEqualTo(MESSAGES_QUEUE);
    assertThat(queue.isDurable()).isFalse();
    assertThat(queue.getArguments().get("x-dead-letter-exchange")).isEqualTo(DLX);
  }

  @Test
  public void testMessagesBindingCreation() {
    final CustomDlxAmqpConfiguration configuration = new CustomDlxAmqpConfiguration();
    final Binding binding = configuration.createMessagesBinding();
    assertThat(binding.getExchange()).isEqualTo(MESSAGES_EXCHANGE);
    assertThat(binding.getDestination()).isEqualTo(MESSAGES_QUEUE);
    assertThat(binding.getRoutingKey()).isEqualTo(MESSAGES_QUEUE);
    assertThat(binding.isDestinationQueue()).isTrue();
    assertThat(binding.getDestinationType().name()).isEqualTo("QUEUE");
  }
}
