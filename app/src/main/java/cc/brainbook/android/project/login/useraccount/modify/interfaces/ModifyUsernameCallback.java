package cc.brainbook.android.project.login.useraccount.modify.interfaces;

import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyUsernameException;

public interface ModifyUsernameCallback {
    void onSuccess();
    void onError(ModifyUsernameException e);
}
