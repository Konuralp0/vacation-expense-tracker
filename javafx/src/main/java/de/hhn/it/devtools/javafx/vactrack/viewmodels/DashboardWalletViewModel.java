package de.hhn.it.devtools.javafx.vactrack.viewmodels;


import de.hhn.it.devtools.apis.vactrack.*;
import de.hhn.it.devtools.javafx.vactrack.views.DashboardWalletController;
import de.hhn.it.devtools.javafx.vactrack.views.EnterExchangeRateController;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;


/**
 * The type Dashboard wallet view model.
 */
public class DashboardWalletViewModel {

  //for dashboard
  private final StringProperty description = new SimpleStringProperty();
  private final StringProperty currency = new SimpleStringProperty();
  private final ObjectProperty<BigDecimal> balance = new SimpleObjectProperty<>();
  private final ObjectProperty<BigDecimal> expenses = new SimpleObjectProperty<>();

  // For Entering Exchange Rate
  private final StringProperty currencyFrom = new SimpleStringProperty();
  private final StringProperty currencyTo = new SimpleStringProperty();
  private final StringProperty exchangeRate = new SimpleStringProperty();

  //For creating Expense

  private final ListProperty<String> expensesList =
          new SimpleListProperty<>(FXCollections.observableArrayList());

  private final StringProperty expenseDescription = new SimpleStringProperty();
  private final ObjectProperty<BigDecimal> expenseAmount = new SimpleObjectProperty<>();

  private final StringProperty budgetError = new SimpleStringProperty();

  private final ObjectProperty<LocalDate> datePickerForExpense = new SimpleObjectProperty<>();
  private final BooleanProperty takeCurrentDateProperty = new SimpleBooleanProperty();

  private final StringProperty timeFieldProperty = new SimpleStringProperty();

  // for generating expense report
  private final ObjectProperty<LocalDate> datePickerFromProperty = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> datePickerToProperty = new SimpleObjectProperty<>();
  StringProperty expenseReport = new SimpleStringProperty();

  // for converting wallet currency

  private final StringProperty currencyToForConvertProperty = new SimpleStringProperty();
  private final StringProperty exchangeRateForConvertProperty = new SimpleStringProperty();

  //for setting daily expense limit
  private final ObjectProperty<BigDecimal> limit = new SimpleObjectProperty<>();
  private final StringProperty remainingExpenseLimit = new SimpleStringProperty();
  private final SimpleObjectProperty<Color> limitColor = new SimpleObjectProperty<>(Color.GREEN);

  //for deleting expense
   private final ListProperty<Expense> expensesListForDeleting =
          new SimpleListProperty<>(FXCollections.observableArrayList());






  //for transfering funds
  private final StringProperty walletTo = new SimpleStringProperty();
  private final ObjectProperty<BigDecimal> amount = new SimpleObjectProperty<>();
  private final StringProperty xchangeRate = new SimpleStringProperty();



  private String selectedId;

  private final VacTrackService vacTrackService;

  /**
   * Instantiates a new Dashboard wallet view model.
   *
   * @param vacTrackService             the vac track service
   * @param dashboardWalletController   the dashboard wallet controller
   * @param enterExchangeRateController the enter exchange rate controller
   */
  public DashboardWalletViewModel(VacTrackService vacTrackService,
                                  DashboardWalletController dashboardWalletController,
                                  EnterExchangeRateController enterExchangeRateController) {
    this.vacTrackService = vacTrackService;
  }
  /**
   * Update data from service.
   *
   * @param walletId the wallet id
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  public void updateDataFromService(String walletId)
          throws InvalidParameterException, WalletNotFoundException {

    this.selectedId = walletId;
    Wallet wallet = vacTrackService.getWallet(walletId);
    BigDecimal balanceValue = wallet.getWalletBalance().getBalance();
    BigDecimal expensesValue = wallet.getExpenseList().getTotalExpenses();
    String currencyValue = wallet.getCurrency();
    final String descriptionValue = wallet.getDescription();
    BigDecimal walletLimit = wallet.getDailyExpenseLimit();


    setBalance(balanceValue);
    setExpenses(expensesValue);
    setCurrency(currencyValue);
    setDescription(descriptionValue);
    if (walletLimit.doubleValue() != 0.0) {

      remainingExpenseLimit.set(String.valueOf((walletLimit.subtract(expensesValue))));
      if (new BigDecimal(remainingExpenseLimit.get()).doubleValue()<=0) {
        limitColor.set(Color.RED);
      } else {
        limitColor.set(Color.GREEN);
      }
    }else {
      remainingExpenseLimit.set("");
    }

    ExpenseReport expenseReport = vacTrackService.getExpenseReport(walletId,
            LocalDateTime.of(2023, 9, 15, 0, 0),
            LocalDateTime.of(2024, 3, 1, 0, 0));
    expensesList.setAll(vacTrackService.generatePrintableReport(expenseReport));
    //List<String> expenseNames = new ArrayList<>();
    //for (Expense expense : vacTrackService.getWallet(selectedId).getExpenseList().getExpenses()){
    //  expenseNames.add(expense.getDescription());
    //}
    expensesListForDeleting.setAll(vacTrackService.getWallet(selectedId).getExpenseList().getExpenses());
  }


  /**
   * Description property string property.
   *
   * @return the string property
   */
  public StringProperty descriptionProperty() {
    return description;
  }

