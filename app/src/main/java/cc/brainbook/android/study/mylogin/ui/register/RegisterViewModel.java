package cc.brainbook.android.study.mylogin.ui.register;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.study.mylogin.R;
import cc.brainbook.android.study.mylogin.data.UserRepository;
import cc.brainbook.android.study.mylogin.exception.RegisterException;
import cc.brainbook.android.study.mylogin.interfaces.RegisterCallback;

import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_USERNAME;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
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

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
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

    public void register(String username, String password, String email, String mobile) {
        // can be launched in a separate asynchronous job
        registerRepository.register(username, password, email, mobile, new RegisterCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                registerResult.postValue(new RegisterResult(true));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(RegisterException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -1:
                        error = R.string.register_exception_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.register_exception_user_exists;
                        break;
                    case 2:
                        error = R.string.register_exception_email_exists;
                        break;
                    case 3:
                        error = R.string.register_exception_mobile_exists;
                        break;
                    default:
                        error = R.string.register_exception_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                registerResult.postValue(new RegisterResult(error));
            }
        });
    }

    public void registerDataChanged(String username, String password, String repeatPassword, String email, String mobile) {
        ///[EditText错误提示]
        ///[FIX#只显示username或password其中一个错误提示！应该同时都显示]
        boolean isUserNameValid = isUserNameValid(username),
                isPasswordValid = isPasswordValid(password),
                isRepeatPasswordValid = isRepeatPasswordValid(password, repeatPassword),
                isEmailValid = isEmailValid(email),
                isMobileValid = isMobileValid(mobile);
        if (isUserNameValid && isPasswordValid && isRepeatPasswordValid && isEmailValid && isMobileValid){
            registerFormState.setValue(new RegisterFormState(true));
        } else {
            registerFormState.setValue(new RegisterFormState(
                    isUserNameValid ? null : R.string.invalid_username,
                    isPasswordValid ? null : R.string.invalid_password,
                    isRepeatPasswordValid ? null : R.string.invalid_repeat_password,
                    isEmailValid ? null : R.string.invalid_email,
                    isMobileValid ? null : R.string.invalid_mobile));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username != null && Pattern.matches(REGEXP_USERNAME, username);
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && Pattern.matches(REGEXP_PASSWORD, password);
    }

    // A placeholder repeat password validation check
    private boolean isRepeatPasswordValid(String password, String repeatPassword) {
        return password != null && password.equals(repeatPassword);
    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return true;    ///允许不填写
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder username validation check
    private boolean isMobileValid(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return true;    ///允许不填写
        }
        return Patterns.PHONE.matcher(mobile).matches();
    }

}
