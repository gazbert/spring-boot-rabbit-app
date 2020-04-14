package com.gazbert.rabbitsample.errorhandling.configuration;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.DLQ;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_EXCHANGE;
import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

/**
 * Tests the SimpleDlqAmqpConfiguration exchange config is created as expected.
 *
 * @author gazbert
 */
public class TestSimpleDlqAmqpConfiguration {

  @Test
  public void testDirectExchangeCreation() {
    final SimpleDlqAmqpConfiguration configuration = new SimpleDlqAmqpConfiguration();
    final DirectExchange exchange = configuration.createMessagesExchange();
    assertThat(exchange.getType()).isEqualTo("direct");
    assertThat(exchange.getName()).isEqualTo(MESSAGES_EXCHANGE);
  }

  @Test
  public void testMessagesQueueCreation() {
    final SimpleDlqAmqpConfiguration configuration = new SimpleDlqAmqpConfiguration();
    final Queue queue = configuration.createMessagesQueue();
    assertThat(queue.getName()).isEqualTo(MESSAGES_QUEUE);
    assertThat(queue.isDurable()).isFalse();
    assertThat(queue.getArguments().get("x-dead-letter-exchange")).isEqualTo("");
    assertThat(queue.getArguments().get("x-dead-letter-routing-key")).isEqualTo(DLQ);
  }

  @Test
  public void testDlqCreation() {
    final SimpleDlqAmqpConfiguration configuration = new SimpleDlqAmqpConfiguration();
    final Queue queue = configuration.createDeadLetterQueue();
    assertThat(queue.getName()).isEqualTo(DLQ);
    assertThat(queue.isDurable()).isFalse();
    assertThat(queue.getArguments().isEmpty()).isTrue();
  }

  @Test
  public void testBindingCreation() {
    final SimpleDlqAmqpConfiguration configuration = new SimpleDlqAmqpConfiguration();
    final Binding binding = configuration.createBinding();
    assertThat(binding.getExchange()).isEqualTo(MESSAGES_EXCHANGE);
    assertThat(binding.getRoutingKey()).isEqualTo(MESSAGES_QUEUE);
    assertThat(binding.getDestination()).isEqualTo(MESSAGES_QUEUE);
    assertThat(binding.isDestinationQueue()).isTrue();
    assertThat(binding.getDestinationType().name()).isEqualTo("QUEUE");
  }
}
