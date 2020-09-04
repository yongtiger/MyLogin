package cc.brainbook.android.project.user.resetpassword.interfaces;

import cc.brainbook.android.project.user.resetpassword.exception.ResetPasswordException;

public interface ResetPasswordCallback {
    void onSuccess(Object object);
    void onError(ResetPasswordException e);
}