  /**
   * Currency property string property.
   *
   * @return the string property
   */
  public StringProperty currencyProperty() {
    return currency;
  }

  /**
   * Balance property object property.
   *
   * @return the object property
   */
  public ObjectProperty<BigDecimal> balanceProperty() {
    return balance;
  }

  /**
   * Expenses property object property.
   *
   * @return the object property
   */
  public ObjectProperty<BigDecimal> expensesProperty() {
    return expenses;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description.get();
  }

  /**
   * Sets description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description.set(description);
  }

  /**
   * Gets currency.
   *
   * @return the currency
   */
  public String getCurrency() {
    return currency.get();
  }

  /**
   * Sets currency.
   *
   * @param currency the currency
   */
  public void setCurrency(String currency) {
    this.currency.set(currency);
  }

  /**
   * Gets balance.
   *
   * @return the balance
   */
  public BigDecimal getBalance() {
    return balance.get();
  }

  /**
   * Sets balance.
   *
   * @param balance the balance
   */
  public void setBalance(BigDecimal balance) {
    this.balance.set(balance);
  }

  /**
   * Gets expenses.
   *
   * @return the expenses
   */
  public BigDecimal getExpenses() {
    return expenses.get();
  }

  /**
   * Sets expenses.
   *
   * @param expenses the expenses
   */
  public void setExpenses(BigDecimal expenses) {
    this.expenses.set(expenses);
  }

  /**
   * Currency from property string property.
   *
   * @return the string property
   */
  public StringProperty currencyFromProperty() {
    return currencyFrom;
  }

  /**
   * Currency to property string property.
   *
   * @return the string property
   */
  public StringProperty currencyToProperty() {
    return currencyTo;
  }

  /**
   * Exchange rate string property.
   *
   * @return the string property
   */
  public StringProperty exchangeRate() {
    return exchangeRate;
  }




  /**
   * Gets exchange rate.
   *
   * @return the exchange rate
   */
  public double getExchangeRate(SimpleStringProperty exchangeRate) {
    try {
      return Double.parseDouble(exchangeRate.get());
    } catch (NumberFormatException e) {

      e.printStackTrace();
      return 0.0;
    }
  }

  /**
   * Gets selected id.
   *
   * @return the selected id
   */
  public String getSelectedId() {
    return selectedId;
  }

  /**
   * Handle exchange rate action.
   *
   * @throws InvalidParameterException    the invalid parameter exception
   * @throws InvalidExchangeRateException the invalid exchange rate exception
   */
  public void handleExchangeRateAction()
          throws InvalidParameterException, InvalidExchangeRateException {
    vacTrackService.enterExchangeRate(currencyFrom.getValue(),
            currencyTo.getValue(), getExchangeRate((SimpleStringProperty) exchangeRate));



  }


  /**
   * Handle create expense action.
   *
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  public void handleCreateExpenseAction()
          throws InvalidParameterException, WalletNotFoundException {

    if (takeCurrentDateProperty.get()) {
      vacTrackService.createExpense(selectedId, expenseDescription.get(), expenseAmount.get(),null);
    }else {
      String hour = timeFieldProperty.get().substring(0,timeFieldProperty.get().indexOf(":"));
      String minute = timeFieldProperty.get().substring(timeFieldProperty.get().indexOf(":")+1);
      LocalTime localTime = LocalTime.of(Integer.parseInt(hour),Integer.parseInt(minute));
      LocalDateTime localDateTime = LocalDateTime.of(datePickerForExpense.get(),localTime);
      vacTrackService.createExpense(selectedId,expenseDescription.get(),expenseAmount.get(),localDateTime);
    }


  }
  public void handleSetLimit() throws InvalidParameterException, WalletNotFoundException {
    BigDecimal limit = limitProperty().get();
    BigDecimal totalExpenses = vacTrackService.getWallet(selectedId).getExpenseList().getTotalExpenses();
    vacTrackService.setDailyExpenseLimit(selectedId,limit.add(totalExpenses));



  }

  /**
   * Expense description property string property.
   *
   * @return the string property
   */
  public StringProperty expenseDescriptionProperty() {
    return expenseDescription;
  }

