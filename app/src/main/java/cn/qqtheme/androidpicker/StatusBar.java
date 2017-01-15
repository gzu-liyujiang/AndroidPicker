package cn.qqtheme.androidpicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 状态栏（通知栏）工具类
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/17
 */
public class StatusBar {
    @IdRes
    private static final int IMMERSION_ID = 0x20170103;

    /**
     * 此方法需在setContentView之后调用
     *
     * @param activity Activity
     * @param color    颜色
     */
    public static void translucent(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //Android4.4以下不支持沉浸式状态栏
            return;
        }
        LogUtils.verbose("let status bar immersion by color" + color);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View immersionView = decorView.findViewById(IMMERSION_ID);
        if (null != immersionView) {
            LogUtils.verbose("status bar has immersion");
            return;
        }
        View contentView = decorView.getChildAt(0);
        //The specified child already has a parent. You must call removeView() on the child's parent first.
        decorView.removeView(contentView);
        LinearLayout layout = new LinearLayout(activity);
        layout.setId(IMMERSION_ID);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(color);
        View stubView = new View(activity);//打桩一个和状态栏高度一致的视图
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = obtainHeight(activity);
        stubView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        layout.addView(stubView);
        layout.addView(contentView);
        decorView.addView(layout);
        translucent(activity, contentView);
    }

    /**
     * 此方法需在setContentView之前调用
     *
     * @param activity Activity
     * @param rootView setContentView的内容
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void translucent(Activity activity, View rootView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //Android4.4以下不支持沉浸式状态栏
            return;
        }
        LogUtils.verbose("let status bar immersion");
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//通知状态栏半透明
        //使用FitsSystemWindows和ClipToPadding来避免状态栏上移，配合沉浸
        rootView.setFitsSystemWindows(true);
        if (rootView instanceof ViewGroup) {
            ((ViewGroup) rootView).setClipToPadding(true);
        }
    }

    /**
     * 获取状态栏的高，单位为px，参阅：http://blog.csdn.net/a_running_wolf/article/details/50477965
     */
    public static int obtainHeight(Context context) {
        int result = 80;// px
        try {
            int dimenResId = obtainDimenResId(context);
            result = context.getResources().getDimensionPixelSize(dimenResId);
        } catch (Exception e) {
            LogUtils.warn("obtain status bar error: " + LogUtils.toStackTraceString(e));
            if (context instanceof Activity) {
                android.graphics.Rect frame = new android.graphics.Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                result = frame.top;
            }
        }
        LogUtils.verbose("status bar height: " + result + " px");
        return result;
    }

    private static int obtainDimenResId(Context ctx) throws ClassNotFoundException,
            NoSuchFieldException, IllegalAccessException, InstantiationException {
        LogUtils.verbose("will obtain status bar height from dimen resources");
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resourceId;
        }
        LogUtils.verbose("will obtain status bar height from R$dimen class");
        Class clazz = Class.forName("com.android.internal.R$dimen");
        Field field = clazz.getField("status_bar_height");
        Object object = field.get(clazz.newInstance());
        return ConvertUtils.toInt(object);
    }

}
