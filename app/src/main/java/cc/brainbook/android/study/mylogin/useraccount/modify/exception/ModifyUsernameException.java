package cc.brainbook.android.study.mylogin.useraccount.modify.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ModifyUsernameException extends RuntimeException {
    public static final int EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED = -4;
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_INVALID_PARAMETERS = -1;
    public static final int EXCEPTION_FAILED_TO_MODIFY_USERNAME = 1;

    private int code;

    public ModifyUsernameException(@ExceptionType int code) {
        this.code = code;
    }

    public ModifyUsernameException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public ModifyUsernameException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ModifyUsernameException(@ExceptionType int code, Throwable cause) {
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
            EXCEPTION_FAILED_TO_MODIFY_USERNAME
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
