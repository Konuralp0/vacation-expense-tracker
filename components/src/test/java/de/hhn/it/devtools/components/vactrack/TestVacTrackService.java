package de.hhn.it.devtools.components.vactrack;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.hhn.it.devtools.apis.vactrack.Expense;
import de.hhn.it.devtools.apis.vactrack.ExpenseReport;
import de.hhn.it.devtools.apis.vactrack.GroupWallet;
import de.hhn.it.devtools.apis.vactrack.InvalidExchangeRateException;
import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.VacTrackService;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



/**
 * The type Test vac track vacTrackService.
 */
public class TestVacTrackService {

  private VacTrackService vacTrackService;

  /**
   * Sets up.
   */
  @BeforeEach
  public void setUp() {
    vacTrackService = new VacTrackServiceImpl();
  }

  /**
   * Test enter exchange rate valid exchange rate success.
   */
  @Test
  @DisplayName("Test entering a valid exchange rate")
  public void testEnterExchangeRate_validExchangeRate_success() {
    String currencyFrom = "USD";
    String currencyTo = "EUR";
    double exchangeRate = 0.85;

    assertDoesNotThrow(()
            -> vacTrackService.enterExchangeRate(currencyFrom, currencyTo, exchangeRate));
  }

  /**
   * Test enter exchange rate zero exchange rate invalid exchange rate exception.
   */
  @Test
  @DisplayName("Test entering a zero exchange rate")
  public void testEnterExchangeRate_zeroExchangeRate_invalidExchangeRateException() {
    String currencyFrom = "USD";
    String currencyTo = "EUR";
    double exchangeRate = 0.0;

    assertThrows(InvalidExchangeRateException.class,
            () -> vacTrackService.enterExchangeRate(currencyFrom, currencyTo, exchangeRate));
  }

