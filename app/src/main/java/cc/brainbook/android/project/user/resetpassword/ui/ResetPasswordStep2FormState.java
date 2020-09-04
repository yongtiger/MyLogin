package cc.brainbook.android.project.user.resetpassword.ui;

import androidx.annotation.Nullable;

/**
 * Data validation state.
 */
class ResetPasswordStep2FormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer mobileError;

    private boolean isAllEmpty;

    ResetPasswordStep2FormState(@Nullable Integer emailError, @Nullable Integer mobileError, boolean isAllEmpty) {
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
