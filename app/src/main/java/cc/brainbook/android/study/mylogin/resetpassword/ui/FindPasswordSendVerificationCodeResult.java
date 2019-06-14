package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Find password send verification code result : success or error message.
 */
class FindPasswordSendVerificationCodeResult extends FindPasswordResult {
    FindPasswordSendVerificationCodeResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        super(success, error);
    }
}