  /**
   * Test enter exchange rate same currencies invalid parameter exception.
   */
  @Test
  @DisplayName("Test entering the same currencies")
  public void testEnterExchangeRate_sameCurrencies_invalidParameterException() {
    String currencyFrom = "USD";
    String currencyTo = "USD";
    double exchangeRate = 1.0;

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.enterExchangeRate(currencyFrom, currencyTo, exchangeRate));
  }

  /**
   * Test get wallet empty wallet id invalid parameter exception.
   */
  @Test
  @DisplayName("Test getting a wallet with an empty ID")
  public void testGetWallet_emptyWalletId_invalidParameterException() {
    String walletId = "";

    assertThrows(InvalidParameterException.class, () -> vacTrackService.getWallet(walletId));
  }

  /**
   * Test generate printable report valid report success.
   */
  @Test
  @DisplayName("Test generating a printable report with valid parameters")
  public void testGeneratePrintableReport_validReport_success() {
    LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 1, 31, 23, 59);
    List<Expense> expenses = new ArrayList<>();
    expenses.add(new Expense("Expense 1", BigDecimal.valueOf(50.0),
            LocalDateTime.of(2023, 1, 5, 12, 0)));
    expenses.add(new Expense("Expense 2", BigDecimal.valueOf(20.0),
            LocalDateTime.of(2022, 5, 2, 1, 20)));
    ExpenseReport report = new ExpenseReport(startDate, endDate, expenses);

    assertDoesNotThrow(() -> vacTrackService.generatePrintableReport(report));
  }

  /**
   * Test generate printable report null report invalid parameter exception.
   */
  @Test
  @DisplayName("Test generating a printable report with null report")
  public void testGeneratePrintableReport_nullReport_invalidParameterException() {

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.generatePrintableReport(null));
  }

  /**
   * Test generate printable report empty expenses empty report string.
   */
  @Test
  @DisplayName("Test generating a printable report with empty expenses")
  public void testGeneratePrintableReport_emptyExpenses_emptyReportString() {
    LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 1, 31, 23, 59);
    List<Expense> expenses = new ArrayList<>();
    ExpenseReport report = new ExpenseReport(startDate, endDate, expenses);

    String printableReport;
    try {
      printableReport = vacTrackService.generatePrintableReport(report);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    assertEquals("""
            Expense Report
            Date Range: 2023-01-01 00:00 to 2023-01-31 23:59

            Total Expenses: 0.0""", printableReport);
  }

  /**
   * Test get wallet invalid wallet id invalid parameter exception.
   */
  @Test
  @DisplayName("Test getting a wallet with null ID")
  public void testGetWallet_invalidWalletId_invalidParameterException() {
    assertThrows(InvalidParameterException.class, () -> vacTrackService.getWallet(null));
  }

  @Test
  public void testGetWalletDescriptions() {
    assertDoesNotThrow(() -> assertNotNull(vacTrackService.getWalletDescriptions()));
  }

  /**
   * Test convert wallet currency invalid wallet id invalid parameter exception.
   */
  @Test
  @DisplayName("Test converting wallet currency with null wallet ID")
  public void testConvertWalletCurrency_invalidWalletId_invalidParameterException() {
    String walletId = null;
    String currencyTo = "EUR";

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, currencyTo));
  }

  /**
   * Test convert wallet currency invalid parameter exception.
   */
  @Test
  @DisplayName("Test converting wallet currency with null wallet ID")
  public void testConvertWalletCurrency_invalidCurrency_invalidParameterException() {
    String currencyTo = "EUR";

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency("", currencyTo));
  }

  /**
   * Test get expense report for group wallet valid parameters' success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  @DisplayName("Test getting an expense report for a group wallet with valid parameters")
  public void testGetExpenseReportForGroupWallet_validParameters_success()
          throws InvalidParameterException, WalletNotFoundException {
    String walletId = vacTrackService.createWallet("hi", "euro", BigDecimal.valueOf(10));
    GroupWallet gw = vacTrackService.createWalletGroup("groupId", BigDecimal.valueOf(10));
    vacTrackService.addWalletToGroup(walletId, String.valueOf(gw.getGroupId()));
    vacTrackService.createExpense(walletId, "essen", BigDecimal.ONE, LocalDateTime.now());
    LocalDateTime startDate = LocalDateTime.of(2023, 11, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 11, 30, 23, 59);
    assertDoesNotThrow(() -> {
      ExpenseReport expenseReport = vacTrackService.getExpenseReportForGroupWallet(
              String.valueOf(gw.getGroupId()), startDate, endDate);
      assertNotNull(expenseReport);
    });
  }

  /**
   * Test get id by description valid description success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test getting an ID by description with a valid description")
  public void testGetIdByDescription_validDescription_success() throws InvalidParameterException {
    String description = "validDescription";
    String expectedId = vacTrackService.createWallet(description, "euro", BigDecimal.ONE);
    String actualId = vacTrackService.getIdByDescription(description);
    assertEquals(expectedId, actualId);
  }

  /**
   * Test get id by description invalid description invalid parameter exception.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test getting an ID by description with an invalid description")
  public void testGetIdByDescription_invalidDescription_invalidParameterException()
          throws InvalidParameterException {
    String description = "invalidDescription";
    vacTrackService.createWallet(description, "euro", BigDecimal.ONE);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.getIdByDescription(""));
  }

  /**
   * Test set daily expense limit valid parameters success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test setting a daily expense limit with valid parameters")
  public void testSetDailyExpenseLimit_validParameters_success() throws InvalidParameterException {
    String userId = vacTrackService.createWallet("hi", "euro", BigDecimal.ONE);
    BigDecimal limit = new BigDecimal("50.00");

    assertDoesNotThrow(() ->
            vacTrackService.setDailyExpenseLimit(userId, limit));
  }

  /**
   * Test set daily expense limit invalid user id invalid parameter exception.
   */
  @Test
  @DisplayName("Test setting a daily expense limit with an invalid userId")
  public void testSetDailyExpenseLimit_invalidUserId_invalidParameterException() {
    BigDecimal limit = new BigDecimal("50.00");

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.setDailyExpenseLimit("", limit));
  }

  /**
   * Test set daily expense limit invalid limit invalid parameter exception.
   */
  @Test
  @DisplayName("Test setting a daily expense limit with an invalid userId")
  public void testSetDailyExpenseLimit_invalidLimit_invalidParameterException() {
    BigDecimal limit = new BigDecimal("50.00");

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.setDailyExpenseLimit(null, limit));
  }

  /**
   * Test get id by description empty description invalid parameter exception.
   */
  @Test
  @DisplayName("Test getting an ID by description with an empty description")
  public void testGetIdByDescription_emptyDescription_invalidParameterException() {
    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.getIdByDescription(""));
  }

  /**
   * Test add wallet to group wallet in group invalid parameter exception.
   */
  @Test
  @DisplayName("Test adding a wallet to a group that already contains the wallet")
  public void testAddWalletToGroup_walletInGroup_invalidParameterException() {
    String walletId = "validWalletId";

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.addWalletToGroup(walletId, ""));
  }

  /**
   * Test add wallet to group wallet already in group invalid parameter exception.
   */
  @Test
  @DisplayName("Test adding a wallet to a group that already contains the wallet")
  public void testAddWalletToGroup_walletAlreadyInGroup_invalidParameterException() {
    String walletId = "validWalletId";

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.addWalletToGroup("", walletId));
  }

  /**
   * Test add wallet to group non-existent group invalid parameter exception.
   */
  @Test
  @DisplayName("Test adding a wallet to a non-existent group")
  public void testAddWalletToGroup_nonExistentGroup_invalidParameterException() {
    String groupId = "nonExistentGroupId";

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.addWalletToGroup(groupId, null));
  }

  /**
   * Test add wallet to group non-existent wallet invalid parameter exception.
   */
  @Test
  @DisplayName("Test adding a non-existent wallet to a group")
  public void testAddWalletToGroup_nonExistentWallet_invalidParameterException() {
    String walletId = "nonExistentWalletId";

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.addWalletToGroup(null, walletId));
  }

  @Test
  @DisplayName("Test adding a non-existent wallet to a group")
  public void testGetGroupWallet_invalid() {

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.getGroupWallet(""));
  }

  @Test
  @DisplayName("Test adding a non-existent wallet to a group")
  public void testGetGroupWallet_invalid1() {

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.getGroupWallet(null));
  }

  /**
   * Test get expense report for group wallet invalid wallet id invalid parameter exception.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  @DisplayName("Test getting an expense report for a group wallet with an invalid walletId")
  public void testGetExpenseReportForGroupWallet_invalidWalletId_invalidParameterException()
          throws InvalidParameterException, WalletNotFoundException {
    String walletId = vacTrackService.createWallet("hi", "euro", BigDecimal.valueOf(10));
    GroupWallet gw = vacTrackService.createWalletGroup("groupId", BigDecimal.valueOf(10));
    vacTrackService.addWalletToGroup(walletId, String.valueOf(gw.getGroupId()));
    vacTrackService.createExpense(walletId, "essen", BigDecimal.ONE, LocalDateTime.now());
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = LocalDateTime.of(2023, 11, 30, 23, 59, 1, 1);

    assertThrows(WalletNotFoundException.class, () ->
            vacTrackService.getExpenseReportForGroupWallet("gw.getGroupId()", startDate, endDate));
  }

  /**
   * Test add wallet to group invalid group id invalid parameter exception.
   */
  @Test
  @DisplayName("Test adding a wallet to a group with an invalid groupId")
  public void testAddWalletToGroup_invalidGroupId_invalidParameterException() {
    String groupId = null;
    String walletId = "1234";

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.addWalletToGroup(groupId, walletId));
  }

  /**
   * Test add wallet to group invalid wallet id invalid parameter exception.
   */
  @Test
  @DisplayName("Test adding a wallet to a group with an invalid walletId")
  public void testAddWalletToGroup_invalidWalletId_invalidParameterException() {
    String groupId = "validGroupId";
    String walletId = null;

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.addWalletToGroup(groupId, walletId));
  }

  /**
   * Test convert wallet currency invalid currency to invalid parameter exception.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid currency to")
  public void testConvertWalletCurrency_invalidCurrencyTo_invalidParameterException() {
    String walletId = "wallet1";
    String currencyTo = null;

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, currencyTo));
  }

  /**
   * Test convert wallet currency wallet not found wallet not found exception.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid wallet id")
  public void testConvertWalletCurrency_walletNotFound_walletNotFoundException() {
    String walletId = "nonExistentWallet";
    String currencyTo = "EUR";

    assertThrows(WalletNotFoundException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, currencyTo));
  }

  /**
   * Test convert wallet currency invalid 1.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid wallet id")
  public void testConvertWalletCurrency_Invalid1() {
    String currencyTo = "EUR";

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency("", currencyTo));
  }

  /**
   * Test convert wallet currency invalid.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid wallet id")
  public void testConvertWalletCurrency_Invalid() {
    String currencyTo = "EUR";

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency(null, currencyTo));
  }

  /**
   * Test convert wallet currency invalid 2.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid wallet id")
  public void testConvertWalletCurrency_Invalid2() {
    String walletId = "nonExistentWallet";

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, ""));
  }

  /**
   * Test convert wallet currency invalid 3.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid wallet id")
  public void testConvertWalletCurrency_Invalid3() {
    String walletId = "nonExistentWallet";

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, null));
  }

  /**
   * Test convert wallet currency valid parameters' success.
   */
  @Test
  @DisplayName("Test converting wallet currency with valid parameters")
  public void testConvertWalletCurrency_validParameters_success() {
    String description = "My Wallet";
    String currency = "USD";
    BigDecimal initialBudget = BigDecimal.valueOf(1000);
    String walletId;
    try {
      walletId = vacTrackService.createWallet(description, currency, initialBudget);
      vacTrackService.enterExchangeRate("USD", "EUR", 0.85);
    } catch (InvalidParameterException | InvalidExchangeRateException e) {
      throw new RuntimeException(e);
    }


    try {
      vacTrackService.convertWalletCurrency(walletId, "EUR");
    } catch (InvalidParameterException | InvalidExchangeRateException | WalletNotFoundException e) {
      throw new RuntimeException(e);
    }


    try {
      assertEquals(0, BigDecimal.valueOf(850.00)
              .compareTo(vacTrackService.getWalletBalance(walletId)));
    } catch (WalletNotFoundException | InvalidParameterException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test get wallet null wallet id throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test getting a wallet with null walletId should throw InvalidParameterException")
  public void testGetWallet_nullWalletId_throwInvalidParameterException() {
    String walletId = null;

    assertThrows(InvalidParameterException.class,
            () -> vacTrackService.getWallet(walletId));
  }

  /**
   * Test create wallet valid parameters' success.
   */
  @Test
  @DisplayName("Test creating a wallet with valid parameters")
  public void testCreateWallet_validParameters_success() {
    String description = "My Wallet";
    String currency = "USD";
    BigDecimal initialBudget = BigDecimal.valueOf(1000);

    assertDoesNotThrow(() -> {
      String walletId = vacTrackService.createWallet(description, currency, initialBudget);
      assertNotNull(walletId);
    });
  }

  /**
   * Test enter expense invalid wallet id wallet not found exception.
   */
  @Test
  @DisplayName("Test entering an expense with invalid walletId")
  public void testEnterExpense_invalidWalletId_walletNotFoundException() {
    String walletId = "invalidWalletId";
    String expenseDescription = "Dinner";
    BigDecimal amount = BigDecimal.valueOf(30.0);
    LocalDateTime expenseDate = LocalDateTime.of(2023, 1, 15, 19, 0);

    assertThrows(WalletNotFoundException.class, () ->
            vacTrackService.createExpense(walletId, expenseDescription, amount, expenseDate));
  }

  /**
   * Test enter exchange rate valid currencies success.
   */
  @Test
  @DisplayName("Test entering an exchange rate with valid currencies")
  public void testEnterExchangeRate_validCurrencies_success() {
    String currencyFrom = "USD";
    String currencyTo = "EUR";
    double exchangeRate = 0.85;

    assertDoesNotThrow(() ->
            vacTrackService.enterExchangeRate(currencyFrom, currencyTo, exchangeRate));
  }

  /**
   * Test transfer funds invalid wallet id wallet not found exception.
   */
  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_walletNotFoundException() {
    String sourceWalletId = "invalidSourceWalletId";
    String destinationWalletId = "destinationWalletId";
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(WalletNotFoundException.class, () ->
            vacTrackService.transferFunds(sourceWalletId, destinationWalletId, amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid() {
    String destinationWalletId = "destinationWalletId";
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds(null, destinationWalletId, amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid1() {
    String destinationWalletId = "destinationWalletId";
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds("", destinationWalletId, amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid2() {
    String sourceWalletId = "invalidSourceWalletId";
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds(sourceWalletId, "", amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid3() {
    String sourceWalletId = "invalidSourceWalletId";
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds(sourceWalletId, null, amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid4() {
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds("", "", amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid5() {
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds(null, null, amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid6() {
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds("", null, amount, 1));
  }

  @Test
  @DisplayName("Test transferring funds with invalid walletId")
  public void testTransferFunds_invalidWalletId_Invalid7() {
    BigDecimal amount = BigDecimal.valueOf(50.0);

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.transferFunds(null, "", amount, 1));
  }

  /**
   * Test get group wallets valid group id success.
   */
  @Test
  @DisplayName("Test getting group wallets with valid groupId")
  public void testGetGroupWallets_validGroupId_success() {

    assertDoesNotThrow(() -> {
      List<String> groupWallets = vacTrackService.getWalletGroups();
      assertNotNull(groupWallets);
    });
  }

  /**
   * Test create group valid parameters' success.
   */
  @Test
  @DisplayName("Test creating a group with valid parameters")
  public void testCreateGroup_validParameters_success() {
    String groupName = "My Group";

    assertDoesNotThrow(() -> {
      GroupWallet groupId = vacTrackService.createWalletGroup(groupName, new BigDecimal(1));
      assertNotNull(groupId);
    });
  }

  /**
   * Test create group empty name invalid parameter exception.
   */
  @Test
  @DisplayName("Test creating a group with an empty name")
  public void testCreateGroup_emptyName_invalidParameterException() {
    String groupName = "";

    assertThrows(InvalidParameterException.class, () ->
            vacTrackService.createWalletGroup(groupName, new BigDecimal(1)));
  }

  /**
   * Test get expense report invalid wallet id wallet not found exception.
   */
  @Test
  @DisplayName("Test getting an expense report with invalid walletId")
  public void testGetExpenseReport_invalidWalletId_walletNotFoundException() {
    String walletId = "invalidWalletId";
    LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 1, 31, 23, 59);

    assertThrows(WalletNotFoundException.class, () ->
            vacTrackService.getExpenseReport(walletId, startDate, endDate));
  }


  /**
   * Test convert wallet currency invalid wallet id wallet not found exception.
   */
  @Test
  @DisplayName("Test converting wallet currency with invalid walletId")
  public void testConvertWalletCurrency_invalidWalletId_walletNotFoundException() {
    String walletId = "invalidWalletId";
    String currencyTo = "EUR";

    assertThrows(WalletNotFoundException.class, () ->
            vacTrackService.convertWalletCurrency(walletId, currencyTo));
  }

  /**
   * Test create wallet null currency throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test creating a wallet with null currency ")
  public void testCreateWallet_nullCurrency_throwInvalidParameterException() {
    String description = "My Wallet";
    String currency = null;
    BigDecimal initialBudget = BigDecimal.valueOf(1000);

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.createWallet(description, currency, initialBudget));
  }

  /**
   * Test create wallet negative initial budget throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test creating a wallet with negative initial budget ")
  public void testCreateWallet_negativeInitialBudget_throwInvalidParameterException() {
    String description = "My Wallet";
    String currency = "USD";
    BigDecimal initialBudget = BigDecimal.valueOf(-100);

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.createWallet(description, currency, initialBudget));
  }

  /**
   * Test transfer funds null wallet ids throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test transferring funds with null walletIds ")
  public void testTransferFunds_nullWalletIds_throwInvalidParameterException() {
    String sourceWalletId = null;
    String destinationWalletId = "67890";
    BigDecimal amount = BigDecimal.valueOf(500);
    double currency = 1.2;

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.transferFunds(
            sourceWalletId, destinationWalletId, amount, currency));
  }

  /**
   * Test transfer funds invalid wallet ids throw wallet not found exception.
   */
  @Test
  @DisplayName("Test transferring funds with invalid walletIds ")
  public void testTransferFunds_invalidWalletIds_throwWalletNotFoundException() {
    String sourceWalletId = "99999";
    String destinationWalletId = "67890";
    BigDecimal amount = BigDecimal.valueOf(500);
    double currency = 1.2;

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.transferFunds(
            sourceWalletId, destinationWalletId, amount, currency));
  }

  /**
   * Test get wallet balance valid wallet id success.
   */
  @Test
  @DisplayName("Test getting wallet balance with valid walletId")
  public void testGetWalletBalance_validWalletId_success() {
    String walletId;
    try {

      walletId = vacTrackService.createWallet("Wallet", "EUR", BigDecimal.valueOf(2));
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    assertDoesNotThrow(() -> {
      BigDecimal balance = vacTrackService.getWalletBalance(walletId);
      assertEquals(BigDecimal.valueOf(2), balance);
    });
  }

  /**
   * Test get expense report valid wallet id success.
   */
  @Test
  @DisplayName("Test getting expense report with valid walletId should succeed")
  public void testGetExpenseReport_validWalletId_success() {
    String walletId;
    try {
      walletId = vacTrackService.createWallet("Wallet", "EUR", BigDecimal.valueOf(2.0));
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    assertDoesNotThrow(() -> {
      List<Expense> expenses = vacTrackService
              .getExpenseReport(walletId,
                      LocalDateTime.of(2023, 1, 31, 23, 59),
                      LocalDateTime.now()).getExpenses();
      assertNotNull(expenses);
      assertEquals(0, expenses.size());
    });
  }

  /**
   * Test get expense report null wallet id throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test getting expense report with null walletId")
  public void testGetExpenseReport_nullWalletId_throwInvalidParameterException() {
    String walletId = "12345";

    assertThrows(WalletNotFoundException.class, () -> vacTrackService.getExpenseReport(walletId,
            LocalDateTime.of(2023, 1, 31, 23, 59),
            LocalDateTime.now()));
  }

  /**
   * Test get expense report invalid wallet id throw wallet not found exception.
   */
  @Test
  @DisplayName("Test getting expense report with invalid walletId")
  public void testGetExpenseReport_invalidWalletId_throwWalletNotFoundException() {
    String walletId = "99999";

    assertThrows(WalletNotFoundException.class, () -> vacTrackService.getExpenseReport(walletId,
            LocalDateTime.of(2023, 1, 31, 23, 59),
            LocalDateTime.now()));
  }

  /**
   * Test delete wallet existing wallet id success.
   */
  @Test
  @DisplayName("Test deleting an existing wallet should succeed")
  public void testDeleteWallet_existingWalletId_success() {
    String description = "My Wallet";
    String currency = "USD";
    BigDecimal initialBudget = BigDecimal.valueOf(1000);
    String walletId;
    try {
      walletId = vacTrackService.createWallet(description, currency, initialBudget);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    String finalWalletId = walletId;
    assertDoesNotThrow(() -> {
      vacTrackService.deleteWallet(finalWalletId);
    });
  }

  /**
   * Test delete wallet null wallet id throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test deleting a wallet with null walletId ")
  public void testDeleteWallet_nullWalletId_throwInvalidParameterException() {
    String walletId = null;

    assertThrows(InvalidParameterException.class, () -> vacTrackService.deleteWallet(walletId));
  }

  /**
   * Test delete wallet invalid wallet id throw wallet not found exception.
   */
  @Test
  @DisplayName("Test deleting a wallet with invalid walletId ")
  public void testDeleteWallet_invalidWalletId_throwWalletNotFoundException() {
    String walletId = "99999";

    assertThrows(WalletNotFoundException.class, () -> vacTrackService.deleteWallet(walletId));
  }

  /**
   * Test transfer funds valid parameters success.
   */
  @Test
  @DisplayName("Test transferring funds with valid parameters")
  public void testTransferFunds_validParameters_success() {
    String walletId1;
    try {
      walletId1 = vacTrackService.createWallet("Wallet 1", "USD", BigDecimal.valueOf(1000));
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    String walletId2;
    try {
      walletId2 = vacTrackService.createWallet("Wallet 2", "USD", BigDecimal.ZERO);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    BigDecimal transferAmount = BigDecimal.valueOf(500.00);

    String finalWalletId1 = walletId1;
    String finalWalletId2 = walletId2;
    assertDoesNotThrow(()
            -> vacTrackService.transferFunds(finalWalletId1, finalWalletId2, transferAmount, 1));

    BigDecimal balance1;
    try {
      balance1 = vacTrackService.getWalletBalance(walletId1);
    } catch (WalletNotFoundException | InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    BigDecimal balance2;
    try {
      balance2 = vacTrackService.getWalletBalance(walletId2);
    } catch (WalletNotFoundException | InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    assertEquals(BigDecimal.valueOf(500.00).setScale(2, 5), balance1);
    assertEquals(BigDecimal.valueOf(500.00).setScale(2, 5), balance2);
  }

  /**
   * Test transfer funds null source wallet id throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test transferring funds with null source walletId")
  public void testTransferFunds_nullSourceWalletId_throwInvalidParameterException() {
    String walletId = null;
    String destinationWalletId = "12345";
    BigDecimal transferAmount = BigDecimal.valueOf(500);

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.transferFunds(walletId, destinationWalletId, transferAmount, 1));
  }

  /**
   * Test transfer funds null destination wallet id throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test transferring funds with null destination walletId")
  public void testTransferFunds_nullDestinationWalletId_throwInvalidParameterException() {
    String walletId = "12345";
    String destinationWalletId = null;
    BigDecimal transferAmount = BigDecimal.valueOf(500);

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.transferFunds(walletId, destinationWalletId, transferAmount, 1));
  }

  /**
   * Test transfer funds invalid source wallet id throw wallet not found exception.
   */
  @Test
  @DisplayName("Test transferring funds with invalid source walletId")
  public void testTransferFunds_invalidSourceWalletId_throwWalletNotFoundException() {
    String walletId = "99999";
    String destinationWalletId = "12345";
    BigDecimal transferAmount = BigDecimal.valueOf(500);

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.transferFunds(walletId, destinationWalletId, transferAmount, 1));
  }

  /**
   * Test transfer funds invalid destination wallet id throw wallet not found exception.
   */
  @Test
  @DisplayName("Test transferring funds with invalid destination walletId ")
  public void testTransferFunds_invalidDestinationWalletId_throwWalletNotFoundException() {
    String walletId = "12345";
    String destinationWalletId = "99999";
    BigDecimal transferAmount = BigDecimal.valueOf(500);

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.transferFunds(walletId, destinationWalletId, transferAmount, 1));
  }

  /**
   * Test transfer funds insufficient balance throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test transferring funds with insufficient balance")
  public void testTransferFunds_insufficientBalance_throwInvalidParameterException() {
    String walletId1;
    try {
      walletId1 = vacTrackService.createWallet("Wallet 1", "USD", BigDecimal.valueOf(100));
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    String walletId2;
    try {
      walletId2 = vacTrackService.createWallet("Wallet 2", "USD", BigDecimal.ZERO);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    BigDecimal transferAmount = BigDecimal.valueOf(500);

    String finalWalletId1 = walletId1;
    String finalWalletId2 = walletId2;
    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.transferFunds(finalWalletId1, finalWalletId2, transferAmount, 1));
  }

  /**
   * Test get wallet balance existing wallet id success.
   */
  @Test
  @DisplayName("Test getting wallet balance of an existing wallet")
  public void testGetWalletBalance_existingWalletId_success() {
    String description = "My Wallet";
    String currency = "USD";
    BigDecimal initialBudget = BigDecimal.valueOf(1000);
    String walletId;
    try {
      walletId = vacTrackService.createWallet(description, currency, initialBudget);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    BigDecimal balance;
    try {
      balance = vacTrackService.getWalletBalance(walletId);
    } catch (WalletNotFoundException | InvalidParameterException e) {
      throw new RuntimeException(e);
    }

    assertEquals(BigDecimal.valueOf(1000), balance);
  }

  /**
   * Test get wallet balance null wallet id throw invalid parameter exception.
   */
  @Test
  @DisplayName("Test getting wallet balance with null walletId")
  public void testGetWalletBalance_nullWalletId_throwInvalidParameterException() {
    String walletId = null;

    assertThrows(InvalidParameterException.class, () -> vacTrackService.getWalletBalance(walletId));
  }

  /**
   * Test get wallet balance invalid wallet id throw wallet not found exception.
   */
  @Test
  @DisplayName("Test getting wallet balance with invalid walletId ")
  public void testGetWalletBalance_invalidWalletId_throwWalletNotFoundException() {
    String walletId = "99999";

    assertThrows(WalletNotFoundException.class, () -> vacTrackService.getWalletBalance(walletId));
  }

  /**
   * Create expense invalid amount exception thrown.
   */
  @Test
  @DisplayName("Test creating expense with invalid amount")
  void createExpense_InvalidAmount_ExceptionThrown() {

    String walletId = "existingWalletId";
    String description = "Test Expense";
    BigDecimal invalidAmount = new BigDecimal("-10.00");

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.createExpense(walletId, description, invalidAmount, null));
  }

  /**
   * Create expense wallet not found exception thrown.
   */
  @Test
  @DisplayName("Test creating expense with a not valid wallet")
  void createExpense_WalletNotFound_ExceptionThrown() {

    String nonExistingWalletId = "nonExistingWalletId";
    String description = "Test Expense";
    BigDecimal amount = new BigDecimal("50.00");

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.createExpense(nonExistingWalletId, description, amount, null));
  }

  /**
   * Sets daily expense limit invalid wallet exception thrown.
   */
  @Test
  @DisplayName("Test setting daily expense limit for a invalid wallet")
  void setDailyExpenseLimit_InvalidWallet_ExceptionThrown() {

    String nonExistingWalletId = "nonExistingWalletId";
    BigDecimal limit = new BigDecimal("100.00");

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.setDailyExpenseLimit(nonExistingWalletId, limit));
  }

  /**
   * Gets expense report for group wallet invalid group exception thrown.
   */
  @Test
  @DisplayName("Test getting expense report for a invalid group wallet")
  void getExpenseReportForGroupWallet_InvalidGroup_ExceptionThrown() {

    String nonExistingGroupId = "nonExistingGroupId";
    LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.getExpenseReportForGroupWallet(
            nonExistingGroupId, startDate, endDate));
  }

  /**
   * Test enter exchange rate.
   */
  @Test
  void testEnterExchangeRate() {
    assertDoesNotThrow(() -> vacTrackService.enterExchangeRate("USD", "EUR", 1.5));
    assertDoesNotThrow(() -> vacTrackService.enterExchangeRate("EUR", "USD", 0.5));
  }

  /**
   * Test enter invalid exchange rate.
   */
  @Test
  void testEnterInvalidExchangeRate() {
    InvalidExchangeRateException exception = assertThrows(InvalidExchangeRateException.class,
            () -> vacTrackService.enterExchangeRate("USD", "EUR", 0));
    assertEquals("Exchange rate must be greater than 0", exception.getMessage());
  }

  /**
   * Test enter same currencies.
   */
  @Test
  void testEnterSameCurrencies() {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.enterExchangeRate("USD", "USD", 1.0));
    assertEquals("Both currencies cannot be the same", exception.getMessage());
  }

  /**
   * Test enter empty currencies.
   */
  @Test
  void testEnterEmptyCurrencies() {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.enterExchangeRate("", "EUR", 1.5));
    assertEquals("Currencies cannot be left blank", exception.getMessage());

    exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.enterExchangeRate("USD", "", 1.5));
    assertEquals("Currencies cannot be left blank", exception.getMessage());
  }

  /**
   * Test enter numeric currencies.
   */
  @Test
  void testEnterNumericCurrencies() {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.enterExchangeRate("USD", "EUR1", 1.5));
    assertEquals("Currencies cannot contain numerical values.", exception.getMessage());

    exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.enterExchangeRate("USD2", "EUR", 1.5));
    assertEquals("Currencies cannot contain numerical values.", exception.getMessage());
  }

  /**
   * Test get wallet.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  void testGetWallet() throws InvalidParameterException, WalletNotFoundException {
    String walletId = vacTrackService.createWallet("My Wallet", "USD", BigDecimal.valueOf(100.0));
    assertNotNull(vacTrackService.getWallet(walletId));
  }

  /**
   * Test convert same currency.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  void testConvertSameCurrency() throws InvalidParameterException {
    String walletId = vacTrackService.createWallet("My Wallet", "USD", BigDecimal.valueOf(100.0));
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, "USD"));
    assertEquals("Cannot convert to the same currency", exception.getMessage());
  }

  /**
   * Test convert invalid currency.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  void testConvertInvalidCurrency() throws InvalidParameterException {
    String walletId = vacTrackService.createWallet("My Wallet", "USD", BigDecimal.valueOf(100.0));
    InvalidExchangeRateException exception = assertThrows(InvalidExchangeRateException.class,
            () -> vacTrackService.convertWalletCurrency(walletId, "INVALID_CURRENCY"));
    assertEquals("Exchange rate not found for USD to INVALID_CURRENCY", exception.getMessage());
  }

  /**
   * Test create wallet.
   */
  @Test
  void testCreateWallet() {
    assertDoesNotThrow(() -> vacTrackService.createWallet(
            "My Wallet", "USD", BigDecimal.valueOf(100.0)));
  }

  /**
   * Test create existing wallet description.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  void testCreateExistingWalletDescription() throws InvalidParameterException {
    vacTrackService.createWallet("My Wallet", "USD", BigDecimal.valueOf(100.0));
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.createWallet("My Wallet", "EUR", BigDecimal.valueOf(200.0)));
    assertEquals("Description already exists", exception.getMessage());
  }

  /**
   * Test create empty currency.
   */
  @Test
  void testCreateEmptyCurrency() {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.createWallet("My Wallet", "", BigDecimal.valueOf(100.0)));
    assertEquals("Invalid currency", exception.getMessage());
  }

  /**
   * Test create negative budget.
   */
  @Test
  void testCreateNegativeBudget() {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class,
            () -> vacTrackService.createWallet("My Wallet", "USD", BigDecimal.valueOf(-100.0)));
    assertEquals("Invalid budget", exception.getMessage());
  }

  /**
   * Create wallet group valid input success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test creating a wallet group with valid parameters")
  void createWalletGroup_ValidInput_Success() throws InvalidParameterException {

    String groupName = "TestGroup";
    BigDecimal initialBudget = new BigDecimal("1000.00");

    GroupWallet groupWallet = vacTrackService.createWalletGroup(groupName, initialBudget);

    assertNotNull(groupWallet);
    assertEquals(groupName, groupWallet.getGroupName());
    assertEquals(initialBudget, groupWallet.getBudget());
  }

  /**
   * Create wallet group invalid group name exception thrown.
   */
  @Test
  @DisplayName("Test creating a wallet group with invalid group name")
  void createWalletGroup_InvalidGroupName_ExceptionThrown() {

    String invalidGroupName = null;
    BigDecimal initialBudget = new BigDecimal("1000.00");

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.createWalletGroup(invalidGroupName, initialBudget));
  }

  /**
   * Create wallet group invalid group name 1 exception thrown.
   */
  @Test
  @DisplayName("Test creating a wallet group with invalid group name")
  void createWalletGroup_InvalidGroupName1_ExceptionThrown() {

    BigDecimal initialBudget = new BigDecimal("1000.00");

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.createWalletGroup("", initialBudget));
  }

  /**
   * Delete expense invalid wallet exception thrown.
   */
  @Test
  @DisplayName("Test deleting an expense for a invalid wallet")
  void deleteExpense_InvalidWallet_ExceptionThrown() {

    String nonExistingWalletId = "nonExistingWalletId";
    String description = "Test Expense";
    BigDecimal amount = new BigDecimal("50.00");

    Expense expense = new Expense(description, amount, LocalDateTime.of(2023, 1, 1, 0, 0));

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.deleteExpense(nonExistingWalletId, expense));
  }

  /**
   * Delete group wallet invalid group exception thrown.
   */
  @Test
  @DisplayName("Test deleting a group wallet with a invalid group id")
  void deleteGroupWallet_InvalidGroup_ExceptionThrown() {

    String nonExistingGroupId = "nonExistingGroupId";

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.deleteGroupWallet(nonExistingGroupId));
  }

  /**
   * Gets wallet groups no groups returns empty list.
   */
  @Test
  @DisplayName("Test getting wallet groups when there are no groups")
  void getWalletGroups_NoGroups_ReturnsEmptyList() {

    List<String> walletGroups = vacTrackService.getWalletGroups();

    assertNotNull(walletGroups);
    assertTrue(walletGroups.isEmpty());
  }

  /**
   * Gets wallet groups with groups returns groups list.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test getting wallet groups with valid parameters")
  void getWalletGroups_WithGroups_ReturnsGroupsList() throws InvalidParameterException {

    String group1 = "Group1";
    String group2 = "Group2";
    BigDecimal initialBudget = new BigDecimal("1000.00");

    vacTrackService.createWalletGroup(group1, initialBudget);
    vacTrackService.createWalletGroup(group2, initialBudget);

    List<String> walletGroups = vacTrackService.getWalletGroups();

    assertNotNull(walletGroups);
    assertTrue(walletGroups.contains(group1));
    assertTrue(walletGroups.contains(group2));
  }

  /**
   * Add wallet to group valid input success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test adding a wallet to group with valid parameters")
  void addWalletToGroup_ValidInput_Success() throws InvalidParameterException {

    String groupName = "Test Group";
    String description = "Test Wallet";
    String currency = "USD";
    BigDecimal initialBudget = BigDecimal.valueOf(50);

    String walletId = vacTrackService.createWallet(description, currency, initialBudget);
    GroupWallet groupWallet = vacTrackService.createWalletGroup(groupName, initialBudget);

    assertDoesNotThrow(() -> vacTrackService.addWalletToGroup(walletId, groupWallet.getGroupId()));

    assertTrue(vacTrackService.getWalletGroups().contains(groupName));
  }

  /**
   * Create expense valid input success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  @DisplayName("Test creating an expense with valid parameters")
  void createExpense_ValidInput_Success()
          throws InvalidParameterException, WalletNotFoundException {

    String walletDescription = "Test Wallet";
    String currency = "USD";
    String expenseDescription = "Test Expense";
    BigDecimal amount = new BigDecimal("50.00");

    String walletId = vacTrackService.createWallet(walletDescription, currency, amount);
    Expense expense = vacTrackService.createExpense(walletId, expenseDescription, amount, null);

    assertNotNull(expense);
    assertEquals(walletId, vacTrackService.getWallet(walletId).getWalletId());
    assertEquals(expenseDescription, expense.getDescription());
    assertEquals(amount, expense.getAmount());
  }



  /**
   * Delete expense valid input success.
   *
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws InvalidParameterException the invalid parameter exception
   */
  @Test
  @DisplayName("Test deleting an expense with valid parameters")
  void deleteExpense_ValidInput_Success()
          throws WalletNotFoundException, InvalidParameterException {

    BigDecimal initialBudget = new BigDecimal("1000.00");
    String description = "Test Expense";
    BigDecimal amount = new BigDecimal("50.00");

    String walletId = vacTrackService.createWallet("Wallet Description", "USD", initialBudget);
    Expense expense = vacTrackService.createExpense(walletId, description, amount, null);

    boolean isDeleted = vacTrackService.deleteExpense(walletId, expense);

    assertTrue(isDeleted);
  }

  /**
   * Remove wallet from group valid input success.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  @DisplayName("Test removing a wallet from a group wallet with valid parameters")
  void removeWalletFromGroup_ValidInput_Success()
          throws InvalidParameterException, WalletNotFoundException {

    String currency = "USD";
    String groupName = "existingGroupName";

    String walletId = vacTrackService.createWallet(
            "Test Wallet", currency, BigDecimal.valueOf(500.00));
    GroupWallet groupWallet = vacTrackService.createWalletGroup(
            groupName, BigDecimal.valueOf(1000.00));
    vacTrackService.addWalletToGroup(walletId, groupWallet.getGroupId());

    assertDoesNotThrow(()
            -> vacTrackService.removeWalletFromGroup(walletId, groupWallet.getGroupId()));
  }

  /**
   * Remove wallet from group valid input failed.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  @DisplayName("Test removing a wallet from a group wallet with valid parameters")
  void removeWalletFromGroup_ValidInput_Failed()
          throws InvalidParameterException, WalletNotFoundException {

    String currency = "USD";
    String groupName = "existingGroupName";

    String walletId = vacTrackService.createWallet(
            "Test Wallet", currency, BigDecimal.valueOf(500.00));
    GroupWallet groupWallet = vacTrackService.createWalletGroup(
            groupName, BigDecimal.valueOf(1000.00));
    vacTrackService.addWalletToGroup(walletId, groupWallet.getGroupId());

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.removeWalletFromGroup("", groupWallet.getGroupId()));
  }

  /**
   * Remove wallet from group valid input failed 1.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  @Test
  @DisplayName("Test removing a wallet from a group wallet with valid parameters")
  void removeWalletFromGroup_ValidInput_Failed1()
          throws InvalidParameterException, WalletNotFoundException {

    String currency = "USD";
    String groupName = "existingGroupName";

    String walletId = vacTrackService.createWallet(
            "Test Wallet", currency, BigDecimal.valueOf(500.00));
    GroupWallet groupWallet = vacTrackService.createWalletGroup(
            groupName, BigDecimal.valueOf(1000.00));
    vacTrackService.addWalletToGroup(walletId, groupWallet.getGroupId());

    assertThrows(InvalidParameterException.class, ()
            -> vacTrackService.removeWalletFromGroup(null, groupWallet.getGroupId()));
  }

  /**
   * Remove wallet from group invalid wallet exception thrown.
   */
  @Test
  @DisplayName("Test removing an invalid wallet from a group wallet")
  void removeWalletFromGroup_InvalidWallet_ExceptionThrown() {

    String nonExistingWalletId = "nonExistingWalletId";
    String groupId = "existingGroupId";

    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.removeWalletFromGroup(nonExistingWalletId, groupId));
  }

  /**
   * Remove wallet from group invalid group exception thrown.
   */
  @Test
  @DisplayName("Test removing a wallet from an invalid group wallet")
  void removeWalletFromGroup_InvalidGroup_ExceptionThrown() {
    // Annahme: Wallet existiert, aber die Gruppe nicht
    String walletId = "existingWalletId";
    String nonExistingGroupId = "nonExistingGroupId";

    // Versuche, ein Wallet aus einer nicht vorhandenen Gruppe zu entfernen
    assertThrows(WalletNotFoundException.class, ()
            -> vacTrackService.removeWalletFromGroup(walletId, nonExistingGroupId));
  }
}