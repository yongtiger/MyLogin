package cc.brainbook.android.study.mylogin.useraccount.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.exception.ModifyMobileException;

public interface ModifyMobileCallback {
    void onSuccess();
    void onError(ModifyMobileException e);
}
