package cc.brainbook.android.project.login.useraccount.authentication.interfaces;

import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException;

public interface LoginCallback {
    void onSuccess(LoggedInUser loggedInUser);
    void onError(LoginException e);
}
