package cc.brainbook.android.study.mylogin.useraccount.modify.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyEmailException;

public interface ModifyEmailCallback {
    void onSuccess();
    void onError(ModifyEmailException e);
}
