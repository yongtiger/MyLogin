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
import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException;
import cc.brainbook.android.project.login.useraccount.modify.interfaces.ModifyCallback;
import cc.brainbook.android.project.login.util.S3TransferUitl;

import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_FAILED_TO_MODIFY_AVATAR;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_INVALID_PARAMETERS;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_IO_EXCEPTION;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_UNKNOWN;

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
        final ModifyFragment modifyFragment = ModifyFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, modifyFragment)
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

        ///[avatar#裁剪/压缩#Yalantis/uCrop]fragment中onActivityResult()方法不会被调用！
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    final File file = new File(resultUri.getPath());
                    final String userId = UserRepository.getInstance().getLoggedInUser().getUserId();
                    final String key = userId + "/" + file.getName();
                    ///[avatar#上传#AWS S3 Transfer Utility]
                    //https://stackoverflow.com/questions/5657411/android-getting-a-file-uri-from-a-content-uri
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
                        onAvatarUploadComplete(avatarUrl);
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

    ///[avatar#上传完成后的处理]
    private void onAvatarUploadComplete(String avatarUrl) {
        ///修改数据库中user的avatar
        final UserRepository userRepository = UserRepository.getInstance();
        userRepository.modifyAvatar(avatarUrl, new ModifyCallback() {
            @Override
            public void onSuccess() {
                showModifyFragment();
            }

            @Override
            public void onError(ModifyException e) {
                Toast.makeText(getApplicationContext(), getErrorIntegerRes(e), Toast.LENGTH_LONG).show();
            }
        });
    }

    private @StringRes int getErrorIntegerRes(ModifyException e) {
        @StringRes final int error;
        switch (e.getCode()) {
            case EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED:
                error = R.string.result_error_token_is_invalid_or_expired;
                break;
            case EXCEPTION_IO_EXCEPTION:
                error = R.string.error_network_error;
                break;
            case EXCEPTION_UNKNOWN:
                error = R.string.error_unknown;
                break;
            case EXCEPTION_INVALID_PARAMETERS:
                error = R.string.error_invalid_parameters;
                break;
            case EXCEPTION_FAILED_TO_MODIFY_AVATAR:
                error = R.string.result_error_failed_to_modify_username;
                break;
            default:
                error = R.string.error_unknown;
        }
        return  error;
    }
}
