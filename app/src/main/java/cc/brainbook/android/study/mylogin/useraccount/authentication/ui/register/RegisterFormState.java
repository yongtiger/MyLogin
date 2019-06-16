package cc.brainbook.android.study.mylogin.useraccount.authentication.ui.register;

import android.support.annotation.Nullable;

/**
 * Data validation state of the register form.
 */
class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repeatPasswordError;

    RegisterFormState(@Nullable Integer usernameError,
                      @Nullable Integer passwordError,
                      @Nullable Integer repeatPasswordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.repeatPasswordError = repeatPasswordError;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
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
        return usernameError == null && passwordError == null && repeatPasswordError == null;
    }
}
