package cn.qqtheme.framework.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * 存储设备工具类
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2013-11-2
 */
public final class StorageUtils {

    /**
     * 判断外置存储是否可用
     *
     * @return the boolean
     */
    public static boolean externalMounted() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        LogUtils.warn("external storage unmounted");
        return false;
    }

    /**
     * 返回以“/”结尾的外置存储根目录，可选若外置存储不可用则是否使用内部存储
     *
     * @return 诸如 ：/mnt/sdcard/subdir
     */
    public static String getRootPath(Context context, boolean onlyExternalStorage, String subdir) {
        File file;
        if (externalMounted()) {
            file = Environment.getExternalStorageDirectory();
        } else {
            if (onlyExternalStorage) {
                file = new File("/");
            } else {
                file = context.getFilesDir();
            }
        }
        if (!TextUtils.isEmpty(subdir)) {
            file = new File(file, subdir);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        String path = "/";
        if (file != null) {
            path = FileUtils.separator(file.getAbsolutePath());
        }
        LogUtils.verbose("storage root path: " + path);
        return path;
    }

    /**
     * 返回以“/”结尾的外置存储根目录，可选若外置存储不可用则是否使用内部存储
     *
     * @return 诸如 ：/mnt/sdcard/
     */
    public static String getRootPath(Context context, String subdir) {
        return getRootPath(context, false, subdir);
    }

    /**
     * 返回以“/”结尾的外置存储根目录，可选若外置存储不可用则是否使用内部存储
     *
     * @return 诸如 ：/mnt/sdcard/
     */
    public static String getRootPath(Context context, boolean onlyExternalStorage) {
        return getRootPath(context, onlyExternalStorage, null);
    }

    /**
     * 返回以“/”结尾的外置存储根目录，若外置存储不可用则使用内部存储
     *
     * @return 诸如 ：/mnt/sdcard/
     */
    public static String getRootPath(Context context) {
        return getRootPath(context, false);
    }

    /**
     * 下载的文件的保存路径，以“/”结尾
     *
     * @return 诸如 ：/mnt/sdcard/Download/
     */
    public static String getDownloadPath(Context context) throws Exception {
        File file;
        if (externalMounted()) {
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else {
            throw new Exception("外置存储不可用！");
        }
        return FileUtils.separator(file.getAbsolutePath());
    }

    /**
     * 各种类型的文件的专用的保存路径，以“/”结尾
     *
     * @return 诸如 ：/mnt/sdcard/Android/data/[package]/files/[type]/
     */
    public static String getPrivatePath(Context context, String type) {
        File file = null;
        if (externalMounted()) {
            file = context.getExternalFilesDir(type);
        }
        //高频触发java.lang.NullPointerException，是SD卡不可用或暂时繁忙么？
        if (file == null) {
            if (type == null) {
                file = context.getFilesDir();
            } else {
                file = new File(FileUtils.separator(context.getFilesDir().getAbsolutePath()) + type);
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
        }
        return FileUtils.separator(file.getAbsolutePath());
    }

    /**
     * 返回以“/”结尾的内部存储根目录
     */
    public static String getInternalRootPath(Context context) {
        return FileUtils.separator(context.getFilesDir().getAbsolutePath());
    }

    /**
     * 各种类型的文件的专用的内部存储保存路径，以“/”结尾
     */
    public static String getPrivateRootPath(Context context) {
        return getPrivatePath(context, null);
    }

    /**
     * 返回以“/”结尾的临时存储目录
     */
    public static String getTemporaryDirPath(Context context) {
        return getPrivatePath(context, "temporary");
    }

    /**
     * 返回以“/”结尾的临时存储文件
     */
    public static String getTemporaryFilePath(Context context) {
        try {
            return File.createTempFile("lyj_", ".tmp", context.getCacheDir()).getAbsolutePath();
        } catch (IOException e) {
            return getTemporaryDirPath(context) + "lyj.tmp";
        }
    }

    /**
     * 各种类型的文件的专用的缓存存储保存路径，以“/”结尾
     */
    public static String getCachePath(Context context, String type) {
        File file;
        if (externalMounted()) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }
        if (type != null) {
            file = new File(file, type);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        return FileUtils.separator(file != null ? file.getAbsolutePath() : null);
    }

    /**
     * 返回以“/”结尾的缓存存储根目录
     */
    public static String getCacheRootPath(Context context) {
        return getCachePath(context, null);
    }

}
