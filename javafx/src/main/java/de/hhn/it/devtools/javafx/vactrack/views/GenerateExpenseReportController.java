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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * The type Generate expense report controller.
 */
public class GenerateExpenseReportController {
  @FXML
  private Button backToDashboard;
  @FXML
  private Pane pane;
  @FXML
  private TextArea expenseReportTextArea;
  @FXML
  private Button generate;
  @FXML
  private DatePicker datePickerFrom;
  @FXML
  private DatePicker datePickerTo;

  private DashboardWalletViewModel dashboardWalletViewModel;


  /**
   * Initialize.
   *
   * @param dashboardWalletViewModel the dashboard wallet view model
   */
  public void initialize(DashboardWalletViewModel dashboardWalletViewModel) {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    datePickerFrom.valueProperty()
            .bindBidirectional(dashboardWalletViewModel.datePickerFromProperty());
    datePickerTo.valueProperty()
            .bindBidirectional(dashboardWalletViewModel.datePickerToProperty());
    expenseReportTextArea.textProperty()
            .bindBidirectional(dashboardWalletViewModel.expenseReport());

  }

  /**
   * Generate on action.
   *
   * @param event the event
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws IOException               the io exception
   */
  public void generateOnAction(ActionEvent event)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    dashboardWalletViewModel.handleGenerateExpenseReport();

  }

  /**
   * Back to dashboard.
   *
   * @param event the event
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws IOException               the io exception
   */
  public void backToDashboard(ActionEvent event)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    reloadDashboardView(dashboardWalletViewModel, dashboardWalletViewModel.getSelectedId());
  }


  /**
   * Reload dashboard view.
   *
   * @param dashboardWalletViewModel the dashboard wallet view model
   * @param id                       the id
   * @throws IOException               the io exception
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  public void reloadDashboardView(DashboardWalletViewModel dashboardWalletViewModel, String id)
          throws IOException, InvalidParameterException, WalletNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass()
            .getResource("/fxml/vactrack/DashboardWallet.fxml"));
    Parent dashboardView = loader.load();
    DashboardWalletController dashboardController = loader.getController();
    dashboardController.initialize(dashboardWalletViewModel, id);
    dashboardController.updateDashboardView(dashboardWalletViewModel, id);


    pane.getChildren().clear();
    pane.getChildren().addAll(dashboardView);

  }
}
