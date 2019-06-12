package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.support.annotation.Nullable;

import cc.brainbook.android.study.mylogin.ui.login.view.LoggedInUserView;

/**
 * Find password result : success or error message.
 */
class FindPasswordResult {
    @Nullable
    private boolean success;
    @Nullable
    private Integer error;

    FindPasswordResult(@Nullable Integer error) {
        this.error = error;
    }

    FindPasswordResult(@Nullable boolean success) {
        this.success = success;
    }

    @Nullable
    boolean getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
