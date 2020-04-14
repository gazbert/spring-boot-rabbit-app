package com.gazbert.rabbitsample.errorhandling.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.gazbert.rabbitsample.domain.MessagePayload;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Tests MessageConsumer receives messages as expected.
 *
 * @author gazbert
 */
public class TestMessageConsumer {

  private static final String MESSAGE = "Business message being sent!";
  private RabbitTemplate rabbitTemplateMock;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
  }

  @Test
  public void testReceiveMessageThrowsBusinessException() {
    final MessagePayload payload = new MessagePayload();
    payload.setId(UUID.randomUUID().toString());
    payload.setPriority("HIGH");
    payload.setType("ALERT");
    payload.setDescription(MESSAGE);

    final MessageConsumer consumer = new MessageConsumer(rabbitTemplateMock);
    assertThatCode(() -> consumer.receiveMessage(payload))
        .hasMessage("Ripley, you've blown the transaxle!");
  }

  @Test
  public void testCreatingSimpleDlqContainer() {
    final MessageConsumer consumer = new MessageConsumer(rabbitTemplateMock);
    final SimpleDlqAmqpContainer container = consumer.createSimpleDlqContainer();
    assertThat(container).isNotNull();
  }

  @Test
  public void testCreatingCustomDlqContainer() {
    final MessageConsumer consumer = new MessageConsumer(rabbitTemplateMock);
    final CustomDlqAmqpContainer container = consumer.createCustomDlqAmqpContainer();
    assertThat(container).isNotNull();
  }

  @Test
  public void testCreatingParkingLotDlqAmqpContainer() {
    final MessageConsumer consumer = new MessageConsumer(rabbitTemplateMock);
    final ParkingLotDlqAmqpContainer container = consumer.createParkingLotDlqAmqpContainer();
    assertThat(container).isNotNull();
  }
}
