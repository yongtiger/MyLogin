package cc.brainbook.android.project.login.resetpassword.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ResetPasswordException extends RuntimeException {
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_INVALID_PARAMETERS = -1;
    public static final int EXCEPTION_CANNOT_RESET_PASSWORD = 1;
    ///FindUser
    public static final int EXCEPTION_INVALID_USERNAME = 2;
    ///CheckSendMode
    public static final int EXCEPTION_INVALID_USER_ID = 3;
    public static final int EXCEPTION_NO_MATCH_EMAIL = 4;
    public static final int EXCEPTION_NO_MATCH_MOBILE = 5;
    public static final int EXCEPTION_NO_MATCH_EMAIL_OR_MOBILE = 6;
    ///SendVerificationCode
    public static final int EXCEPTION_FAILED_TO_SEND_EMAIL = 7;
    public static final int EXCEPTION_FAILED_TO_SEND_MOBILE = 8;
    public static final int EXCEPTION_FAILED_TO_SEND_EMAIL_AND_MOBILE = 9;
    ///VerifyCode
    public static final int EXCEPTION_INVALID_VERIFICATION_CODE = 10;

    private int code;

    public ResetPasswordException(@ExceptionType int code) {
        this.code = code;
    }

    public ResetPasswordException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public ResetPasswordException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ResetPasswordException(@ExceptionType int code, Throwable cause) {
        super(cause);
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Exception type.
     */
    @IntDef({EXCEPTION_IO_EXCEPTION,
            EXCEPTION_UNKNOWN,
            EXCEPTION_INVALID_PARAMETERS,
            EXCEPTION_CANNOT_RESET_PASSWORD,
            EXCEPTION_INVALID_USERNAME,
            EXCEPTION_INVALID_USER_ID,
            EXCEPTION_NO_MATCH_EMAIL,
            EXCEPTION_NO_MATCH_MOBILE,
            EXCEPTION_NO_MATCH_EMAIL_OR_MOBILE,
            EXCEPTION_FAILED_TO_SEND_EMAIL,
            EXCEPTION_FAILED_TO_SEND_MOBILE,
            EXCEPTION_FAILED_TO_SEND_EMAIL_AND_MOBILE,
            EXCEPTION_INVALID_VERIFICATION_CODE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
