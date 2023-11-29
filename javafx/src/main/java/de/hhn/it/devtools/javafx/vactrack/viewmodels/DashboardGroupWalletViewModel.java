package de.hhn.it.devtools.javafx.vactrack.viewmodels;

import de.hhn.it.devtools.apis.vactrack.*;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class DashboardGroupWalletViewModel {

   private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> balance = new SimpleObjectProperty<>();
    private final StringProperty expenses = new SimpleStringProperty();
    private final StringProperty currency = new SimpleStringProperty();
    private final ListProperty<String> memberWallets =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    private final StringProperty walletName = new SimpleStringProperty();

    private final StringProperty walletDescription = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> datePickerFromProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> datePickerToProperty = new SimpleObjectProperty<>();
    StringProperty expenseReport = new SimpleStringProperty();

    private final ListProperty<String> walletsToAdd =
          new SimpleListProperty<>(FXCollections.observableArrayList());

     private final ListProperty<String> walletsToDelete =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    private VacTrackService vacTrackService;
    private String selectedId;

    public DashboardGroupWalletViewModel(VacTrackService vacTrackService){
        this.vacTrackService = vacTrackService;
    }

    public void updateDataFromService(String id) throws InvalidParameterException, WalletNotFoundException {
        selectedId = id;
        GroupWallet groupWallet = vacTrackService.getGroupWallet(id);
        List<Wallet> members = groupWallet.getMemberWallets();
        StringBuilder walletDescriptions = new StringBuilder();
        StringBuilder currencies = new StringBuilder();
        Map<String, BigDecimal> balancesByCurrency = new HashMap<>();
        Map<String, BigDecimal> expensesByCurrency = new HashMap<>();
        List<String> memberNames = new ArrayList<>();

        if (members.isEmpty()) {
            setBalance(BigDecimal.ZERO);
            setCurrency("");
            setExpenses("");
        } else {
            BigDecimal totalWalletBalances = BigDecimal.ZERO;
            BigDecimal totalExpenses = BigDecimal.ZERO;

            for (Wallet wallet : members) {
              memberNames.add(wallet.getDescription());
              walletDescriptions.append(wallet.getDescription()).append("----Balance: ").append(wallet.getWalletBalance().getBalance().doubleValue()).append("\n");
                totalWalletBalances = totalWalletBalances.add(wallet.getWalletBalance().getBalance());
                totalExpenses = totalExpenses.add(wallet.getExpenseList().getTotalExpenses());

                String currency = wallet.getCurrency();
                balancesByCurrency.put(currency, balancesByCurrency.getOrDefault(currency, BigDecimal.ZERO)
                        .add(wallet.getWalletBalance().getBalance()));

                expensesByCurrency.put(currency, expensesByCurrency.getOrDefault(currency, BigDecimal.ZERO)
                        .add(wallet.getExpenseList().getTotalExpenses()));

                if (!currencies.toString().contains(wallet.getCurrency()) && !currencies.isEmpty()) {
                    currencies.append(", ").append(wallet.getCurrency());
                } else if (!currencies.toString().contains(wallet.getCurrency())) {
                    currencies.append(wallet.getCurrency());
                }
            }

            setBalance(totalWalletBalances);
            StringJoiner currenciesJoiner = new StringJoiner(", \n");
            for (Map.Entry<String, BigDecimal> entry : balancesByCurrency.entrySet()) {
                String currencyBalance = entry.getKey() + ": " + entry.getValue().doubleValue();
                currenciesJoiner.add(currencyBalance);
            }
            setCurrency(currenciesJoiner.toString());

            StringJoiner expensesJoiner = new StringJoiner(", \n");
            for (Map.Entry<String, BigDecimal> entry : expensesByCurrency.entrySet()) {
                String currencyExpenses = entry.getKey() + ": " + entry.getValue().doubleValue();
                expensesJoiner.add(currencyExpenses);
            }
            setExpenses(expensesJoiner.toString());
            walletsToDelete.setAll(memberNames);
        }
        memberWallets.setAll(walletDescriptions.toString());
        walletsToAdd.setAll(vacTrackService.getWalletDescriptions());
      setDescription(groupWallet.getGroupName());
    }


    public ObservableList<String> getMemberWallets() {
        return memberWallets.get();
    }

    public ListProperty<String> memberWalletsProperty() {
        return memberWallets;
    }

    public void setMemberWallets(ObservableList<String> memberWallets) {
        this.memberWallets.set(memberWallets);
    }

    public String getWalletDescription() {
        return walletDescription.get();
    }

  public ObservableList<String> getWalletsToAdd() {
    return walletsToAdd.get();
  }

  public ListProperty<String> walletsToAddProperty() {
    return walletsToAdd;
  }

  public void setWalletsToAdd(ObservableList<String> walletsToAdd) {
    this.walletsToAdd.set(walletsToAdd);
  }

  public StringProperty walletDescriptionProperty() {
        return walletDescription;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public BigDecimal getBalance() {
        return balance.get();
    }

    public ObjectProperty<BigDecimal> balanceProperty() {
        return balance;
    }

  public ObservableList<String> getWalletsToDelete() {
    return walletsToDelete.get();
  }

  public ListProperty<String> walletsToDeleteProperty() {
    return walletsToDelete;
  }

  public void setBalance(BigDecimal balance) {
        this.balance.set(balance);
    }

    public String getExpenses() {
        return expenses.get();
    }

    public StringProperty expensesProperty() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses.set(expenses);
    }

    public String getCurrency() {
        return currency.get();
    }

    public StringProperty currencyProperty() {
        return currency;
    }
    public StringProperty walletNameProperty() {
        return walletName;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public void handleAddWallet(String description) throws InvalidParameterException, WalletNotFoundException {
        String walletId = vacTrackService.getIdByDescription(description);
        String groupId = selectedId;
        vacTrackService.addWalletToGroup(walletId,groupId);
    }


    public void handleDeleteWallet(String description) throws InvalidParameterException, WalletNotFoundException {
        String id = vacTrackService.getIdByDescription(description);
        vacTrackService.removeWalletFromGroup(id,selectedId);
    }

    public LocalDate getDatePickerFromProperty() {
        return datePickerFromProperty.get();
    }

    public ObjectProperty<LocalDate> datePickerFromProperty() {
        return datePickerFromProperty;
    }

    public void setDatePickerFromProperty(LocalDate datePickerFromProperty) {
        this.datePickerFromProperty.set(datePickerFromProperty);
    }

    public LocalDate getDatePickerToProperty() {
        return datePickerToProperty.get();
    }

    public ObjectProperty<LocalDate> datePickerToProperty() {
        return datePickerToProperty;
    }

    public void setDatePickerToProperty(LocalDate datePickerToProperty) {
        this.datePickerToProperty.set(datePickerToProperty);
    }

    public String getExpenseReport() {
        return expenseReport.get();
    }

    public StringProperty expenseReport() {
        return expenseReport;
    }

    public void setExpenseReport(String expenseReport) {
        this.expenseReport.set(expenseReport);
    }

    public void handleGenerateExpenseReport() throws InvalidParameterException, WalletNotFoundException {
        ExpenseReport expenseReport = vacTrackService.getExpenseReportForGroupWallet(selectedId,datePickerFromProperty.get().atStartOfDay(),datePickerToProperty.get().atStartOfDay());
        setExpenseReport(vacTrackService.generatePrintableReport(expenseReport));
    }
}
