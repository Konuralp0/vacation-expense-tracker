package de.hhn.it.devtools.apis.vactrack;


import java.math.BigDecimal;
import java.util.Random;

/**
 * The type Internal wallet.
 */
public class InternalWallet {
  private String description;
  private String walletId;
  private String currency;
  private WalletBalance walletBalance;
  private ExpenseList expenseList;
  private BigDecimal limit;

  /**
   * Instantiates a new Internal wallet.
   *
   * @param description   the description
   * @param currency      the currency
   * @param walletBalance the wallet balance
   */
  public InternalWallet(String description, String currency, WalletBalance walletBalance) {
    this.description = description;
    this.walletId = generateWalletId();
    this.currency = currency;
    this.walletBalance = walletBalance;
    expenseList = new ExpenseList();
    limit = BigDecimal.ZERO;
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
   * Gets wallet balance.
   *
   * @return the wallet balance
   */
  public WalletBalance getWalletBalance() {
    return walletBalance;
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
   * Gets immutable wallet.
   *
   * @return the immutable wallet
   */
  public Wallet getImmutableWallet() {
    return new Wallet(
            description, walletId, currency, walletBalance, expenseList, limit
    );
  }

  /**
   * Deposit.
   *
   * @param amount the amount
   */
  public void deposit(BigDecimal amount) {
    walletBalance.setBalance(walletBalance.getBalance().add(amount));

  }

  /**
   * Withdraw.
   *
   * @param amount the amount
   */
  public void withdraw(BigDecimal amount) {
    walletBalance.setBalance(walletBalance.getBalance().subtract(amount));
  }

  /**
   * Add expense.
   *
   * @param expense the expense
   */
  public void addExpense(Expense expense) {
    expenseList.addExpense(expense);
    walletBalance.setBalance(walletBalance.getBalance().subtract(expense.getAmount()));
  }

  /**
   * Remove expense.
   *
   * @param expense the expense
   */
  public void removeExpense(Expense expense) {
    expenseList.removeExpense(expense);
  }


  /**
   * Sets currency.
   *
   * @param currency the currency
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  /**
   * Sets daily expense limit.
   *
   * @param limit the limit
   */
  public void setDailyExpenseLimit(BigDecimal limit) {
    this.limit = limit;
  }


  private String generateWalletId() {
    Random rand = new Random();
    int n = rand.nextInt(1000000);
    return String.valueOf(n + 1);
  }
}

