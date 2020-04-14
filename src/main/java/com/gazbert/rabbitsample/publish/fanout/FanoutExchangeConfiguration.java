package com.gazbert.rabbitsample.publish.fanout;

import com.gazbert.rabbitsample.configuration.BaseRabbitConfiguration;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration and beans for creating a Fanout exchange.
 *
 * @author gazbert
 */
@Configuration
class FanoutExchangeConfiguration extends BaseRabbitConfiguration {

  static final boolean IS_DURABLE = false;
  static final boolean AUTO_DELETE = false;

  static final String FANOUT_EXCHANGE_NAME = "com.gazbert.rabbitsample.publish.fanout.exchange";
  static final String FANOUT_QUEUE_1_NAME = "com.gazbert.rabbitsample.publish.fanout.queue1";
  static final String FANOUT_QUEUE_2_NAME = "com.gazbert.rabbitsample.publish.fanout.queue2";

  /**
   * Creates a Fanout exchange with two queues bound to it.
   *
   * <p>When a message is sent to this exchange, both queues will receive it. The Fanout exchange
   * ignores any routing key included in the message.
   *
   * <p>Spring AMQP allows all the declarations of queues, exchanges, and bindings to be placed in a
   * Declarables object.
   *
   * @return declarables containing Fanout queues, Fanout exchange, and Bindings.
   */
  @Bean
  Declarables createFanoutBindings() {

    final Queue fanoutQueue1 = new Queue(FANOUT_QUEUE_1_NAME, IS_DURABLE);
    final Queue fanoutQueue2 = new Queue(FANOUT_QUEUE_2_NAME, IS_DURABLE);

    final FanoutExchange fanoutExchange =
        new FanoutExchange(FANOUT_EXCHANGE_NAME, IS_DURABLE, AUTO_DELETE);

    return new Declarables(
        fanoutQueue1,
        fanoutQueue2,
        fanoutExchange,
        BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
        BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
  }
}
