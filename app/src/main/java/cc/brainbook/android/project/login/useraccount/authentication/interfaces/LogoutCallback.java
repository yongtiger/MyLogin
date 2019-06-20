package cc.brainbook.android.project.login.useraccount.authentication.interfaces;

import cc.brainbook.android.project.login.useraccount.authentication.exception.LogoutException;

public interface LogoutCallback {
    void onSuccess();
    void onError(LogoutException e);
}
