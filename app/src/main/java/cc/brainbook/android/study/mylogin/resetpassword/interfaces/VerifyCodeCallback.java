package cc.brainbook.android.study.mylogin.resetpassword.interfaces;

import cc.brainbook.android.study.mylogin.resetpassword.exception.VerifyCodeException;

public interface VerifyCodeCallback {
    void onSuccess();
    void onError(VerifyCodeException e);
}
