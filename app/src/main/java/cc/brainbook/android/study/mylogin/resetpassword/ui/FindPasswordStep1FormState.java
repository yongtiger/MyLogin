package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class FindPasswordStep1FormState {
    @Nullable
    private Integer usernameError;

    FindPasswordStep1FormState(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    boolean isDataValid() {
        return usernameError == null;
    }
}
