package cc.brainbook.android.project.login.application;

import android.app.Application;
import android.content.Context;

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
    }

}