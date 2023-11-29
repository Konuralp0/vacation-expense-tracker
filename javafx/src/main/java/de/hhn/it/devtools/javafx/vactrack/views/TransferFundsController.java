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
 * The type Transfer funds controller.
 */
public class TransferFundsController {
  @FXML
  private TextField xchangeRate;
  /**
   * The Pane.
   */
  @FXML
  public Pane pane;
  @FXML
  private TextField toTextField;
  @FXML
  private Button transfer;
  @FXML
  private TextField transferAmount;

  private DashboardWalletViewModel dashboardWalletViewModel;

  /**
   * Initialize.
   *
   * @param dashboardWalletViewModel the dashboard wallet view model
   */
  public void initialize(DashboardWalletViewModel dashboardWalletViewModel) {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    toTextField.textProperty().bindBidirectional(dashboardWalletViewModel.walletToProperty());
    transferAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        dashboardWalletViewModel.amountProperty().set(new BigDecimal(newValue));
      }
    });
    xchangeRate.textProperty().bindBidirectional(dashboardWalletViewModel.xchangeRateProperty());

  }

  /**
   * Transfer on action.
   *
   * @param event the event
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws IOException               the io exception
   */
  public void transferOnAction(ActionEvent event)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    dashboardWalletViewModel.handleTransferFund();
    reloadDashboardView();
  }

  /**
   * Reload dashboard view.
   *
   * @throws IOException               the io exception
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   */
  public void reloadDashboardView()
          throws IOException, InvalidParameterException, WalletNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/DashboardWallet.fxml"));
    final Parent dashboardView = loader.load();

    DashboardWalletController dashboardController = loader.getController();
    System.out.println(
            "################################################################################"
                    + dashboardWalletViewModel + "##########################");
    dashboardController.initialize(dashboardWalletViewModel, dashboardWalletViewModel
            .getSelectedId());
    dashboardController.updateDashboardView(dashboardWalletViewModel,
            dashboardWalletViewModel.getSelectedId());

    pane.getChildren().clear();

    pane.getChildren().addAll(dashboardView);


  }
}
