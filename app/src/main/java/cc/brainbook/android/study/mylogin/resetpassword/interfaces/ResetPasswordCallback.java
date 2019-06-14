package cc.brainbook.android.study.mylogin.resetpassword.interfaces;

import cc.brainbook.android.study.mylogin.resetpassword.exception.ResetPasswordException;

public interface ResetPasswordCallback {
    void onSuccess();
    void onError(ResetPasswordException e);
}
