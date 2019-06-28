package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.lang.ref.WeakReference;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnOauthCompleteListener;

///https://github.com/maksim88/EasyLogin
public abstract class SocialNetwork {

    public enum Network {
        ///??????有待解决network重复问题！
        GOOGLE, FACEBOOK, TWITTER, MOB_FACEBOOK, MOB_TWITTER, MOB_LINKEDIN, MOB_QQ, MOB_WECHAT, MOB_SINAWEIBO
    }

    protected WeakReference<Activity> activity;

    protected WeakReference<View> button;

    protected OnOauthCompleteListener listener;

    protected AccessToken accessToken;

    public abstract Network getNetwork();

    public abstract AccessToken getAccessToken();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Logout from social network
     */
    public abstract void logout();

    /**
     * Check if selected social network connected: true or false
     * @return true if connected, else false
     */
    public abstract boolean isConnected();

    /**
     * Enable or disable button
     *
     * @param enabled
     */
    public abstract void setButtonEnabled(boolean enabled);

    protected void callOauthSuccess() {
        setButtonEnabled(false);
        listener.onOauthSuccess(getNetwork(), accessToken);
    }

    protected void callOauthFailure(final String errorMessage) {
        setButtonEnabled(true);
        listener.onOauthError(getNetwork(), errorMessage);
    }

}
