package org.gnucash.android.test.ui.testFramework.screenObjects;

import android.support.test.espresso.ViewInteraction;

import org.gnucash.android.R;
import org.gnucash.android.db.adapter.DatabaseAdapter;
import org.gnucash.android.model.Account;
import org.gnucash.android.model.AccountType;
import org.gnucash.android.model.Commodity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class AccountsScreenObject extends BaseScreenObject {

    public void createAccountByUI(AccountType accountType, String name){
        onView(allOf(isDisplayed(), withId(R.id.fab_create_account))).perform(click());
        onView(withId(R.id.input_account_name)).perform(typeText(name), closeSoftKeyboard());
        sleep(1000);
        onView(allOf(isDisplayed(), withId(R.id.input_account_type_spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(accountType.name()))).perform(click());
        onView(allOf(isDisplayed(), withId(R.id.input_currency_spinner))).perform(click());

        onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")), 0)).atPosition(68).perform(click());

        onView(withId(R.id.menu_save)).perform(click());
    }

    public void createSubAccount(AccountType accountType, String name) {
        onView(allOf(isDisplayed(), withId(R.id.fab_create_transaction))).perform(click());

        onView(withId(R.id.input_account_name)).perform(typeText(name), closeSoftKeyboard());
        sleep(1000);
        onView(allOf(isDisplayed(), withId(R.id.input_account_type_spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(accountType.name()))).perform(click());

        onView(allOf(isDisplayed(), withId(R.id.input_currency_spinner))).perform(click());

        onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")), 0)).atPosition(68).perform(click());
        onView(withId(R.id.menu_save)).perform(click());
    }

    public void createSubAccountByCode(String parentUID, String name, AccountType accountType, Commodity commodity) {
        Account subAccount = new Account(name);
        Double randomValue = Math.random();
        subAccount.setUID(name + randomValue);
        subAccount.setParentUID(parentUID);
        subAccount.setAccountType(accountType);
        subAccount.setCommodity(commodity);
        mAccountsDbAdapter.addRecord(subAccount, DatabaseAdapter.UpdateMethod.insert);
    }

    public void createAccountByCode(String name, AccountType type, String currencyCode){
        Account account = new Account(name);
        Double randomValue = Math.random();
        account.setUID(name + randomValue);
        account.setCommodity(Commodity.getInstance(currencyCode));
        account.setAccountType(type);
        mAccountsDbAdapter.addRecord(account, DatabaseAdapter.UpdateMethod.insert);
    }

    public void openSubAccountTab() {
        onView(withId(R.id.fragment_transaction_list)).perform(swipeRight());
    }

    public void openAccount(String name) {
        onView(allOf(withText(name), isDisplayed())).perform(click());
    }

    public void findAccountBalanceView(String balance) {
        onView(allOf(withId(R.id.account_balance), withText(balance))).check(matches(isDisplayed()));
    }

}
