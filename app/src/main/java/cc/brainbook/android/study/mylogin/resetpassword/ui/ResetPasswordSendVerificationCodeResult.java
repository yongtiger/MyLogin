package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Reset password send verification code result : success or error message.
 */
class ResetPasswordSendVerificationCodeResult extends ResetPasswordResult {
    ResetPasswordSendVerificationCodeResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        super(success, error);
    }
}
