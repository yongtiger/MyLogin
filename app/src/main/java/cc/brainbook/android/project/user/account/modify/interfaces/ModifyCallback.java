package cc.brainbook.android.project.user.account.modify.interfaces;

import cc.brainbook.android.project.user.account.modify.exception.ModifyException;

public interface ModifyCallback {
    void onSuccess();
    void onError(ModifyException e);
}
