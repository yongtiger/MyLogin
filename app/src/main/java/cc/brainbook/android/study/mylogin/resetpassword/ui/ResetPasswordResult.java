package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Reset password result : success or error message.
 */
class ResetPasswordResult {
    @Nullable @StringRes
    private Integer success;
    @Nullable @StringRes
    private Integer error;

    ResetPasswordResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        this.success = success;
        this.error = error;
    }

    @Nullable @StringRes
    Integer getSuccess() {
        return success;
    }

    @Nullable @StringRes
    Integer getError() {
        return error;
    }
}
