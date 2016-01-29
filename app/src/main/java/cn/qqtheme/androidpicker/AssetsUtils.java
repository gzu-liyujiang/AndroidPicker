package cn.qqtheme.androidpicker;

import android.content.Context;

import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 操作安装包中的“assets”目录下的文件
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2013-11-2
 */
public class AssetsUtils {

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static String readText(Context context, String assetPath) {
        LogUtils.debug("read assets file as text: " + assetPath);
        try {
            return ConvertUtils.toString(context.getAssets().open(assetPath));
        } catch (Exception e) {
            LogUtils.error(e);
            return "";
        }
    }

}
