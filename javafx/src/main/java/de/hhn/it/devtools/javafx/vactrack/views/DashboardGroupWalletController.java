package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardGroupWalletViewModel;
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
 * The type Dashboard group wallet controller.
 */
public class DashboardGroupWalletController {

  @FXML
  private Button generateExpenseReport;
  @FXML
    private AnchorPane controlAnchorPane;
  @FXML
    private Button addWalletToGroup;
  @FXML
    private Button removeWallet;
  @FXML
    private Button convertCurrency;

  @FXML
    private Text expenses;
  @FXML
    private Text currency;
  @FXML
    private Label description;
  @FXML
    private ListView memberWallets;

  private DashboardGroupWalletViewModel dashboardGroupWalletViewModel;

  /**
     * Initialize.
     *
     * @param dashboardGroupWalletViewModel the dashboard group wallet view model
     * @param id                            the id
     * @throws InvalidParameterException the invalid parameter exception
     * @throws WalletNotFoundException   the wallet not found exception
     */
  public void initialize(DashboardGroupWalletViewModel dashboardGroupWalletViewModel,
                           String id) throws InvalidParameterException, WalletNotFoundException {
    this.dashboardGroupWalletViewModel = dashboardGroupWalletViewModel;
    description.textProperty().bind(dashboardGroupWalletViewModel.descriptionProperty());
    currency.textProperty().bind(dashboardGroupWalletViewModel.currencyProperty());
    //balance.textProperty().bind(dashboardGroupWalletViewModel.balanceProperty().asString());
    expenses.textProperty().bind(dashboardGroupWalletViewModel.expensesProperty());
    memberWallets.itemsProperty()
            .bindBidirectional(dashboardGroupWalletViewModel.memberWalletsProperty());
    dashboardGroupWalletViewModel.updateDataFromService(id);
  }

  /**
     * Add wallet to group on action.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
  public void addWalletToGroupOnAction(ActionEvent actionEvent) throws IOException {
    loadAddWalletScene();
  }

  /**
     * Remove wallet on action.
     *
     * @param actionEvent the action event
     */
  public void removeWalletOnAction(ActionEvent actionEvent) throws IOException {
    loadDeleteWalletScene();
  }



  /**
     * Update dashboard view.
     *
     * @param viewModel the view model
     * @param id        the id
     * @throws InvalidParameterException the invalid parameter exception
     * @throws WalletNotFoundException   the wallet not found exception
     */
  public void updateDashboardView(DashboardGroupWalletViewModel viewModel, String id)
            throws InvalidParameterException, WalletNotFoundException {
    this.dashboardGroupWalletViewModel = viewModel;
    dashboardGroupWalletViewModel.updateDataFromService(id);
  }

  public void generateOnAction(ActionEvent actionEvent) throws IOException {
    loadExpenseReportScene();
  }

  /**
     * Load add wallet scene.
     *
     * @throws IOException the io exception
     */
  public void loadAddWalletScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/vactrack/AddWalletToGroup.fxml"));
    final Parent addWalletView = loader.load();

    AddWalletToGroupControlle addWalletController = loader.getController();

    addWalletController.initialize(dashboardGroupWalletViewModel);
    controlAnchorPane.getChildren().setAll(addWalletView);
  }

  public void loadDeleteWalletScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/RemoveWalletFromGroup.fxml"));
    final Parent deleteWalletView = loader.load();

    RemoveWalletFromGroupController removeWallet = loader.getController();


    removeWallet.initialize(dashboardGroupWalletViewModel);
    controlAnchorPane.getChildren().setAll(deleteWalletView);

  }

  public void loadExpenseReportScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/GenerateExpenseReportForGroup.fxml"));
    final Parent generateExpenseReportView = loader.load();

    GenerateExpenseReportGroupController generateExpenseReportController = loader.getController();

    generateExpenseReportController.initialize(dashboardGroupWalletViewModel);
    controlAnchorPane.getChildren().setAll(generateExpenseReportView);
  }


}
