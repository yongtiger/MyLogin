package cc.brainbook.android.project.user.account.modify.interfaces;

import cc.brainbook.android.project.user.account.modify.exception.OauthUnbindException;

public interface OauthUnbindCallback {
    void onSuccess(String[] networks);
    void onError(OauthUnbindException e);
}
