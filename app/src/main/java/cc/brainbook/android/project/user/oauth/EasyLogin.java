package cc.brainbook.android.project.user.oauth;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cc.brainbook.android.project.user.oauth.config.Config;
import cc.brainbook.android.project.user.oauth.networks.SocialNetwork;

///https://github.com/maksim88/EasyLogin
public class EasyLogin {

    private static EasyLogin instance = null;

    private HashMap<Config.Network, SocialNetwork> socialNetworksMap = new HashMap<>();

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
        ///If the map previously contained a mapping for the key, the old value is replaced.
        ///https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html#put%28K,%20V%29
        socialNetworksMap.put(socialNetwork.getNetwork(), socialNetwork);
    }

    public boolean hasSocialNetwork(Config.Network network) {
        return socialNetworksMap.containsKey(network);
    }

    public SocialNetwork getSocialNetwork(Config.Network network) {
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
