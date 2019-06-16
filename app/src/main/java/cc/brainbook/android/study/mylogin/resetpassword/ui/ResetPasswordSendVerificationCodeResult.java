package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import cc.brainbook.android.study.mylogin.result.Result;

/**
 * Reset password send verification code result : success or error message.
 */
class ResetPasswordSendVerificationCodeResult extends Result {
    ResetPasswordSendVerificationCodeResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        super(success, error);
    }
}
