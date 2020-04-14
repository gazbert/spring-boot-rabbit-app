package com.gazbert.rabbitsample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests the MessagePayload behaves as expected.
 *
 * @author gazbert
 */
public class TestMessagePayload {

  private static final String ID = "some-id-123";
  private static final String PRIORITY = "HIGH";
  private static final String TYPE = "WARN";
  private static final String MESSAGE = "They mostly come at night... mostly.";

  @Test
  public void testSettersAndGetters() {

    final MessagePayload messagePayload = new MessagePayload();
    assertThat(messagePayload.getId()).isNull();
    assertThat(messagePayload.getPriority()).isNull();
    assertThat(messagePayload.getType()).isNull();
    assertThat(messagePayload.getDescription()).isNull();

    messagePayload.setId(ID);
    messagePayload.setPriority(PRIORITY);
    messagePayload.setType(TYPE);
    messagePayload.setDescription(MESSAGE);

    assertThat(messagePayload.getId()).isEqualTo(ID);
    assertThat(messagePayload.getPriority()).isEqualTo(PRIORITY);
    assertThat(messagePayload.getType()).isEqualTo(TYPE);
    assertThat(messagePayload.getDescription()).isEqualTo(MESSAGE);
  }
}
