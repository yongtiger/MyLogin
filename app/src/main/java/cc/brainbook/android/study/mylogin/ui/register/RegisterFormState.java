package cc.brainbook.android.study.mylogin.ui.register;

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
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer mobileError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError,
                      @Nullable Integer passwordError,
                      @Nullable Integer repeatPasswordError,
                      @Nullable Integer emailError,
                      @Nullable Integer mobileError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.repeatPasswordError = repeatPasswordError;
        this.emailError = emailError;
        this.mobileError = mobileError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.repeatPasswordError = null;
        this.emailError = null;
        this.mobileError = null;
        this.isDataValid = isDataValid;
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

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getMobileError() {
        return mobileError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
