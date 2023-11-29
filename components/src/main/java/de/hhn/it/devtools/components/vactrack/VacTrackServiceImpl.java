package de.hhn.it.devtools.components.vactrack;


import de.hhn.it.devtools.apis.vactrack.Expense;
import de.hhn.it.devtools.apis.vactrack.ExpenseReport;
import de.hhn.it.devtools.apis.vactrack.GroupWallet;
import de.hhn.it.devtools.apis.vactrack.InternalWallet;
import de.hhn.it.devtools.apis.vactrack.InvalidExchangeRateException;
import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.VacTrackService;
import de.hhn.it.devtools.apis.vactrack.Wallet;
import de.hhn.it.devtools.apis.vactrack.WalletBalance;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * The type VacTrackService.
 */
public class VacTrackServiceImpl implements VacTrackService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(VacTrackServiceImpl.class);
  private final Map<String, Double> exchangeRates;
  private final Map<String, BigDecimal> walletBalances;
  private final Map<String, InternalWallet> internalWallets;
  private final Map<String, GroupWallet> groupWallets;
  private final Map<String, String> descriptions;

  /**
   * Instantiates a new VacTrackService.
   */
  public VacTrackServiceImpl() {
    this.exchangeRates = new HashMap<>();
    this.walletBalances = new HashMap<>();
    this.internalWallets = new HashMap<>();
    this.groupWallets = new HashMap<>();
    this.descriptions = new HashMap<>();
  }

  @Override
  public void enterExchangeRate(String currencyFrom, String currencyTo, double exchangeRate)
          throws InvalidExchangeRateException, InvalidParameterException {
    try {
      logger.info("Entering exchange rate: {} to {} - Rate: {}",
              currencyFrom, currencyTo, exchangeRate);

      if (exchangeRate <= 0) {
        logger.error("Invalid exchange rate: {}", exchangeRate);
        throw new InvalidExchangeRateException("Exchange rate must be greater than 0");
      }

      if (currencyFrom.equals(currencyTo)) {
        logger.error("Both currencies are the same: {}", currencyFrom);
        throw new InvalidParameterException("Both currencies cannot be the same");
      } else if (currencyFrom.isEmpty()
              || currencyTo.isEmpty()) {
        logger.error("Currencies cannot be left blank - From: {}, To: {}",
                currencyFrom, currencyTo);
        throw new
                InvalidParameterException("Currencies cannot be left blank");
      } else if (currencyFrom.matches(".*\\d.*") || currencyTo.matches(".*\\d.*")) {
        logger.error("Currencies cannot contain numerical values - From: {}, To: {}",
                currencyFrom, currencyTo);
        throw new InvalidParameterException("Currencies cannot contain numerical values.");
      }

      String key = currencyFrom + currencyTo;
      exchangeRates.put(key, exchangeRate);

      String keyForTheInvers = currencyTo + currencyFrom;
      exchangeRates.put(keyForTheInvers, 1 / exchangeRate);
    } catch (InvalidParameterException | InvalidExchangeRateException e) {
      logger.error("Error entering exchange rate", e);
      throw e;
    }
  }


  @Override
  public Wallet getWallet(String walletId)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Getting wallet with ID: {}", walletId);

      if (walletId == null || walletId.isEmpty()) {
        logger.error("Invalid wallet ID: {}", walletId);
        throw new InvalidParameterException("Invalid wallet ID");
      }

      Wallet wallet = internalWallets.get(walletId).getImmutableWallet();
      if (wallet == null) {
        logger.error("Wallet not found for ID: {}", walletId);
        throw new WalletNotFoundException("One or both wallets not found");
      }

      logger.info("Wallet retrieved successfully: {}", wallet);
      return wallet;
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error getting wallet", e);
      throw e;
    }
  }

  @Override
  public void convertWalletCurrency(String walletId, String currencyTo)
          throws InvalidParameterException, InvalidExchangeRateException,
          WalletNotFoundException {
    try {
      logger.info("Converting currency for wallet ID: {} to currency: {}", walletId, currencyTo);

      if (walletId == null || walletId.isEmpty()) {
        logger.error("Invalid wallet ID: {}", walletId);
        throw new InvalidParameterException("Invalid wallet ID");
      }

      if (currencyTo == null || currencyTo.isEmpty()) {
        logger.error("Invalid target currency: {}", currencyTo);
        throw new InvalidParameterException("Invalid target currency");
      }

      InternalWallet sourceWallet = internalWallets.get(walletId);

      if (sourceWallet == null) {
        logger.error("Wallet not found for ID: {}", walletId);
        throw new WalletNotFoundException("Wallet not found");
      }

      String sourceCurrency = sourceWallet.getCurrency();

      if (sourceCurrency.equals(currencyTo)) {
        logger.error("Cannot convert to the same currency: {}", currencyTo);
        throw new InvalidParameterException("Cannot convert to the same currency");
      }

      String exchangeRateKey = sourceCurrency + currencyTo;

      if (!exchangeRates.containsKey(exchangeRateKey)) {
        logger.error("Exchange rate not found for {} to {}", sourceCurrency, currencyTo);
        throw new InvalidExchangeRateException("Exchange rate not found for "
                + sourceCurrency + " to " + currencyTo);
      }

      double exchangeRate = exchangeRates.get(exchangeRateKey);

      BigDecimal currentBalance = sourceWallet.getWalletBalance().getBalance();
      BigDecimal convertedBalance = currentBalance.multiply(BigDecimal.valueOf(exchangeRate));
      walletBalances.put(walletId, convertedBalance);

      sourceWallet.setCurrency(currencyTo);
      sourceWallet.getWalletBalance().setCurrency(currencyTo);
      sourceWallet.getWalletBalance().setBalance(sourceWallet.getWalletBalance()
              .getBalance().multiply(
                      BigDecimal.valueOf(exchangeRate)));

      logger.info("Currency conversion successful for wallet ID: {}", walletId);
    } catch (InvalidExchangeRateException | WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error converting currency", e);
      throw e;
    }
  }


  @Override
  public String createWallet(String description, String currency, BigDecimal initialBudget)
          throws InvalidParameterException {

    try {
      logger.info("Creating wallet with description: {}, currency: {}, initial budget: {}",
              description, currency, initialBudget);

      if (descriptions.containsKey(description)) {
        logger.error("Description already exists: {}", description);
        throw new InvalidParameterException("Description already exists");
      }

      if (currency == null || currency.isEmpty()) {
        logger.error("Invalid currency: {}", currency);
        throw new InvalidParameterException("Invalid currency");
      }

      if (initialBudget.doubleValue() < 0) {
        logger.error("Invalid budget: {}", initialBudget);
        throw new InvalidParameterException("Invalid budget");
      }

      InternalWallet internalWallet =
              new InternalWallet(description, currency, new WalletBalance(initialBudget, currency));

      final Wallet wallet = internalWallet.getImmutableWallet();

      internalWallets.put(internalWallet.getWalletId(), internalWallet);
      walletBalances.put(internalWallet.getWalletId(), initialBudget);
      descriptions.put(description, internalWallet.getWalletId());

      logger.info("Wallet creation successful. Wallet ID: {}", wallet.getWalletId());
      return internalWallet.getWalletId();
    } catch (InvalidParameterException e) {
      logger.error("Error creating wallet", e);
      throw e;
    }
  }


  @Override
  public void transferFunds(String sourceWalletId, String destinationWalletId,
                            BigDecimal amount, double currency)
          throws InvalidParameterException, WalletNotFoundException {
    try {
      logger.info("Transferring funds from wallet ID: {} to wallet ID: {}, amount: {}, "
                      + "currency: {}",
              sourceWalletId, destinationWalletId, amount, currency);

      if (sourceWalletId == null || sourceWalletId.isEmpty() || destinationWalletId == null
              || destinationWalletId.isEmpty()) {
        logger.error("Invalid wallet IDs: sourceWalletId={}, destinationWalletId={}",
                sourceWalletId, destinationWalletId);
        throw new InvalidParameterException("Invalid wallet IDs");
      }

      InternalWallet sourceWallet = internalWallets.get(sourceWalletId);
      InternalWallet destinationWallet = internalWallets.get(destinationWalletId);

      if (sourceWallet == null || destinationWallet == null) {
        logger.error("One or both wallets not found: sourceWallet={}, destinationWallet={}",
                sourceWallet, destinationWallet);
        throw new WalletNotFoundException("One or both wallets not found");
      }

      if (sourceWallet.getWalletBalance().getBalance().compareTo(amount) < 0) {
        logger.error("Insufficient funds in the source wallet: sourceWallet={}, amount={}",
                sourceWallet, amount);
        throw new InvalidParameterException("Insufficient funds in the source wallet");
      }

      sourceWallet.withdraw(amount);

      if (currency != 1) {
        amount = amount.multiply(BigDecimal.valueOf(currency));
      }

      destinationWallet.deposit(amount);

      walletBalances.put(sourceWalletId, sourceWallet.getWalletBalance().getBalance());
      walletBalances.put(destinationWalletId, destinationWallet.getWalletBalance().getBalance());

      logger.info("Funds transferred successfully");
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error transferring funds", e);
      throw e;
    }
  }


  @Override
  public BigDecimal getWalletBalance(String walletId)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Getting wallet balance for wallet ID: {}", walletId);

      if (walletId == null || walletId.isEmpty()) {
        logger.error("Invalid wallet ID: {}", walletId);
        throw new InvalidParameterException("Invalid wallet ID");
      }

      if (!walletBalances.containsKey(walletId)) {
        logger.error("Wallet not found: walletId={}", walletId);
        throw new WalletNotFoundException("Wallet not found");
      }

      BigDecimal balance = walletBalances.get(walletId);

      logger.info("Wallet balance retrieved successfully: walletId={}, balance={}",
              walletId, balance);

      return balance;
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error getting wallet balance", e);
      throw e;
    }
  }


  @Override
  public ExpenseReport getExpenseReport(String walletId,
                                        LocalDateTime startDate, LocalDateTime endDate)
          throws WalletNotFoundException, InvalidParameterException {

    try {
      logger.info("Generating expense report for wallet ID: {}", walletId);

      if (walletId == null || walletId.isEmpty()) {
        logger.error("Invalid wallet ID: {}", walletId);
        throw new InvalidParameterException("Invalid wallet ID");
      }
      if (!internalWallets.containsKey(walletId)) {
        throw new WalletNotFoundException("Wallet not found");
      }
      Wallet wallet = internalWallets.get(walletId).getImmutableWallet();
      if (wallet == null) {
        logger.error("Wallet not found: walletId={}", walletId);
        throw new WalletNotFoundException("Wallet not found");
      }

      List<Expense> expensesWithinRange = wallet.getExpensesWithinDateRange(startDate, endDate);

      ExpenseReport expenseReport = new ExpenseReport(startDate, endDate, expensesWithinRange);

      logger.info("Expense report generated successfully for wallet ID: {}", walletId);

      return expenseReport;
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error generating expense report", e);
      throw e;
    }


  }

  @Override
  public void setDailyExpenseLimit(String walletId, BigDecimal limit)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Setting daily expense limit for wallet ID: {}", walletId);

      if (walletId == null || walletId.isEmpty()) {
        logger.error("Invalid wallet ID: {}", walletId);
        throw new InvalidParameterException("Invalid wallet ID");
      }

      InternalWallet internalWallet = internalWallets.get(walletId);
      if (internalWallet == null) {
        logger.error("Wallet not found: walletId={}", walletId);
        throw new WalletNotFoundException("Wallet not found");
      }

      internalWallet.setDailyExpenseLimit(limit);

      logger.info("Daily expense limit set successfully for wallet ID: {}", walletId);
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error setting daily expense limit", e);
      throw e;
    }
  }


  @Override
  public GroupWallet createWalletGroup(String groupName, BigDecimal initialBudget)
          throws InvalidParameterException {
    try {
      logger.info("Creating wallet group: groupName={}, initialBudget={}",
              groupName, initialBudget);

      if (groupName == null || groupName.isEmpty()) {
        logger.error("Invalid group name: {}", groupName);
        throw new InvalidParameterException("Invalid group name");
      }

      GroupWallet groupWallet = new GroupWallet(groupName, initialBudget);

      groupWallets.put(groupWallet.getGroupId(), groupWallet);
      descriptions.put(groupName, groupWallet.getGroupId());

      logger.info("Wallet group created successfully: groupId={}", groupWallet.getGroupId());

      return groupWallet;
    } catch (InvalidParameterException e) {
      logger.error("Error creating wallet group", e);
      throw e;
    }
  }

  @Override
  public GroupWallet getGroupWallet(String walletId)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Getting Group Wallet : walletId={},",
              walletId);

      if (walletId == null || walletId.isEmpty()) {
        logger.error("Invalid wallet ID: {}", walletId);
        throw new InvalidParameterException("Invalid wallet ID");
      }

      GroupWallet wallet = groupWallets.get(walletId);
      if (wallet == null) {
        logger.error("Wallet not found for ID: {}", walletId);
        throw new WalletNotFoundException("One or both wallets not found");
      }

      logger.info("Wallet retrieved successfully: {}", wallet);
      return wallet;
    } catch (InvalidParameterException e) {
      logger.error("Error creating wallet group", e);
      throw e;
    }
  }

  @Override
  public void addWalletToGroup(String walletId, String groupId)
          throws InvalidParameterException, WalletNotFoundException {
    try {
      logger.info("Adding wallet to group: walletId={}, groupId={}", walletId, groupId);

      if (walletId == null || walletId.isEmpty() || groupId == null || groupId.isEmpty()) {
        logger.error("Invalid wallet ID or group ID: walletId={}, groupId={}", walletId, groupId);
        throw new InvalidParameterException("Invalid wallet ID or group ID");
      }

      Wallet wallet1 = internalWallets.get(walletId).getImmutableWallet();
      if (wallet1 == null) {
        logger.error("Wallet not found: walletId={}", walletId);
        throw new WalletNotFoundException("Wallet not found");
      }

      GroupWallet groupWallet = groupWallets.get(groupId);
      if (groupWallet == null) {
        logger.error("Group not found: groupId={}", groupId);
        throw new InvalidParameterException("Group not found");
      }

      groupWallet.addWallet(wallet1);

      logger.info("Wallet added to group successfully: walletId={}, groupId={}", walletId, groupId);
    } catch (InvalidParameterException | WalletNotFoundException e) {
      logger.error("Error adding wallet to group", e);
      throw e;
    }
  }

  @Override
  public void removeWalletFromGroup(String walletId, String groupId)
          throws InvalidParameterException, WalletNotFoundException {
    try {
      logger.info("Removing wallet from group: walletId={}, groupId={}", walletId, groupId);

      if (walletId == null || walletId.isEmpty() || groupId == null || groupId.isEmpty()) {
        logger.error("Invalid wallet ID or group ID: walletId={}, groupId={}", walletId, groupId);
        throw new InvalidParameterException("Invalid wallet ID or group ID");
      }

      if (!internalWallets.containsKey(walletId) || !groupWallets.containsKey(groupId)) {
        logger.error("Group does not contain wallet: groupId={}", groupId);
        throw new WalletNotFoundException("Group does not contain wallet");
      }

      Wallet wallet = internalWallets.get(walletId).getImmutableWallet();
      if (wallet == null) {
        logger.error("Wallet not found: walletId={}", walletId);
        throw new WalletNotFoundException("Wallet not found");
      }

      GroupWallet groupWallet = groupWallets.get(groupId);
      if (groupWallet == null) {
        logger.error("Group not found: groupId={}", groupId);
        throw new InvalidParameterException("Group not found");
      }

      groupWallet.removeWallet(wallet);

      logger.info("Wallet removed from group successfully: walletId={}, groupId={}",
              walletId, groupId);
    } catch (InvalidParameterException | WalletNotFoundException e) {
      logger.error("Error removing wallet from group", e);
      throw e;
    }

  }

  @Override
  public List<String> getWalletGroups() {

    try {
      logger.info("Getting wallet groups");

      List<String> groupNames = new ArrayList<>();
      for (Map.Entry<String, GroupWallet> entry : groupWallets.entrySet()) {
        groupNames.add(entry.getValue().getGroupName());
      }

      logger.info("Wallet groups retrieved successfully");
      return groupNames;
    } catch (Exception e) {
      logger.error("Error getting wallet groups", e);
      throw e;
    }
  }

  @Override
  public boolean deleteWallet(String walletId)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Deleting wallet with ID: {}", walletId);

      if (walletId == null || walletId.isEmpty()) {
        throw new InvalidParameterException("Invalid wallet ID");
      }

      if (!internalWallets.containsKey(walletId)) {
        throw new WalletNotFoundException("Wallet not found");
      }
      descriptions.remove(internalWallets.get(walletId).getDescription());
      internalWallets.remove(walletId);
      walletBalances.remove(walletId);

      logger.info("Wallet deleted successfully");
      return true;
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error deleting wallet", e);
      throw e;
    }
  }

  @Override
  public boolean deleteGroupWallet(String groupId) throws InvalidParameterException {
    try {
      logger.info("Deleting group wallet with ID: {}", groupId);

      if (groupId == null || groupId.isEmpty()) {
        throw new InvalidParameterException("Invalid group ID");
      }

      if (!groupWallets.containsKey(groupId)) {
        throw new InvalidParameterException("Group does not exist");
      }

      descriptions.remove(groupWallets.get(groupId).getGroupName());
      groupWallets.remove(groupId);



      logger.info("Group wallet deleted successfully");
      return true;
    } catch (InvalidParameterException e) {
      logger.error("Error deleting group wallet", e);
      throw e;
    }
  }


  @Override
  public Expense createExpense(String walletId, String description,
                               BigDecimal amount, LocalDateTime dateTime)
          throws InvalidParameterException, WalletNotFoundException {
    try {
      logger.info("Creating expense for wallet with ID: {}", walletId);

      if (amount.doubleValue() <= 0) {
        throw new InvalidParameterException("Amount cannot be 0 or smaller than 0");
      }
      if (walletId == null || walletId.isEmpty()) {
        throw new InvalidParameterException("Invalid wallet ID");
      }

      if (!walletBalances.containsKey(walletId)) {
        throw new WalletNotFoundException("Wallet not found");
      }

      Expense expense = new Expense(description,
              amount, Objects.requireNonNullElseGet(dateTime, LocalDateTime::now));
      BigDecimal currentBalance = walletBalances.get(walletId);
      if (currentBalance.compareTo(amount) < 0) {
        throw new InvalidParameterException("Insufficient funds in the source wallet");
      }
      BigDecimal newBalance = currentBalance.subtract(expense.getAmount());
      walletBalances.put(walletId, newBalance);

      InternalWallet wallet = internalWallets.get(walletId);
      wallet.addExpense(expense);

      logger.info("Expense created successfully");
      return expense;
    } catch (InvalidParameterException | WalletNotFoundException e) {
      logger.error("Error creating expense", e);
      throw e;
    }
  }

  @Override
  public ExpenseReport getExpenseReportForGroupWallet(String groupId,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Generating expense report for group wallet with ID: {}", groupId);

      if (groupId == null || groupId.isEmpty()) {
        throw new InvalidParameterException("Invalid group wallet ID");
      }

      GroupWallet groupWallet = groupWallets.get(groupId);
      if (groupWallet == null) {
        throw new WalletNotFoundException("Group wallet not found");
      }

      List<Expense> expensesWithinRange = groupWallet.getExpensesWithinDateRange(startDate,
              endDate);

      ExpenseReport expenseReport = new ExpenseReport(startDate, endDate, expensesWithinRange);

      logger.info("Expense report generated successfully");
      return expenseReport;
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error generating expense report", e);
      throw e;
    }

  }


  @Override
  public boolean deleteExpense(String walletId, Expense expense)
          throws WalletNotFoundException, InvalidParameterException {
    try {
      logger.info("Deleting expense for wallet with ID: {}", walletId);

      if (walletId == null || walletId.isEmpty()) {
        throw new InvalidParameterException("Invalid wallet ID");
      }

      if (!internalWallets.containsKey(walletId)) {
        throw new WalletNotFoundException("Wallet not found");
      }

      InternalWallet wallet = internalWallets.get(walletId);
      wallet.removeExpense(expense);

      logger.info("Expense deleted successfully");
      return true;
    } catch (WalletNotFoundException | InvalidParameterException e) {
      logger.error("Error deleting expense", e);
      throw e;
    }
  }


  @Override
  public String generatePrintableReport(ExpenseReport report) throws InvalidParameterException {
    try {
      logger.info("Generating printable report");

      if (report == null) {
        throw new InvalidParameterException("Not a valid report");
      }

      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

      StringBuilder printableReport = new StringBuilder();
      printableReport.append("Expense Report\n");
      printableReport.append("Date Range: ")
              .append(report.getStartDate().format(dateTimeFormatter))
              .append(" to ")
              .append(report.getEndDate().format(dateTimeFormatter))
              .append("\n\n");

      for (Expense expense : report.getExpenses()) {
        printableReport.append("Date: ")
                .append(expense.getDate().format(dateTimeFormatter))
                .append("\n");
        printableReport.append("Description: ").append(expense.getDescription()).append("\n");
        printableReport.append("Amount: ").append(expense.getAmount()).append("\n\n");
      }

      double totalExpenses = report.getTotalExpenses().doubleValue();
      printableReport.append("Total Expenses: ").append(totalExpenses);

      logger.info("Printable report generated successfully");
      return printableReport.toString();
    } catch (InvalidParameterException e) {
      logger.error("Error generating printable report", e);
      throw e;
    }
  }

  @Override
  public List<String> getWalletDescriptions() {
    try {
      logger.info("Getting wallet descriptions");

      List<String> descriptions = new ArrayList<>();
      for (Map.Entry<String, InternalWallet> entry : internalWallets.entrySet()) {
        descriptions.add(entry.getValue().getDescription());
      }

      logger.info("Wallet descriptions retrieved successfully");
      return descriptions;
    } catch (Exception e) {
      logger.error("Error getting wallet descriptions", e);
      throw e;
    }
  }

  @Override
  public String getIdByDescription(String description) throws InvalidParameterException {
    try {
      logger.info("Getting id by description");
      if (description.isEmpty()) {
        throw new InvalidParameterException("Description cannot be left blank");
      }
      if (descriptions.get(description).isEmpty() || descriptions.get(description) == null) {
        logger.error("No id found for this description");
        throw new InvalidParameterException("No Wallet Id for this description");
      }
      return descriptions.get(description);
    } catch (Exception e) {
      logger.error("Error getting wallet id by description", e);
      throw e;
    }
  }
}