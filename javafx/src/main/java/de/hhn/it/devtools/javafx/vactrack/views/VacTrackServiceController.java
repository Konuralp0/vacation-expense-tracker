package de.hhn.it.devtools.javafx.vactrack.views;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.VacTrackService;
import de.hhn.it.devtools.apis.vactrack.Wallet;
import de.hhn.it.devtools.apis.vactrack.WalletNotFoundException;
import de.hhn.it.devtools.components.vactrack.VacTrackServiceImpl;
import de.hhn.it.devtools.javafx.controllers.Controller;
import de.hhn.it.devtools.javafx.vactrack.viewmodels.*;
import java.io.IOException;
import java.math.BigDecimal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The type Vac track service controller.
 */
public class VacTrackServiceController extends Controller {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(VacTrackServiceController.class);
  @FXML
  private AnchorPane outerAnchorPane;
  @FXML
  private Button deleteWallet;
  @FXML
  private Button deleteWalletGroupButton;

  @FXML
  private VBox walletsVbox;

  @FXML
  private Label walletsLabel;

  @FXML
  private Button createWalletButton;

  @FXML
  private ListView<String> walletListView;

  @FXML
  private Label groupWalletsLabel;

  @FXML
  private Button createWalletGroupButton;

  @FXML
  private ListView<String> groupWalletListView;

  @FXML
  private AnchorPane controlAnchorPane;

  @FXML
  private Text welcoming;

  private   VacTrackService vacTrackService;

  private  VacTrackServiceViewModel vacTrackServiceViewModel;
  private  CreateWalletViewModel createWalletViewModel;

  private  DashboardWalletViewModel dashboardWalletViewModel;

  private CreateGroupWalletViewModel createGroupWalletViewModel;

  private DashboardGroupWalletViewModel dashboardGroupWalletViewModel;


  /**
   * The initialize method.
   */
  public void initialize() throws InvalidParameterException, WalletNotFoundException {
    vacTrackService = new VacTrackServiceImpl();
    vacTrackServiceViewModel = new VacTrackServiceViewModel(vacTrackService);
    createWalletViewModel = new CreateWalletViewModel(vacTrackService, vacTrackServiceViewModel);
    EnterExchangeRateController enterExchangeRateController = new EnterExchangeRateController();
    DashboardWalletController dashboardWalletController = new DashboardWalletController();
    dashboardWalletViewModel = new DashboardWalletViewModel(vacTrackService,
            dashboardWalletController, enterExchangeRateController);
    walletListView.itemsProperty()
            .bindBidirectional(vacTrackServiceViewModel.walletsListProperty());
    groupWalletListView.itemsProperty()
            .bindBidirectional(vacTrackServiceViewModel.groupWalletListProperty());
    createGroupWalletViewModel =
            new CreateGroupWalletViewModel(vacTrackService, vacTrackServiceViewModel);
    dashboardGroupWalletViewModel = new DashboardGroupWalletViewModel(vacTrackService);
  }


  @FXML
  private void createWalletOnAction() {

    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource(
              "/fxml/vactrack/CreateWallet.fxml"));
      Parent createWalletView = loader.load();


      CreateWalletController createWalletController = loader.getController();
      createWalletController.initialize(createWalletViewModel, dashboardWalletViewModel);