  /**
   * Expense amount object property.
   *
   * @return the object property
   */
  public ObjectProperty<BigDecimal> expenseAmount() {
    return expenseAmount;
  }

  /**
   * Budget error property string property.
   *
   * @return the string property
   */
  public StringProperty budgetErrorProperty() {
    return budgetError;
  }

  /**
   * Expenses list property list property.
   *
   * @return the list property
   */
  public ListProperty<String> expensesListProperty() {
    return expensesList;
  }

  public ObjectProperty<LocalDate> datePickerFromProperty() {
    return datePickerFromProperty;
  }
  public ObjectProperty<LocalDate> datePickerToProperty() {
    return datePickerToProperty;
  }

  public StringProperty expenseReport(){

    return expenseReport;
  }

  public void setExpenseReport(String expenseReport) {
    this.expenseReport.set(expenseReport);
  }

  public void handleGenerateExpenseReport() throws InvalidParameterException, WalletNotFoundException {
    ExpenseReport expenseReport = vacTrackService.getExpenseReport(selectedId, datePickerFromProperty.get().atStartOfDay(),datePickerToProperty.get().atStartOfDay());
    setExpenseReport(vacTrackService.generatePrintableReport(expenseReport));
  }

  public StringProperty timeFieldProperty() {


    return timeFieldProperty;
  }


  public ObjectProperty<LocalDate> datePickerForExpense() {
    return datePickerForExpense;
  }

  public BooleanProperty takeCurrentDateProperty(){
    return takeCurrentDateProperty;
  }

  public StringProperty currencyToForConvertProperty(){
    return currencyToForConvertProperty;
  }

  public StringProperty exchangeRateForConvertProperty(){
    return exchangeRateForConvertProperty;
  }

  public void convertWalletHandler() throws InvalidParameterException, WalletNotFoundException, InvalidExchangeRateException {
    Wallet wallet = vacTrackService.getWallet(selectedId);

    vacTrackService.enterExchangeRate(wallet.getCurrency(), currencyToForConvertProperty.get(),getExchangeRate((SimpleStringProperty) exchangeRateForConvertProperty));
    vacTrackService.convertWalletCurrency(selectedId,currencyToForConvertProperty.get());
    currencyToForConvertProperty.set("");
    exchangeRateForConvertProperty.set("");
  }


  public ObjectProperty<BigDecimal> limitProperty() {
    return limit;
  }
  public SimpleObjectProperty<Color> limitColorProperty() {
    return limitColor;
  }

  public void setRemainingExpenseLimit(String remainingExpenseLimit) {
    this.remainingExpenseLimit.set(remainingExpenseLimit);
  }

  public StringProperty remainingExpenseLimitProperty() {
    return remainingExpenseLimit;
  }



  public StringProperty walletToProperty() {
    return walletTo;
  }



  public ObjectProperty<BigDecimal> amountProperty() {
    return amount;
  }

  public StringProperty xchangeRateProperty() {
    return xchangeRate;
  }



  public ListProperty<Expense> expensesListForDeletingProperty() {
    return expensesListForDeleting;
  }



  public void handleTransferFund() throws InvalidParameterException, WalletNotFoundException {
    String idToWallet = vacTrackService.getIdByDescription(walletTo.get());
    if (xchangeRateProperty().get() == null){
    vacTrackService.transferFunds(selectedId,idToWallet,amountProperty().get(), 1.0D);
    }else {
      vacTrackService.transferFunds(selectedId,idToWallet,amountProperty().get(),getExchangeRate((SimpleStringProperty) xchangeRate));
    }
  }

  public void handleDeleteExpense(Expense expense) throws InvalidParameterException, WalletNotFoundException {
    vacTrackService.deleteExpense(selectedId,expense);
  }
}

