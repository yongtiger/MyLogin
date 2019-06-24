package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class MobQQNetwork extends MobBaseNetwork {

    public MobQQNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        super(activity, button, onLoginCompleteListener, ShareSDK.getPlatform(QQ.NAME));
    }

    @Override
    public void doComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        if (action == Platform.ACTION_USER_INFOR && activity.get() != null) {
            ///http://wiki.mob.com/%E8%8E%B7%E5%8F%96%E6%8E%88%E6%9D%83%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99-2/
            ///获取数平台数据DB
            final PlatformDb platDB = platform.getDb();
            //通过DB获取各种数据
            accessToken = new AccessToken.Builder(platDB.getToken())
                    .secret(platDB.getTokenSecret())
                    .userId(platDB.getUserId())
//                            .email()???????????????????????????????????????????????????????
                    .userName(platDB.getUserName())
                    .photoUrl(platDB.getUserIcon())   ///[EasyLogin#photoUrl]
                    .build();
            callLoginSuccess();
        }
    }

}
