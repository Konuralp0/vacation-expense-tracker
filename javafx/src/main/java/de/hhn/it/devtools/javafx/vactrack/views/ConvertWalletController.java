package de.hhn.it.devtools.javafx.vactrack.views;


import de.hhn.it.devtools.apis.vactrack.InvalidExchangeRateException;
import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardWalletViewModel;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * The type Convert wallet controller.
 */
public class ConvertWalletController {

  @FXML
    private Pane pane;
  @FXML
    private Button convert;
  @FXML
    private TextField currencyToTextField;
  @FXML
    private TextField exchangeRateField;

  private DashboardWalletViewModel dashboardWalletViewModel;

  /**
     * Convert on action.
     *
     * @param actionEvent the action event
     * @throws InvalidParameterException    the invalid parameter exception
     * @throws WalletNotFoundException      the wallet not found exception
     * @throws InvalidExchangeRateException the invalid exchange rate exception
     * @throws IOException                  the io exception
     */
  public void convertOnAction(ActionEvent actionEvent) throws
          InvalidParameterException, WalletNotFoundException,
          InvalidExchangeRateException, IOException {
    dashboardWalletViewModel.convertWalletHandler();
    reloadDashboardView(dashboardWalletViewModel, dashboardWalletViewModel.getSelectedId());
  }

  /**
     * Initialize.
     *
     * @param dashboardWalletViewModel the dashboard wallet view model
     */
  public void initialize(DashboardWalletViewModel dashboardWalletViewModel) {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    currencyToTextField.textProperty()
            .bindBidirectional(dashboardWalletViewModel.currencyToForConvertProperty());
    exchangeRateField.textProperty()
            .bindBidirectional(dashboardWalletViewModel.exchangeRateForConvertProperty());
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
