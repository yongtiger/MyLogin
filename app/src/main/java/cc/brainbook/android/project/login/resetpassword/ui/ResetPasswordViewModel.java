package cc.brainbook.android.project.login.resetpassword.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.resetpassword.data.ResetPasswordRepository;
import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;
import cc.brainbook.android.project.login.resetpassword.interfaces.ResetPasswordCallback;
import cc.brainbook.android.project.login.result.Result;

import static cc.brainbook.android.project.login.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.project.login.config.Config.REGEXP_USERNAME;
import static cc.brainbook.android.project.login.config.Config.REGEXP_VERIFICATION_CODE;

public class ResetPasswordViewModel extends ViewModel {
    private MutableLiveData<ResetPasswordStep1FormState> resetPasswordStep1FormStateLiveData = new MutableLiveData<>();
    private MutableLiveData<ResetPasswordStep2FormState> resetPasswordStep2FormStateLiveData = new MutableLiveData<>();
    private MutableLiveData<ResetPasswordStep3FormState> resetPasswordStep3FormStateLiveData = new MutableLiveData<>();
    private MutableLiveData<ResetPasswordStep4FormState> resetPasswordStep4FormStateLiveData = new MutableLiveData<>();

    private MutableLiveData<Result> resultLiveData;

    private ResetPasswordRepository resetPasswordRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    ResetPasswordViewModel(ResetPasswordRepository resetPasswordRepository) {
        this.resetPasswordRepository = resetPasswordRepository;
    }

    LiveData<ResetPasswordStep1FormState> getResetPasswordStep1FormStateLiveData() {
        return resetPasswordStep1FormStateLiveData;
    }
    LiveData<ResetPasswordStep2FormState> getResetPasswordStep2FormStateLiveData() {
        return resetPasswordStep2FormStateLiveData;
    }
    LiveData<ResetPasswordStep3FormState> getResetPasswordStep3FormStateLiveData() {
        return resetPasswordStep3FormStateLiveData;
    }
    LiveData<ResetPasswordStep4FormState> getResetPasswordStep4FormStateLiveData() {
        return resetPasswordStep4FormStateLiveData;
    }

    LiveData<Result> getResultLiveData() {
        return resultLiveData;
    }
    public void setResultLiveData() {
        resultLiveData = new MutableLiveData<>();
    }

    public String getUserId() {
        return resetPasswordRepository.getResetPasswordUser().getUserId();
    }
    public String getEmail() {
        return resetPasswordRepository.getResetPasswordUser().getEmail();
    }
    public String getMobile() {
        return resetPasswordRepository.getResetPasswordUser().getMobile();
    }

    ///[SendMode传递参数给下个fragment]
    private int sendMode;
    public int getSendMode() {
        return sendMode;
    }
    public void setSendMode(int sendMode) {
        this.sendMode = sendMode;
    }

    ///[SessionId]
    private String sessionId;
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    ///[EditText显示/隐藏Password]
    private MutableLiveData<Boolean> passwordVisibilityLiveData = new MutableLiveData<>();
    public LiveData<Boolean> getPasswordVisibilityLiveData() {
        return passwordVisibilityLiveData;
    }
    public void setPasswordVisibilityLiveData(boolean isVisible) {
        passwordVisibilityLiveData.setValue(isVisible);
    }
    private MutableLiveData<Boolean> repeatPasswordVisibilityLiveData = new MutableLiveData<>();
    public LiveData<Boolean> getRepeatPasswordVisibilityLiveData() {
        return repeatPasswordVisibilityLiveData;
    }
    public void setRepeatPasswordVisibilityLiveData(boolean isVisible) {
        repeatPasswordVisibilityLiveData.setValue(isVisible);
    }

