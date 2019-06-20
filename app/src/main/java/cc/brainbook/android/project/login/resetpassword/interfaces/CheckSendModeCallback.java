package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.CheckSendModeException;

public interface CheckSendModeCallback {
    void onSuccess(int sendMode);
    void onError(CheckSendModeException e);
}
