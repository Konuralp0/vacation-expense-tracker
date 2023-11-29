package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardGroupWalletViewModel;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.DashboardWalletViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GenerateExpenseReportGroupController {

    @FXML private
    Pane pane;
    @FXML
    private
    TextArea expenseReportTextArea;
    @FXML private
    Button generate;
    @FXML private
    DatePicker datePickerFrom;
    @FXML private
    DatePicker datePickerTo;
    @FXML private
    Button backToDashboard;
    private DashboardGroupWalletViewModel dashboardGroupWalletViewModel;

    public void initialize(DashboardGroupWalletViewModel dashboardGroupWalletViewModel){
        this.dashboardGroupWalletViewModel = dashboardGroupWalletViewModel;
        datePickerFrom.valueProperty()
                .bindBidirectional(dashboardGroupWalletViewModel.datePickerFromProperty());
        datePickerTo.valueProperty()
                .bindBidirectional(dashboardGroupWalletViewModel.datePickerToProperty());
        expenseReportTextArea.textProperty()
                .bindBidirectional(dashboardGroupWalletViewModel.expenseReport());
    }

    public void generateOnAction(ActionEvent actionEvent) throws InvalidParameterException, WalletNotFoundException {
        dashboardGroupWalletViewModel.handleGenerateExpenseReport();
    }

    public void backToDashboard(ActionEvent actionEvent) throws InvalidParameterException, WalletNotFoundException, IOException {
        reloadDashboardView(dashboardGroupWalletViewModel, dashboardGroupWalletViewModel.getSelectedId());
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
    public void reloadDashboardView(DashboardGroupWalletViewModel dashboardWalletViewModel, String id)
            throws IOException, InvalidParameterException, WalletNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/fxml/vactrack/DashboardGroupWallet.fxml"));
        Parent dashboardView = loader.load();
        DashboardGroupWalletController dashboardController = loader.getController();
        dashboardController.initialize(dashboardGroupWalletViewModel, id);
        dashboardController.updateDashboardView(dashboardGroupWalletViewModel, id);


        pane.getChildren().clear();
        pane.getChildren().addAll(dashboardView);

    }
}
