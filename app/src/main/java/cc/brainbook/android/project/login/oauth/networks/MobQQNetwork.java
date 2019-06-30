package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.config.Config;
import cc.brainbook.android.project.login.oauth.listener.OnOauthCompleteListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobQQNetwork extends MobBaseNetwork {

    public MobQQNetwork(Activity activity, View button, OnOauthCompleteListener onOauthCompleteListener) {
        super(activity, button, onOauthCompleteListener, ShareSDK.getPlatform(QQ.NAME));
    }

    @Override
    public Config.Network getNetwork() {
        return Config.Network.MOB_QQ;
    }

    @Override
    public void addExtraData(HashMap<String, Object> hashMap) {

    }

}
