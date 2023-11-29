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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


/**
 * The type Create expense controller.
 */
public class CreateExpenseController {
  @FXML
  private DatePicker datePickerForExpense;
  @FXML
  private CheckBox takeCurrentDate;
  @FXML
  private TextField timeField;
  @FXML
  private Text budgetError;
  @FXML
  private Pane pane;
  @FXML
  private Button enter;
  @FXML
  private TextField description;
  @FXML
  private TextField amount;

  private DashboardWalletViewModel dashboardWalletViewModel;


  /**
   * Initialize.
   *
   * @param dashboardWalletViewModel the dashboard wallet view model
   */
  public void initialize(DashboardWalletViewModel dashboardWalletViewModel) {
    this.dashboardWalletViewModel = dashboardWalletViewModel;
    description.textProperty().bindBidirectional(
            dashboardWalletViewModel.expenseDescriptionProperty());
    budgetError.textProperty().bind(dashboardWalletViewModel.budgetErrorProperty());
    amount.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        dashboardWalletViewModel.expenseAmount().set(new BigDecimal(newValue));
      }
    });
    datePickerForExpense
            .valueProperty().bindBidirectional(dashboardWalletViewModel.datePickerForExpense());
    takeCurrentDate.selectedProperty()
            .bindBidirectional(dashboardWalletViewModel.takeCurrentDateProperty());
    timeField.textProperty().bindBidirectional(dashboardWalletViewModel.timeFieldProperty());

  }

  /**
   * Enter on action.
   *
   * @param event the event
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws IOException               the io exception
   */
  public void enterOnAction(ActionEvent event)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    dashboardWalletViewModel.handleCreateExpenseAction();
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
