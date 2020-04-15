package com.gazbert.rabbitsample.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Base Rabbit configuration for all demos.
 *
 * @author gazbert
 */
@Configuration
public class BaseRabbitConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(BaseRabbitConfiguration.class);

  @Value("${amqp.connection.hostname}")
  private String hostname;

  @Value("${amqp.connection.port}")
  private int port;

  @Value("${amqp.connection.username}")
  private String username;

  @Value("${amqp.connection.password}")
  private String password;

  /**
   * Creates a Rabbit Connection factory.
   *
   * <p>NOTE: Spring AMQP will use default settings to connect if none are supplied via a Connection
   * Factory bean like this one: $hostname, 5672, guest/guest.
   *
   * @return the factory.
   */
  @Bean
  ConnectionFactory connectionFactory() {

    LOG.info("amqp.connection.hostname: {}", hostname);
    LOG.info("amqp.connection.port: {}", port);
    LOG.info("amqp.connection.username: {}", username);

    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname, port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    return connectionFactory;
  }

  /**
   * JSON converter for marshalling/unmarshalling our sample JSON payload from queues.
   *
   * <p>That's it, simples!
   *
   * @return the message converter.
   */
  @Bean
  MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // --------------------------------------------------------------------------
  // For unit testing
  // --------------------------------------------------------------------------

  void setHostname(String hostname) {
    this.hostname = hostname;
  }

  void setPort(int port) {
    this.port = port;
  }

  void setUsername(String username) {
    this.username = username;
  }

  void setPassword(String password) {
    this.password = password;
  }
}
