package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public abstract class MobBaseNetwork extends SocialNetwork implements PlatformActionListener {
    protected Platform plat;

    public MobBaseNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener, Platform plat) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onLoginCompleteListener;
        this.plat = plat;

        this.button.get().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    setButtonEnabled(false);
                    plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面
                }
            }
        });

        //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.SSOSetting(false);
        //授权回调监听，监听onComplete，onError，onCancel三种状态
        plat.setPlatformActionListener(this);
        //抖音登录适配安卓9.0
        ShareSDK.setActivity(activity);
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

    protected void callLoginSuccess() {
        setButtonEnabled(false);
        listener.onLoginSuccess(getNetwork(), accessToken);
    }

    protected void callLoginFailure(final String errorMessage) {
        setButtonEnabled(true);
        listener.onError(getNetwork(), errorMessage);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        doComplete(platform, action, hashMap);
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        callLoginFailure(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int action) {
        callLoginFailure("Authorization failed, request was canceled.");
    }

    public abstract void doComplete(Platform platform, int action, HashMap<String, Object> hashMap);

}
