package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.support.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class FindPasswordStepThreeFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer mobileError;

    FindPasswordStepThreeFormState(@Nullable Integer emailError, @Nullable Integer mobileError) {
        this.emailError = emailError;
        this.mobileError = mobileError;
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
        return emailError == null || mobileError == null;
    }
}
