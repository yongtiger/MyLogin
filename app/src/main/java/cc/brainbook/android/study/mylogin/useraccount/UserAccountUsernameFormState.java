package cc.brainbook.android.study.mylogin.useraccount;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class UserAccountUsernameFormState {
    @Nullable
    private Integer usernameError;

    UserAccountUsernameFormState(@Nullable Integer usernameError) {
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
