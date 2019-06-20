package cc.brainbook.android.project.login.useraccount.modify.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ModifyMobileException extends RuntimeException {
    public static final int EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED = -4;
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_INVALID_PARAMETERS = -1;
    public static final int EXCEPTION_FAILED_TO_MODIFY_MOBILE = 1;

    private int code;

    public ModifyMobileException(@ExceptionType int code) {
        this.code = code;
    }

    public ModifyMobileException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public ModifyMobileException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ModifyMobileException(@ExceptionType int code, Throwable cause) {
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
    @IntDef({EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED,
            EXCEPTION_IO_EXCEPTION,
            EXCEPTION_UNKNOWN,
            EXCEPTION_INVALID_PARAMETERS,
            EXCEPTION_FAILED_TO_MODIFY_MOBILE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
