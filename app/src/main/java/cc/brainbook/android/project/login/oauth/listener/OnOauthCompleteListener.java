package cc.brainbook.android.project.login.oauth.listener;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;

///https://github.com/maksim88/EasyLogin
public interface OnOauthCompleteListener {
    /**
     * Called when oauth complete.
     * @param network id of social network where request was complete
     */
    void onOauthSuccess(SocialNetwork.Network network, AccessToken accessToken);

    void onOauthError(SocialNetwork.Network socialNetwork, String errorMessage);
}
