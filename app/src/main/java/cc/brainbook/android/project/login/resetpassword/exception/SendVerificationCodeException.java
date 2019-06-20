package cc.brainbook.android.project.login.resetpassword.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SendVerificationCodeException extends RuntimeException {
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_INVALID_PARAMETERS = -1;
    public static final int EXCEPTION_INVALID_USER_ID = 1;
    public static final int EXCEPTION_CANNOT_RESET_PASSWORD = 2;    ///nor email or mobile
    public static final int EXCEPTION_FAILED_TO_SEND_EMAIL = 3;
    public static final int EXCEPTION_FAILED_TO_SEND_MOBILE = 4;
    public static final int EXCEPTION_FAILED_TO_SEND_EMAIL_AND_MOBILE = 5;

    private int code;

    public SendVerificationCodeException(@ExceptionType int code) {
        this.code = code;
    }

    public SendVerificationCodeException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public SendVerificationCodeException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SendVerificationCodeException(@ExceptionType int code, Throwable cause) {
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
            EXCEPTION_INVALID_USER_ID,
            EXCEPTION_CANNOT_RESET_PASSWORD,
            EXCEPTION_FAILED_TO_SEND_EMAIL,
            EXCEPTION_FAILED_TO_SEND_MOBILE,
            EXCEPTION_FAILED_TO_SEND_EMAIL_AND_MOBILE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
