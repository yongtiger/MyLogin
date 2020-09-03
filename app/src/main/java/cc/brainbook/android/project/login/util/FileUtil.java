package cc.brainbook.android.project.login.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

public class FileUtil {

    /**
     * 获取文件的Uri（适应Android 7.0）
     *
     * 在Android 7.0以上的系统中，尝试传递 file://URI可能会触发FileUriExposedException
     *
     * <root-path/> 代表设备的根目录new File("/");
     * <files-path/> 代表context.getFilesDir()
     * <cache-path/> 代表context.getCacheDir()
     * <external-path/> 代表Environment.getExternalStorageDirectory()
     * <external-files-path>代表context.getExternalFilesDirs()
     * <external-cache-path>代表getExternalCacheDirs()
     *
     * https://blog.csdn.net/lmj623565791/article/details/72859156
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFile(@NonNull Context context, @NonNull File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".android7.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private FileUtil() {}
}
