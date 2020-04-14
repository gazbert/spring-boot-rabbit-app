package com.gazbert.rabbitsample.domain;

import com.google.common.base.MoreObjects;

/**
 * Domain class for demonstrating sending JSON payloads using Spring AMQP.
 *
 * <p>The Jackson2JsonMessageConverter bean is created in BaseRabbitConfiguration.
 *
 * @author gazbert
 */
public class MessagePayload {

  private String id;
  private String priority;
  private String type;
  private String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("priority", priority)
        .add("type", type)
        .add("description", description)
        .toString();
  }
}
