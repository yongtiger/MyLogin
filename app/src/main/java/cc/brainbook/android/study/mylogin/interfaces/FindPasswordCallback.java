package cc.brainbook.android.study.mylogin.interfaces;

import cc.brainbook.android.study.mylogin.data.model.FindPasswordUser;
import cc.brainbook.android.study.mylogin.exception.FindPasswordCheckException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordFindException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordRequestException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordResetException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordSendException;

public interface FindPasswordCallback {
    interface FindCallback {
        void onSuccess(FindPasswordUser findPasswordUser);
        void onError(FindPasswordFindException e);
    }

    interface CheckCallback {
        void onSuccess();
        void onError(FindPasswordCheckException e);
    }

    interface RequestCallback {
        void onSuccess();
        void onError(FindPasswordRequestException e);
    }

    interface SendCallback {
        void onSuccess();
        void onError(FindPasswordSendException e);
    }

    interface ResetCallback {
        void onSuccess();
        void onError(FindPasswordResetException e);
    }
}
