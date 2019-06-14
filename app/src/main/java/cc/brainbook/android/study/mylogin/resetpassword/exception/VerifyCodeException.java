package cc.brainbook.android.study.mylogin.resetpassword.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VerifyCodeException extends RuntimeException {
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_INVALID_PARAMETERS = -1;
    public static final int EXCEPTION_INVALID_USER_ID = 1;
    public static final int EXCEPTION_INVALID_VERIFICATION_CODE = 2;

    private int code;

    public VerifyCodeException(@VerifyCodeException.ExceptionType int code) {
        this.code = code;
    }

    public VerifyCodeException(@VerifyCodeException.ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public VerifyCodeException(@VerifyCodeException.ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public VerifyCodeException(@VerifyCodeException.ExceptionType int code, Throwable cause) {
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
            EXCEPTION_INVALID_VERIFICATION_CODE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
