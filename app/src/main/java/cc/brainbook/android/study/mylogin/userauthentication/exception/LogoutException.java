package cc.brainbook.android.study.mylogin.userauthentication.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LogoutException extends RuntimeException {
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED = -1;

    private int code;

    public LogoutException(@ExceptionType int code) {
        this.code = code;
    }

    public LogoutException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public LogoutException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public LogoutException(@ExceptionType int code, Throwable cause) {
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
            EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
