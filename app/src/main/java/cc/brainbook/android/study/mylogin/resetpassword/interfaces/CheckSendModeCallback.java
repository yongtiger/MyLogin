package cc.brainbook.android.study.mylogin.resetpassword.interfaces;

import cc.brainbook.android.study.mylogin.resetpassword.exception.CheckSendModeException;

public interface CheckSendModeCallback {
    void onSuccess(int sendMode);
    void onError(CheckSendModeException e);
}
