package com.gazbert.rabbitsample.helloworld;

import static com.gazbert.rabbitsample.helloworld.Application.IS_DURABLE;
import static com.gazbert.rabbitsample.helloworld.Application.QUEUE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;

/**
 * Tests the HelloWorld! Boot app is created and initialised as expected.
 *
 * @author gazbert
 */
public class TestApplication {

  private RabbitTemplate rabbitTemplate;

  @Before
  public void setupMocks() {
    rabbitTemplate = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testCreatingAppSuccessfully() {
    final Application application = new Application();
    assertThat(application).isNotNull();
  }

  @Test
  public void testGettingApplicationRunner() {
    final Application application = new Application();
    final ApplicationRunner runner = application.runner(rabbitTemplate);
    assertThat(runner).isNotNull();
  }

  @Test
  public void testCreatingQueueSuccessfully() {
    final Application application = new Application();
    final Queue queue = application.createQueue();
    assertThat(queue.getName()).isEqualTo(QUEUE_NAME);
    assertThat(queue.isDurable()).isEqualTo(IS_DURABLE);
  }

  @Test
  public void testReceivingMessageSuccessfully() {
    final Application application = new Application();
    assertThatCode(() -> application.receiveMessage("Hello World!")).doesNotThrowAnyException();
  }
}
