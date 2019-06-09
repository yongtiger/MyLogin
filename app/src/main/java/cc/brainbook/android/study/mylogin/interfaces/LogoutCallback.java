package cc.brainbook.android.study.mylogin.interfaces;

import cc.brainbook.android.study.mylogin.exception.LogoutException;

public interface LogoutCallback {
    void onSuccess();
    void onError(LogoutException e);
}
