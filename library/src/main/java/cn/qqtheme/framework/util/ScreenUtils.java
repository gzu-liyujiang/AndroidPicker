package cn.qqtheme.framework.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * 获取屏幕宽高等信息、全屏切换、保持屏幕常亮、截屏等
 *
 * @author liyujiang[QQ:1032694760]
 * @since 2015/11/26
 */
public final class ScreenUtils {
    private static boolean isFullScreen = false;

    /**
     * Display metrics display metrics.
     *
     * @param context the context
     * @return the display metrics
     */
    public static DisplayMetrics displayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        LogUtils.debug("screen width=" + dm.widthPixels + "px, screen height=" + dm.heightPixels
                + "px, densityDpi=" + dm.densityDpi + ", density=" + dm.density);
        return dm;
    }

    /**
     * Width pixels int.
     *
     * @param context the context
     * @return the int
     */
    public static int widthPixels(Context context) {
        return displayMetrics(context).widthPixels;
    }

    /**
     * Height pixels int.
     *
     * @param context the context
     * @return the int
     */
    public static int heightPixels(Context context) {
        return displayMetrics(context).heightPixels;
    }

    /**
     * Density float.
     *
     * @param context the context
     * @return the float
     */
    public static float density(Context context) {
        return displayMetrics(context).density;
    }

    /**
     * Density dpi int.
     *
     * @param context the context
     * @return the int
     */
    public static int densityDpi(Context context) {
        return displayMetrics(context).densityDpi;
    }

    /**
     * Is full screen boolean.
     *
     * @return the boolean
     */
    public static boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * Toggle full displayMetrics.
     *
     * @param activity the activity
     */
    public static void toggleFullScreen(Activity activity) {
        Window window = activity.getWindow();
        int flagFullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (isFullScreen) {
            window.clearFlags(flagFullscreen);
            isFullScreen = false;
        } else {
            window.setFlags(flagFullscreen, flagFullscreen);
            isFullScreen = true;
        }
    }

    /**
     * 保持屏幕常亮
     *
     * @param activity the activity
     */
    public static void keepBright(Activity activity) {
        //需在setContentView前调用
        int keepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        activity.getWindow().setFlags(keepScreenOn, keepScreenOn);
    }

}
