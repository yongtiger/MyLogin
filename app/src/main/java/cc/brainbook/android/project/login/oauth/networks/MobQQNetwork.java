package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobQQNetwork extends SocialNetwork implements PlatformActionListener {
    private Platform plat = ShareSDK.getPlatform(QQ.NAME);

    public MobQQNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onLoginCompleteListener;

        this.button.get().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面
                }
            }
        });

        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.setPlatformActionListener(MobQQNetwork.this);//授权回调监听，监听onComplete，onError，onCancel三种状态
        ShareSDK.setActivity(activity);//抖音登录适配安卓9.0
    }

    @Nullable
    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public Network getNetwork() {
        return Network.MOB_QQ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

    @Override
    public void logout() {
        if (isConnected()) {
            ///移除授权状态和本地缓存，下次授权会重新授权
            plat.removeAccount(true);
        }
        setButtonEnabled(true);
    }

    @Override
    public boolean isConnected() {
        return plat.isAuthValid();
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        if (button != null && button.get() != null) {
            ((Button)button.get()).setEnabled(enabled);
        }
    }

    private void callLoginSuccess() {
        setButtonEnabled(false);
        listener.onLoginSuccess(getNetwork(), accessToken);
    }

    private void callLoginFailure(final String errorMessage) {
        setButtonEnabled(true);
        listener.onError(getNetwork(), errorMessage);
    }

    ///[oAuth#MobService]
    ///http://wiki.mob.com/sdk-share-android-3-0-0/#map-5
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Log.d("TAG", "onComplete: ");

        if (action == Platform.ACTION_USER_INFOR && activity.get() != null) {
            activity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///http://wiki.mob.com/%E8%8E%B7%E5%8F%96%E6%8E%88%E6%9D%83%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99-2/
                    ///获取数平台数据DB
                    final PlatformDb platDB = platform.getDb();
                    //通过DB获取各种数据
                    accessToken = new AccessToken.Builder(platDB.getToken())
                            .secret(platDB.getTokenSecret())
                            .userId(platDB.getUserId())
//                            .email()???????????????????????????????????????????????????????
                            .userName(platDB.getUserName())
                            .photoUrl(platDB.getUserIcon())   ///[EasyLogin#photoUrl]
                            .build();
                    callLoginSuccess();
                }
            });
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        Log.d("TAG", "onError: ");
        callLoginFailure(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int action) {
        Log.d("TAG", "onCancel: ");
        callLoginFailure("Authorization failed, request was canceled.");
    }

}
