package cc.brainbook.android.project.login.useraccount.modify.interfaces;

import cc.brainbook.android.project.login.useraccount.modify.exception.OauthUnbindException;

public interface OauthUnbindCallback {
    void onSuccess(String[] networks);
    void onError(OauthUnbindException e);
}
