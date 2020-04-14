package com.gazbert.rabbitsample.publish;

import static org.assertj.core.api.Assertions.assertThat;

import com.gazbert.rabbitsample.publish.direct.DirectMessageProducer;
import com.gazbert.rabbitsample.publish.fanout.FanoutMessageProducer;
import com.gazbert.rabbitsample.publish.topic.TopicMessageProducer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.ApplicationRunner;

/**
 * Tests the Publish Boot app is created and initialised as expected.
 *
 * @author gazbert
 */
public class TestPublishMessageApp {

  private DirectMessageProducer directMessageProducer;
  private FanoutMessageProducer fanoutMessageProducer;
  private TopicMessageProducer topicMessageProducer;

  /** Setup mocks for all tests. */
  @Before
  public void setupMocks() {
    directMessageProducer = Mockito.mock(DirectMessageProducer.class);
    fanoutMessageProducer = Mockito.mock(FanoutMessageProducer.class);
    topicMessageProducer = Mockito.mock(TopicMessageProducer.class);
  }

  @Test
  public void testCreatingAppSuccessfully() {
    final PublishMessageApp app =
        new PublishMessageApp(directMessageProducer, fanoutMessageProducer, topicMessageProducer);
    assertThat(app).isNotNull();
  }

  @Test
  public void testGettingApplicationRunner() {
    final PublishMessageApp app =
        new PublishMessageApp(directMessageProducer, fanoutMessageProducer, topicMessageProducer);
    final ApplicationRunner runner = app.runner();
    assertThat(runner).isNotNull();
  }
}
