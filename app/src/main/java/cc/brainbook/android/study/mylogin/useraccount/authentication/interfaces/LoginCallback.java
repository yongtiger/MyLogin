package cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.data.model.LoggedInUser;
import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.LoginException;

public interface LoginCallback {
    void onSuccess(LoggedInUser loggedInUser);
    void onError(LoginException e);
}
