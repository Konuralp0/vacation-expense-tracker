package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.Expense;
import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardWalletViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DeleteExpenseController {
  public Pane pane;
  @FXML
  private Button delete;
  @FXML
  private ComboBox dropBoxExpenses;

  private DashboardWalletViewModel dashboardWalletViewModel;


  public void initialize(DashboardWalletViewModel dashboardWalletViewModel) {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    dropBoxExpenses.itemsProperty().bindBidirectional(dashboardWalletViewModel.expensesListForDeletingProperty());

  }
  public void deleteOnAction(ActionEvent event) throws InvalidParameterException, WalletNotFoundException, IOException {
    int index = dropBoxExpenses.getSelectionModel().getSelectedIndex();
    Expense expense = (Expense) dropBoxExpenses.getItems().get(index);
    dashboardWalletViewModel.handleDeleteExpense(expense);
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
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/DashboardWallet.fxml"));
    final Parent dashboardView = loader.load();


    DashboardWalletController dashboardController = loader.getController();
    System.out.println(
            "################################################################################"
                    + dashboardWalletViewModel + "##########################");
    dashboardController.initialize(dashboardWalletViewModel, id);
    dashboardController.updateDashboardView(dashboardWalletViewModel, id);

    pane.getChildren().clear();

    pane.getChildren().addAll(dashboardView);




  }


}
