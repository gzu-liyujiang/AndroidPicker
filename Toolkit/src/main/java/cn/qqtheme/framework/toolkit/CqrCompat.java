package cn.qqtheme.framework.toolkit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * 此类主要是用来放一些系统过时方法的处理，兼容旧版&新版
 * <p>
 * Android 6，参阅 https://developer.android.google.cn/about/versions/marshmallow/android-6.0-changes
 * Android 7，参阅 https://developer.android.google.cn/about/versions/nougat/android-7.0-changes
 * Android 8，参阅 https://developer.android.google.cn/about/versions/oreo/android-8.0-changes
 * Android 9，参阅 https://developer.android.google.cn/about/versions/pie/android-9.0-changes-all
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2015/10/19
 */
@SuppressWarnings("WeakerAccess")
public class CqrCompat {

    protected CqrCompat() {
        throw new UnsupportedOperationException("You can't instantiate me");
    }

    public static void setTextAppearance(@NonNull TextView view, @StyleRes int appearanceRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //noinspection deprecation
            view.setTextAppearance(view.getContext(), appearanceRes);
        } else {
            view.setTextAppearance(appearanceRes);
        }
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawableRes) {
        return ContextCompat.getDrawable(context, drawableRes);
    }

    public static int getColor(Context context, @ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener victim) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(victim);
        } else {
            //noinspection deprecation
            viewTreeObserver.removeGlobalOnLayoutListener(victim);
        }
    }

    /**
     * Android7.0+文件共享兼容
     */
    private static Uri getUriForFileCompatAndroid7Plus(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", file);
    }

    public static Uri fromFile(Context context, File file) {
        //FileUriExposedException: file:///xxx exposed beyond app through ClipData.Item.getUri()
        //参阅 https://blog.csdn.net/lmj623565791/article/details/72859156
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = getUriForFileCompatAndroid7Plus(context, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static void setDataAndType(Context context, Intent intent, String type, File file) {
        setDataAndType(context, intent, type, file, true);
    }

    public static void setDataAndType(Context context, Intent intent, String type, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getUriForFileCompatAndroid7Plus(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    public static void setData(Context context, Intent intent, File file) {
        setData(context, intent, file, true);
    }

    public static void setData(Context context, Intent intent, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setData(getUriForFileCompatAndroid7Plus(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setData(Uri.fromFile(file));
        }
    }

    public static void grantPermissions(Context context, Intent intent, Uri uri) {
        grantPermissions(context, intent, uri, true);
    }

    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }

}
