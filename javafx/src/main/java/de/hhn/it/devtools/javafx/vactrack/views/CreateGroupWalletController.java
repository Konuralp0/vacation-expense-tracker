package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.CreateGroupWalletViewModel;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardGroupWalletViewModel;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


/**
 * The type Create group wallet controller.
 */
public class CreateGroupWalletController {
  @FXML
    private Pane pane;
  @FXML
    private Button createWalletGroup;
  @FXML
    private TextField groupName;

  private CreateGroupWalletViewModel createGroupWalletViewModel;
  private DashboardGroupWalletViewModel dashboardGroupWalletViewModel;

  /**
     * Initialize.
     *
     * @param createGroupWalletViewModel    the create group wallet view model
     * @param dashboardGroupWalletViewModel the dashboard group wallet view model
     */
  public void initialize(CreateGroupWalletViewModel createGroupWalletViewModel,
                         DashboardGroupWalletViewModel dashboardGroupWalletViewModel) {
    groupName.textProperty()
            .bindBidirectional(createGroupWalletViewModel.groupNamePropertyProperty());
    this.createGroupWalletViewModel = createGroupWalletViewModel;
    this.dashboardGroupWalletViewModel = dashboardGroupWalletViewModel;
  }

  /**
     * Create wallet group on action.
     *
     * @param actionEvent the action event
     * @throws InvalidParameterException the invalid parameter exception
     * @throws WalletNotFoundException   the wallet not found exception
     * @throws IOException               the io exception
     */
  public void createWalletGroupOnAction(ActionEvent actionEvent) throws
          InvalidParameterException, WalletNotFoundException, IOException {
    createGroupWalletViewModel.handleCreate();
    loadDashboard();
  }

  private void loadDashboard()
            throws IOException, InvalidParameterException, WalletNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/vactrack/DashboardGroupWallet.fxml"));
    Parent dashboardView = loader.load();
    DashboardGroupWalletController dashboardController = loader.getController();
    dashboardController.initialize(dashboardGroupWalletViewModel,
            createGroupWalletViewModel.getGroupWalletId());
    pane.getChildren().setAll(dashboardView);
  }
}