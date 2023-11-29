package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardGroupWalletViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class RemoveWalletFromGroupController {

    public ComboBox dropdownForDelete;
    @FXML
    private Pane pane;
    @FXML
    private Button remove;

    private DashboardGroupWalletViewModel dashboardGroupWalletViewModel;

    public void initialize(DashboardGroupWalletViewModel dashboardGroupWalletViewModel){
        this.dashboardGroupWalletViewModel = dashboardGroupWalletViewModel;
        dropdownForDelete.itemsProperty().bindBidirectional(dashboardGroupWalletViewModel.walletsToDeleteProperty());

    }

    public void removeOnAction(ActionEvent actionEvent) throws InvalidParameterException, WalletNotFoundException, IOException {
        int index = dropdownForDelete.getSelectionModel().getSelectedIndex();
        String description = (String) dropdownForDelete.getItems().get(index);
        dashboardGroupWalletViewModel.handleDeleteWallet(description);
        reloadDashboardView(dashboardGroupWalletViewModel, dashboardGroupWalletViewModel.getSelectedId());

    }


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
