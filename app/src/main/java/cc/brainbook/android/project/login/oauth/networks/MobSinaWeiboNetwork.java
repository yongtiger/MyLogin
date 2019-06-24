package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobSinaWeiboNetwork extends MobBaseNetwork {

    public MobSinaWeiboNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        super(activity, button, onLoginCompleteListener, ShareSDK.getPlatform(SinaWeibo.NAME));
    }

    @Override
    public Network getNetwork() {
        return Network.MOB_SINAWEIBO;
    }

    @Override
    public void addExtraData(HashMap<String, Object> hashMap) {

    }

}
