package cn.qqtheme.framework.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.File;

/**
 * 描述
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/21
 * Created By Android Studio
 */
public class Common {

    /**
     * 判断外置存储是否可用
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
     * @return 诸如：/mnt/sdcard/subdir
     */
    public static String getRootPath(Context context) {
        File file;
        if (externalMounted()) {
            file = Environment.getExternalStorageDirectory();
        } else {
            file = context.getFilesDir();
        }
        String path = "/";
        if (file != null) {
            path = FileUtils.separator(file.getAbsolutePath());
        }
        LogUtils.debug("storage root path: " + path);
        return path;
    }

    /**
     * 获取屏幕宽高像素
     */
    public static int[] getPixels(Context ctx) {
        int[] pixels = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(dm);
        pixels[0] = dm.widthPixels;
        pixels[1] = dm.heightPixels;
        LogUtils.debug("width=" + pixels[0] + ", height=" + pixels[1]);
        return pixels;// e.g. 720,1184
    }

    /**
     * dp转换为px
     */
    public static int toPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pxValue = (int) (dpValue * scale + 0.5f);
        LogUtils.debug(dpValue + " dp == " + pxValue + " px");
        return pxValue;
    }

    /**
     * 转换为6位十六进制颜色代码，不含“#”
     */
    public static String toColorString(int color) {
        return toColorString(color, false);
    }

    /**
     * 转换为6位十六进制颜色代码，不含“#”
     */
    public static String toColorString(int color, boolean includeAlpha) {
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }
        if (red.length() == 1) {
            red = "0" + red;
        }
        if (green.length() == 1) {
            green = "0" + green;
        }
        if (blue.length() == 1) {
            blue = "0" + blue;
        }
        String colorString;
        if (includeAlpha) {
            colorString = alpha + red + green + blue;
            LogUtils.debug(String.format("%d to color string is %s", color, colorString));
        } else {
            colorString = red + green + blue;
            LogUtils.debug(String.format("%d to color string is %s%s%s%s, exclude alpha is %s", color, alpha, red, green, blue, colorString));
        }
        return colorString;
    }

    public static ColorStateList toColorStateList(int normalColor, int pressedColor, int focusedColor, int unableColor) {
        int[] colors = new int[]{pressedColor, focusedColor, normalColor, focusedColor, unableColor, normalColor};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList toColorStateList(int normalColor, int pressedColor) {
        return toColorStateList(normalColor, pressedColor, pressedColor, normalColor);
    }

    public static StateListDrawable toStateListDrawable(Drawable normal, Drawable pressed, Drawable focused, Drawable unable) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        drawable.addState(new int[]{android.R.attr.state_enabled}, normal);
        drawable.addState(new int[]{android.R.attr.state_focused}, focused);
        drawable.addState(new int[]{android.R.attr.state_window_focused}, unable);
        drawable.addState(new int[]{}, normal);
        return drawable;
    }

    public static StateListDrawable toStateListDrawable(int normalColor, int pressedColor, int focusedColor, int unableColor) {
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = new ColorDrawable(normalColor);
        Drawable pressed = new ColorDrawable(pressedColor);
        Drawable focused = new ColorDrawable(focusedColor);
        Drawable unable = new ColorDrawable(unableColor);
        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        drawable.addState(new int[]{android.R.attr.state_enabled}, normal);
        drawable.addState(new int[]{android.R.attr.state_focused}, focused);
        drawable.addState(new int[]{android.R.attr.state_window_focused}, unable);
        drawable.addState(new int[]{}, normal);
        return drawable;
    }

    public static StateListDrawable toStateListDrawable(Drawable normal, Drawable pressed) {
        return toStateListDrawable(normal, pressed, pressed, normal);
    }

    public static StateListDrawable toStateListDrawable(int normalColor, int pressedColor) {
        return toStateListDrawable(normalColor, pressedColor, pressedColor, normalColor);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int drawableRes) {
        if (Build.VERSION.SDK_INT < 21) {
            //noinspection deprecation
            return context.getResources().getDrawable(drawableRes);
        } else {
            return context.getDrawable(drawableRes);
        }
    }

    /**
     * 计算view的高
     */
    public static int calculateHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        if (view instanceof ListView) {
            ListView listView = (ListView) view;
            Adapter adapter = listView.getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                for (int i = 0; i < count; i++) {
                    View convertView = adapter.getView(i, null, listView);
                    convertView.measure(widthMeasureSpec, heightMeasureSpec);
                    totalHeight += convertView.getMeasuredHeight();
                }
                totalHeight += listView.getDividerHeight() * (count - 1);
                LogUtils.debug("ListView height is " + totalHeight + " dp");
            }
        } else if (view instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) view;
            for (int i = 0; i < scrollView.getChildCount(); i++) {
                totalHeight += calculateHeight(scrollView.getChildAt(i));
            }
        } else if (view instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) view;
            if (layout.getOrientation() == LinearLayout.VERTICAL) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    totalHeight += calculateHeight(layout.getChildAt(i));
                }
            } else {
                layout.measure(widthMeasureSpec, heightMeasureSpec);
                totalHeight += layout.getMeasuredHeight();
            }
            LogUtils.debug("LinearLayout height is " + totalHeight + " dp");
        } else {
            view.measure(widthMeasureSpec, heightMeasureSpec);
            totalHeight += view.getMeasuredHeight();
            LogUtils.debug("View height is " + totalHeight + "");
        }
        LogUtils.debug("View total height is " + totalHeight + "");
        return totalHeight;
    }


}
