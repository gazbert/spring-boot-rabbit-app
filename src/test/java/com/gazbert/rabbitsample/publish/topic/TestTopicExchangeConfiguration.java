package com.gazbert.rabbitsample.publish.topic;

import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.BINDING_PATTERN_ERROR;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.BINDING_PATTERN_HIGH_PRIORITY;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_EXCHANGE_NAME;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_QUEUE_1_NAME;
import static com.gazbert.rabbitsample.publish.topic.TopicExchangeConfiguration.TOPIC_QUEUE_2_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

/**
 * Tests the Topic exchange config is created as expected.
 *
 * @author gazbert
 */
public class TestTopicExchangeConfiguration {

  @Test
  public void testTopicBindingCreation() {
    final TopicExchangeConfiguration topicConfig = new TopicExchangeConfiguration();
    final Declarables declarables = topicConfig.createTopicBindings();

    final List<Queue> queues = declarables.getDeclarablesByType(Queue.class);
    assertThat(queues.size()).isEqualTo(2);
    assertThat(queues.get(0).getName()).isEqualTo(TOPIC_QUEUE_1_NAME);
    assertThat(queues.get(0).isDurable()).isFalse();
    assertThat(queues.get(1).getName()).isEqualTo(TOPIC_QUEUE_2_NAME);
    assertThat(queues.get(1).isDurable()).isFalse();

    final List<TopicExchange> exchanges = declarables.getDeclarablesByType(TopicExchange.class);
    assertThat(exchanges.size()).isEqualTo(1);
    assertThat(exchanges.get(0).getType().equals("topic")).isTrue();
    assertThat(exchanges.get(0).getName().equals(TOPIC_EXCHANGE_NAME)).isTrue();

    final List<Binding> bindings = declarables.getDeclarablesByType(Binding.class);
    assertThat(bindings.size()).isEqualTo(2);

    assertThat(bindings.get(0).getExchange()).isEqualTo(TOPIC_EXCHANGE_NAME);
    assertThat(bindings.get(0).getDestination()).isEqualTo(TOPIC_QUEUE_1_NAME);
    assertThat(bindings.get(0).getRoutingKey()).isEqualTo(BINDING_PATTERN_HIGH_PRIORITY);
    assertThat(bindings.get(1).getExchange()).isEqualTo(TOPIC_EXCHANGE_NAME);
    assertThat(bindings.get(1).getDestination()).isEqualTo(TOPIC_QUEUE_2_NAME);
    assertThat(bindings.get(1).getRoutingKey()).isEqualTo(BINDING_PATTERN_ERROR);
  }
}
