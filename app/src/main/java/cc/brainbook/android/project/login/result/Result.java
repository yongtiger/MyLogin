package cc.brainbook.android.project.login.result;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Result : success or error message.
 */
public class Result {
    @Nullable @StringRes
    private Integer success;
    @Nullable @StringRes
    private Integer error;

    public Result(@Nullable @StringRes Integer success, @Nullable @StringRes Integer error) {
        this.success = success;
        this.error = error;
    }

    @Nullable @StringRes
    public Integer getSuccess() {
        return success;
    }

    @Nullable @StringRes
    public Integer getError() {
        return error;
    }
}
