package cn.qqtheme.framework.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

/**
 * 兼容旧版&新版
 *
 * @author 李玉江[QQ :1032694760]
 * @since 2015 /10/19 Created By Android Studio
 */
public class CompatUtils {

    /**
     * Sets background.
     *
     * @param view     the view
     * @param drawable the drawable
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    /**
     * Sets text appearance.
     *
     * @param view          the view
     * @param appearanceRes the appearance res
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setTextAppearance(TextView view, @StyleRes int appearanceRes) {
        if (Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            view.setTextAppearance(view.getContext(), appearanceRes);
        } else {
            view.setTextAppearance(appearanceRes);
        }
    }

    /**
     * Gets drawable.
     *
     * @param context     the context
     * @param drawableRes the drawable res
     * @return the drawable
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, @DrawableRes int drawableRes) {
        if (Build.VERSION.SDK_INT < 21) {
            //noinspection deprecation
            return context.getResources().getDrawable(drawableRes);
        } else {
            return context.getDrawable(drawableRes);
        }
    }

    /**
     * Gets string.
     *
     * @param context   the context
     * @param stringRes the string res
     * @return the string
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getString(Context context, @StringRes int stringRes) {
        if (Build.VERSION.SDK_INT < 21) {
            //noinspection deprecation
            return context.getResources().getString(stringRes);
        } else {
            return context.getString(stringRes);
        }
    }

    /**
     * Gets color.
     *
     * @param context  the context
     * @param colorRes the color res
     * @return the color
     */
    @ColorInt
    public static int getColor(Context context, @ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT < 21) {
            //noinspection deprecation
            return context.getResources().getColor(colorRes);
        } else {
            return context.getResources().getColor(colorRes, null);
        }
    }

}
