package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

import cc.brainbook.android.study.mylogin.R;
import cc.brainbook.android.study.mylogin.data.FindPasswordRepository;
import cc.brainbook.android.study.mylogin.data.model.FindPasswordUser;
import cc.brainbook.android.study.mylogin.exception.FindPasswordCheckException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordFindException;
import cc.brainbook.android.study.mylogin.interfaces.FindPasswordCallback;

import static cc.brainbook.android.study.mylogin.config.Config.REGEXP_USERNAME;

public class FindPasswordViewModel extends ViewModel {
    private MutableLiveData<FindPasswordStepOneFormState> findPasswordStepOneFormState = new MutableLiveData<>();
    private MutableLiveData<FindPasswordStepTwoFormState> findPasswordStepTwoFormState = new MutableLiveData<>();
    private MutableLiveData<FindPasswordResult> findPasswordResult;

    private FindPasswordRepository findPasswordRepository; ///ViewModel should not be doing any data loading tasks. Use Repository instead.

    FindPasswordViewModel(FindPasswordRepository findPasswordRepository) {
        this.findPasswordRepository = findPasswordRepository;
    }

    LiveData<FindPasswordStepOneFormState> getFindPasswordStepOneFormState() {
        return findPasswordStepOneFormState;
    }
    LiveData<FindPasswordStepTwoFormState> getFindPasswordStepTwoFormState() {
        return findPasswordStepTwoFormState;
    }

    LiveData<FindPasswordResult> getFindPasswordResult() {
        return findPasswordResult;
    }
    public void resetFindPasswordResult() {
        findPasswordResult = new MutableLiveData<>();
    }

    public String getUserId() {
        return findPasswordRepository.getFindPasswordUser().getUserId();
    }
    public String getEmail() {
        return findPasswordRepository.getFindPasswordUser().getEmail();
    }
    public String getMobile() {
        return findPasswordRepository.getFindPasswordUser().getMobile();
    }

    public void find(String username) {
        // can be launched in a separate asynchronous job
        findPasswordRepository.find(username, new FindPasswordCallback.FindCallback() {
            @Override
            public void onSuccess(FindPasswordUser findPasswordUser) {
                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordResult(true));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(FindPasswordFindException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -1:
                        error = R.string.find_password_exception_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.find_password_exception_invalid_username;
                        break;
                    case 2:
                        error = R.string.find_password_exception_invalid_cannot_find_password;
                        break;
                    default:
                        error = R.string.find_password_exception_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordResult(error));
            }
        });
    }
    public void check(String email, String mobile) {
        // can be launched in a separate asynchronous job
        findPasswordRepository.check(getUserId(), email, mobile, new FindPasswordCallback.CheckCallback() {
            @Override
            public void onSuccess() {
                ///[返回结果及错误处理]返回结果
                findPasswordResult.postValue(new FindPasswordResult(true));   ///use live data's postValue(..) method from background thread.
            }

            @Override
            public void onError(FindPasswordCheckException e) {
                ///[返回结果及错误处理]错误处理
                int error;
                switch (e.getCode()) {
                    case -1:
                        error = R.string.find_password_exception_invalid_parameters;
                        break;
                    case 1:
                        error = R.string.find_password_exception_invalid_user_id;
                        break;
                    case 2:
                        error = R.string.find_password_exception_invalid_cannot_find_password;
                        break;
                    case 3:
                        error = R.string.find_password_exception_no_match_email;
                        break;
                    case 4:
                        error = R.string.find_password_exception_no_match_mobile;
                        break;
                    case 5:
                        error = R.string.find_password_exception_no_match_email_or_mobile;
                        break;
                    default:
                        error = R.string.find_password_exception_unknown;
                }
                ///use live data's postValue(..) method from background thread.
                findPasswordResult.postValue(new FindPasswordResult(error));
            }
        });
    }

    public void findPasswordStepOneDataChanged(String username) {
        ///[EditText错误提示]
        ///[FIX#只显示username或password其中一个错误提示！应该同时都显示]
        findPasswordStepOneFormState.setValue(new FindPasswordStepOneFormState(isUsernameValid(username) ? null : R.string.invalid_username));
    }

    // A placeholder username validation check
    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && Pattern.matches(REGEXP_USERNAME, username);
    }

    public void findPasswordStepTwoDataChanged(String email, String mobile) {
        ///[EditText错误提示]
        ///email/mobile只要有一个验证通过，即状态校验成功
        final boolean isAllEmpty = TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile);
        findPasswordStepTwoFormState.setValue(new FindPasswordStepTwoFormState(
                isEmailValid(email) ? null : R.string.invalid_email,
                isMobileValid(mobile) ? null : R.string.invalid_mobile,
                isAllEmpty));
    }

    // A placeholder password validation check
    private boolean isEmailValid(String email) {
        return TextUtils.isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder password validation check
    private boolean isMobileValid(String mobile) {
        return TextUtils.isEmpty(mobile) || Patterns.PHONE.matcher(mobile).matches();
    }
}
