package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardWalletViewModel;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


/**
 * The type Dashboard wallet controller.
 */
public class DashboardWalletController {

  @FXML
  private Button delete;
  @FXML
  private Text expenseRemaining;
  @FXML
  private ListView expensesList;
  @FXML
  private AnchorPane controlAnchorPane;
  @FXML
  private Text currency;
  @FXML
  private Button generateExpenseReport;

  @FXML
  private Button setExpenseLimit;

  @FXML
  private Button addWallet;

  @FXML
  private Button removeWallet;

  @FXML
  private Button transferFunds;

  @FXML
  private Button deleteWallet;

  @FXML
  private Button convertCurrency;

  @FXML
  private Button enterExchangeRate;

  @FXML
  private Button enterExpense;

  @FXML
  private Text balance;

  @FXML
  private Text expenses;

  @FXML
  private Label description;


  private DashboardWalletViewModel dashboardWalletViewModel;


  /**
   * Initialize.
   *
   * @param dashboardWalletViewModel the dashboard wallet view model
   * @param id                       the id
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  public void initialize(DashboardWalletViewModel dashboardWalletViewModel, String id)
          throws InvalidParameterException, WalletNotFoundException {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    System.out.println("DashboardWalletViewModel initialized: " + dashboardWalletViewModel);
    description.textProperty().bind(dashboardWalletViewModel.descriptionProperty());
    currency.textProperty().bind(dashboardWalletViewModel.currencyProperty());
    balance.textProperty().bind(dashboardWalletViewModel.balanceProperty().asString());
    expenses.textProperty().bind(dashboardWalletViewModel.expensesProperty().asString());
    expensesList.itemsProperty().bind(dashboardWalletViewModel.expensesListProperty());
    expenseRemaining.textProperty().bind(dashboardWalletViewModel.remainingExpenseLimitProperty());
    expenseRemaining.fillProperty().bind(dashboardWalletViewModel.limitColorProperty());
    dashboardWalletViewModel.updateDataFromService(id);
  }



  @FXML
  private void generateExpenseReportOnAction() throws IOException {
    loadGenerateExpenseReport();


  }

  @FXML
  private void setExpenseLimitOnAction() throws IOException {
    loadSetDailyLimitScene();

  }


  @FXML
  private void transferFundsOnAction() throws IOException {
    loadTransferFundsScene();
  }


  @FXML
  private void convertCurrencyOnAction() throws IOException {
    loadConvertWalletScene();

  }


  @FXML
  private void enterExpenseOnAction() throws IOException {
    loadCreateExpenseScene();
  }
  @FXML
  public void deleteExpenseOnAction(ActionEvent event) throws IOException {
    loadDeleteExpenseScene();
  }

  private void loadConvertWalletScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/ConvertCurrency.fxml"));
    Parent convertCurrency = loader.load();

    ConvertWalletController convertWalletCurrencyController = loader.getController();
    convertWalletCurrencyController.initialize(dashboardWalletViewModel);


    controlAnchorPane.getChildren().setAll(convertCurrency);
  }

  private void loadSetDailyLimitScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/SetDailyExpenseLimit.fxml"));
    Parent dailyLimit = loader.load();

    SetDailyExpenseController setDaily = loader.getController();
    setDaily.initialize(dashboardWalletViewModel);


    controlAnchorPane.getChildren().setAll(dailyLimit);
  }



  private void loadCreateExpenseScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vactrack/CreateExpense.fxml"));
    Parent createExpense = loader.load();

    CreateExpenseController createExpenseController = loader.getController();
    createExpenseController.initialize(dashboardWalletViewModel);


    controlAnchorPane.getChildren().setAll(createExpense);
  }


  private void loadGenerateExpenseReport() throws IOException {
    FXMLLoader loader =
            new FXMLLoader(getClass().getResource("/fxml/vactrack/GenerateExpenseReport.fxml"));
    Parent generateExpenseReport = loader.load();
    GenerateExpenseReportController generateExpenseReportController = loader.getController();
    generateExpenseReportController.initialize(dashboardWalletViewModel);
    controlAnchorPane.getChildren().setAll(generateExpenseReport);
  }

  private void loadTransferFundsScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vactrack/TransferFunds.fxml"));
    Parent transferFundsView = loader.load();
    TransferFundsController transferFundsController = loader.getController();
    transferFundsController.initialize(dashboardWalletViewModel);
    controlAnchorPane.getChildren().setAll(transferFundsView);
  }

  private void loadDeleteExpenseScene() throws IOException{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vactrack/DeleteExpense.fxml"));
    Parent deleteExpenseView = loader.load();
    DeleteExpenseController deleteExpenseController = loader.getController();
    deleteExpenseController.initialize(dashboardWalletViewModel);
    controlAnchorPane.getChildren().setAll(deleteExpenseView);

  }

  /**
   * Update dashboard view.
   *
   * @param viewModel the view model
   * @param id        the id
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  public void updateDashboardView(DashboardWalletViewModel viewModel, String id)
          throws InvalidParameterException, WalletNotFoundException {
    this.dashboardWalletViewModel = viewModel;
    dashboardWalletViewModel.updateDataFromService(id);
  }



}
