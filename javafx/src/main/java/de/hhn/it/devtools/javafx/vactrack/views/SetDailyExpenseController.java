package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardWalletViewModel;
import java.io.IOException;
import java.math.BigDecimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * The type Set daily expense controller.
 */
public class SetDailyExpenseController {

  /**
     * The Pane.
     */
  public Pane pane;
  @FXML
    private Button set;
  @FXML
    private TextField limit;

  private DashboardWalletViewModel dashboardWalletViewModel;

  /**
     * Initialize.
     *
     * @param dashboardWalletViewModel the dashboard wallet view model
     */
  public void initialize(DashboardWalletViewModel dashboardWalletViewModel) {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    limit.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        dashboardWalletViewModel.limitProperty().set(new BigDecimal(newValue));
      }
    });
  }

  /**
     * Sets on action.
     *
     * @param actionEvent the action event
     * @throws InvalidParameterException the invalid parameter exception
     * @throws WalletNotFoundException   the wallet not found exception
     * @throws IOException               the io exception
     */
  public void setOnAction(ActionEvent actionEvent)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    dashboardWalletViewModel.handleSetLimit();
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
