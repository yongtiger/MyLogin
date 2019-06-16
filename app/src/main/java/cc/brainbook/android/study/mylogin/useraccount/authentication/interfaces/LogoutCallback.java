package cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.LogoutException;

public interface LogoutCallback {
    void onSuccess();
    void onError(LogoutException e);
}
