package cn.qqtheme.framework.tool;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 获取屏幕宽高
 *
 * @author liyujiang[QQ:1032694760]
 * @version 2015/11/26
 *          Created by Intellij IDEA 14.1
 */
public class ScreenHelper {

    public static class Screen {
        public int widthPixels;
        public int heightPixels;
        public int densityDpi;
        public float density;
    }

    public static Screen getScreenPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        Screen screen = new Screen();
        screen.widthPixels = dm.widthPixels;// e.g. 1080
        screen.heightPixels = dm.heightPixels;// e.g. 1920
        screen.densityDpi = dm.densityDpi;// e.g. 480
        screen.density = dm.density;// e.g. 2.0
        LogUtils.debug("width=" + screen.widthPixels + ", height=" + screen.heightPixels
                + ", densityDpi=" + screen.densityDpi + ", density=" + screen.density);
        return screen;
    }

}
