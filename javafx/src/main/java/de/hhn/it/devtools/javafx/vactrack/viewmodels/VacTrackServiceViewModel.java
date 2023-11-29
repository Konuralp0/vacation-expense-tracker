package de.hhn.it.devtools.javafx.vactrack.viewmodels;

import de.hhn.it.devtools.apis.vactrack.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

/**
 * The type Vac track service view model.
 */
public class VacTrackServiceViewModel {


  private final VacTrackService service;
   private final StringProperty selectedWalletId = new SimpleStringProperty();
  private final ListProperty<String> walletsList =
          new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ListProperty<String> groupWalletsList =
          new SimpleListProperty<>(FXCollections.observableArrayList());

  /**
   * Instantiates a new Vac track service view model.
   *
   * @param service the service
   */
  public VacTrackServiceViewModel(VacTrackService service) throws InvalidParameterException, WalletNotFoundException {
    this.service = service;
    String id = service.createWallet("Alice","Euro", BigDecimal.valueOf(1000.0D));
    String id2 =service.createWallet("Bob","Euro",BigDecimal.valueOf(50.0D));
    GroupWallet gw = service.createWalletGroup("Vacation",BigDecimal.ZERO);
    service.addWalletToGroup(id, gw.getGroupId());
    service.addWalletToGroup(id2,gw.getGroupId());
    updateWalletsList();
    updateGroupWalletList();
  }

  /*public StringProperty selectedWalletIdProperty() {
    return selectedWalletId;
  }

  public String getSelectedWalletId() {
    return selectedWalletId.get();
  }

  public void setSelectedWalletId(String id) {
    selectedWalletId.set(id);
  }*/


  /**
   * Wallets list property list property.
   *
   * @return the list property
   */
  public ListProperty<String> walletsListProperty() {
    return walletsList;
  }
  public ListProperty<String> groupWalletListProperty() {
    return groupWalletsList;
  }

  /**
   * Gets wallets list.
   *
   * @return the wallets list
   */
  public ObservableList<String> getWalletsList() {
    return walletsList.get();
  }

  /**
   * Update wallets list.
   */
  public void updateWalletsList() {


    walletsList.setAll(service.getWalletDescriptions());
  }

  public void updateGroupWalletList() {


    groupWalletsList.setAll(service.getWalletGroups());
  }

  public int handleDeleteWallet(String description) throws InvalidParameterException, WalletNotFoundException {
    String id = service.getIdByDescription(description);
    if(service.getWallet(id).getWalletBalance().getBalance().doubleValue() == 0){
      service.deleteWallet(id);

    }

    updateWalletsList();
    return walletsList.size();
  }

  public int handleDeleteGroupWallet(String description) throws InvalidParameterException {
    String id = service.getIdByDescription(description);
    service.deleteGroupWallet(id);
    updateGroupWalletList();
    return groupWalletsList.size();

  }
}

