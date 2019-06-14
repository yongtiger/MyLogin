package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cc.brainbook.android.study.mylogin.R;

public class FindPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            showFindPasswordStep1Fragment();
        }
    }

    public void showFindPasswordStep1Fragment() {
        final FindPasswordStep1Fragment findPasswordStep1Fragment = FindPasswordStep1Fragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStep1Fragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showFindPasswordStep2Fragment() {
        final FindPasswordStep2Fragment findPasswordStep2Fragment = FindPasswordStep2Fragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStep2Fragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showFindPasswordStep3Fragment() {
        final FindPasswordStep3Fragment findPasswordStep3Fragment = FindPasswordStep3Fragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStep3Fragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showFindPasswordStep4Fragment() {
        final FindPasswordStep4Fragment findPasswordStep4Fragment = FindPasswordStep4Fragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStep4Fragment)
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
