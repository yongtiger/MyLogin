package cc.brainbook.android.study.mylogin.userauthentication.interfaces;

import cc.brainbook.android.study.mylogin.userauthentication.exception.RegisterException;

public interface RegisterCallback {
    void onSuccess();
    void onError(RegisterException e);
}
