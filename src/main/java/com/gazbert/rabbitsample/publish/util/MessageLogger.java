package com.gazbert.rabbitsample.publish.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazbert.rabbitsample.domain.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class that logs messages to System.out in DEV environment for purposes of integration
 * testing with Testcontainers.
 *
 * <p>The spring.profiles.active property in the application.properties file must be set to
 * 'integration-test' for the Testcontainers IT tests to work.
 *
 * @author gazbert
 */
@Component
public class MessageLogger {

  private static final Logger LOG = LoggerFactory.getLogger(MessageLogger.class);

  @Value("${spring.profiles.active}")
  private String activeProfile;

  /**
   * Logs message to System.out for integration tests using Testcontainers to pick up.
   *
   * @param message the message to log.
   */
  public void logMessage(MessagePayload message) {
    if (activeProfile != null && activeProfile.equals("integration-test")) {
      final ObjectMapper objectMapper = new ObjectMapper();
      try {
        System.out.println(objectMapper.writeValueAsString(message));
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    } else {
      LOG.debug("Spring profile not set to integration-test - not logging message to System.out");
    }
  }

  // --------------------------------------------------------------------------
  // For unit testing
  // --------------------------------------------------------------------------

  public void setActiveProfile(String activeProfile) {
    this.activeProfile = activeProfile;
  }
}