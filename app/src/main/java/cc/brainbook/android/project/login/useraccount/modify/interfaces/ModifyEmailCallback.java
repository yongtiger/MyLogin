package cc.brainbook.android.project.login.useraccount.modify.interfaces;

import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyEmailException;

public interface ModifyEmailCallback {
    void onSuccess();
    void onError(ModifyEmailException e);
}
