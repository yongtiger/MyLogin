package cc.brainbook.android.study.mylogin.resetpassword.interfaces;

import cc.brainbook.android.study.mylogin.resetpassword.exception.SendVerificationCodeException;

public interface SendVerificationCodeCallback {
    void onSuccess(String sessionId);
    void onError(SendVerificationCodeException e);
}
