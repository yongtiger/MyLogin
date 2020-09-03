package cc.brainbook.android.project.login.useraccount.modify.exception;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ModifyException extends RuntimeException {
    public static final int EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED = -4;
    public static final int EXCEPTION_IO_EXCEPTION = -3;
    public static final int EXCEPTION_UNKNOWN = -2;
    public static final int EXCEPTION_INVALID_PARAMETERS = -1;
    public static final int EXCEPTION_FAILED_TO_MODIFY_USERNAME = 1;
    public static final int EXCEPTION_FAILED_TO_MODIFY_PASSWORD = 2;
    public static final int EXCEPTION_FAILED_TO_MODIFY_EMAIL = 3;
    public static final int EXCEPTION_FAILED_TO_MODIFY_MOBILE = 4;
    public static final int EXCEPTION_FAILED_TO_MODIFY_AVATAR = 5;

    private int code;

    public ModifyException(@ExceptionType int code) {
        this.code = code;
    }

    public ModifyException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public ModifyException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ModifyException(@ExceptionType int code, Throwable cause) {
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
            EXCEPTION_FAILED_TO_MODIFY_USERNAME,
            EXCEPTION_FAILED_TO_MODIFY_PASSWORD,
            EXCEPTION_FAILED_TO_MODIFY_EMAIL,
            EXCEPTION_FAILED_TO_MODIFY_MOBILE,
            EXCEPTION_FAILED_TO_MODIFY_AVATAR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
