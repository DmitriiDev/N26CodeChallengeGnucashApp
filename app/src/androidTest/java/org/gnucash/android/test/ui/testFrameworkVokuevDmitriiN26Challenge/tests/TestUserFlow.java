package org.gnucash.android.test.ui.testFrameworkVokuevDmitriiN26Challenge.tests;

import org.gnucash.android.model.Account;
import org.gnucash.android.model.AccountType;
import org.gnucash.android.model.Commodity;
import org.gnucash.android.model.Transaction;
import org.gnucash.android.model.TransactionType;
import org.gnucash.android.test.ui.testFrameworkVokuevDmitriiN26Challenge.screenObjects.AccountsScreenObject;
import org.gnucash.android.test.ui.testFrameworkVokuevDmitriiN26Challenge.screenObjects.TransactionScreenObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserFlow extends TestBase {

    /**
     * Since the classical integration tests were already implemented by the authors of the program,
     * I decided to implement a complex scenario of program testing. I take the real model of using the program by the user and automate this scenario.
     * In some way this test is similar to manual testing process. Each test leaves a certain program state which uses the next test.
     * This way we can test the data evolution without making a complicated test.
     *
     * Model: Weather has 4 accounts. Each account has a subaccount.
     * From each account there are transactions. The last test makes sure that all accounts show the correct balance.
     * This scenario has  advantages and disadvantages.
     */
    
    /**
     Names for parent accounts.
     */
    static String asstesAccountName = "Assets";
    static String incomeAccountName = "Income";
    static String expensesAccountName = "Expenses";
    static String liabilitiesAccountName = "liabilities";

    /**
     Names for child accounts.
     */
    static String assetsAccountCash = "Cash";
    static String assetsAccountCard = "Card";
    static String assetsAccountSaving = "Saving";

    static String incomeAccountBonus = "Bonus";
    static String incomeAccountSalary = "Salary";

    static String expensesAccountRent = "Rent";
    static String expensesAccountElectricity = "Electricity";
    static String expensesAccountPhone = "Phone";
    static String expensesAccountFood = "Food";

    static String liabilitiesCredit = "Credit MacBook";

    /**
     * Names for transactions
     */
    static String nameTransferSalaryToSaving = "Salary transfer to saving";
    static String nameTransferSalaryToCard = "Salary transfer to card";

    static String nameTransferBonusToCard = "Bonus Transfer";
    static String nameTransferPhoneToCard = "Phone Bill";
    static String nameTransferRentApart = "Rent Apartment";
    static String nameTransferFoodToCash = "Food";
    static String nameTransferMacBookPayment = "MacBook payment";

    /**
     * Amounts for transactions
     */
    static String salaryIncome = "2000";
    static String salaryIncome2 = "1000";

    static String bonusIncome = "200";

    static String rentExpenses = "1000";
    static String phoneExpenses = "15";
    static String foodExpenses = "40";

    static String liabilitiesMacBook = "150";

    /**
     * Final balances and amounts after all transactions. Data for final checks
     */
    static String finalSumAssets = "€1,995.00";
    static String salaryTransferTransaction = "€1,000.00";
    static String bonusTransaction = "€200.00";
    static String rentApartTransaction = "-€1,000.00";
    static String phoneBillTransaction = "-€15.00";


    @Before
    public void setUp() throws Exception {
        mAccountsActivity = mActivityRule.getActivity();
    }

    AccountsScreenObject accountsScreenObject = new AccountsScreenObject();
    TransactionScreenObject transactionScreenObject = new TransactionScreenObject();


    /**
     * This test creates 4 parent accounts. Assets account created by UI interaction and 3 other accounts created by code.
     * I created one account by UI interface to be sure that UI allows do it. Other 3 accounts created by code just because
     * it faster and you able to prepare more test data for future tests.
     */
    @Test
    public void A_testCreateAccount(){
        assertThat(mAccountsDbAdapter.getAllRecords()).hasSize(0);

        accountsScreenObject.createAccountByUI(AccountType.ASSET, asstesAccountName);

        List<Account> accounts = mAccountsDbAdapter.getAllRecords();
        assertThat(accounts).isNotNull();
        assertThat(accounts).hasSize(1);
        Account newestAccount = accounts.get(0);

        assertThat(newestAccount.getName()).isEqualTo(asstesAccountName);
        assertThat(newestAccount.getCommodity().getCurrencyCode()).isEqualTo("EUR");
        assertThat(newestAccount.isPlaceholderAccount()).isFalse();

        accountsScreenObject.createAccountByCode(incomeAccountName, AccountType.INCOME, "EUR");
        accountsScreenObject.createAccountByCode(expensesAccountName, AccountType.EXPENSE, "EUR");
        accountsScreenObject.createAccountByCode(liabilitiesAccountName, AccountType.LIABILITY, "EUR");

        refreshAccountsList();
        List<Account> accounts2 = mAccountsDbAdapter.getAllRecords();

        assertThat(accounts2).isNotNull();
        assertThat(accounts2).hasSize(4);
        Account newestAccount1 = accounts2.get(1);
        Account newestAccount2 = accounts2.get(2);
        Account newestAccount3 = accounts2.get(3);

        assertThat(newestAccount1.getName()).isEqualTo(expensesAccountName);
        assertThat(newestAccount2.getName()).isEqualTo(incomeAccountName);
        assertThat(newestAccount3.getName()).isEqualTo(liabilitiesAccountName);
    }
    /**
     * This test creates 10 sub-accounts. The main purpose of the test to check that app allows us to create sub-accounts
     * and create a more and less difficult hierarchy of parent-child accounts.
     * */
    @Test
    public void B_crateSubAccounts(){
        accountsScreenObject.openAccount(asstesAccountName);
        assertThat(mAccountsDbAdapter.getAllRecords()).hasSize(4);
        accountsScreenObject.openSubAccountTab();
        List<Account> accounts = mAccountsDbAdapter.getAllRecords();

        sleep(1000);
        accountsScreenObject.createSubAccount(AccountType.CASH, assetsAccountCash);

        Account assetsAccount = accounts.get(0);
        accountsScreenObject.createSubAccountByCode(assetsAccount.getUID(), assetsAccountCard, AccountType.BANK, Commodity.getInstance("EUR"));
        accountsScreenObject.createSubAccountByCode(assetsAccount.getUID(), assetsAccountSaving, AccountType.BANK, Commodity.getInstance("EUR"));

        Account expensesAccount = accounts.get(1);
        accountsScreenObject.createSubAccountByCode(expensesAccount.getUID(), expensesAccountRent, AccountType.EXPENSE, Commodity.getInstance("EUR"));
        accountsScreenObject.createSubAccountByCode(expensesAccount.getUID(), expensesAccountElectricity, AccountType.EXPENSE, Commodity.getInstance("EUR"));
        accountsScreenObject.createSubAccountByCode(expensesAccount.getUID(), expensesAccountPhone, AccountType.EXPENSE, Commodity.getInstance("EUR"));
        accountsScreenObject.createSubAccountByCode(expensesAccount.getUID(), expensesAccountFood, AccountType.EXPENSE, Commodity.getInstance("EUR"));


        Account incomeAccount = accounts.get(2);
        accountsScreenObject.createSubAccountByCode(incomeAccount.getUID(), incomeAccountSalary, AccountType.INCOME, Commodity.getInstance("EUR"));
        accountsScreenObject.createSubAccountByCode(incomeAccount.getUID(), incomeAccountBonus, AccountType.INCOME, Commodity.getInstance("EUR"));

        Account liabilitiesAccount = accounts.get(3);
        accountsScreenObject.createSubAccountByCode(liabilitiesAccount.getUID(), liabilitiesCredit, AccountType.LIABILITY, Commodity.getInstance("EUR"));

        List<Account> accountsAfterDBUpdates = mAccountsDbAdapter.getAllRecords();
        refreshAccountsList();
        assertThat(accountsAfterDBUpdates).isNotNull();
        assertThat(accountsAfterDBUpdates).hasSize(14);
    }

    @Test
    public void C_testCreateTransactions(){
        accountsScreenObject.openAccount(incomeAccountName);
        accountsScreenObject.openAccount(incomeAccountSalary);
        transactionScreenObject.addNewTransactionIncome(salaryIncome, nameTransferSalaryToSaving);

        transactionScreenObject.addTransactionByCode(nameTransferSalaryToCard,
                accountFinder(incomeAccountSalary),
                accountFinder(assetsAccountCard),
                Commodity.getInstance("EUR"),
                salaryIncome2,
                TransactionType.CREDIT);

        transactionScreenObject.addTransactionByCode(nameTransferBonusToCard,
                accountFinder(incomeAccountBonus),
                accountFinder(assetsAccountCard),
                Commodity.getInstance("EUR"),
                bonusIncome,
                TransactionType.CREDIT);

        transactionScreenObject.addTransactionByCode(nameTransferRentApart,
                accountFinder(expensesAccountRent),
                accountFinder(assetsAccountCard),
                Commodity.getInstance("EUR"),
                rentExpenses,
                TransactionType.DEBIT);

        transactionScreenObject.addTransactionByCode(nameTransferPhoneToCard,
                accountFinder(expensesAccountPhone),
                accountFinder(assetsAccountCard),
                Commodity.getInstance("EUR"),
                phoneExpenses,
                TransactionType.DEBIT);

        transactionScreenObject.addTransactionByCode(nameTransferFoodToCash,
                accountFinder(expensesAccountFood),
                accountFinder(assetsAccountCash),
                Commodity.getInstance("EUR"),
                foodExpenses,
                TransactionType.DEBIT);


        transactionScreenObject.addTransactionByCode(nameTransferMacBookPayment,
                accountFinder(liabilitiesCredit),
                accountFinder(assetsAccountSaving),
                Commodity.getInstance("EUR"),
                liabilitiesMacBook,
                TransactionType.DEBIT);

        List<Transaction> transactions = mTransactionsDbAdapter.getAllTransactions();
        assertThat(transactions).hasSize(7);
    }

    @Test
    public void D_testCheckBalanceAndListTransactions() {

        accountsScreenObject.findAccountBalanceView(finalSumAssets);
        accountsScreenObject.openAccount(asstesAccountName);
        sleep(500);
        accountsScreenObject.openAccount(assetsAccountCard);
        transactionScreenObject.validateTransactionListDisplayed();
        transactionScreenObject.findTrancactionBalanceView(phoneBillTransaction);
        transactionScreenObject.findTrancactionBalanceView(rentApartTransaction);
        transactionScreenObject.findTrancactionBalanceView(bonusTransaction);
        transactionScreenObject.findTrancactionBalanceView(salaryTransferTransaction);
    }

    }
