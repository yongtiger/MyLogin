package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.study.mylogin.R;
import cc.brainbook.android.study.mylogin.resetpassword.data.ResetPasswordRepository;
import cc.brainbook.android.study.mylogin.resetpassword.data.model.ResetPasswordUser;
import cc.brainbook.android.study.mylogin.resetpassword.exception.CheckSendModeException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.FindUserException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.SendVerificationCodeException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.ResetPasswordException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.VerifyCodeException;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.CheckSendModeCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.FindUserCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.ResetPasswordCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.SendVerificationCodeCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.VerifyCodeCallback;

import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_PASSWORD;
import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_USERNAME;
import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_VERIFICATION_CODE;

public class FindPasswordViewModel extends ViewModel {
    private MutableLiveData<FindPasswordStep1FormState> findPasswordStep1FormState = new MutableLiveData<>();
    private MutableLiveData<FindPasswordStep2FormState> findPasswordStep2FormState = new MutableLiveData<>();
    private MutableLiveData<FindPasswordStep3FormState> findPasswordStep3FormState = new MutableLiveData<>();
    private MutableLiveData<FindPasswordStep4FormState> findPasswordStep4FormState = new MutableLiveData<>();

    private MutableLiveData<FindPasswordResult> findPasswordResult;

    private ResetPasswordRepository resetPasswordRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    FindPasswordViewModel(ResetPasswordRepository resetPasswordRepository) {
        this.resetPasswordRepository = resetPasswordRepository;
    }

    LiveData<FindPasswordStep1FormState> getFindPasswordStep1FormState() {
        return findPasswordStep1FormState;
    }
    LiveData<FindPasswordStep2FormState> getFindPasswordStep2FormState() {
        return findPasswordStep2FormState;
    }
    LiveData<FindPasswordStep3FormState> getFindPasswordStep3FormState() {
        return findPasswordStep3FormState;
    }
    LiveData<FindPasswordStep4FormState> getFindPasswordStep4FormState() {
        return findPasswordStep4FormState;
    }

    LiveData<FindPasswordResult> getFindPasswordResult() {
        return findPasswordResult;
    }
    public void resetFindPasswordResult() {
        findPasswordResult = new MutableLiveData<>();
    }

    public String getUserId() {
        return resetPasswordRepository.getFindPasswordUser().getUserId();
    }
    public String getEmail() {
        return resetPasswordRepository.getFindPasswordUser().getEmail();
    }
    public String getMobile() {
        return resetPasswordRepository.getFindPasswordUser().getMobile();
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

    public void findUser(String username) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.findUser(username, new FindUserCallback() {
            @Override
            public void onSuccess(ResetPasswordUser resetPasswordUser) {
                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordResult(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(FindUserException e) {
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
                        error = R.string.error_invalid_username;
                        break;
                    case 2:
                        error = R.string.result_error_cannot_reset_password;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordResult(null, error));
            }
        });
    }

    public void checkSendMode(String email, String mobile) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.checkSendMode(getUserId(), email, mobile, new CheckSendModeCallback() {
            @Override
            public void onSuccess(int sendMode) {
                ///[SendMode传递参数给下个fragment]
                setSendMode(sendMode);

                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordResult(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(CheckSendModeException e) {
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
                        error = R.string.result_error_invalid_user_id;
                        break;
                    case 2:
                        error = R.string.result_error_cannot_reset_password;
                        break;
                    case 3:
                        error = R.string.result_error_not_match_email;
                        break;
                    case 4:
                        error = R.string.result_error_not_match_mobile;
                        break;
                    case 5:
                        error = R.string.result_error_not_match_email_or_mobile;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordResult(null, error));
            }
        });
    }

    public void sendVerificationCode() {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.sendVerificationCode(getUserId(), getSendMode(), new SendVerificationCodeCallback() {
            @Override
            public void onSuccess(String sessionId) {
                ///[SessionId]
                setSessionId(sessionId);

                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordSendVerificationCodeResult(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(SendVerificationCodeException e) {
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
                        error = R.string.result_error_invalid_user_id;
                        break;
                    case 2:
                        error = R.string.result_error_cannot_reset_password;
                        break;
                    case 3:
                        error = R.string.result_error_failed_to_send_email;
                        break;
                    case 4:
                        error = R.string.result_error_failed_to_send_mobile;
                        break;
                    case 5:
                        error = R.string.result_error_failed_to_send_email_and_mobile;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordSendVerificationCodeResult(null, error));
            }
        });
    }

    public void verifyCode(String verificationCode) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.verifyCode(getSessionId(), verificationCode, new VerifyCodeCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordResult(null, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(VerifyCodeException e) {
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
                        error = R.string.result_error_invalid_user_id;
                        break;
                    case 2:
                        error = R.string.result_error_invalid_verification_code;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordResult(null, error));
            }
        });
    }

    public void resetPassword(String password) {
        // can be launched in a separate asynchronous job
        resetPasswordRepository.resetPassword(getUserId(), password, new ResetPasswordCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordResult(R.string.result_success_successfully_reset_password, null));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(ResetPasswordException e) {
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
                        error = R.string.result_error_invalid_user_id;
                        break;
                    default:
                        error = R.string.error_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordResult(null, error));
            }
        });
    }

    public void findPasswordStep1DataChanged(String username) {
        findPasswordStep1FormState.setValue(new FindPasswordStep1FormState(isUsernameValid(username) ? null : R.string.error_invalid_username));
    }

    // A placeholder username validation
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && Pattern.matches(REGEXP_USERNAME, username);
    }

    public void findPasswordStep2DataChanged(String email, String mobile) {
        final boolean isAllEmpty = TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile);
        findPasswordStep2FormState.setValue(new FindPasswordStep2FormState(
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


    public void findPasswordStep3DataChanged(String verificationCode) {
        findPasswordStep3FormState.setValue(new FindPasswordStep3FormState(
                isVerificationCodeValid(verificationCode) ? null : R.string.error_invalid_verification_code,
                !TextUtils.isEmpty(getSessionId()))); ///[SessionId]
    }

    // A placeholder verification code validation
    private boolean isVerificationCodeValid(String verificationCode) {
        return !TextUtils.isEmpty(verificationCode) && Pattern.matches(REGEXP_VERIFICATION_CODE, verificationCode);
    }

    public void findPasswordStep4DataChanged(String password, String repeatPassword) {
        findPasswordStep4FormState.setValue(new FindPasswordStep4FormState(
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

}
