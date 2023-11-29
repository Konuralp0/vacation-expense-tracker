package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Expense report.
 */
public class ExpenseReport {
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final List<Expense> expenses;

  /**
   * Instantiates a new Expense report.
   *
   * @param startDate the start date
   * @param endDate   the end date
   * @param expenses  the expenses
   */
  public ExpenseReport(LocalDateTime startDate, LocalDateTime endDate, List<Expense> expenses) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.expenses = expenses;
  }

  /**
   * Gets start date.
   *
   * @return the start date
   */
  public LocalDateTime getStartDate() {
    return startDate;
  }

  /**
   * Gets end date.
   *
   * @return the end date
   */
  public LocalDateTime getEndDate() {
    return endDate;
  }

  /**
   * Gets expenses.
   *
   * @return the expenses
   */
  public List<Expense> getExpenses() {
    return expenses;
  }

  /**
   * Gets total expenses.
   *
   * @return the total expenses as BigDecimal
   */
  public BigDecimal getTotalExpenses() {
    BigDecimal total = BigDecimal.ZERO;
    for (Expense expense : expenses) {
      total = total.add(expense.getAmount());
    }
    return total;
  }
}
