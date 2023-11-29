package de.hhn.it.devtools.javafx.vactrack.viewmodels;

import de.hhn.it.devtools.apis.vactrack.GroupWallet;
import de.hhn.it.devtools.apis.vactrack.InvalidParameterException;
import de.hhn.it.devtools.apis.vactrack.VacTrackService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class CreateGroupWalletViewModel {

    private StringProperty groupNameProperty = new SimpleStringProperty();

    private VacTrackService vacTrackService;
    private VacTrackServiceViewModel vacTrackServiceViewModel;

    private StringProperty groupWalletId = new SimpleStringProperty();

    public CreateGroupWalletViewModel(VacTrackService vacTrackService, VacTrackServiceViewModel vacTrackServiceViewModel){
        this.vacTrackService = vacTrackService;
        this.vacTrackServiceViewModel = vacTrackServiceViewModel;
    }

    public StringProperty groupNamePropertyProperty() {
        return groupNameProperty;
    }

    public void setGroupWalletId(String groupWalletId) {
        this.groupWalletId.set(groupWalletId);
    }

    public String getGroupWalletId() {
        return groupWalletId.get();
    }
    public void handleCreate() throws InvalidParameterException {
        GroupWallet groupWallet = vacTrackService.createWalletGroup(groupNameProperty.get(), BigDecimal.ZERO);
        setGroupWalletId(groupWallet.getGroupId());
        vacTrackServiceViewModel.updateGroupWalletList();
        setGroupWalletId(groupWallet.getGroupId());

    }
}
