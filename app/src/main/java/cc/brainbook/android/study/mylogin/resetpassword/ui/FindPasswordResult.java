package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Find password result : success or error message.
 */
class FindPasswordResult {
    @Nullable @StringRes
    private Integer success;
    @Nullable @StringRes
    private Integer error;

    FindPasswordResult(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
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
