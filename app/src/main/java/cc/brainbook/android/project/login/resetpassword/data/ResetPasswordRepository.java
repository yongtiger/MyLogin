package cc.brainbook.android.project.login.resetpassword.data;

import cc.brainbook.android.project.login.resetpassword.data.model.ResetPasswordUser;
import cc.brainbook.android.project.login.resetpassword.exception.CheckSendModeException;
import cc.brainbook.android.project.login.resetpassword.exception.FindUserException;
import cc.brainbook.android.project.login.resetpassword.exception.SendVerificationCodeException;
import cc.brainbook.android.project.login.resetpassword.exception.ResetPasswordException;
import cc.brainbook.android.project.login.resetpassword.exception.VerifyCodeException;
import cc.brainbook.android.project.login.resetpassword.interfaces.CheckSendModeCallback;
import cc.brainbook.android.project.login.resetpassword.interfaces.FindUserCallback;
import cc.brainbook.android.project.login.resetpassword.interfaces.ResetPasswordCallback;
import cc.brainbook.android.project.login.resetpassword.interfaces.SendVerificationCodeCallback;
import cc.brainbook.android.project.login.resetpassword.interfaces.VerifyCodeCallback;

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

    public void findUser(String username, final FindUserCallback findUserCallback) {
        mResetPasswordDataSource.findUser(username, new FindUserCallback(){
            @Override
            public void onSuccess(ResetPasswordUser resetPasswordUser) {
                setResetPasswordUser(resetPasswordUser);
                findUserCallback.onSuccess(resetPasswordUser);
            }

            @Override
            public void onError(FindUserException e) {
                findUserCallback.onError(e);
            }
        });
    }

    public void checkSendMode(String userId, String email, String mobile, final CheckSendModeCallback checkSendModeCallback) {
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

    public void sendVerificationCode(String userId, int sendMode, final SendVerificationCodeCallback verificationCodeCallback) {
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

    public void verifyCode(String sessionId, String verificationCode, final VerifyCodeCallback verifyCodeCallback) {
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

    public void resetPassword(String userId, String password, final ResetPasswordCallback resetPasswordCallback) {
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

    public ResetPasswordUser getResetPasswordUser() {
        return mResetPasswordUser;
    }

    private void setResetPasswordUser(ResetPasswordUser resetPasswordUser) {
        mResetPasswordUser = resetPasswordUser;
        // If mUser credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

}
