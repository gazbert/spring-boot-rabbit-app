package com.gazbert.rabbitsample.it;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

import com.gazbert.rabbitsample.publish.PublishMessageApp;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

/**
 * Integration tests for the Publish Message app.
 *
 * @author gazbert
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PublishMessageApp.class)
@ContextConfiguration(initializers = TestPublishMessageAppIT.Initializer.class)
public class TestPublishMessageAppIT {

  private static final Logger LOG = LoggerFactory.getLogger(TestPublishMessageAppIT.class);

  @Rule public OutputCaptureRule outputCapture = new OutputCaptureRule();

  @ClassRule
  public static GenericContainer rabbit =
      new GenericContainer("rabbitmq:3-management").withExposedPorts(5672, 15672);

  /** Checks that the messages have been consumed. */
  @Test
  public void testPublishingMessages() {
    await().atMost(10, TimeUnit.SECONDS).until(areMessagesConsumed(), is(true));
  }

  private Callable<Boolean> areMessagesConsumed() {
    return () -> {
      LOG.info("outputCapture: " + outputCapture.toString());
      return outputCapture.getOut().contains("Direct message sent!")
          && outputCapture.getOut().contains("Fanout message being sent!")
          && outputCapture.getOut().contains("a warning message")
          && outputCapture.getOut().contains("an error message");
    };
  }

  /**
   * Initialises the Spring context with the Rabbit Connection property values.
   *
   * @author gazbert
   */
  public static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * Initialises the Spring app test context.
     *
     * @param configurableApplicationContext the config context for running the app.
     */
    public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
      final TestPropertyValues testPropertyValues =
          TestPropertyValues.of(
              "amqp.connection.hostname=" + rabbit.getContainerIpAddress(),
              "amqp.connection.port=" + rabbit.getMappedPort(5672));
      testPropertyValues.applyTo(configurableApplicationContext);
    }
  }
}
