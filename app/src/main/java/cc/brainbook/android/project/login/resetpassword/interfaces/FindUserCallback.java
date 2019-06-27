package cc.brainbook.android.project.login.resetpassword.interfaces;

import cc.brainbook.android.project.login.resetpassword.data.model.ResetPasswordUser;
import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;

public interface FindUserCallback {
    void onSuccess(ResetPasswordUser resetPasswordUser);
    void onError(ResetPasswordException e);
}
