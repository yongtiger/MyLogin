package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.SendVerificationCodeException;

public interface SendVerificationCodeCallback {
    void onSuccess(String sessionId);
    void onError(SendVerificationCodeException e);
}
