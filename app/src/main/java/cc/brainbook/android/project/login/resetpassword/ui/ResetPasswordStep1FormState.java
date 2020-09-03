package cc.brainbook.android.project.login.resetpassword.ui;

import androidx.annotation.Nullable;

/**
 * Data validation state.
 */
class ResetPasswordStep1FormState {
    @Nullable
    private Integer usernameError;

    ResetPasswordStep1FormState(@Nullable Integer usernameError) {
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
