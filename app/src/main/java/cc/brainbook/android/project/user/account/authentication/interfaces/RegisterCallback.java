package cc.brainbook.android.project.user.account.authentication.interfaces;

import cc.brainbook.android.project.user.account.authentication.exception.RegisterException;

public interface RegisterCallback {
    void onSuccess();
    void onError(RegisterException e);
}
