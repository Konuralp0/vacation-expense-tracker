package de.hhn.it.devtools.apis.vactrack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Demo usage of VacTrack.
 */
public class DemoVacTrackUsage {
  private static final Logger logger = LoggerFactory.getLogger(DemoVacTrackUsage.class);

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    VacTrackService vacTrackService = null; {
    }

    try {
      vacTrackService.enterExchangeRate("USD", "EUR", 0.85);


      String walletId = vacTrackService.createWallet("Credit Card", "USD", new BigDecimal("160.5"));
      Wallet wallet1 = vacTrackService.getWallet(walletId);
      logger.info("Wallet created: " + wallet1);
      vacTrackService.convertWalletCurrency(wallet1.getWalletId(), "EUR");
      logger.info("Changed currency: " + wallet1.getWalletBalance().getBalance());
      logger.info("Check to see whether currency has changed "
              + vacTrackService.getWallet(walletId));

      Expense expense1 = vacTrackService.createExpense(wallet1.getWalletId(), "Lunch",
              new BigDecimal("35.5"), null);
      logger.info("Expense created: " + expense1);

      Expense expense2 = vacTrackService
              .createExpense(wallet1.getWalletId(), "Renting a car", new BigDecimal("155.5"), null);
      logger.info("Expense created: " + expense2);

      Expense expense3 = vacTrackService
              .createExpense(wallet1.getWalletId(), "Renting a car", new BigDecimal("155.5"),
                      LocalDateTime.of(2023, 11, 20, 10, 40));
      logger.info("Expense created: " + expense3);


      ExpenseReport report = vacTrackService.getExpenseReport(wallet1.getWalletId(),
              LocalDateTime.of(2012, Month.DECEMBER, 2, 1, 3), LocalDateTime.now());
      logger.info("Getting " + vacTrackService.generatePrintableReport(report));


      BigDecimal balance1 = vacTrackService.getWalletBalance(wallet1.getWalletId());
      logger.info("Wallet 1 balance: " + balance1);

      vacTrackService.setDailyExpenseLimit(wallet1.getWalletId(), new BigDecimal("200"));
      logger.info("Expense Limit added: " + wallet1);


      GroupWallet groupWallet = vacTrackService
              .createWalletGroup("Family Expenses", new BigDecimal("150.5"));
      logger.info("GroupWallet created: " + groupWallet);


      vacTrackService.addWalletToGroup(wallet1.getWalletId(), groupWallet.getGroupId());
      String walletForAliceId = vacTrackService
              .createWallet("Son's Wallet", wallet1.getCurrency(), new BigDecimal("256"));
      Wallet walletForAlice = vacTrackService.getWallet(walletForAliceId);

      logger.info("Another wallet created: " + walletForAlice);

      vacTrackService.addWalletToGroup(walletForAlice.getWalletId(), groupWallet.getGroupId());
      logger.info("Wallets added to group: " + groupWallet);

      vacTrackService
              .transferFunds(walletForAlice.getWalletId(),
                      wallet1.getWalletId(), new BigDecimal("50"), 0);
      logger.info("Transferring funds");


      List<String> walletGroups = vacTrackService.getWalletGroups();
      logger.info("Wallet groups: " + walletGroups);

      vacTrackService.removeWalletFromGroup(wallet1.getWalletId(), groupWallet.getGroupId());
      logger.info("Wallet removed from the group wallet");

      vacTrackService.deleteGroupWallet(groupWallet.getGroupId());
      logger.info("GroupWallet deleted");

      vacTrackService.deleteWallet(wallet1.getWalletId());
      logger.info("Wallet deleted");

      ExpenseReport reportGroup = vacTrackService
              .getExpenseReportForGroupWallet(groupWallet.getGroupId(), LocalDateTime.now(),
                      LocalDateTime.of(2023, 12, 31, 20, 0));
      logger.info("Getting: " + reportGroup);

    } catch (InvalidParameterException | InvalidExchangeRateException | WalletNotFoundException e) {
      logger.error("Exception occurred: " + e.getMessage(), e);
    }
  }
}