      controlAnchorPane.getChildren().setAll(createWalletView);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void createWalletGroupOnAction() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
              "/fxml/vactrack/CreateGroupWallet.fxml"));
      Parent createGroupWalletView = loader.load();


      CreateGroupWalletController createWalletController = loader.getController();
      createWalletController.initialize(createGroupWalletViewModel, dashboardGroupWalletViewModel);


      controlAnchorPane.getChildren().setAll(createGroupWalletView);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void resume() {
    logger.debug("resume: VacTrack is back on stage ...");
  }

  /**
   * Select wallet.
   *
   * @param mouseEvent the mouse event
   * @throws InvalidParameterException the invalid parameter exception
   */
  public void selectWallet(MouseEvent mouseEvent) throws InvalidParameterException {
    String description = getDescriptionByIndexForWallets(
            walletListView.getSelectionModel().getSelectedIndex());
    loadWalletDashboardByDescription(description);

  }

  /**
   * Select group wallet.
   *
   * @param mouseEvent the mouse event
   * @throws InvalidParameterException the invalid parameter exception
   */
  public void selectGroupWallet(MouseEvent mouseEvent) throws InvalidParameterException {
    String groupName = getDescriptionByIndexForGroupWallets(groupWalletListView
            .getSelectionModel().getSelectedIndex());
    loadGroupWalletDashboardByGroupName(groupName);


  }

  /**
   * Gets description by index.
   *
   * @param index the index
   * @return the description by index
   */
  public String getDescriptionByIndexForWallets(int index) {

    return walletListView.getItems().get(index);
  }

  /**
   * Gets description by index.
   *
   * @param index the index
   * @return the description by index
   */
  public String getDescriptionByIndexForGroupWallets(int index) {

    return groupWalletListView.getItems().get(index);
  }


  /**
   * Delete wallet on action.
   *
   * @param event the event
   * @throws InvalidParameterException the invalid parameter exception
   * @throws WalletNotFoundException   the wallet not found exception
   * @throws IOException               the io exception
   */
  public void deleteWalletOnAction(ActionEvent event)
          throws InvalidParameterException, WalletNotFoundException, IOException {
    String description = getDescriptionByIndexForWallets(
            walletListView.getSelectionModel().getSelectedIndex());

    int size = vacTrackServiceViewModel.handleDeleteWallet(description);
    if (size == 0) {
      reloadWelcomingScene();

    } else {
      String existingWallet = walletListView.getItems().getFirst();
      loadWalletDashboardByDescription(existingWallet);
    }

  }

  /**
   * Delete wallet group on action.
   *
   * @param event the event
   */
  public void deleteWalletGroupOnAction(ActionEvent event) throws InvalidParameterException, IOException, WalletNotFoundException {
    String groupName = getDescriptionByIndexForGroupWallets(groupWalletListView.getSelectionModel().getSelectedIndex());
    int sizeForGroups = vacTrackServiceViewModel.handleDeleteGroupWallet(groupName);
    int sizeForWallets = walletListView.getItems().size();

    if (sizeForGroups == 0 && sizeForWallets == 0){
      reloadWelcomingScene();
    } else if (sizeForGroups !=0) {
      loadGroupWalletDashboardByGroupName(groupWalletListView.getItems().getFirst());

    } else if (sizeForGroups == 0 && sizeForWallets != 0) {
      String existingWallet = walletListView.getItems().getFirst();
      loadWalletDashboardByDescription(existingWallet);

    }


  }


  /**
   * Reload welcoming scene.
   *
   * @throws IOException the io exception
   */
  public void reloadWelcomingScene() throws IOException, InvalidParameterException, WalletNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/fxml/VacTrackService.fxml"));
    Parent welcoming = loader.load();


    VacTrackServiceController vactrackServiceController = loader.getController();
    vactrackServiceController.initialize();


    outerAnchorPane.getChildren().setAll(welcoming);
  }

  /**
   * Load wallet dashboard by description.
   *
   * @param description the description
   * @throws InvalidParameterException the invalid parameter exception
   */
  public void loadWalletDashboardByDescription(String description)
          throws InvalidParameterException {
    String id = vacTrackService.getIdByDescription(description);
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource(
              "/fxml/vactrack/DashboardWallet.fxml"));
      Parent dashboardWalletView = loader.load();


      DashboardWalletController dashboardWalletController = loader.getController();
      dashboardWalletController.initialize(dashboardWalletViewModel, id);


      controlAnchorPane.getChildren().setAll(dashboardWalletView);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Load group wallet dashboard by group name.
   *
   * @param groupName the group name
   * @throws InvalidParameterException the invalid parameter exception
   */
  public void loadGroupWalletDashboardByGroupName(String groupName)
          throws InvalidParameterException {
    String id = vacTrackService.getIdByDescription(groupName);
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource(
              "/fxml/vactrack/DashboardGroupWallet.fxml"));
      Parent dashboardWalletView = loader.load();


      DashboardGroupWalletController dashboardWalletController = loader.getController();
      dashboardWalletController.initialize(dashboardGroupWalletViewModel, id);


      controlAnchorPane.getChildren().setAll(dashboardWalletView);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}

