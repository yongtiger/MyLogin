package cc.brainbook.android.study.mylogin.useraccount;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class UserAccountMobileFormState {
    @Nullable
    private Integer mobileError;

    UserAccountMobileFormState(@Nullable Integer mobileError) {
        this.mobileError = mobileError;
    }

    @Nullable
    Integer getMobileError() {
        return mobileError;
    }

    boolean isDataValid() {
        return mobileError == null;
    }
}
