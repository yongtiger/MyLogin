package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.lang.ref.WeakReference;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;

///https://github.com/maksim88/EasyLogin
public abstract class SocialNetwork {

    protected static final String SHARED_PREFS_NAME = "easy_login_prefs";

    public enum Network {
        GOOGLE, FACEBOOK, TWITTER, MOB_FACEBOOK, MOB_TWITTER, MOB_LINKEDIN, MOB_QQ, MOB_WECHAT, MOB_SINAWEIBO
    }

    protected WeakReference<Activity> activity;

    protected WeakReference<View> button;

    protected OnLoginCompleteListener listener;

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

}
