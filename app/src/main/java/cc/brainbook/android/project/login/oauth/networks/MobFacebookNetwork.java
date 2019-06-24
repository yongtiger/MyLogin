package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.ShareSDK;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobFacebookNetwork extends MobBaseNetwork {

    public MobFacebookNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        super(activity, button, onLoginCompleteListener, ShareSDK.getPlatform(Facebook.NAME));
    }

    @Override
    public Network getNetwork() {
        return Network.MOB_FACEBOOK;
    }

    @Override
    public void addExtraData(HashMap<String, Object> hashMap) {

    }

}
