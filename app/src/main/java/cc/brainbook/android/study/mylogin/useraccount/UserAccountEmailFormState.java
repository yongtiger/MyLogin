package cc.brainbook.android.study.mylogin.useraccount;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class UserAccountEmailFormState {
    @Nullable
    private Integer emailError;

    UserAccountEmailFormState(@Nullable Integer emailError) {
        this.emailError = emailError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    boolean isDataValid() {
        return emailError == null;
    }
}
