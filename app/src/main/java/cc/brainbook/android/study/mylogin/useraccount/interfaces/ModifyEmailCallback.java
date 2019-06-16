package cc.brainbook.android.study.mylogin.useraccount.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyEmailException;

public interface ModifyEmailCallback {
    void onSuccess();
    void onError(ModifyEmailException e);
}