    public void findUser(String username) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.findUser(username, new ResetPasswordCallback() {
            @Override
            public void onSuccess(Object object) {
                resultLiveData.postValue(new Result(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ResetPasswordException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void checkSendMode(String email, String mobile) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.checkSendMode(getUserId(), email, mobile, new ResetPasswordCallback() {
            @Override
            public void onSuccess(Object object) {
                final int sendMode = (int) object;

                ///[SendMode传递参数给下个fragment]
                setSendMode(sendMode);

                ///[返回结果及错误处理]返回结果
                resultLiveData.postValue(new Result(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ResetPasswordException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void sendVerificationCode() {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.sendVerificationCode(getUserId(), getSendMode(), new ResetPasswordCallback() {
            @Override
            public void onSuccess(Object object) {
                final String sessionId = (String) object;

                ///[SessionId]
                setSessionId(sessionId);

                ///[返回结果及错误处理]返回结果
                resultLiveData.postValue(new ResetPasswordSendVerificationCodeResult(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ResetPasswordException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new ResetPasswordSendVerificationCodeResult(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void verifyCode(String verificationCode) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.verifyCode(getSessionId(), verificationCode, new ResetPasswordCallback() {
            @Override
            public void onSuccess(Object object) {
                ///[返回结果及错误处理]返回结果
                resultLiveData.postValue(new Result(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ResetPasswordException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void resetPassword(String password) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.resetPassword(getUserId(), password, new ResetPasswordCallback() {
            @Override
            public void onSuccess(Object object) {
                ///[返回结果及错误处理]返回结果
                resultLiveData.postValue(new Result(R.string.result_success_reset_password, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ResetPasswordException e) {
                ///use live data's postValue(..) method from background thread.
                resultLiveData.postValue(new Result(null, getErrorIntegerRes(e)));
            }
        });
    }

    public void resetPasswordStep1DataChanged(String username) {
        resetPasswordStep1FormStateLiveData.setValue(new ResetPasswordStep1FormState(isUsernameValid(username) ? null : R.string.error_invalid_username));
    }

    // A placeholder username validation
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && Pattern.matches(REGEXP_USERNAME, username);
    }

    public void resetPasswordStep2DataChanged(String email, String mobile) {
        final boolean isAllEmpty = TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile);
        resetPasswordStep2FormStateLiveData.setValue(new ResetPasswordStep2FormState(
                isEmailValid(email) ? null : R.string.error_invalid_email,
                isMobileValid(mobile) ? null : R.string.error_invalid_mobile,
                isAllEmpty));
    }

    // A placeholder email validation
    private boolean isEmailValid(String email) {
        return TextUtils.isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder mobile validation
    private boolean isMobileValid(String mobile) {
        return TextUtils.isEmpty(mobile) || Patterns.PHONE.matcher(mobile).matches();
    }


    public void resetPasswordStep3DataChanged(String verificationCode) {
        resetPasswordStep3FormStateLiveData.setValue(new ResetPasswordStep3FormState(
                isVerificationCodeValid(verificationCode) ? null : R.string.error_invalid_verification_code,
                !TextUtils.isEmpty(getSessionId()))); ///[SessionId]
    }

    // A placeholder verification code validation
    private boolean isVerificationCodeValid(String verificationCode) {
        return !TextUtils.isEmpty(verificationCode) && Pattern.matches(REGEXP_VERIFICATION_CODE, verificationCode);
    }

    public void resetPasswordStep4DataChanged(String password, String repeatPassword) {
        resetPasswordStep4FormStateLiveData.setValue(new ResetPasswordStep4FormState(
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

    private @StringRes int getErrorIntegerRes(ResetPasswordException e) {
        @StringRes int error;
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
                error = R.string.result_error_cannot_reset_password;
                break;
            case 2:
                error = R.string.error_invalid_username;
                break;
            case 3:
                error = R.string.result_error_invalid_user_id;
                break;
            case 4:
                error = R.string.result_error_not_match_email;
                break;
            case 5:
                error = R.string.result_error_not_match_mobile;
                break;
            case 6:
                error = R.string.result_error_not_match_email_or_mobile;
                break;
            case 7:
                error = R.string.result_error_failed_to_send_email;
                break;
            case 8:
                error = R.string.result_error_failed_to_send_mobile;
                break;
            case 9:
                error = R.string.result_error_failed_to_send_email_and_mobile;
                break;
            case 10:
                error = R.string.result_error_invalid_verification_code;
                break;
            default:
                error = R.string.error_unknown;
        }
        return error;
    }

}
