package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cc.brainbook.android.study.mylogin.R;

public class FindPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_password);

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
            showFindPasswordStepOneFragment();
        }
    }

    public void showFindPasswordStepOneFragment() {
        final FindPasswordStepOneFragment findPasswordStepOneFragment = FindPasswordStepOneFragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStepOneFragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showFindPasswordStepTwoFragment() {
        final FindPasswordStepTwoFragment findPasswordStepTwoFragment = FindPasswordStepTwoFragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStepTwoFragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showFindPasswordStepThreeFragment() {
        final FindPasswordStepThreeFragment findPasswordStepThreeFragment = FindPasswordStepThreeFragment.newInstance();

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, findPasswordStepThreeFragment)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showFailedMessage(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
