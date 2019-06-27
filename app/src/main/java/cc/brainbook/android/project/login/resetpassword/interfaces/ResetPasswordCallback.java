package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;

public interface ResetPasswordCallback {
    void onSuccess(Object object);
    void onError(ResetPasswordException e);
}
