package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The type Wallet.
 */
public class Wallet {
  private final String description;
  private final String walletId;
  private final String currency;
  private final de.hhn.it.devtools.apis.vactrack.WalletBalance walletBalance;
  private final de.hhn.it.devtools.apis.vactrack.ExpenseList expenseList;
  private final BigDecimal limit;

  /**
   * Instantiates a new Wallet.
   *
   * @param description   the description
   * @param walletId      the wallet id
   * @param currency      the currency
   * @param walletBalance the wallet balance
   * @param expenseList   the expense list
   * @param limit         the limit
   */
  public Wallet(
          String description, String walletId, String currency,
          WalletBalance walletBalance, ExpenseList expenseList, BigDecimal limit
  ) {
    this.description = description;
    this.walletId = walletId;
    this.currency = currency;
    this.walletBalance = walletBalance;
    this.expenseList = expenseList;
    this.limit = limit;
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
   * Gets wallet id.
   *
   * @return the wallet id
   */
  public String getWalletId() {
    return walletId;
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
   * Gets wallet balance.
   *
   * @return the wallet balance
   */
  public WalletBalance getWalletBalance() {
    return walletBalance;
  }

  /**
   * Gets expenses within date range.
   *
   * @param startDate the start date
   * @param endDate   the end date
   * @return the expenses within date range
   */
  public List<Expense> getExpensesWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    List<Expense> expensesWithinRange = new ArrayList<>();
    List<Expense> allExpenses = expenseList.getExpenses();

    for (Expense expense : allExpenses) {
      if (expense.getDate().isAfter(startDate) && expense.getDate().isBefore(endDate)) {
        expensesWithinRange.add(expense);
      }
    }

    return expensesWithinRange;
  }

  /**
   * Gets expense list.
   *
   * @return the expense list
   */
  public ExpenseList getExpenseList() {
    return expenseList;
  }

  /**
   * Gets daily expense limit.
   *
   * @return the daily expense limit
   */
  public BigDecimal getDailyExpenseLimit() {
    return limit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Wallet wallet = (Wallet) o;
    return Objects.equals(walletId, wallet.walletId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(walletId);
  }

  @Override
  public String toString() {
    return "Wallet{"
            + "description='" + description + '\''
            + ", walletId='" + walletId + '\''
            + ", currency='" + currency + '\''
            + ", walletBalance=" + walletBalance
            + ", expenseList=" + expenseList
            + ", limit=" + limit
            + '}';
  }
}

