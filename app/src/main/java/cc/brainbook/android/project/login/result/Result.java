package cc.brainbook.android.project.login.result;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;

/**
 * Result : success or error message.
 */
public class Result {
    @Nullable @StringRes
    private Integer success;
    @Nullable @StringRes
    private Integer error;

    ///[oAuth#oAuthLogin]
    private SocialNetwork.Network network;
    private AccessToken accessToken;

    public Result(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        this.success = success;
        this.error = error;
    }

    ///[oAuth#oAuthLogin]
    public Result(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error, SocialNetwork.Network network, AccessToken accessToken) {
        this.success = success;
        this.error = error;
        this.network = network;
        this.accessToken = accessToken;
    }

    @Nullable @StringRes
    public Integer getSuccess() {
        return success;
    }

    @Nullable @StringRes
    public Integer getError() {
        return error;
    }

    public Object getNetwork() {
        return network;
    }

    ///[oAuth#oAuthLogin]
    public Object getAccessToken() {
        return accessToken;
    }
}
