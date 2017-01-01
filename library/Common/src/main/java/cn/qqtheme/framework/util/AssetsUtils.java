package cn.qqtheme.framework.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 操作安装包中的“assets”目录下的文件
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2013-11-2
 */
public final class AssetsUtils {

    public static byte[] readByteArray(Context context, String assetPath) {
        LogUtils.verbose("read assets file as byte array: " + assetPath);
        try {
            return ConvertUtils.toByteArray(context.getAssets().open(assetPath));
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return null;
    }

    public static String readText(Context context, String assetPath) {
        LogUtils.verbose("read assets file as text: " + assetPath);
        try {
            return ConvertUtils.toString(context.getAssets().open(assetPath)).trim();
        } catch (IOException e) {
            LogUtils.error(e);
            return "";
        }
    }

    /**
     * 通过文件名从Assets中获得资源，以位图的形式返回
     *
     * @param context   the context
     * @param assetPath 文件名应为assets文件下载绝对路径
     * @return 以位图的形式返回 bitmap
     */
    public static Bitmap readBitmap(Context context, String assetPath) {
        LogUtils.verbose("read assets file as bitmap: " + assetPath);
        Bitmap bitmap = null;
        try {
            InputStream is = context.getAssets().open(assetPath);
            bitmap = BitmapFactory.decodeStream(is);
            bitmap.setDensity(96);// 96 dpi
            is.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return bitmap;
    }

}
