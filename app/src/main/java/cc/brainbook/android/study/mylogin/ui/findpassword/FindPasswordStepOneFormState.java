package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.support.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class FindPasswordStepOneFormState {
    @Nullable
    private Integer usernameError;

    FindPasswordStepOneFormState(@Nullable Integer usernameError) {
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
