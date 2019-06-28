package cc.brainbook.android.project.login.useraccount.data;

import java.util.HashMap;

import cc.brainbook.android.project.login.application.MyApplication;
import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;
import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException;
import cc.brainbook.android.project.login.useraccount.modify.interfaces.ModifyCallback;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LogoutException;
import cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.LoginCallback;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.LogoutCallback;
import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.RegisterCallback;
import cc.brainbook.android.project.login.util.PrefsUtil;

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

    ///[oAuth#NetworkAccessTokenMap]
    public void register(String username, String password,
                         HashMap<SocialNetwork.Network, AccessToken> networkAccessTokenMap, final RegisterCallback registerCallback) {
        // handle login
        mDataSource.register(username, password, networkAccessTokenMap, new RegisterCallback(){
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

    ///[oAuth#NetworkAccessTokenMap]
    public void login(String username, String password,
                      HashMap<SocialNetwork.Network, AccessToken> networkAccessTokenMap, final LoginCallback loginCallback) {
        // handle login
        mDataSource.login(username, password, networkAccessTokenMap, new LoginCallback(){
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

    public void logout(final LogoutCallback logoutCallback) {
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


    /* --------------------- ///[Modify Account] --------------------- */
    public void modifyUsername(final String username, final ModifyCallback modifyCallback) {
        mDataSource.modifyUsername(getLoggedInUser(), username, new ModifyCallback(){
            @Override
            public void onSuccess() {
                getLoggedInUser().setUsername(username);
                setLoggedInUser(getLoggedInUser());
                modifyCallback.onSuccess();
            }

            @Override
            public void onError(ModifyException e) {
                modifyCallback.onError(e);
            }
        });
    }

    public void modifyPassword(String password, final ModifyCallback modifyCallback) {
        mDataSource.modifyPassword(getLoggedInUser(), password, new ModifyCallback(){
            @Override
            public void onSuccess() {
                modifyCallback.onSuccess();
            }

            @Override
            public void onError(ModifyException e) {
                modifyCallback.onError(e);
            }
        });
    }

    public void modifyEmail(final String email, final ModifyCallback modifyCallback) {
        mDataSource.modifyEmail(getLoggedInUser(), email, new ModifyCallback(){
            @Override
            public void onSuccess() {
                getLoggedInUser().setEmail(email);
                setLoggedInUser(getLoggedInUser());
                modifyCallback.onSuccess();
            }

            @Override
            public void onError(ModifyException e) {
                modifyCallback.onError(e);
            }
        });
    }

    public void modifyMobile(final String mobile, final ModifyCallback modifyCallback) {
        mDataSource.modifyMobile(getLoggedInUser(), mobile, new ModifyCallback(){
            @Override
            public void onSuccess() {
                getLoggedInUser().setMobile(mobile);
                setLoggedInUser(getLoggedInUser());
                modifyCallback.onSuccess();
            }

            @Override
            public void onError(ModifyException e) {
                modifyCallback.onError(e);
            }
        });
    }


    /* --------------------- ///[oAuth] --------------------- */
    ///[oAuth#oAuthLogin]
    ///[oAuth#NetworkAccessTokenMap]
    public void oAuthLogin(SocialNetwork.Network network, AccessToken accessToken,
                           HashMap<SocialNetwork.Network, AccessToken> networkAccessTokenMap, final LoginCallback loginCallback) {
        // handle login
        mDataSource.oAuthLogin(network, accessToken, networkAccessTokenMap, new LoginCallback(){
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

}
