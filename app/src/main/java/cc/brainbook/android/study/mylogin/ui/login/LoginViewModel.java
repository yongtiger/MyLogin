package cc.brainbook.android.study.mylogin.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.study.mylogin.exception.LoginException;
import cc.brainbook.android.study.mylogin.interfaces.LoginCallback;
import cc.brainbook.android.study.mylogin.data.UserRepository;
import cc.brainbook.android.study.mylogin.data.model.LoggedInUser;
import cc.brainbook.android.study.mylogin.R;
import cc.brainbook.android.study.mylogin.ui.login.view.LoggedInUserView;

import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_USERNAME;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
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

    LiveData<LoginResult> getLoginResult() {
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
                loginResult.postValue(new LoginResult(new LoggedInUserView(loggedInUser.getUsername())));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(LoginException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -1:
                        error = R.string.login_exception_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.login_exception_invalid_username;
                        break;
                    case 2:
                        error = R.string.login_exception_invalid_password;
                        break;
                    default:
                        error = R.string.login_exception_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                loginResult.postValue(new LoginResult(error));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        ///[EditText错误提示]
        ///[FIX#只显示username或password其中一个错误提示！应该同时都显示]
        loginFormState.setValue(new LoginFormState(
                isUsernameValid(username) ? null : R.string.invalid_username,
                isPasswordValid(password) ? null : R.string.invalid_password));
    }

    // A placeholder username validation check
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && (Pattern.matches(REGEXP_USERNAME, username)
                || Patterns.EMAIL_ADDRESS.matcher(username).matches()
                || Patterns.PHONE.matcher(username).matches());
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && Pattern.matches(REGEXP_PASSWORD, password);
    }
}
