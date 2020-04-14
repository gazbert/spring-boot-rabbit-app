package com.gazbert.rabbitsample.publish.direct;

import static com.gazbert.rabbitsample.publish.direct.DirectExchangeConfiguration.IS_DURABLE;
import static com.gazbert.rabbitsample.publish.direct.DirectExchangeConfiguration.QUEUE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.amqp.core.Queue;

/**
 * Tests the Direct exchange config is created as expected.
 *
 * @author gazbert
 */
public class TestDirectExchangeConfiguration {

  @Test
  public void testDirectQueueCreation() {
    final DirectExchangeConfiguration exchangeConfig = new DirectExchangeConfiguration();
    final Queue queue = exchangeConfig.directQueue();
    assertThat(queue.getName()).isEqualTo(QUEUE_NAME);
    assertThat(queue.isDurable()).isEqualTo(IS_DURABLE);
  }
}
