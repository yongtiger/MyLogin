package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;

public interface CheckSendModeCallback {
    void onSuccess(int sendMode);
    void onError(ResetPasswordException e);
}
