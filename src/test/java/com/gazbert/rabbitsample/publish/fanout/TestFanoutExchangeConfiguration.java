package com.gazbert.rabbitsample.publish.fanout;

import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.AUTO_DELETE;
import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_EXCHANGE_NAME;
import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_QUEUE_1_NAME;
import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.FANOUT_QUEUE_2_NAME;
import static com.gazbert.rabbitsample.publish.fanout.FanoutExchangeConfiguration.IS_DURABLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

/**
 * Tests the Fanout exchange config is created as expected.
 *
 * @author gazbert
 */
public class TestFanoutExchangeConfiguration {

  @Test
  public void testFanoutBindingCreation() {

    final Queue fanoutQueue1 = new Queue(FANOUT_QUEUE_1_NAME, IS_DURABLE);
    final Queue fanoutQueue2 = new Queue(FANOUT_QUEUE_2_NAME, IS_DURABLE);

    final FanoutExchange fanoutExchange =
        new FanoutExchange(FANOUT_EXCHANGE_NAME, IS_DURABLE, AUTO_DELETE);

    final FanoutExchangeConfiguration fanoutConfig = new FanoutExchangeConfiguration();
    final Declarables declarables = fanoutConfig.createFanoutBindings();

    final List<Queue> queues = declarables.getDeclarablesByType(Queue.class);
    assertThat(queues.contains(fanoutQueue1)).isTrue();
    assertThat(queues.contains(fanoutQueue2)).isTrue();

    final List<FanoutExchange> exchanges = declarables.getDeclarablesByType(FanoutExchange.class);
    assertThat(exchanges.contains(fanoutExchange)).isTrue();

    final List<Binding> bindings = declarables.getDeclarablesByType(Binding.class);
    assertThat(bindings.size()).isEqualTo(2);
    assertThat(bindings.get(0).getExchange()).isEqualTo(FANOUT_EXCHANGE_NAME);
    assertThat(bindings.get(0).getDestination()).isEqualTo(FANOUT_QUEUE_1_NAME);
    assertThat(bindings.get(0).getRoutingKey()).isEqualTo("");
    assertThat(bindings.get(1).getExchange()).isEqualTo(FANOUT_EXCHANGE_NAME);
    assertThat(bindings.get(1).getDestination()).isEqualTo(FANOUT_QUEUE_2_NAME);
    assertThat(bindings.get(1).getRoutingKey()).isEqualTo("");
  }
}
