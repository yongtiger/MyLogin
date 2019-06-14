package cc.brainbook.android.study.mylogin.userauthentication.interfaces;

import cc.brainbook.android.study.mylogin.userauthentication.exception.LogoutException;

public interface LogoutCallback {
    void onSuccess();
    void onError(LogoutException e);
}
