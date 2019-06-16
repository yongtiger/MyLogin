package cc.brainbook.android.study.mylogin.useraccount;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Reset password result : success or error message.
 */
class UserAccountResult {
    @Nullable @StringRes
    private Integer success;
    @Nullable @StringRes
    private Integer error;

    UserAccountResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
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
