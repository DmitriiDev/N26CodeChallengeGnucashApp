package org.gnucash.android.test.ui.testFramework.tests;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import org.gnucash.android.test.ui.testFramework.screenObjects.BaseScreenObject;
import org.gnucash.android.test.ui.util.DisableAnimationsRule;
import org.gnucash.android.ui.account.AccountsActivity;
import org.gnucash.android.ui.account.AccountsListFragment;
import org.junit.ClassRule;
import org.junit.Rule;


public class TestBase extends BaseScreenObject {

    @Rule
    public GrantPermissionRule animationPermissionsRule = GrantPermissionRule.grant(Manifest.permission.SET_ANIMATION_SCALE);

    @ClassRule
    public static DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Rule
    public ActivityTestRule<AccountsActivity> mActivityRule = new ActivityTestRule<>(AccountsActivity.class);

    protected void refreshAccountsList(){
        try {
            mActivityRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = mAccountsActivity.getCurrentAccountListFragment();
                    ((AccountsListFragment) fragment).refresh();
                }
            });
        } catch (Throwable throwable) {
            System.err.println("Failed to refresh fragment");
        }
    }


}
