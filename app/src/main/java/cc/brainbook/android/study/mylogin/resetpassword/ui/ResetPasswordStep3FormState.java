package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.support.annotation.Nullable;

/**
 * Data validation state.
 */
class ResetPasswordStep3FormState {
    @Nullable
    private Integer verificationCodeError;
    private boolean isSessionIDValid;

    ResetPasswordStep3FormState(@Nullable Integer verificationCodeError, boolean isSessionIDValid) {
        this.verificationCodeError = verificationCodeError;
        this.isSessionIDValid = isSessionIDValid;
    }

    @Nullable
    Integer getVerificationCodeError() {
        return verificationCodeError;
    }

    boolean isDataValid() {
        return verificationCodeError == null && isSessionIDValid;
    }
}
