package cc.brainbook.android.project.login.useraccount.modify;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class ModifyEmailFormState {
    @Nullable
    private Integer emailError;

    ModifyEmailFormState(@Nullable Integer emailError) {
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
