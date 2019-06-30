package cc.brainbook.android.project.login.oauth.listener;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.config.Config;

///https://github.com/maksim88/EasyLogin
public interface OnOauthCompleteListener {
    /**
     * Called when oauth complete.
     * @param network id of social network where request was complete
     */
    void onOauthSuccess(Config.Network network, AccessToken accessToken);

    void onOauthError(Config.Network socialNetwork, String errorMessage);
}
