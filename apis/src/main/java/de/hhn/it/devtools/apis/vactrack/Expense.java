package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Expense.
 */
public class Expense {
  private final String description;
  private final BigDecimal amount;
  private final LocalDateTime date;

  /**
   * Instantiates a new Expense.
   *
   * @param description the description
   * @param amount      the amount
   * @param date        the date
   */
  public Expense(String description, BigDecimal amount, LocalDateTime date) {
    this.description = description;
    this.amount = amount;
    this.date = date;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets amount.
   *
   * @return the amount
   */
  public BigDecimal getAmount() {
    return amount.setScale(2, 5);
  }

  /**
   * Gets date.
   *
   * @return the date
   */
  public LocalDateTime getDate() {
    return date;
  }

  @Override
  public String toString() {
    return ""
            + "description='" + description + '\''
            + ", amount=" + amount
            + ", date=" + date.getDayOfMonth()+"."+date.getMonth();
  }
}
