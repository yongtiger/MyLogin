package cc.brainbook.android.study.mylogin.userauthentication.data;

import cc.brainbook.android.study.mylogin.application.MyApplication;
import cc.brainbook.android.study.mylogin.userauthentication.exception.LoginException;
import cc.brainbook.android.study.mylogin.userauthentication.exception.LogoutException;
import cc.brainbook.android.study.mylogin.userauthentication.exception.RegisterException;
import cc.brainbook.android.study.mylogin.userauthentication.interfaces.LoginCallback;
import cc.brainbook.android.study.mylogin.userauthentication.interfaces.LogoutCallback;
import cc.brainbook.android.study.mylogin.userauthentication.data.model.LoggedInUser;
import cc.brainbook.android.study.mylogin.userauthentication.interfaces.RegisterCallback;
import cc.brainbook.android.study.mylogin.util.PrefsUtil;

/**
 * Class that requests authentication and mUser information from the remote data source and
 * maintains an in-memory cache of login status and mUser credentials information.
 */
public class UserRepository {
    private static final String KEY_USER = "user";

    private static volatile UserRepository sInstance;

    private UserDataSource mDataSource;

    // If mUser credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser mUser = null;

    // private constructor : singleton access
    private UserRepository(UserDataSource dataSource) {
        mDataSource = dataSource;

        ///[local storage (SharedPreferences)]Load user
        mUser = (LoggedInUser) PrefsUtil.getFromJson(MyApplication.getContext(), KEY_USER, null, LoggedInUser.class);
    }

    public static UserRepository getInstance() {
        if (sInstance == null) {
            sInstance = new UserRepository(new UserDataSource());
        }
        return sInstance;
    }

    public boolean isLoggedIn() {
        return mUser != null && System.currentTimeMillis() < mUser.getTokenExpiredAt() * 1000;
    }

    public void register(String username, String password, RegisterCallback registerCallback) {
        // handle login
        mDataSource.register(username, password, new RegisterCallback(){
            @Override
            public void onSuccess() {
                registerCallback.onSuccess();
            }

            @Override
            public void onError(RegisterException e) {
                registerCallback.onError(e);
            }
        });
    }

    public void login(String username, String password, LoginCallback loginCallback) {
        // handle login
        mDataSource.login(username, password, new LoginCallback(){
            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                setLoggedInUser(loggedInUser);
                loginCallback.onSuccess(loggedInUser);
            }

            @Override
            public void onError(LoginException e) {
                loginCallback.onError(e);
            }
        });
    }

    public void logout(LogoutCallback logoutCallback) {
        mDataSource.logout(mUser, new LogoutCallback() {
            @Override
            public void onSuccess() {
                logoutCallback.onSuccess();

                ///clear the user in SharedPreferences
                setLoggedInUser(null);
            }

            @Override
            public void onError(LogoutException e) {
                logoutCallback.onError(e);
            }
        });
    }

    public LoggedInUser getLoggedInUser() {
        return mUser;
    }

    private void setLoggedInUser(LoggedInUser user) {
        mUser = user;
        // If mUser credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore

        ///[local storage (SharedPreferences)]Save user
        ///Note: If user is null, clear the user in SharedPreferences
        PrefsUtil.putToJson(MyApplication.getContext(), KEY_USER, user);
    }

}
