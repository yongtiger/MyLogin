package cc.brainbook.android.project.login.useraccount.authentication.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;
import cc.brainbook.android.project.login.result.Result;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.LoginCallback;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;
import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;
import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException;

import static cc.brainbook.android.project.login.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.project.login.config.Config.REGEXP_USERNAME;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_OAUTH_NETWORK_AND_OPENID;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_PARAMETERS;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_PASSWORD;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_INVALID_USERNAME;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_IO_EXCEPTION;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException.EXCEPTION_UNKNOWN;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<Result> loginResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> passwordVisibility = new MutableLiveData<>();

    private UserRepository loginRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    LoginViewModel(UserRepository loginRepository, boolean passwordVisibility) {
        this.loginRepository = loginRepository;

        ///[EditText显示/隐藏Password]初始化
        this.passwordVisibility.setValue(passwordVisibility);
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<Result> getResult() {
        return loginResult;
    }

    ///[EditText显示/隐藏Password]
    public LiveData<Boolean> getPasswordVisibility() {
        return passwordVisibility;
    }
    public void setPasswordVisibility(boolean isVisible) {
        passwordVisibility.setValue(isVisible);
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password, new LoginCallback() {
            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                ///[返回结果及错误处理]返回结果
                loginResult.postValue(new Result(R.string.result_success_login, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(LoginException e) {
                ///use live data's postValue(..) method from background thread.
                loginResult.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        ///[EditText错误提示]
        ///[FIX#只显示username或password其中一个错误提示！应该同时都显示]
        loginFormState.setValue(new LoginFormState(
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

    private int getErrorIntegerRes(LoginException e) {
        int error;
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
    public void oAuthLogin(SocialNetwork.Network network, String openId) {
        // can be launched in a separate asynchronous job
        loginRepository.oAuthLogin(network, openId, new LoginCallback() {
            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                ///[返回结果及错误处理]返回结果
                loginResult.postValue(new Result(R.string.result_success_login, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(LoginException e) {
                ///use live data's postValue(..) method from background thread.
                loginResult.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

}
