package cn.qqtheme.framework.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * 兼容旧版&新版
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/19
 */
public class CompatUtils {

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
    public static Drawable getDrawable(Context context, @DrawableRes int drawableRes) {
        if (Build.VERSION.SDK_INT < 21) {
            //noinspection deprecation
            return context.getResources().getDrawable(drawableRes);
        } else {
            return context.getDrawable(drawableRes);
        }
    }

}
