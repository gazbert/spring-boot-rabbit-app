package com.gazbert.rabbitsample.errorhandling;

import com.gazbert.rabbitsample.errorhandling.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot app to demo Rabbit error handling features.
 *
 * <p>To run with mvn: {@code mvn spring-boot:run
 * -Dstart-class=com.gazbert.rabbitsample.errorhandling.ErrorHandlingApp }
 *
 * @author gazbert
 */
@SpringBootApplication
@EnableScheduling
public class ErrorHandlingApp {

  private MessageProducer messageProducer;

  @Autowired
  public ErrorHandlingApp(MessageProducer messageProducer) {
    this.messageProducer = messageProducer;
  }

  /**
   * Main method starts the app.
   *
   * @param args no args required.
   */
  public static void main(String[] args) {
    SpringApplication.run(ErrorHandlingApp.class, args);
  }

  /** When Boot app finishes starting, send a message to Rabbit. */
  @EventListener(ApplicationReadyEvent.class)
  void whenApplicationReady() {
    messageProducer.sendMessage();
  }
}
