package cc.brainbook.android.study.mylogin.useraccount.modify;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class ModifyMobileFormState {
    @Nullable
    private Integer mobileError;

    ModifyMobileFormState(@Nullable Integer mobileError) {
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
