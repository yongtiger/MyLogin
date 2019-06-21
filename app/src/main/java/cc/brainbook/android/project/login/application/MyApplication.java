package cc.brainbook.android.project.login.application;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;

import cc.brainbook.android.project.login.oauth.EasyLogin;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private static Context sContext;
    public static MyApplication getInstance() {
        return sInstance;
    }
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sContext = getApplicationContext();

        ///[oAuth#EasyLogin]
        EasyLogin.initialize();

        ///[oAuth#MobService]
        ///http://www.mob.com
        ///http://wiki.mob.com/sdk-share-android-3-0-0/
        MobSDK.init(this);
    }

}