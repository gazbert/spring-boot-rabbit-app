package com.gazbert.rabbitsample.errorhandling.errorhandler;

/**
 * Business exception used for demoing Rabbit error handling.
 *
 * @author gazbert
 */
public class BusinessException extends Exception {

  /**
   * Builds the exception with a given error message.
   *
   * @param error the error message.
   */
  public BusinessException(String error) {
    super(error);
  }
}
