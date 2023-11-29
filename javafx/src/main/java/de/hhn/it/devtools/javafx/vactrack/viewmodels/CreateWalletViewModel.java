package de.hhn.it.devtools.javafx.vactrack.viewmodels;

import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.VacTrackService;
import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The type Create wallet view model.
 */
public class CreateWalletViewModel {

  private final StringProperty description = new SimpleStringProperty();
  private final StringProperty currency = new SimpleStringProperty();
  private final ObjectProperty<BigDecimal> budget = new SimpleObjectProperty<>();

  private final StringProperty walletId = new SimpleStringProperty();

  private final VacTrackService vacTrackService;
  private final VacTrackServiceViewModel vacTrackServiceViewModel;

  /**
   * Instantiates a new Create wallet view model.
   *
   * @param vacTrackService          the vac track service
   * @param vacTrackServiceViewModel the vac track service view model
   */
  public CreateWalletViewModel(VacTrackService vacTrackService,
                               VacTrackServiceViewModel vacTrackServiceViewModel) {
    this.vacTrackService = vacTrackService;
    this.vacTrackServiceViewModel = vacTrackServiceViewModel;

  }


  /**
   * Description property property.
   *
   * @return the property
   */
  public Property<String> descriptionProperty() {
    return description;
  }

  /**
   * Currency property property.
   *
   * @return the property
   */
  public Property<String> currencyProperty() {
    return currency;
  }

  /**
   * Budget property object property.
   *
   * @return the object property
   */
  public ObjectProperty<BigDecimal> budgetProperty() {
    return budget;
  }

  /**
   * Wallet id property string property.
   *
   * @return the string property
   */
  public StringProperty walletIdProperty() {
    return walletId;
  }

  /**
   * Gets wallet id.
   *
   * @return the wallet id
   */
  public String getWalletId() {
    return walletId.get();
  }

  /**
   * Sets wallet id.
   *
   * @param id the id
   */
  public void setWalletId(String id) {
    walletId.set(id);
  }


  /**
   * Create wallet.
   *
   * @throws InvalidParameterException the invalid parameter exception
   */
  public void createWallet() throws InvalidParameterException {

    String id =  vacTrackService.createWallet(descriptionProperty().getValue(),
            currencyProperty().getValue(), budgetProperty().getValue());
    vacTrackServiceViewModel.updateWalletsList();
    setWalletId(id);

  }
}

