package cc.brainbook.android.project.login.oauth.listener;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;

///https://github.com/maksim88/EasyLogin
public interface OnLoginCompleteListener {
    /**
     * Called when login complete.
     * @param network id of social network where request was complete
     */
    void onLoginSuccess(SocialNetwork.Network network, AccessToken accessToken);

    void onError(SocialNetwork.Network socialNetwork, String errorMessage);
}
