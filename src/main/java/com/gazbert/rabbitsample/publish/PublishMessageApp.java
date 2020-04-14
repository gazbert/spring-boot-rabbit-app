package com.gazbert.rabbitsample.publish;

import com.gazbert.rabbitsample.publish.direct.DirectMessageProducer;
import com.gazbert.rabbitsample.publish.fanout.FanoutMessageProducer;
import com.gazbert.rabbitsample.publish.topic.TopicMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Produces a message and sends it to a Direct, Fanout, and Topic exchange.
 *
 * <p>The Direct exchange will send the message to the specified queue name only. The Fanout
 * exchange will publish the same message to all bound queues, while Topic exchange use a routing
 * key for passing messages to a particular queue(s).
 *
 * <p>To run with mvn: {@code mvn spring-boot:run
 * -Dstart-class=com.gazbert.rabbitsample.publish.PublishMessageApp}
 *
 * @author gazbert
 */
@SpringBootApplication
public class PublishMessageApp {

  private static final String DIRECT_MESSAGE = "Direct message sent!";
  private static final String FANOUT_MESSAGE = "Fanout message being sent!";
  private static final String WARNING_MESSAGE = " a warning message";
  private static final String ERROR_MESSAGE = " an error message";

  private DirectMessageProducer directMessageProducer;
  private FanoutMessageProducer fanoutMessageProducer;
  private TopicMessageProducer topicMessageProducer;

  /**
   * Creates the app and injects the Producers.
   *
   * @param directMessageProducer the Direct message producer.
   * @param fanoutMessageProducer the Fanout message producer.
   * @param topicMessageProducer the Topic message producer.
   */
  @Autowired
  public PublishMessageApp(
      DirectMessageProducer directMessageProducer,
      FanoutMessageProducer fanoutMessageProducer,
      TopicMessageProducer topicMessageProducer) {
    this.directMessageProducer = directMessageProducer;
    this.fanoutMessageProducer = fanoutMessageProducer;
    this.topicMessageProducer = topicMessageProducer;
  }

  /**
   * Main method starts the app.
   *
   * @param args no args required.
   */
  public static void main(String[] args) {
    SpringApplication.run(PublishMessageApp.class, args);
  }

  /**
   * Creates the Boot ApplicationRunner.
   *
   * @return the Application runner that sends the messages.
   */
  @Bean
  ApplicationRunner runner() {
    return args -> {
      directMessageProducer.sendMessage(DIRECT_MESSAGE);
      fanoutMessageProducer.broadcastMessage(FANOUT_MESSAGE);
      topicMessageProducer.sendWarningMessage(WARNING_MESSAGE);
      topicMessageProducer.sendErrorMessage(ERROR_MESSAGE);
    };
  }
}
