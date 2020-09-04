package cc.brainbook.android.project.user.oauth.listener;

import cc.brainbook.android.project.user.oauth.AccessToken;
import cc.brainbook.android.project.user.oauth.config.Config;

///https://github.com/maksim88/EasyLogin
public interface OnOauthCompleteListener {
    /**
     * Called when oauth complete.
     * @param network id of social network where request was complete
     */
    void onOauthSuccess(Config.Network network, AccessToken accessToken);

    void onOauthError(Config.Network socialNetwork, String errorMessage);
}
