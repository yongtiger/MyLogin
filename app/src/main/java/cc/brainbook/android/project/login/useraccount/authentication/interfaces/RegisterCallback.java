package cc.brainbook.android.project.login.useraccount.authentication.interfaces;

import cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException;

public interface RegisterCallback {
    void onSuccess();
    void onError(RegisterException e);
}
