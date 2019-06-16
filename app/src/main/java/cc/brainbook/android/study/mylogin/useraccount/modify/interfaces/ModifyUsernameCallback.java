package cc.brainbook.android.study.mylogin.useraccount.modify.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyUsernameException;

public interface ModifyUsernameCallback {
    void onSuccess();
    void onError(ModifyUsernameException e);
}
