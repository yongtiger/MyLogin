package cc.brainbook.android.project.user.account.authentication.interfaces;

import cc.brainbook.android.project.user.account.authentication.exception.LogoutException;

public interface LogoutCallback {
    void onSuccess();
    void onError(LogoutException e);
}
