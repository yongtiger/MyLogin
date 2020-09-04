package cc.brainbook.android.project.user.account.modify;

import androidx.annotation.Nullable;

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
