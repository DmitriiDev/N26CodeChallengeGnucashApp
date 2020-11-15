package org.gnucash.android.test.ui.testFrameworkVokuevDmitriiN26Challenge.screenObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.kobakei.ratethisapp.RateThisApp;
import org.gnucash.android.R;
import org.gnucash.android.app.GnuCashApplication;
import org.gnucash.android.db.DatabaseHelper;
import org.gnucash.android.db.adapter.AccountsDbAdapter;
import org.gnucash.android.db.adapter.BooksDbAdapter;
import org.gnucash.android.db.adapter.TransactionsDbAdapter;
import org.gnucash.android.model.Account;
import org.gnucash.android.ui.account.AccountsActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.hamcrest.EasyMock2Matchers.equalTo;

public class BaseScreenObject extends AndroidJUnitRunner {

    protected static DatabaseHelper mDbHelper;
    protected static SQLiteDatabase mDb;
    protected static AccountsDbAdapter mAccountsDbAdapter;
    protected static TransactionsDbAdapter mTransactionsDbAdapter;
    protected AccountsActivity mAccountsActivity;

    @BeforeClass
    public static void prepTest(){
        preventFirstRunDialogs(GnuCashApplication.getAppContext());

        String activeBookUID = BooksDbAdapter.getInstance().getActiveBookUID();
        mDbHelper = new DatabaseHelper(GnuCashApplication.getAppContext(), activeBookUID);
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("AccountsActivityTest", "Error getting database: " + e.getMessage());
            mDb = mDbHelper.getReadableDatabase();
        }
        mTransactionsDbAdapter  = TransactionsDbAdapter.getInstance();
        mAccountsDbAdapter      = AccountsDbAdapter.getInstance();
        mAccountsDbAdapter.deleteAllRecords(); //clear the data
    }


    public static void preventFirstRunDialogs(Context context) {
        AccountsActivity.rateAppConfig = new RateThisApp.Config(10000, 10000);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        //do not show first run dialog
        editor.putBoolean(context.getString(R.string.key_first_run), false);
        editor.putInt(AccountsActivity.LAST_OPEN_TAB_INDEX, AccountsActivity.INDEX_TOP_LEVEL_ACCOUNTS_FRAGMENT);

        //do not show "What's new" dialog
        String minorVersion = context.getString(R.string.app_minor_version);
        int currentMinor = Integer.parseInt(minorVersion);
        editor.putInt(context.getString(R.string.key_previous_minor_version), currentMinor);
        editor.commit();
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }

    public Account accountFinder(final String accountName) {
        List<Account> accounts = mAccountsDbAdapter.getAllRecords();

        Optional<Account> java = accounts.stream()
                .filter(s -> s.getName().equals(accountName))
                .findAny();

        return java.orElse(new Account("No account"));
    }

}
