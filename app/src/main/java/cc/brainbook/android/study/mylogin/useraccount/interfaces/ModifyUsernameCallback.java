package cc.brainbook.android.study.mylogin.useraccount.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyUsernameException;

public interface ModifyUsernameCallback {
    void onSuccess();
    void onError(ModifyUsernameException e);
}
