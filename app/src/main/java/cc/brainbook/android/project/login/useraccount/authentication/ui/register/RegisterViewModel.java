package cc.brainbook.android.project.login.useraccount.authentication.ui.register;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import java.util.regex.Pattern;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.result.Result;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;
import cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.RegisterCallback;

import static cc.brainbook.android.project.login.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.project.login.config.Config.REGEXP_USERNAME;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException.EXCEPTION_INVALID_PARAMETERS;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException.EXCEPTION_IO_EXCEPTION;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException.EXCEPTION_UNKNOWN;
import static cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException.EXCEPTION_USER_EXISTS;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<Result> result = new MutableLiveData<>();
    private MutableLiveData<Boolean> passwordVisibility = new MutableLiveData<>();
    private MutableLiveData<Boolean> repeatPasswordVisibility = new MutableLiveData<>();

    private UserRepository registerRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    RegisterViewModel(UserRepository registerRepository, boolean passwordVisibility, boolean repeatPasswordVisibility) {
        this.registerRepository = registerRepository;

        ///[EditText显示/隐藏Password]初始化
        this.passwordVisibility.setValue(passwordVisibility);
        this.repeatPasswordVisibility.setValue(repeatPasswordVisibility);
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<Result> getResult() {
        return result;
    }

    ///[EditText显示/隐藏Password]
    public LiveData<Boolean> getPasswordVisibility() {
        return passwordVisibility;
    }
    public void setPasswordVisibility(boolean isVisible) {
        passwordVisibility.setValue(isVisible);
    }
    public LiveData<Boolean> getRepeatPasswordVisibility() {
        return repeatPasswordVisibility;
    }
    public void setRepeatPasswordVisibility(boolean isVisible) {
        repeatPasswordVisibility.setValue(isVisible);
    }

    public void register(String username, String password) {
        // can be launched in a separate asynchronous job
        registerRepository.register(username, password, new RegisterCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                result.postValue(new Result(R.string.result_success_register, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(RegisterException e) {
                ///use live data's postValue(..) method from background thread.
                result.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void registerDataChanged(String username, String password, String repeatPassword) {
        ///[EditText错误提示]
        ///[FIX#只显示username或password其中一个错误提示！应该同时都显示]
        registerFormState.setValue(new RegisterFormState(
                isUsernameValid(username) ? null : R.string.error_invalid_username,
                isPasswordValid(password) ? null : R.string.error_invalid_password,
                isRepeatPasswordValid(password, repeatPassword) ? null : R.string.error_invalid_repeat_password));
    }

    // A placeholder username validation
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && Pattern.matches(REGEXP_USERNAME, username);
    }

    // A placeholder password validation
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && Pattern.matches(REGEXP_PASSWORD, password);
    }

    // A placeholder repeat password validation
    private boolean isRepeatPasswordValid(String password, String repeatPassword) {
        return !TextUtils.isEmpty(password) && password.equals(repeatPassword);
    }

    private int getErrorIntegerRes(RegisterException e) {
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
            case EXCEPTION_USER_EXISTS:
                error = R.string.register_error_user_exists;
                break;
            default:
                error = R.string.error_unknown;
        }
        return error;
    }
}
