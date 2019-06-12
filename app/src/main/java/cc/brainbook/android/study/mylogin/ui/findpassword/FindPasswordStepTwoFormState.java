package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.support.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class FindPasswordStepTwoFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer mobileError;

    private boolean isAllEmpty;

    FindPasswordStepTwoFormState(@Nullable Integer emailError, @Nullable Integer mobileError, boolean isAllEmpty) {
        this.emailError = emailError;
        this.mobileError = mobileError;
        this.isAllEmpty = isAllEmpty;
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
        return !isAllEmpty && (emailError == null && mobileError == null);
    }
}
