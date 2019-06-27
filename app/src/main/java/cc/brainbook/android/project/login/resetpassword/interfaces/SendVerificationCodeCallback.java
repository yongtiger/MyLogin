package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;

public interface SendVerificationCodeCallback {
    void onSuccess(String sessionId);
    void onError(ResetPasswordException e);
}
