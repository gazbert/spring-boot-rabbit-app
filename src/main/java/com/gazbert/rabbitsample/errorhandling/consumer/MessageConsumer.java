package com.gazbert.rabbitsample.errorhandling.consumer;

import static com.gazbert.rabbitsample.errorhandling.configuration.QueueAndExchangeNames.MESSAGES_QUEUE;

import com.gazbert.rabbitsample.domain.MessagePayload;
import com.gazbert.rabbitsample.errorhandling.errorhandler.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sample Consumer of messages.
 *
 * @author gazbert
 */
@Configuration
public class MessageConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);
  private final RabbitTemplate rabbitTemplate;

  /**
   * Creates Consumer with Rabbit template.
   *
   * @param rabbitTemplate the Rabbit template.
   */
  public MessageConsumer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * Creates a Listener for Business messages for a given Queue.
   *
   * <p>Immediately throws an Exception to demo Rabbit error handling.
   *
   * @param message the incoming message.
   * @throws BusinessException thrown to demo Rabbit error handling.
   */
  @RabbitListener(queues = MESSAGES_QUEUE)
  void receiveMessage(MessagePayload message) throws BusinessException {
    LOG.info("Received message: {}", message);
    throw new BusinessException("Ripley, you've blown the transaxle!");
    // throw new RuntimeException("FATAL ERROR! Rabbit decides using CustomFatalExceptionStrategy");
  }

  // --------------------------------------------------------------------------
  // Containers for demo'ing diff ways of handling error messages.
  // Update the application.properties file amqp.configuration.current property
  // value accordingly.
  // The Rabbit Docker container will need to be restarted after each change
  // to remove the queues.
  // --------------------------------------------------------------------------

  /**
   * Creates convenience container bean that creates the Rabbit Listeners for failure messages.
   *
   * @return the container.
   */
  @Bean
  @ConditionalOnProperty(value = "amqp.configuration.current", havingValue = "simple-dlq")
  SimpleDlqAmqpContainer createSimpleDlqContainer() {
    return new SimpleDlqAmqpContainer(rabbitTemplate);
  }

  /**
   * Creates convenience container bean that creates the Rabbit Listener to listen for error
   * messages and re-tries sending configurable number of times.
   *
   * @return the container.
   */
  @Bean
  @ConditionalOnProperty(value = "amqp.configuration.current", havingValue = "custom-dlx")
  CustomDlqAmqpContainer createCustomDlqAmqpContainer() {
    return new CustomDlqAmqpContainer(rabbitTemplate);
  }

  /**
   * Creates convenience container that moves failed messages off the DLQ and sends them to a
   * Parking Lot queue.
   *
   * @return the container.
   */
  @Bean
  @ConditionalOnProperty(value = "amqp.configuration.current", havingValue = "parking-lot-dlx")
  ParkingLotDlqAmqpContainer createParkingLotDlqAmqpContainer() {
    return new ParkingLotDlqAmqpContainer(rabbitTemplate);
  }
}
