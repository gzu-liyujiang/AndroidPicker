package cn.qqtheme.framework.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 数据类型转换、单位转换
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2014-4-18
 */
public class ConvertUtils {
    public static final long GB = 1073741824;
    public static final long MB = 1048576;
    public static final long KB = 1024;

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
            LogUtils.verbose(String.format(Locale.CHINA, "%d to color string is %s", color, colorString));
        } else {
            colorString = red + green + blue;
            LogUtils.verbose(String.format(Locale.CHINA, "%d to color string is %s%s%s%s, exclude alpha is %s", color, alpha, red, green, blue, colorString));
        }
        return colorString;
    }

    /**
     * 将指定的日期转换为一定格式的字符串
     */
    public static String toDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 将当前的日期转换为一定格式的字符串
     */
    public static String toDateString(String format) {
        return toDateString(Calendar.getInstance(Locale.CHINA).getTime(), format);
    }

    /**
     * 将指定的日期字符串转换为日期时间
     *
     * @param dateStr 如：2014-04-08 23:02
     */
    public static Date toDate(String dateStr) {
        return DateUtils.parseDate(dateStr);
    }

    /**
     * 将指定的日期字符串转换为时间戳
     *
     * @param dateStr 如：2014-04-08 23:02
     */
    public static long toTimemillis(String dateStr) {
        return toDate(dateStr).getTime();
    }

    public static String toSlashString(String str) {
        String result = "";
        char[] chars = str.toCharArray();
        for (char chr : chars) {
            if (chr == '"' || chr == '\'' || chr == '\\') {
                result += "\\";//符合"、'、\这三个符号的前面加一个\
            }
            result += chr;
        }
        return result;
    }

    public static <T> T[] toArray(List<T> list) {
        //noinspection unchecked
        return (T[]) list.toArray();
    }

    public static <T> List<T> toList(T[] array) {
        return Arrays.asList(array);
    }

    public static String toString(Object[] objects) {
        return Arrays.deepToString(objects);
    }

    public static String toString(Object[] objects, String tag) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object);
            sb.append(tag);
        }
        return sb.toString();
    }

    public static byte[] toByteArray(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = is.read(buff, 0, 100)) > 0) {
                os.write(buff, 0, rc);
            }
            byte[] bytes = os.toByteArray();
            os.close();
            is.close();
            return bytes;
        } catch (IOException e) {
            LogUtils.warn(e);
        }
        return null;
    }

    public static byte[] toByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储，除了PNG还有很多常见格式，如jpeg等。
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();
        try {
            os.close();
        } catch (IOException e) {
            LogUtils.warn(e);
        }
        return bytes;
    }

    public static Bitmap toBitmap(byte[] bytes, int width, int height) {
        Bitmap bitmap = null;
        if (bytes.length != 0) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                // 不进行图片抖动处理
                options.inDither = false;
                // 设置让解码器以最佳方式解码
                options.inPreferredConfig = null;
                if (width > 0 && height > 0) {
                    options.outWidth = width;
                    options.outHeight = height;
                }
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            } catch (Exception e) {
                LogUtils.error(e);
            }
        }
        return bitmap;
    }

    public static Bitmap toBitmap(byte[] bytes) {
        return toBitmap(bytes, -1, -1);
    }

    /**
     * 将Drawable转换为Bitmap
     * 参考：http://kylines.iteye.com/blog/1660184
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            //color
            Bitmap bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
            if (Build.VERSION.SDK_INT >= 11) {
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(((ColorDrawable) drawable).getColor());
            }
            return bitmap;
        } else if (drawable instanceof NinePatchDrawable) {
            //.9.png
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }

    /**
     * 从第三方文件选择器获取路径。
     * 参见：http://blog.csdn.net/zbjdsbj/article/details/42387551
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String toPath(Context context, Uri uri) {
        if (uri == null) {
            LogUtils.verbose("uri is null");
            return "";
        }
        LogUtils.verbose("uri: " + uri.toString());
        String path = uri.getPath();
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        //是否是4.4及以上版本
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            String type = split[0];
            Uri contentUri = null;
            switch (authority) {
                // ExternalStorageProvider
                case "com.android.externalstorage.documents":
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    break;
                // DownloadsProvider
                case "com.android.providers.downloads.documents":
                    contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    return _queryPathFromMediaStore(context, contentUri, null, null);
                // MediaProvider
                case "com.android.providers.media.documents":
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return _queryPathFromMediaStore(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else {
            if ("content".equalsIgnoreCase(scheme)) {
                // Return the remote address
                if (authority.equals("com.google.android.apps.photos.content")) {
                    return uri.getLastPathSegment();
                }
                return _queryPathFromMediaStore(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(scheme)) {
                return uri.getPath();
            }
        }
        LogUtils.verbose("uri to path: " + path);
        return path;
    }

    private static String _queryPathFromMediaStore(Context context, Uri uri, String selection, String[] selectionArgs) {
        String filePath = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
                cursor.close();
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return filePath;
    }

    /**
     * 把view转化为bitmap（截图）
     * 参见：http://www.cnblogs.com/lee0oo0/p/3355468.html
     */
    public static Bitmap toBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (view instanceof ListView) {
            height = 0;
            // 获取listView实际高度
            ListView listView = (ListView) view;
            for (int i = 0; i < listView.getChildCount(); i++) {
                height += listView.getChildAt(i).getHeight();
            }
        } else if (view instanceof ScrollView) {
            height = 0;
            // 获取scrollView实际高度
            ScrollView scrollView = (ScrollView) view;
            for (int i = 0; i < scrollView.getChildCount(); i++) {
                height += scrollView.getChildAt(i).getHeight();
            }
        }
        view.setDrawingCacheEnabled(true);
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        // Reset the drawing cache background color to fully transparent for the duration of this operation
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(Color.WHITE);//截图去黑色背景(透明像素)
        if (color != Color.WHITE) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(cacheBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (!bitmap.isRecycled()) {
            LogUtils.verbose("recycle bitmap: " + bitmap.toString());
            bitmap.recycle();
        }
        // Restore the view
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Drawable toDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(null, bitmap);
    }

    public static byte[] toByteArray(Drawable drawable) {
        return toByteArray(toBitmap(drawable));
    }

    public static Drawable toDrawable(byte[] bytes) {
        return toDrawable(toBitmap(bytes));
    }

    /**
     * dp转换为px
     */
    public static int toPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pxValue = (int) (dpValue * scale + 0.5f);
        LogUtils.verbose(dpValue + " dp == " + pxValue + " px");
        return pxValue;
    }

    public static int toPx(float dpValue) {
        Resources resources = Resources.getSystem();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
        return (int) px;
    }

    /**
     * px转换为dp
     */
    public static int toDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int dpValue = (int) (pxValue / scale + 0.5f);
        LogUtils.verbose(pxValue + " px == " + dpValue + " dp");
        return dpValue;
    }

    /**
     * px转换为sp
     */
    public static int toSp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        int spValue = (int) (pxValue / fontScale + 0.5f);
        LogUtils.verbose(pxValue + " px == " + spValue + " sp");
        return spValue;
    }

    public static String toGbk(String str) {
        try {
            return new String(str.getBytes("utf-8"), "gbk");
        } catch (UnsupportedEncodingException e) {
            LogUtils.warn(e.getMessage());
            return str;
        }
    }

    public static String toFileSizeString(long fileSize) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString;
        if (fileSize < KB) {
            fileSizeString = fileSize + "B";
        } else if (fileSize < MB) {
            fileSizeString = df.format((double) fileSize / KB) + "K";
        } else if (fileSize < GB) {
            fileSizeString = df.format((double) fileSize / MB) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / GB) + "G";
        }
        return fileSizeString;
    }

    public static String toString(InputStream is, String charset) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return sb.toString();
    }

    public static String toString(InputStream is) {
        return toString(is, "utf-8");
    }

    public static ShapeDrawable toRoundDrawable(@ColorInt int color, int radius) {
        float[] outerR = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        RoundRectShape shape = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(color);
        return drawable;
    }

    public static StateListDrawable toStateRoundDrawable(@ColorInt int color, int radius) {
        return toStateListDrawable(new ColorDrawable(Color.TRANSPARENT), toRoundDrawable(color, radius));
    }

    public static StateListDrawable toStateRoundDrawable(@ColorInt int normalColor, @ColorInt int focusColor, int radius) {
        return toStateListDrawable(toRoundDrawable(normalColor, radius), toRoundDrawable(focusColor, radius));
    }

    /**
     * 对TextView、Button等设置不同状态时其文字颜色。
     * 参见：http://blog.csdn.net/sodino/article/details/6797821
     * Modified by liyujiang at 2015.08.13
     */
    public static ColorStateList toColorStateList(@ColorInt int normalColor, @ColorInt int pressedColor,
                                                  @ColorInt int focusedColor, @ColorInt int unableColor) {
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

    public static ColorStateList toColorStateList(@ColorInt int normalColor, @ColorInt int pressedColor) {
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

    public static StateListDrawable toStateListDrawable(@ColorInt int normalColor, @ColorInt int pressedColor,
                                                        @ColorInt int focusedColor, @ColorInt int unableColor) {
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

    public static StateListDrawable toStateListDrawable(@ColorInt int normalColor, @ColorInt int pressedColor) {
        return toStateListDrawable(normalColor, pressedColor, pressedColor, normalColor);
    }

    public static StateListDrawable toStateListDrawable(@ColorInt int pressedColor) {
        return toStateListDrawable(Color.TRANSPARENT, pressedColor);
    }

}
