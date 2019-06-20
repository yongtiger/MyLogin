package cc.brainbook.android.project.login.useraccount.modify.interfaces;

import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyMobileException;

public interface ModifyMobileCallback {
    void onSuccess();
    void onError(ModifyMobileException e);
}
