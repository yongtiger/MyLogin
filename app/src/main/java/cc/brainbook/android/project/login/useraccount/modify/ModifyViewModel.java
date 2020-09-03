package cc.brainbook.android.project.login.useraccount.modify;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.result.Result;
import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException;
import cc.brainbook.android.project.login.useraccount.modify.interfaces.ModifyCallback;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;

import static cc.brainbook.android.project.login.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.project.login.config.Config.REGEXP_USERNAME;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_FAILED_TO_MODIFY_EMAIL;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_FAILED_TO_MODIFY_MOBILE;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_FAILED_TO_MODIFY_PASSWORD;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_FAILED_TO_MODIFY_USERNAME;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_INVALID_PARAMETERS;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_IO_EXCEPTION;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED;
import static cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException.EXCEPTION_UNKNOWN;

public class ModifyViewModel extends ViewModel {
    private MutableLiveData<ModifyUsernameFormState> modifyUsernameFormState = new MutableLiveData<>();
    private MutableLiveData<ModifyPasswordFormState> modifyPasswordFormState = new MutableLiveData<>();
    private MutableLiveData<ModifyEmailFormState> modifyEmailFormState = new MutableLiveData<>();
    private MutableLiveData<ModifyMobileFormState> modifyMobileFormState = new MutableLiveData<>();

    private MutableLiveData<Result> result;

    private UserRepository userRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    ModifyViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LiveData<ModifyUsernameFormState> getModifyUsernameFormState() {
        return modifyUsernameFormState;
    }
    LiveData<ModifyPasswordFormState> getModifyPasswordFormState() {
        return modifyPasswordFormState;
    }
    LiveData<ModifyEmailFormState> getModifyEmailFormState() {
        return modifyEmailFormState;
    }
    LiveData<ModifyMobileFormState> getModifyMobileFormState() {
        return modifyMobileFormState;
    }
    LiveData<Result> getResult() {
        return result;
    }
    public void setResult() {
        result = new MutableLiveData<>();
    }

    public String getUserId() {
        return userRepository.getLoggedInUser().getUserId();
    }
    public String getEmail() {
        return userRepository.getLoggedInUser().getEmail();
    }
    public String getMobile() {
        return userRepository.getLoggedInUser().getMobile();
    }

    ///[EditText显示/隐藏Password]
    private MutableLiveData<Boolean> passwordVisibility = new MutableLiveData<>();
    public LiveData<Boolean> getPasswordVisibility() {
        return passwordVisibility;
    }
    public void setPasswordVisibility(boolean isVisible) {
        passwordVisibility.setValue(isVisible);
    }
    private MutableLiveData<Boolean> repeatPasswordVisibility = new MutableLiveData<>();
    public LiveData<Boolean> getRepeatPasswordVisibility() {
        return repeatPasswordVisibility;
    }
    public void setRepeatPasswordVisibility(boolean isVisible) {
        repeatPasswordVisibility.setValue(isVisible);
    }

    public void modifyUsername(String username) {
        // can be launched in a separate asynchronous job
        userRepository.modifyUsername(username, new ModifyCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                result.postValue(new Result(R.string.result_success_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyException e) {
                ///use live data's postValue(..) method from background thread.
                result.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void modifyPassword(String password) {
        // can be launched in a separate asynchronous job
        userRepository.modifyPassword(password, new ModifyCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                result.postValue(new Result(R.string.result_success_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyException e) {
                ///use live data's postValue(..) method from background thread.
                result.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void modifyEmail(String email) {
        // can be launched in a separate asynchronous job
        userRepository.modifyEmail(email, new ModifyCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                result.postValue(new Result(R.string.result_success_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyException e) {
                ///use live data's postValue(..) method from background thread.
                result.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void modifyMobile(String mobile) {
        // can be launched in a separate asynchronous job
        userRepository.modifyMobile(mobile, new ModifyCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                result.postValue(new Result(R.string.result_success_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyException e) {
                ///use live data's postValue(..) method from background thread.
                result.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void modifyUsernameDataChanged(String username) {
        modifyUsernameFormState.setValue(new ModifyUsernameFormState(isUsernameValid(username) ? null : R.string.error_invalid_username));
    }

    // A placeholder username validation
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && Pattern.matches(REGEXP_USERNAME, username);
    }

    public void modifyPasswordDataChanged(String password, String repeatPassword) {
        modifyPasswordFormState.setValue(new ModifyPasswordFormState(
                isPasswordValid(password) ? null : R.string.error_invalid_password,
                isRepeatPasswordValid(password, repeatPassword) ? null : R.string.error_invalid_repeat_password));
    }

    // A placeholder password validation
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && Pattern.matches(REGEXP_PASSWORD, password);
    }

    // A placeholder repeat password validation
    private boolean isRepeatPasswordValid(String password, String repeatPassword) {
        return !TextUtils.isEmpty(password) && password.equals(repeatPassword);
    }

    public void modifyEmailDataChanged(String email) {
        modifyEmailFormState.setValue(new ModifyEmailFormState(isEmailValid(email) ? null : R.string.error_invalid_email));
    }

    // A placeholder email validation
    private boolean isEmailValid(String email) {
        return TextUtils.isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void modifyMobileDataChanged(String mobile) {
        modifyMobileFormState.setValue(new ModifyMobileFormState(isMobileValid(mobile) ? null : R.string.error_invalid_mobile));
    }

    // A placeholder mobile validation
    private boolean isMobileValid(String mobile) {
        return TextUtils.isEmpty(mobile) || Patterns.PHONE.matcher(mobile).matches();
    }

    private @StringRes int getErrorIntegerRes(ModifyException e) {
        @StringRes final int error;
        switch (e.getCode()) {
            case EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED:
                error = R.string.result_error_token_is_invalid_or_expired;
                break;
            case EXCEPTION_IO_EXCEPTION:
                error = R.string.error_network_error;
                break;
            case EXCEPTION_UNKNOWN:
                error = R.string.error_unknown;
                break;
            case EXCEPTION_INVALID_PARAMETERS:
                error = R.string.error_invalid_parameters;
                break;
            case EXCEPTION_FAILED_TO_MODIFY_USERNAME:
                error = R.string.result_error_failed_to_modify_username;
                break;
            case EXCEPTION_FAILED_TO_MODIFY_PASSWORD:
                error = R.string.result_error_failed_to_modify_password;
                break;
            case EXCEPTION_FAILED_TO_MODIFY_EMAIL:
                error = R.string.result_error_failed_to_modify_email;
                break;
            case EXCEPTION_FAILED_TO_MODIFY_MOBILE:
                error = R.string.result_error_failed_to_modify_mobile;
                break;
            default:
                error = R.string.error_unknown;
        }
        return  error;
    }

}
