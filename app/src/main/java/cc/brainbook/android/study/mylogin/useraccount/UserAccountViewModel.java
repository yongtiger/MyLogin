package cc.brainbook.android.study.mylogin.useraccount;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.study.mylogin.R;
import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyEmailException;
import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyMobileException;
import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyPasswordException;
import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyUsernameException;
import cc.brainbook.android.study.mylogin.useraccount.interfaces.ModifyEmailCallback;
import cc.brainbook.android.study.mylogin.useraccount.interfaces.ModifyMobileCallback;
import cc.brainbook.android.study.mylogin.useraccount.interfaces.ModifyPasswordCallback;
import cc.brainbook.android.study.mylogin.useraccount.interfaces.ModifyUsernameCallback;
import cc.brainbook.android.study.mylogin.userauthentication.data.UserRepository;

import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_USERNAME;

public class UserAccountViewModel extends ViewModel {
    private MutableLiveData<UserAccountUsernameFormState> userAccountUsernameFormState = new MutableLiveData<>();
    private MutableLiveData<UserAccountPasswordFormState> userAccountPasswordFormState = new MutableLiveData<>();
    private MutableLiveData<UserAccountEmailFormState> userAccountEmailFormState = new MutableLiveData<>();
    private MutableLiveData<UserAccountMobileFormState> userAccountMobileFormState = new MutableLiveData<>();

    private MutableLiveData<UserAccountResult> userAccountResult;

    private UserRepository userRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    UserAccountViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LiveData<UserAccountUsernameFormState> getUserAccountUsernameFormState() {
        return userAccountUsernameFormState;
    }
    LiveData<UserAccountPasswordFormState> getUserAccountPasswordFormState() {
        return userAccountPasswordFormState;
    }
    LiveData<UserAccountEmailFormState> getUserAccountEmailFormState() {
        return userAccountEmailFormState;
    }
    LiveData<UserAccountMobileFormState> getUserAccountMobileFormState() {
        return userAccountMobileFormState;
    }
    LiveData<UserAccountResult> getUserAccountResult() {
        return userAccountResult;
    }
    public void setUserAccountResult() {
        userAccountResult = new MutableLiveData<>();
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
        userRepository.modifyUsername(username, new ModifyUsernameCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                userAccountResult.postValue(new UserAccountResult(R.string.result_success_successfully_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyUsernameException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -3:
                        error = R.string.error_network_error;
                        break;
                    case -2:
                        error = R.string.error_unknown;
                        break;
                    case -1:
                        error = R.string.error_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.result_error_failed_to_modify_username;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                userAccountResult.postValue(new UserAccountResult(null, error));
            }
        });
    }

    public void modifyPassword(String password) {
        // can be launched in a separate asynchronous job
        userRepository.modifyPassword(password, new ModifyPasswordCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                userAccountResult.postValue(new UserAccountResult(R.string.result_success_successfully_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyPasswordException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -3:
                        error = R.string.error_network_error;
                        break;
                    case -2:
                        error = R.string.error_unknown;
                        break;
                    case -1:
                        error = R.string.error_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.result_error_failed_to_modify_username;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                userAccountResult.postValue(new UserAccountResult(null, error));
            }
        });
    }

    public void modifyEmail(String email) {
        // can be launched in a separate asynchronous job
        userRepository.modifyEmail(email, new ModifyEmailCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                userAccountResult.postValue(new UserAccountResult(R.string.result_success_successfully_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyEmailException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -3:
                        error = R.string.error_network_error;
                        break;
                    case -2:
                        error = R.string.error_unknown;
                        break;
                    case -1:
                        error = R.string.error_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.result_error_failed_to_modify_username;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                userAccountResult.postValue(new UserAccountResult(null, error));
            }
        });
    }

    public void modifyMobile(String mobile) {
        // can be launched in a separate asynchronous job
        userRepository.modifyMobile(mobile, new ModifyMobileCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                userAccountResult.postValue(new UserAccountResult(R.string.result_success_successfully_saved, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ModifyMobileException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -3:
                        error = R.string.error_network_error;
                        break;
                    case -2:
                        error = R.string.error_unknown;
                        break;
                    case -1:
                        error = R.string.error_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.result_error_failed_to_modify_username;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                userAccountResult.postValue(new UserAccountResult(null, error));
            }
        });
    }

    public void userAccountUsernameDataChanged(String username) {
        userAccountUsernameFormState.setValue(new UserAccountUsernameFormState(isUsernameValid(username) ? null : R.string.error_invalid_username));
    }

    // A placeholder username validation
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && Pattern.matches(REGEXP_USERNAME, username);
    }

    public void userAccountPasswordDataChanged(String password, String repeatPassword) {
        userAccountPasswordFormState.setValue(new UserAccountPasswordFormState(
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

    public void userAccountEmailDataChanged(String email) {
        userAccountEmailFormState.setValue(new UserAccountEmailFormState(isEmailValid(email) ? null : R.string.error_invalid_email));
    }

    // A placeholder email validation
    private boolean isEmailValid(String email) {
        return TextUtils.isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void userAccountMobileDataChanged(String mobile) {
        userAccountMobileFormState.setValue(new UserAccountMobileFormState(isMobileValid(mobile) ? null : R.string.error_invalid_mobile));
    }

    // A placeholder mobile validation
    private boolean isMobileValid(String mobile) {
        return TextUtils.isEmpty(mobile) || Patterns.PHONE.matcher(mobile).matches();
    }

}
