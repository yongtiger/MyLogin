package cc.brainbook.android.study.mylogin.useraccount.modify.interfaces;

import cc.brainbook.android.study.mylogin.useraccount.modify.exception.ModifyMobileException;

public interface ModifyMobileCallback {
    void onSuccess();
    void onError(ModifyMobileException e);
}
