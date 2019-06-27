package cc.brainbook.android.project.login.resetpassword.data;

import cc.brainbook.android.project.login.resetpassword.data.model.ResetPasswordUser;
import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;
import cc.brainbook.android.project.login.resetpassword.interfaces.ResetPasswordCallback;

public class ResetPasswordRepository {
    private static volatile ResetPasswordRepository sInstance;

    private ResetPasswordDataSource mResetPasswordDataSource;

    // If mUser credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private ResetPasswordUser mResetPasswordUser = null;

    // private constructor : singleton access
    private ResetPasswordRepository(ResetPasswordDataSource resetPasswordDataSource) {
        mResetPasswordDataSource = resetPasswordDataSource;
    }

    public static ResetPasswordRepository getInstance() {
        if (sInstance == null) {
            sInstance = new ResetPasswordRepository(new ResetPasswordDataSource());
        }
        return sInstance;
    }

    public void findUser(String username, final ResetPasswordCallback resetPasswordCallback) {
        mResetPasswordDataSource.findUser(username, new ResetPasswordCallback(){
            @Override
            public void onSuccess(Object object) {
                final ResetPasswordUser resetPasswordUser = (ResetPasswordUser) object;
                setResetPasswordUser(resetPasswordUser);
                resetPasswordCallback.onSuccess(resetPasswordUser);
            }

            @Override
            public void onError(ResetPasswordException e) {
                resetPasswordCallback.onError(e);
            }
        });
    }

    public void checkSendMode(String userId, String email, String mobile, final ResetPasswordCallback resetPasswordCallback) {
        mResetPasswordDataSource.checkSendMode(userId, email, mobile, new ResetPasswordCallback(){
            @Override
            public void onSuccess(Object object) {
                final int sendMode = (int) object;
                resetPasswordCallback.onSuccess(sendMode);
            }

            @Override
            public void onError(ResetPasswordException e) {
                resetPasswordCallback.onError(e);
            }
        });
    }

    public void sendVerificationCode(String userId, int sendMode, final ResetPasswordCallback resetPasswordCallback) {
        mResetPasswordDataSource.sendVerificationCode(userId, sendMode, new ResetPasswordCallback(){
            @Override
            public void onSuccess(Object object) {
                final String sessionId = (String) object;
                resetPasswordCallback.onSuccess(sessionId);
            }

            @Override
            public void onError(ResetPasswordException e) {
                resetPasswordCallback.onError(e);
            }
        });
    }

    public void verifyCode(String sessionId, String verificationCode, final ResetPasswordCallback resetPasswordCallback) {
        mResetPasswordDataSource.verifyCode(sessionId, verificationCode, new ResetPasswordCallback(){
            @Override
            public void onSuccess(Object object) {
                resetPasswordCallback.onSuccess(null);
            }

            @Override
            public void onError(ResetPasswordException e) {
                resetPasswordCallback.onError(e);
            }
        });
    }

    public void resetPassword(String userId, String password, final ResetPasswordCallback resetPasswordCallback) {
        mResetPasswordDataSource.resetPassword(userId, password, new ResetPasswordCallback(){
            @Override
            public void onSuccess(Object object) {
                resetPasswordCallback.onSuccess(null);
            }

            @Override
            public void onError(ResetPasswordException e) {
                resetPasswordCallback.onError(e);
            }
        });
    }

    public ResetPasswordUser getResetPasswordUser() {
        return mResetPasswordUser;
    }

    private void setResetPasswordUser(ResetPasswordUser resetPasswordUser) {
        mResetPasswordUser = resetPasswordUser;
        // If mUser credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

}
