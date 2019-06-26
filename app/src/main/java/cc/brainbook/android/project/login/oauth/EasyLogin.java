package cc.brainbook.android.project.login.oauth;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;

///https://github.com/maksim88/EasyLogin
public class EasyLogin {

    private static EasyLogin instance = null;

    private Map<SocialNetwork.Network, SocialNetwork> socialNetworksMap = new HashMap<>();

    private EasyLogin() {}

    public static void initialize() {
        if (instance == null) {
            instance = new EasyLogin();
        }
    }

    public static EasyLogin getInstance() {
        return instance;
    }


    public void addSocialNetwork(SocialNetwork socialNetwork) {
        if (!hasSocialNetwork(socialNetwork.getNetwork())) {
            socialNetworksMap.put(socialNetwork.getNetwork(), socialNetwork);
        }
    }

    public boolean hasSocialNetwork(SocialNetwork.Network network) {
        return socialNetworksMap.containsKey(network);
    }

    public SocialNetwork getSocialNetwork(SocialNetwork.Network network) {
        return socialNetworksMap.get(network);
    }

    /**
     * Get list of initialized social networks
     * @return list of initialized social networks
     */
    public List<SocialNetwork> getInitializedSocialNetworks() {
        return Collections.unmodifiableList(new ArrayList<>(socialNetworksMap.values()));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (SocialNetwork network : socialNetworksMap.values()) {
            network.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void logoutAllNetworks() {
        for (SocialNetwork socialNetwork : getInitializedSocialNetworks()) {
            socialNetwork.logout();
        }
    }

}
