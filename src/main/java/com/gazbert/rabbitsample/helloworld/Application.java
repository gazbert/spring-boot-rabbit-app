package com.gazbert.rabbitsample.helloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The most basic app that shows "Hello World!" being sent and received using Spring AMQP.
 *
 * <p>To run with mvn: {@code mvn spring-boot:run}
 *
 * <p>Boot auto-configuration will create ConnectionFactory, RabbitTemplate, and RabbitAdmin beans.
 *
 * @author gazbert
 */
@SpringBootApplication
public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);
  static final boolean IS_DURABLE = false;
  static final String QUEUE_NAME = "com.gazbert.rabbitsample.helloworld.queue";

  /**
   * Main method starts the app.
   *
   * @param args no args required.
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * Run the sample app.
   *
   * @param template Rabbit template for publishing events to Exchange.
   * @return the app runner.
   */
  @Bean
  ApplicationRunner runner(RabbitTemplate template) {
    final String message = "Hello World!";
    LOG.info("Sending message to exchange: " + message);
    return args -> template.convertAndSend(QUEUE_NAME, message);
  }

  /**
   * RabbitAdmin will find this and bind it to the default exchange with a queue name of QUEUE_NAME.
   *
   * @return the created queue.
   */
  @Bean
  Queue createQueue() {
    return new Queue(QUEUE_NAME, IS_DURABLE);
  }

  /**
   * Consumes from the queue.
   *
   * @param message the value on the queue.
   */
  @RabbitListener(queues = QUEUE_NAME)
  void receiveMessage(String message) {
    LOG.info("Message read from " + QUEUE_NAME + " : " + message);
  }
}
