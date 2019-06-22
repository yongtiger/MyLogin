package cc.brainbook.android.project.login.oauth.networks;

import android.content.Intent;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;

///https://github.com/maksim88/EasyLogin
public abstract class SocialNetwork {

    static final String SHARED_PREFS_NAME = "easy_login_prefs";

    public enum Network {
        EL_GOOGLE, EL_FACEBOOK, EL_TWITTER
    }

    OnLoginCompleteListener listener;

    /**
     * Check if selected social network connected: true or false
     * @return true if connected, else false
     */
    public abstract boolean isConnected();

    public abstract void requestLogin(OnLoginCompleteListener listener);

    public void setListener(OnLoginCompleteListener listener) {
        this.listener = listener;
    }

    /**
     * Logout from social network
     */
    public abstract void logout();

    public abstract Network getNetwork();

    public abstract AccessToken getAccessToken();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

}
