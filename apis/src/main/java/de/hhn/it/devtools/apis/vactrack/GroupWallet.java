package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a Group wallet.
 */
public class GroupWallet {
  private String groupId;
  private final String groupName;
  private BigDecimal budget;
  private final List<Wallet> memberWallets;

  /**
   * Instantiates a new Group wallet.
   *
   * @param groupName the group name
   * @param budget    the budget
   */
  public GroupWallet(String groupName, BigDecimal budget) {
    this.groupId = generateWalletId();
    this.groupName = groupName;
    this.budget = budget;
    memberWallets = new ArrayList<>();
  }

  /**
   * Gets group id.
   *
   * @return the group id
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * Gets group name.
   *
   * @return the group name
   */
  public String getGroupName() {
    return groupName;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public List<Wallet> getMemberWallets() {
    return memberWallets;
  }

  /**
   * Sets group id.
   *
   * @param groupId the group id
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * Sets budget.
   *
   * @param budget the budget
   */
  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  /**
   * Adds wallet.
   *
   * @param wallet the wallet
   */
  public void addWallet(Wallet wallet) {
    memberWallets.add(wallet);
  }

  /**
   * Removes wallet.
   *
   * @param wallet the wallet
   */
  public void removeWallet(Wallet wallet) {
    memberWallets.remove(wallet);
  }

  /**
   * Get expenses for the group wallet within a specified date range.
   *
   * @param startDate the start date of the date range
   * @param endDate   the end date of the date range
   * @return a list of expenses within the date range
   */
  public List<Expense> getExpensesWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    List<Expense> expensesWithinRange = new ArrayList<>();

    for (Wallet wallet : memberWallets) {
      List<Expense> walletExpenses = wallet.getExpensesWithinDateRange(startDate, endDate);
      expensesWithinRange.addAll(walletExpenses);
    }

    return expensesWithinRange;
  }

  /**
   * Generate wallet id string.
   *
   * @return the string
   */
  public String generateWalletId() {
    // Generate a unique wallet ID
    Random rand = new Random();
    int n = rand.nextInt(1000000);

    return String.valueOf(n + 1);
  }

  @Override
  public String toString() {
    return "GroupWallet{"
            + "groupId='" + groupId + '\''
            + ", groupName='" + groupName + '\''
            + ", budget=" + budget
            + ", memberWallets=" + memberWallets
            + '}';
  }
}
