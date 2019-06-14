package cc.brainbook.android.study.mylogin.resetpassword.interfaces;

import cc.brainbook.android.study.mylogin.resetpassword.data.model.ResetPasswordUser;
import cc.brainbook.android.study.mylogin.resetpassword.exception.FindUserException;

public interface FindUserCallback {
    void onSuccess(ResetPasswordUser resetPasswordUser);
    void onError(FindUserException e);
}
