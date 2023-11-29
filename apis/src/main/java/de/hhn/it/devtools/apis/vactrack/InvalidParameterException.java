package de.hhn.it.devtools.apis.vactrack;

/**
 * Gets thrown when a parameter does not fulfill its specification.
 */
public class InvalidParameterException extends Exception {
  /**
   * Instantiates a new Invalid parameter exception.
   *
   * @param message A message explaining the reason for the exception.
   */
  public InvalidParameterException(String message) {
    super(message);
  }
}