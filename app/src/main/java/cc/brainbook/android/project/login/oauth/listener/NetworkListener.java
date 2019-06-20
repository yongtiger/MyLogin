package cc.brainbook.android.project.login.oauth.listener;

import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;

///https://github.com/maksim88/EasyLogin
interface NetworkListener {

    void onError(SocialNetwork.Network socialNetwork, String errorMessage);
}
