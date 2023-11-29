package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The interface VacTrackService.
 */
public interface VacTrackService {

  /**
   * Creates wallet.
   *
   * @param currency      the currency
   * @param initialBudget the initial budget
   * @return the wallet
   * @throws InvalidParameterException the invalid parameter exception
   */
  String createWallet(String description, String currency, BigDecimal initialBudget)
          throws InvalidParameterException;

  /**
   * Gets wallet.
   *
   * @param walletId the wallet id
   * @return the wallet
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  Wallet getWallet(String walletId)
      throws WalletNotFoundException, InvalidParameterException;

  /**
   * Enters exchange rate.
   *
   * @param currencyFrom the currency from
   * @param currencyTo   the currency to
   * @param exchangeRate the exchange rate
   * @throws InvalidParameterException    the invalid parameter exception
   * @throws InvalidExchangeRateException the invalid exchange rate exception
   */

  void enterExchangeRate(String currencyFrom, String currencyTo, double exchangeRate)
          throws InvalidParameterException, InvalidExchangeRateException;


  /**
   * Converts wallet currency.
   *
   * @param walletId  the wallet id
   * @param currencyTo the currency to
   * @throws InvalidParameterException    the invalid parameter exception
   * @throws InvalidExchangeRateException the invalid exchange rate exception
   * @throws WalletNotFoundException      the wallet not found exception
   */
  void convertWalletCurrency(String walletId, String currencyTo)
        throws InvalidParameterException, InvalidExchangeRateException, WalletNotFoundException;


  /**
   * Transfers funds.
   *
   * @param sourceWalletId      the source wallet id
   * @param destinationWalletId the destination wallet id
   * @param amount              the amount
   * @param currency            the exchange currency
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  void transferFunds(String sourceWalletId, String destinationWalletId,
                     BigDecimal amount, double currency)
          throws InvalidParameterException, WalletNotFoundException;


  /**
   * Gets wallet balance.
   *
   * @param walletId the wallet id
   * @return the wallet balance
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  BigDecimal getWalletBalance(String walletId)
          throws WalletNotFoundException, InvalidParameterException;


  /**
   * Creates wallet group.
   *
   * @param groupName the group name
   * @return the group wallet
   * @throws InvalidParameterException the invalid parameter exception
   */
  GroupWallet createWalletGroup(String groupName, BigDecimal initialBudget)
          throws InvalidParameterException;


  /**
   * Adds wallet to group.
   *
   * @param walletId the wallet id
   * @param groupId  the group id
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  void addWalletToGroup(String walletId, String groupId)
          throws InvalidParameterException, WalletNotFoundException;


  /**
   * Removes wallet from group.
   *
   * @param walletId the wallet id
   * @param groupId  the group id
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  void removeWalletFromGroup(String walletId, String groupId)
          throws InvalidParameterException, WalletNotFoundException;

  /**
   * Gets wallet groups.
   *
   * @return the wallet groups
   */
  List<String> getWalletGroups();

  /**
   * Gets wallet descriptions.
   *
   * @return the wallet descriptions
   */
  List<String> getWalletDescriptions();

  /**
   * Gets id by description.
   *
   * @return the id by description
   */
  String getIdByDescription(String description) throws InvalidParameterException;


  /**
   * Creates expense.
   *
   * @param description the description
   * @param amount      the amount
   * @return the expense
   * @throws InvalidParameterException the invalid parameter exception
   */
  Expense createExpense(String walletId, String description,
                        BigDecimal amount, LocalDateTime localDateTime)
          throws InvalidParameterException, WalletNotFoundException;


  /**
   * Gets expense report.
   *
   * @param walletId  the wallet id
   * @param startDate the start date
   * @param endDate   the end date
   * @return the expense report
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  ExpenseReport getExpenseReport(String walletId, LocalDateTime startDate, LocalDateTime endDate)
          throws WalletNotFoundException, InvalidParameterException;


  /**
   * Gets expense report for group wallet.
   *
   * @param groupId   the group id
   * @param startDate the start date
   * @param endDate   the end date
   * @return the expense report for group wallet
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  ExpenseReport getExpenseReportForGroupWallet(String groupId,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate)
      throws WalletNotFoundException, InvalidParameterException;


  /**
   * Sets daily expense limit.
   *
   * @param walletId the wallet id
   * @param limit    the limit
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  void setDailyExpenseLimit(String walletId, BigDecimal limit)
          throws WalletNotFoundException, InvalidParameterException;

  /**
   * Gets group wallet.
   *
   * @param walletId the wallet id
   * @return the group wallet
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  GroupWallet getGroupWallet(String walletId)
          throws WalletNotFoundException, InvalidParameterException;

  /**
   * Deletes wallet.
   *
   * @param walletId the wallet id
   * @return the boolean
   * @throws WalletNotFoundException the wallet not found exception
   */
  boolean deleteWallet(String walletId)
          throws WalletNotFoundException, InvalidParameterException;

  /**
   * Deletes group wallet.
   *
   * @param groupId the group id
   * @return the boolean
   * @throws InvalidParameterException the invalid parameter exception
   */
  boolean deleteGroupWallet(String groupId)
      throws InvalidParameterException;


  /**
   * Delete expense boolean.
   *
   * @param walletId the wallet id
   * @param expense  the expense
   * @return the boolean
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  boolean deleteExpense(String walletId, Expense expense)
      throws WalletNotFoundException, InvalidParameterException;

  /**
   * Generate printable report string.
   *
   * @param report the report
   * @return the string
   * @throws InvalidParameterException the invalid parameter exception
   */
  String generatePrintableReport(ExpenseReport report)
      throws InvalidParameterException;


}