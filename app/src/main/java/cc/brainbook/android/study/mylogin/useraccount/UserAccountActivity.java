package cc.brainbook.android.study.mylogin.useraccount;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cc.brainbook.android.study.mylogin.R;

public class UserAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html

        if (savedInstanceState == null) {
            showUserAccountFragment();
        }
    }

    public void showUserAccountFragment() {
        UserAccountFragment userAccountFragment = UserAccountFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, userAccountFragment)
//                .addToBackStack(null)   ///[关闭其它fragment后回退显示UserAccountFragment]注意：第一个Fragment不用添加addToBackStack()，就能解决空白页的问题
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    public void showUserAccountUsernameFragment() {
        final UserAccountUsernameFragment userAccountUsernameFragment = UserAccountUsernameFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, userAccountUsernameFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示UserAccountFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showUserAccountPasswordFragment() {
        final UserAccountPasswordFragment userAccountPasswordFragment = UserAccountPasswordFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, userAccountPasswordFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示UserAccountFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showUserAccountEmailFragment() {
        final UserAccountEmailFragment userAccountEmailFragment = UserAccountEmailFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, userAccountEmailFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示UserAccountFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showUserAccountMobileFragment() {
        final UserAccountMobileFragment userAccountMobileFragment = UserAccountMobileFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, userAccountMobileFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示UserAccountFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void updateUi(@StringRes Integer successString) {
        Toast.makeText(getApplicationContext(), successString, Toast.LENGTH_LONG).show();
    }

    public void showFailedMessage(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
