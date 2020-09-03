package cc.brainbook.android.project.login.resetpassword.ui;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import cc.brainbook.android.project.login.result.Result;

/**
 * Reset password send verification code result : success or error message.
 */
class ResetPasswordSendVerificationCodeResult extends Result {
    ResetPasswordSendVerificationCodeResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        super(success, error);
    }
}
