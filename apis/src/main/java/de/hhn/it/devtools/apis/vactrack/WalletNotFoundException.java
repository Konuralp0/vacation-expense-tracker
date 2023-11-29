package de.hhn.it.devtools.apis.vactrack;

/**
 * Gets thrown when an operation is attempted on a wallet which does not exist.
 */
public class WalletNotFoundException extends Exception {

  /**
   * Instantiates a new Wallet not found exception.
   *
   * @param message A message explaining the reason for the exception
   */
  public WalletNotFoundException(String message) {
    super(message);
  }
}