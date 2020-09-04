package cc.brainbook.android.project.user.oauth.networks;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

import cc.brainbook.android.project.user.oauth.config.Config;
import cc.brainbook.android.project.user.oauth.listener.OnOauthCompleteListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobWechatNetwork extends MobBaseNetwork {

    public MobWechatNetwork(Activity activity, View button, OnOauthCompleteListener onOauthCompleteListener) {
        super(activity, button, onOauthCompleteListener, ShareSDK.getPlatform(Wechat.NAME));
    }

    @Override
    public Config.Network getNetwork() {
        return Config.Network.MOB_WECHAT;
    }

    @Override
    public void addExtraData(HashMap<String, Object> hashMap) {

    }

}
