package com.gazbert.rabbitsample.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Tests the Base Rabbit config is created as expected.
 *
 * @author gazbert
 */
public class TestBaseRabbitConfiguration {

  private static final String HOSTNAME = "nostromo.space";
  private static final int PORT = 5672;
  private static final String USERNAME = "ripley";

  @Test
  public void testConnectionFactoryCreation() {
    final BaseRabbitConfiguration configuration = new BaseRabbitConfiguration();
    configuration.setHostname(HOSTNAME);
    configuration.setPort(PORT);
    configuration.setUsername(USERNAME);

    final ConnectionFactory connectionFactory = configuration.connectionFactory();
    assertThat(connectionFactory.getHost()).isEqualTo(HOSTNAME);
    assertThat(connectionFactory.getPort()).isEqualTo(PORT);
    assertThat(connectionFactory.getUsername()).isEqualTo(USERNAME);
  }

  @Test
  public void testJsonMessageConverterCreation() {
    final BaseRabbitConfiguration configuration = new BaseRabbitConfiguration();
    final MessageConverter messageConverter = configuration.jsonMessageConverter();
    assertThat(messageConverter).isNotNull();
  }
}
