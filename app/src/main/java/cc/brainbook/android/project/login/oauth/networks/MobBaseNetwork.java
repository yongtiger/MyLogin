package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnOauthCompleteListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public abstract class MobBaseNetwork extends SocialNetwork implements PlatformActionListener {
    private Platform plat;

    public MobBaseNetwork(Activity activity, View button, OnOauthCompleteListener onOauthCompleteListener, Platform plat) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onOauthCompleteListener;
        this.plat = plat;

        this.button.get().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    setButtonEnabled(false);
                    plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面

                    ///[FIX BUG]从RegisterActivity返回到LoginActivity后，Mob按钮点击后无onComplete状态！
                    plat.setPlatformActionListener(MobBaseNetwork.this);
                }
            }
        });

        //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.SSOSetting(false);
        //授权回调监听，监听onComplete，onOauthError，onCancel三种状态
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
        if (button != null && button.get() != null
                && activity != null && activity.get() != null) {
            activity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.get().setEnabled(enabled);
                }
            });
        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        if (action == Platform.ACTION_USER_INFOR && activity.get() != null) {
            ///http://wiki.mob.com/%E8%8E%B7%E5%8F%96%E6%8E%88%E6%9D%83%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99-2/
            ///获取数平台数据DB
            final PlatformDb platDB = platform.getDb();
            ///通过DB获取各种数据
            //////??????注意：无Email！
            accessToken = new AccessToken.Builder(platDB.getToken())
                    .network(getNetwork().toString())
                    .secret(platDB.getTokenSecret())
                    .openId(platDB.getUserId())
                    .username(platDB.getUserName())
                    .avatar(platDB.getUserIcon())   ///[EasyLogin#avatar]
                    .build();

            addExtraData(hashMap);

            callOauthSuccess();
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        callOauthFailure(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int action) {
        callOauthFailure("Authorization failed, request was canceled.");
    }

    /**
     * 给accessToken添加数据
     * 各个平台hashMap返回值不同，需要各平台子类具体处理
     *
     * @param hashMap
     */
    public abstract void addExtraData(HashMap<String, Object> hashMap);

}
