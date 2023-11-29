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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * The type Add wallet to group controller.
 */
public class AddWalletToGroupControlle {

  public ComboBox walletsDropdown;
  @FXML
  private Pane pane;
  @FXML
  private Button add;

  private DashboardGroupWalletViewModel dashboardGroupWalletViewModel;

  /**
     * Initialize.
     *
     * @param dashboardGroupWalletViewModel the dashboard group wallet view model
     */
  public void initialize(DashboardGroupWalletViewModel dashboardGroupWalletViewModel) {
    this.dashboardGroupWalletViewModel = dashboardGroupWalletViewModel;
    walletsDropdown.itemsProperty().bindBidirectional(dashboardGroupWalletViewModel.walletsToAddProperty());
  }

  /**
     * Add on action.
     *
     * @param actionEvent the action event
     * @throws InvalidParameterException the invalid parameter exception
     * @throws WalletNotFoundException   the wallet not found exception
     * @throws IOException               the io exception
     */
  public void addOnAction(ActionEvent actionEvent)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    int index = walletsDropdown.getSelectionModel().getSelectedIndex();
    String description =  walletsDropdown.getItems().get(index).toString();
    dashboardGroupWalletViewModel.handleAddWallet(description);
    //dashboardGroupWalletViewModel.updateDataFromService(dashboardGroupWalletViewModel.getSelectedId());
    reloadDashboardView(dashboardGroupWalletViewModel,
            dashboardGroupWalletViewModel.getSelectedId());
  }


  /**
     * Reload dashboard view.
     *
     * @param dashboardGroupWalletViewModel the dashboard group wallet view model
     * @param id                            the id
     * @throws IOException               the io exception
     * @throws InvalidParameterException the invalid parameter exception
     * @throws WalletNotFoundException   the wallet not found exception
     */
  public void reloadDashboardView(DashboardGroupWalletViewModel dashboardGroupWalletViewModel,
                                  String id) throws IOException,
          InvalidParameterException, WalletNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/vactrack/DashboardGroupWallet.fxml"));
    final Parent dashboardView = loader.load();
    DashboardGroupWalletController dashboardController = loader.getController();
    dashboardController.initialize(dashboardGroupWalletViewModel, id);
    dashboardController.updateDashboardView(dashboardGroupWalletViewModel, id);
    pane.getChildren().clear();
    pane.getChildren().addAll(dashboardView);
  }
}
