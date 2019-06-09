package cc.brainbook.android.study.mylogin.interfaces;

import cc.brainbook.android.study.mylogin.exception.RegisterException;

public interface RegisterCallback {
    void onSuccess();
    void onError(RegisterException e);
}
