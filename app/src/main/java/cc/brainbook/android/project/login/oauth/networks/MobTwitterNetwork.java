package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.listener.OnOauthCompleteListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.twitter.Twitter;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobTwitterNetwork extends MobBaseNetwork {

    public MobTwitterNetwork(Activity activity, View button, OnOauthCompleteListener onOauthCompleteListener) {
        super(activity, button, onOauthCompleteListener, ShareSDK.getPlatform(Twitter.NAME));
    }

    @Override
    public Network getNetwork() {
        return Network.MOB_TWITTER;
    }

    @Override
    public void addExtraData(HashMap<String, Object> hashMap) {

    }

}
