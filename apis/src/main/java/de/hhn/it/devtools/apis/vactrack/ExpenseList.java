package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an expense list.
 */
public class ExpenseList {
  private List<Expense> expenses;

  public ExpenseList() {
    expenses = new ArrayList<>();
  }

  /**
   * Instantiates a new expense list.
   *
   * @param expenses the expenses
   */
  public ExpenseList(List<Expense> expenses) {
    this.expenses = expenses;
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
   * Sets expenses.
   *
   * @param expenses the expenses
   */
  public void setExpenses(List<Expense> expenses) {
    this.expenses = expenses;
  }

  /**
   * Adds an expense.
   *
   * @param expense the expense
   */
  public void addExpense(Expense expense) {
    expenses.add(expense);
  }

  /**
   * Remove expense.
   *
   * @param expense the expense
   */
  public void removeExpense(Expense expense) {
    expenses.remove(expense);
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

  @Override
  public String toString() {
    return "ExpenseList{"
            + "expenses=" + expenses
            + '}';
  }
}
