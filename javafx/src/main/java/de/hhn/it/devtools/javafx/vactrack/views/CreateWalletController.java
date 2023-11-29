package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.controllers.Controller;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.CreateWalletViewModel;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardWalletViewModel;
import java.io.IOException;
import java.math.BigDecimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/**
 * The type Create wallet controller.
 */
public class CreateWalletController extends Controller {
  @FXML
  private Pane createWalletPane;

  @FXML
  private Button createWalletButton;

  @FXML
  private TextField descriptionTextField;

  @FXML
  private TextField initialBudgetTextField;

  @FXML
  private TextField currencyTextField;

  private CreateWalletViewModel createWalletViewModel;

  private DashboardWalletViewModel dashboardWalletViewModel;

  /**
   * Initialize.
   *
   * @param viewModel                the view model
   * @param dashboardWalletViewModel the dashboard wallet view model
   */
  @FXML
  public void initialize(CreateWalletViewModel viewModel,
                         DashboardWalletViewModel dashboardWalletViewModel) {
    this.createWalletViewModel = viewModel;
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    descriptionTextField.textProperty().bindBidirectional(viewModel.descriptionProperty());
    currencyTextField.textProperty().bindBidirectional(viewModel.currencyProperty());
    initialBudgetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        viewModel.budgetProperty().set(new BigDecimal(newValue));
      }
    });


  }

  @FXML
  private void createWalletOnAction(ActionEvent event)
          throws InvalidParameterException, IOException, WalletNotFoundException {
    createWalletViewModel.createWallet();
    loadDashboard();
  }

  private void loadDashboard()
          throws IOException, InvalidParameterException, WalletNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/vactrack/DashboardWallet.fxml"));
    Parent dashboardView = loader.load();


    DashboardWalletController dashboardController = loader.getController();
    dashboardController.initialize(dashboardWalletViewModel, createWalletViewModel.getWalletId());


    createWalletPane.getChildren().setAll(dashboardView);
  }

}
