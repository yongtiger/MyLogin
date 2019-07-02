package cc.brainbook.android.project.login.useraccount.modify;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import cc.brainbook.android.project.login.R;

public class ModifyActivity extends AppCompatActivity {

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
        //
        if (savedInstanceState == null) {
            showModifyFragment();
        }
    }

    public void showModifyFragment() {
        ModifyFragment modifyFragment = ModifyFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, modifyFragment)
//                .addToBackStack(null)   ///[关闭其它fragment后回退显示ModifyFragment]注意：第一个Fragment不用添加addToBackStack()，就能解决空白页的问题
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    public void showModifyUsernameFragment() {
        final ModifyUsernameFragment modifyUsernameFragment = ModifyUsernameFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, modifyUsernameFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示ModifyFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showModifyPasswordFragment() {
        final ModifyPasswordFragment modifyPasswordFragment = ModifyPasswordFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, modifyPasswordFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示ModifyFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showModifyEmailFragment() {
        final ModifyEmailFragment modifyEmailFragment = ModifyEmailFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, modifyEmailFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示ModifyFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showModifyMobileFragment() {
        final ModifyMobileFragment modifyMobileFragment = ModifyMobileFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, modifyMobileFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示ModifyFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void showModifyOauthFragment() {
        final ModifyOauthFragment modifyOauthFragment = ModifyOauthFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, modifyOauthFragment)
                .addToBackStack(null)   ///[关闭其它fragment后回退显示ModifyFragment]
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void updateUI(@StringRes Integer successString) {
        Toast.makeText(getApplicationContext(), successString, Toast.LENGTH_LONG).show();
    }

    public void showFailedMessage(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ///[avatar#裁剪/压缩#Yalantis/uCrop]https://github.com/Yalantis/uCrop
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                // todo ...
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                // todo ...
            }
        }
    }

}
