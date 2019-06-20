package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.exception.VerifyCodeException;

public interface VerifyCodeCallback {
    void onSuccess();
    void onError(VerifyCodeException e);
}
