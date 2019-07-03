package cc.brainbook.android.project.login.useraccount.modify;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.config.Config;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;
import cc.brainbook.android.project.login.util.S3TransferUitl;

public class ModifyActivity extends AppCompatActivity {
    private ModifyFragment modifyFragment;

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
        modifyFragment = ModifyFragment.newInstance();
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
                ///删除头像文件（当Camera拍照会产生）
                modifyFragment.removeAvatarFiles();

                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    final File file = new File(resultUri.getPath());
                    final String userId = UserRepository.getInstance().getLoggedInUser().getUserId();
                    final String key = userId + "/" + file.getName();
                    ///[avatar#上传#AWS S3 Transfer Utility]
                    https://stackoverflow.com/questions/5657411/android-getting-a-file-uri-from-a-content-uri
//                    awsS3Upload(new File(resultUri.toString()));
                    awsS3Upload(file, key);
                }

            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                if (cropError != null) {
                    Toast.makeText(getApplicationContext(), cropError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    ///[avatar#上传#AWS S3 Transfer Utility]
    ///https://blog.csdn.net/codehxy/article/details/78105321
    private S3TransferUitl s3TransferUitl = new S3TransferUitl();
    private void awsS3Upload(File file, String key) {
        if (file == null || file.isDirectory() || !file.exists()) {
            Toast.makeText(getApplicationContext(), "Could not find the selected file", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            TransferUtility transferUtility = s3TransferUitl.getTransferUtility(this);
            TransferObserver observer =
                    transferUtility.upload(Config.BUCKET_NAME, key, file);

            /*
             * Note that usually we set the transfer listener after initializing the
             * transfer. However it isn't required in this sample app. The flow is
             * click upload button -> start an activity for image selection
             * startActivityForResult -> onActivityResult -> beginUpload -> onResume
             * -> set listeners to in progress transfers.
             */
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state == TransferState.COMPLETED) {
                        ///获得上传头像的下载Url
                        final String avatarUrl = s3TransferUitl.getSignatureUrl(ModifyActivity.this, key);

                        ///[avatar#上传完成后的处理]
                        modifyFragment.onAvatarUploadComplete(avatarUrl);
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {}

                @Override
                public void onError(int id, Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
