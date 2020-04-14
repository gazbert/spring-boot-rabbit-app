package com.gazbert.rabbitsample.errorhandling.configuration;

/**
 * Declares the Queue and Exchange names used in the demos.
 *
 * @author gazbert
 */
public final class QueueAndExchangeNames {

  public static final String MESSAGES_EXCHANGE = "com.gazbert.rabbitsample.errorhandling.exchange";
  public static final String MESSAGES_QUEUE = "com.gazbert.rabbitsample.errorhandling.queue";

  static final String DLX = "com.gazbert.rabbitsample.errorhandling.dlx";
  public static final String DLQ = "com.gazbert.rabbitsample.errorhandling.dlq";

  public static final String PARKING_LOT_EXCHANGE =
      "com.gazbert.rabbitsample.errorhandling.parking-lot.exchange";
  public static final String PARKING_LOT_QUEUE =
      "com.gazbert.rabbitsample.errorhandling.parking-lot.queue";

  private QueueAndExchangeNames() {
  }
}
