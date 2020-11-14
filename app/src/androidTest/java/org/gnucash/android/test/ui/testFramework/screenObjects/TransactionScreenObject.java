package org.gnucash.android.test.ui.testFramework.screenObjects;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;

import org.gnucash.android.R;
import org.gnucash.android.db.adapter.DatabaseAdapter;
import org.gnucash.android.model.Account;
import org.gnucash.android.model.Commodity;
import org.gnucash.android.model.Money;
import org.gnucash.android.model.Split;
import org.gnucash.android.model.Transaction;
import org.gnucash.android.model.TransactionType;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TransactionScreenObject extends BaseScreenObject {


    public void addNewTransactionIncome(String amount, String name) {

        onView(withId(R.id.fab_create_transaction)).perform(click());
        onView(withId(R.id.input_transaction_name)).perform(typeText(name));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.input_transaction_amount)).perform(typeText(amount));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.input_transaction_type))
                .check(matches(allOf(isDisplayed(), withText(R.string.label_charge))))
                .perform(click())
                .check(matches(withText(R.string.label_income)));

        onView(withId(R.id.input_transfer_account_spinner)).perform(click());

        DataInteraction pickAccountTransferDistination = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        pickAccountTransferDistination.perform(click());

//        String expectedValue = NumberFormat.getInstance().format(-899);
//        onView(withId(R.id.input_transaction_amount)).check(matches(withText(expectedValue)));

//        int transactionsCount = getTransactionCount();
        onView(withId(R.id.menu_save)).perform(click());

//        validateTransactionListDisplayed();
//
//        List<Transaction> transactions = mTransactionsDbAdapter.getAllTransactionsForAccount(accountUID);
//        assertThat(transactions).hasSize(2);
//        Transaction transaction = transactions.get(0);
//        assertThat(transaction.getSplits()).hasSize(2);

    }

    public void addTransactionByCode(String name, Account baseAccount, Account transferAccount, Commodity commodity, String amount, TransactionType transactionType) {

        long mTransactionTimeMillis = System.currentTimeMillis();
        Transaction mTransaction = new Transaction(name);
        mTransaction.setCommodity(commodity);
        mTransaction.setTime(mTransactionTimeMillis);

        Split split = new Split(new Money(amount, commodity.getCurrencyCode()), baseAccount.getUID());
        split.setType(transactionType);

        mTransaction.addSplit(split);
        mTransaction.addSplit(split.createPair(transferAccount.getUID()));

        baseAccount.addTransaction(mTransaction);
        mTransactionsDbAdapter.addRecord(mTransaction, DatabaseAdapter.UpdateMethod.insert);

    }

    public void validateTransactionListDisplayed(){
        onView(withId(R.id.transaction_recycler_view)).check(matches(isDisplayed()));
    }

    public void findTrancactionBalanceView(String balance) {
        onView(allOf(withId(R.id.transaction_amount), withText(balance))).check(matches(isDisplayed()));
    }

    public void findTrancactionNameView(String name) {
        onView(allOf(withId(R.id.primary_text), withText(name))).check(matches(isDisplayed()));
    }
 }
