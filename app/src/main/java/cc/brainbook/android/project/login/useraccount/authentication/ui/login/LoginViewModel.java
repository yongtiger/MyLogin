package cc.brainbook.android.project.login.useraccount.authentication.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.HashMap;
import java.util.regex.Pattern;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.config.Config;
import cc.brainbook.android.project.login.result.Result;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.LoginCallback;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;
import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;
import cc.brainbook.android.project.login.R;

import static cc.brainbook.android.project.login.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.project.login.config.Config.REGEXP_USERNAME;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_OAUTH_NETWORK_AND_OPENID;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_PARAMETERS;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_PASSWORD;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_USERNAME;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_IO_EXCEPTION;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_UNKNOWN;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormStateLiveData = new MutableLiveData<>();
    private MutableLiveData<Result> resultLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> passwordVisibilityLiveData = new MutableLiveData<>();

    private UserRepository loginRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    LoginViewModel(UserRepository loginRepository, boolean passwordVisibility) {
        this.loginRepository = loginRepository;

        ///[EditText显示/隐藏Password]初始化
        passwordVisibilityLiveData.setValue(passwordVisibility);

        ///[oAuth#NetworkAccessTokenMap]
        networkAccessTokenMapLiveData.setValue(new HashMap<Config.Network, AccessToken>());
    }

    LiveData<LoginFormState> getLoginFormStateLiveData() {
        return loginFormStateLiveData;
    }

    LiveData<Result> getResultLiveData() {
        return resultLiveData;
    }

    ///[EditText显示/隐藏Password]
    public LiveData<Boolean> getPasswordVisibilityLiveData() {
        return passwordVisibilityLiveData;
    }
    public void setPasswordVisibilityLiveData(boolean isVisible) {
        passwordVisibilityLiveData.setValue(isVisible);
    }

    public void login(String username, String password) {
        ///[oAuth#NetworkAccessTokenMap]
        final HashMap<Config.Network, AccessToken> networkAccessTokenMap = getNetworkAccessTokenMapLiveData().getValue();
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password, networkAccessTokenMap, new LoginCallback() {
            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                ///[返回结果及错误处理]返回结果
                resultLiveData.postValue(new Result(R.string.result_success_login, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(LoginException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        ///[EditText错误提示]
        ///[FIX#只显示username或password其中一个错误提示！应该同时都显示]
        loginFormStateLiveData.setValue(new LoginFormState(
                isUsernameValid(username) ? null : R.string.error_invalid_username,
                isPasswordValid(password) ? null : R.string.error_invalid_password));
    }

    // A placeholder username validation
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && (Pattern.matches(REGEXP_USERNAME, username)
                || Patterns.EMAIL_ADDRESS.matcher(username).matches()
                || Patterns.PHONE.matcher(username).matches());
    }

    // A placeholder password validation
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && Pattern.matches(REGEXP_PASSWORD, password);
    }

    private @StringRes int getErrorIntegerRes(LoginException e) {
        @StringRes final int error;
        switch (e.getCode()) {
            case EXCEPTION_IO_EXCEPTION:
                error = R.string.error_network_error;
                break;
            case EXCEPTION_UNKNOWN:
                error = R.string.error_unknown;
                break;
            case EXCEPTION_INVALID_PARAMETERS:
                error = R.string.error_invalid_parameters;
                break;
            case EXCEPTION_INVALID_USERNAME:
                error = R.string.login_error_invalid_username;
                break;
            case EXCEPTION_INVALID_PASSWORD:
                error = R.string.login_error_invalid_password;
                break;
            case EXCEPTION_INVALID_OAUTH_NETWORK_AND_OPENID:    ///[oAuth]
                error = R.string.error_invalid_oauth_network_and_openid;
                break;
            default:
                error = R.string.error_unknown;
        }
        return error;
    }


    /* --------------------- ///[oAuth] --------------------- */

    ///[oAuth#NetworkAccessTokenMap]
    private MutableLiveData<HashMap<Config.Network, AccessToken>> networkAccessTokenMapLiveData = new MutableLiveData<>();

    LiveData<HashMap<Config.Network, AccessToken>> getNetworkAccessTokenMapLiveData() {
        return networkAccessTokenMapLiveData;
    }

    public void addNetworkAccessTokenMap(Config.Network network, AccessToken accessToken) {
        if (networkAccessTokenMapLiveData.getValue() != null) {
            networkAccessTokenMapLiveData.getValue().put(network, accessToken); ///注意：不能触发onChange
        }
        networkAccessTokenMapLiveData.setValue(networkAccessTokenMapLiveData.getValue());   ///触发onChange
    }

    public void clearNetworkAccessTokenMap() {
        if (networkAccessTokenMapLiveData.getValue() != null) {
            networkAccessTokenMapLiveData.getValue().clear();
        }
    }

    ///[oAuth#oAuthLogin]
    public void oAuthLogin(Config.Network network, AccessToken accessToken) {
        final HashMap<Config.Network, AccessToken> networkAccessTokenMap = getNetworkAccessTokenMapLiveData().getValue();
        // can be launched in a separate asynchronous job
        loginRepository.oAuthLogin(network, accessToken, networkAccessTokenMap, new LoginCallback() {
            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                ///[返回结果及错误处理]返回结果
                resultLiveData.postValue(new Result(R.string.result_success_login, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(LoginException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new Result(null, getErrorIntegerRes(e), network, accessToken));
            }
        });
    }

}
