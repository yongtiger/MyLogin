package cc.brainbook.android.project.login.useraccount.modify.interfaces;

import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException;

public interface ModifyCallback {
    void onSuccess();
    void onError(ModifyException e);
}
