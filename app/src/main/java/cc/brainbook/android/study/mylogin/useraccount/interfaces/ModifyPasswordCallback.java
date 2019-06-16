package cc.brainbook.android.study.mylogin.useraccount.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyPasswordException;

public interface ModifyPasswordCallback {
    void onSuccess();
    void onError(ModifyPasswordException e);
}
