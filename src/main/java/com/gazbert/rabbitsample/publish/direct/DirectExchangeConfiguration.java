package com.gazbert.rabbitsample.publish.direct;

import com.gazbert.rabbitsample.configuration.BaseRabbitConfiguration;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration and beans for creating a Direct exchange.
 *
 * @author gazbert
 */
@Configuration
class DirectExchangeConfiguration extends BaseRabbitConfiguration {

  static final String QUEUE_NAME = "com.gazbert.rabbitsample.publish.direct.queue";
  static final boolean IS_DURABLE = false;

  /**
   * RabbitAdmin will find this and bind it to the default exchange with a queue name of QUEUE_NAME.
   *
   * @return the created queue.
   */
  @Bean
  Queue directQueue() {
    return new Queue(QUEUE_NAME, IS_DURABLE);
  }
}
