package cc.brainbook.android.study.mylogin.data;

import cc.brainbook.android.study.mylogin.data.model.FindPasswordUser;
import cc.brainbook.android.study.mylogin.exception.FindPasswordCheckException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordFindException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordRequestException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordResetException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordSendException;
import cc.brainbook.android.study.mylogin.interfaces.FindPasswordCallback;

public class FindPasswordRepository {
    private static volatile FindPasswordRepository sInstance;

    private FindPasswordDataSource mFindPasswordDataSource;

    // If mUser credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private FindPasswordUser mFindPasswordUser = null;

    // private constructor : singleton access
    private FindPasswordRepository(FindPasswordDataSource findPasswordDataSource) {
        mFindPasswordDataSource = findPasswordDataSource;
    }

    public static FindPasswordRepository getInstance() {
        if (sInstance == null) {
            sInstance = new FindPasswordRepository(new FindPasswordDataSource());
        }
        return sInstance;
    }

    public void find(String username, FindPasswordCallback.FindCallback findPasswordFindCallback) {
        mFindPasswordDataSource.find(username, new FindPasswordCallback.FindCallback(){
            @Override
            public void onSuccess(FindPasswordUser findPasswordUser) {
                setFindPasswordUser(findPasswordUser);
                findPasswordFindCallback.onSuccess(findPasswordUser);
            }

            @Override
            public void onError(FindPasswordFindException e) {
                findPasswordFindCallback.onError(e);
            }
        });
    }

    public void check(String userId, String email, String mobile, FindPasswordCallback.CheckCallback findPasswordCheckCallback) {
        mFindPasswordDataSource.check(userId, email, mobile, new FindPasswordCallback.CheckCallback(){
            @Override
            public void onSuccess() {
                findPasswordCheckCallback.onSuccess();
            }

            @Override
            public void onError(FindPasswordCheckException e) {
                findPasswordCheckCallback.onError(e);
            }
        });
    }

    public void request(String userId, FindPasswordCallback.RequestCallback findPasswordRequestCallback) {
        mFindPasswordDataSource.request(userId, new FindPasswordCallback.RequestCallback(){
            @Override
            public void onSuccess() {
                findPasswordRequestCallback.onSuccess();
            }

            @Override
            public void onError(FindPasswordRequestException e) {
                findPasswordRequestCallback.onError(e);
            }
        });
    }

    public void send(String userId, String verificationCode, FindPasswordCallback.SendCallback findPasswordSendCallback) {
        mFindPasswordDataSource.send(userId, verificationCode, new FindPasswordCallback.SendCallback(){
            @Override
            public void onSuccess() {
                findPasswordSendCallback.onSuccess();
            }

            @Override
            public void onError(FindPasswordSendException e) {
                findPasswordSendCallback.onError(e);
            }
        });
    }

    public void reset(String userId, String password, FindPasswordCallback.ResetCallback findPasswordResetCallback) {
        mFindPasswordDataSource.reset(userId, password, new FindPasswordCallback.ResetCallback(){
            @Override
            public void onSuccess() {
                findPasswordResetCallback.onSuccess();
            }

            @Override
            public void onError(FindPasswordResetException e) {
                findPasswordResetCallback.onError(e);
            }
        });
    }

    public FindPasswordUser getFindPasswordUser() {
        return mFindPasswordUser;
    }

    private void setFindPasswordUser(FindPasswordUser findPasswordUser) {
        mFindPasswordUser = findPasswordUser;
        // If mUser credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

}
