package cc.brainbook.android.study.mylogin.useraccount.modify.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyPasswordException;

public interface ModifyPasswordCallback {
    void onSuccess();
    void onError(ModifyPasswordException e);
}
