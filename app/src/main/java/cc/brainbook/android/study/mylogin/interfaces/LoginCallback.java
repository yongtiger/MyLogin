package cc.brainbook.android.study.mylogin.interfaces;

import cc.brainbook.android.study.mylogin.data.model.LoggedInUser;
import cc.brainbook.android.study.mylogin.exception.LoginException;

public interface LoginCallback {
    void onSuccess(LoggedInUser loggedInUser);
    void onError(LoginException e);
}
