package cc.brainbook.android.study.mylogin.useraccount.data;

import cc.brainbook.android.study.mylogin.application.MyApplication;
import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyEmailException;
import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyMobileException;
import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyPasswordException;
import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyUsernameException;
import cc.brainbook.android.study.mylogin.useraccount.modify.interfaces.ModifyEmailCallback;
import cc.brainbook.android.study.mylogin.useraccount.modify.interfaces.ModifyMobileCallback;
import cc.brainbook.android.study.mylogin.useraccount.modify.interfaces.ModifyPasswordCallback;
import cc.brainbook.android.study.mylogin.useraccount.modify.interfaces.ModifyUsernameCallback;
import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.LoginException;
import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.LogoutException;
import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.RegisterException;
import cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces.LoginCallback;
import cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces.LogoutCallback;
import cc.brainbook.android.study.mylogin.useraccount.data.model.LoggedInUser;
import cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces.RegisterCallback;
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

    public boolean isLoggedIn() {
        return getLoggedInUser() != null && System.currentTimeMillis() < getLoggedInUser().getTokenExpiredAt() * 1000;
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

    ///[oAuth]
    public void oAuthLogin(String network, String openId, LoginCallback loginCallback) {
        // handle login
        mDataSource.oAuthLogin(network, openId, new LoginCallback(){
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
        ///[oAuth]
//        oAuthLogout();    //////?????

        mDataSource.logout(getLoggedInUser(), new LogoutCallback() {
            @Override
            public void onSuccess() {
                logoutCallback.onSuccess();

                ///clear the user in SharedPreferences
                setLoggedInUser(null);
            }

            @Override
            public void onError(LogoutException e) {
                ///[返回结果及错误处理]错误处理
                //////?????注意：也返回logout成功，以便清除本地user
                logoutCallback.onError(e);
            }
        });
    }


    public void modifyUsername(String username, ModifyUsernameCallback modifyUsernameCallback) {
        mDataSource.modifyUsername(getLoggedInUser(), username, new ModifyUsernameCallback(){
            @Override
            public void onSuccess() {
                getLoggedInUser().setUsername(username);
                setLoggedInUser(getLoggedInUser());
                modifyUsernameCallback.onSuccess();
            }

            @Override
            public void onError(ModifyUsernameException e) {
                modifyUsernameCallback.onError(e);
            }
        });
    }

    public void modifyPassword(String password, ModifyPasswordCallback modifyPasswordCallback) {
        mDataSource.modifyPassword(getLoggedInUser(), password, new ModifyPasswordCallback(){
            @Override
            public void onSuccess() {
                modifyPasswordCallback.onSuccess();
            }

            @Override
            public void onError(ModifyPasswordException e) {
                modifyPasswordCallback.onError(e);
            }
        });
    }

    public void modifyEmail(String email, ModifyEmailCallback modifyEmailCallback) {
        mDataSource.modifyEmail(getLoggedInUser(), email, new ModifyEmailCallback(){
            @Override
            public void onSuccess() {
                getLoggedInUser().setEmail(email);
                setLoggedInUser(getLoggedInUser());
                modifyEmailCallback.onSuccess();
            }

            @Override
            public void onError(ModifyEmailException e) {
                modifyEmailCallback.onError(e);
            }
        });
    }

    public void modifyMobile(String mobile, ModifyMobileCallback modifyMobileCallback) {
        mDataSource.modifyMobile(getLoggedInUser(), mobile, new ModifyMobileCallback(){
            @Override
            public void onSuccess() {
                getLoggedInUser().setMobile(mobile);
                setLoggedInUser(getLoggedInUser());
                modifyMobileCallback.onSuccess();
            }

            @Override
            public void onError(ModifyMobileException e) {
                modifyMobileCallback.onError(e);
            }
        });
    }

}
