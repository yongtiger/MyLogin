package cc.brainbook.android.project.login.useraccount.modify.interfaces;

import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyPasswordException;

public interface ModifyPasswordCallback {
    void onSuccess();
    void onError(ModifyPasswordException e);
}
