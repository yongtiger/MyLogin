package cc.brainbook.android.study.mylogin.userauthentication.ui.register;

import android.support.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class RegisterResult {
    @Nullable
    private boolean success;
    @Nullable
    private Integer error;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(boolean success) {
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
