package cc.brainbook.android.study.mylogin.resetpassword.data;

import cc.brainbook.android.study.mylogin.resetpassword.data.model.ResetPasswordUser;
import cc.brainbook.android.study.mylogin.resetpassword.exception.CheckSendModeException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.FindUserException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.SendVerificationCodeException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.ResetPasswordException;
import cc.brainbook.android.study.mylogin.resetpassword.exception.VerifyCodeException;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.CheckSendModeCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.FindUserCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.ResetPasswordCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.SendVerificationCodeCallback;
import cc.brainbook.android.study.mylogin.resetpassword.interfaces.VerifyCodeCallback;

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

    public void findUser(String username, FindUserCallback findCallback) {
        mResetPasswordDataSource.findUser(username, new FindUserCallback(){
            @Override
            public void onSuccess(ResetPasswordUser resetPasswordUser) {
                setFindPasswordUser(resetPasswordUser);
                findCallback.onSuccess(resetPasswordUser);
            }

            @Override
            public void onError(FindUserException e) {
                findCallback.onError(e);
            }
        });
    }

    public void checkSendMode(String userId, String email, String mobile, CheckSendModeCallback checkSendModeCallback) {
        mResetPasswordDataSource.checkSendMode(userId, email, mobile, new CheckSendModeCallback(){
            @Override
            public void onSuccess(int sendMode) {
                checkSendModeCallback.onSuccess(sendMode);
            }

            @Override
            public void onError(CheckSendModeException e) {
                checkSendModeCallback.onError(e);
            }
        });
    }

    public void sendVerificationCode(String userId, int sendMode, SendVerificationCodeCallback verificationCodeCallback) {
        mResetPasswordDataSource.sendVerificationCode(userId, sendMode, new SendVerificationCodeCallback(){
            @Override
            public void onSuccess(String sessionId) {
                verificationCodeCallback.onSuccess(sessionId);
            }

            @Override
            public void onError(SendVerificationCodeException e) {
                verificationCodeCallback.onError(e);
            }
        });
    }

    public void verifyCode(String sessionId, String verificationCode, VerifyCodeCallback verifyCodeCallback) {
        mResetPasswordDataSource.verifyCode(sessionId, verificationCode, new VerifyCodeCallback(){
            @Override
            public void onSuccess() {
                verifyCodeCallback.onSuccess();
            }

            @Override
            public void onError(VerifyCodeException e) {
                verifyCodeCallback.onError(e);
            }
        });
    }

    public void resetPassword(String userId, String password, ResetPasswordCallback resetPasswordCallback) {
        mResetPasswordDataSource.resetPassword(userId, password, new ResetPasswordCallback(){
            @Override
            public void onSuccess() {
                resetPasswordCallback.onSuccess();
            }

            @Override
            public void onError(ResetPasswordException e) {
                resetPasswordCallback.onError(e);
            }
        });
    }

    public ResetPasswordUser getFindPasswordUser() {
        return mResetPasswordUser;
    }

    private void setFindPasswordUser(ResetPasswordUser resetPasswordUser) {
        mResetPasswordUser = resetPasswordUser;
        // If mUser credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

}
