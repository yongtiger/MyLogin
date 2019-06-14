package cc.brainbook.android.study.mylogin.userauthentication.interfaces;

import cc.brainbook.android.study.mylogin.userauthentication.data.model.LoggedInUser;
import cc.brainbook.android.study.mylogin.userauthentication.exception.LoginException;

public interface LoginCallback {
    void onSuccess(LoggedInUser loggedInUser);
    void onError(LoginException e);
}
