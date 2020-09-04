package cc.brainbook.android.project.user.account.authentication.interfaces;

import cc.brainbook.android.project.user.account.data.model.LoggedInUser;
import cc.brainbook.android.project.user.account.authentication.exception.LoginException;

public interface LoginCallback {
    void onSuccess(LoggedInUser loggedInUser);
    void onError(LoginException e);
}
