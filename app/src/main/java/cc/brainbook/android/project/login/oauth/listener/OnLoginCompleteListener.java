package cc.brainbook.android.project.login.oauth.listener;

import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;

///https://github.com/maksim88/EasyLogin
public interface OnLoginCompleteListener extends NetworkListener {
    /**
     * Called when login complete.
     * @param network id of social network where request was complete
     */
    void onLoginSuccess(SocialNetwork.Network network);
}
