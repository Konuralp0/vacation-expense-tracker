package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;

/**
 * This class represents the Wallet balance.
 */
public class WalletBalance {
  private BigDecimal balance;
  private String currency;

  /**
   * Instantiates a new Wallet balance.
   *
   * @param balance the balance
   */
  public WalletBalance(BigDecimal balance, String currency) {
    this.balance = balance;
    this.currency = currency;
  }

  /**
   * Gets balance.
   *
   * @return the balance
   */
  public BigDecimal getBalance() {
    return balance.setScale(2, 5);
  }

  /**
   * Sets balance.
   *
   * @param balance the balance
   */
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }


  /**
   * Gets currency.
   *
   * @return the currency
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * Sets currency.
   *
   * @param currency the currency
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Override
  public String toString() {
    return "WalletBalance{"
           + "balance=" + balance
           + ", currency='" + currency + '\''
           + '}';
  }
}
