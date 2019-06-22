package cc.brainbook.android.project.login.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.mob.MobSDK;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import cc.brainbook.android.project.login.R;
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

        ///[oAuth#EasyLogin#Twitter]初始化
        ///Twitter initialization needs to happen before setContentView() if using the LoginButton!
        final String twitterKey = getString(R.string.twitter_consumer_key);
        final String twitterSecret = getString(R.string.twitter_consumer_secret);
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        final TwitterConfig config = new TwitterConfig.Builder(getApplicationContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .build();
        Twitter.initialize(config);

        ///[oAuth#MobService]
        ///http://www.mob.com
        ///http://wiki.mob.com/sdk-share-android-3-0-0/
        MobSDK.init(this);
    }

}