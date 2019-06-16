package cc.brainbook.android.study.mylogin.useraccount;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class UserAccountPasswordFormState {
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repeatPasswordError;

    UserAccountPasswordFormState(@Nullable Integer passwordError, @Nullable Integer repeatPasswordError) {
        this.passwordError = passwordError;
        this.repeatPasswordError = repeatPasswordError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getRepeatPasswordError() {
        return repeatPasswordError;
    }

    boolean isDataValid() {
        return passwordError == null && repeatPasswordError == null;
    }
}
