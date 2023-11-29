package de.hhn.it.devtools.apis.vactrack;

/**
 * Gets thrown when an exchange rate parameter does not fulfill its specification.
 */
public class InvalidExchangeRateException extends Exception {
  /**
   * Instantiates a new InvalidExchangeRate exception.
   *
   * @param message A message explaining the reason for the exception.
   */
  public InvalidExchangeRateException(String message) {
    super(message);
  }
}
