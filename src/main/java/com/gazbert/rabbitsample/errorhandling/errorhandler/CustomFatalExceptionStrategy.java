package com.gazbert.rabbitsample.errorhandling.errorhandler;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

/**
 * Fatal error handling strategy to tell Rabbit all errors are fatal apart from those that extend
 * our BusinessException.
 *
 * <p>Decides whether to reject a specific message or not. Such messages will be discarded or sent
 * to a Dead Letter Exchange, depending on broker configuration.
 *
 * <p>By default these exceptions are fatal:
 *
 * <p>MessageConversionException, MessageConversionException, MethodArgumentNotValidException,
 * MethodArgumentTypeMismatchException, NoSuchMethodException, ClassCastException.
 *
 * <p>See:
 * https://docs.spring.io/spring-amqp/docs/current/api/org/springframework/amqp/rabbit/listener/ConditionalRejectingErrorHandler.html
 *
 * @author gazbert
 */
public class CustomFatalExceptionStrategy
    extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

  /**
   * Tell Rabbit all errors are fatal apart from those that extend our BusinessException.
   *
   * @param throwable the exception that was thrown.
   * @return true if fatal, false otherwise.
   */
  @Override
  public boolean isFatal(Throwable throwable) {
    return !(throwable.getCause() instanceof BusinessException);
  }
}
