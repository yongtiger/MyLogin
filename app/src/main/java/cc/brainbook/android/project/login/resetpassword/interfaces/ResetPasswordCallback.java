package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;

public interface ResetPasswordCallback {
    void onSuccess();
    void onError(ResetPasswordException e);
}
