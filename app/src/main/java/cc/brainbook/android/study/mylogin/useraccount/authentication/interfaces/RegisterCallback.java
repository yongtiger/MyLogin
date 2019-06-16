package cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.RegisterException;

public interface RegisterCallback {
    void onSuccess();
    void onError(RegisterException e);
}
