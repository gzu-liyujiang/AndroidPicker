package cn.qqtheme.framework.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.TypedValue;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Arrays;

/**
 * 数据类型转换、单位转换
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2014-4-18
 */
public class ConvertUtils {
    /**
     * The constant GB.
     */
    public static final long GB = 1073741824;
    /**
     * The constant MB.
     */
    public static final long MB = 1048576;
    /**
     * The constant KB.
     */
    public static final long KB = 1024;

    /**
     * 转换为6位十六进制颜色代码，不含“#”
     *
     * @param color the color
     * @return string string
     */
    public static String toColorString(int color) {
        return toColorString(color, false);
    }

    /**
     * 转换为6位十六进制颜色代码，不含“#”
     *
     * @param color        the color
     * @param includeAlpha the include alpha
     * @return string string
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
     *
     * @param date   the date
     * @param format the format
     * @return string string
     */
    public static String toDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 将当前的日期转换为一定格式的字符串
     *
     * @param format the format
     * @return string string
     */
    public static String toDateString(String format) {
        return toDateString(Calendar.getInstance(Locale.CHINA).getTime(), format);
    }

    /**
     * 将指定的日期字符串转换为日期时间
     *
     * @param dateStr 如：2014-04-08 23:02
     * @return date date
     */
    public static Date toDate(String dateStr) {
        return DateUtils.parseDate(dateStr);
    }

    /**
     * 将指定的日期字符串转换为时间戳
     *
     * @param dateStr 如：2014-04-08 23:02
     * @return long long
     */
    public static long toTimemillis(String dateStr) {
        return toDate(dateStr).getTime();
    }

    /**
     * To string string.
     *
     * @param objects the objects
     * @return the string
     */
    public static String toString(Object[] objects) {
        return Arrays.deepToString(objects);
    }

    /**
     * To string string.
     *
     * @param objects the objects
     * @param tag     the tag
     * @return the string
     */
    public static String toString(Object[] objects, String tag) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object);
            sb.append(tag);
        }
        return sb.toString();
    }

    /**
     * To byte array byte [ ].
     *
     * @param is the is
     * @return the byte [ ]
     */
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
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To byte array byte [ ].
     *
     * @param bitmap the bitmap
     * @return the byte [ ]
     */
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
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * To bitmap bitmap.
     *
     * @param bytes  the bytes
     * @param width  the width
     * @param height the height
     * @return the bitmap
     */
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

    /**
     * To bitmap bitmap.
     *
     * @param bytes the bytes
     * @return the bitmap
     */
    public static Bitmap toBitmap(byte[] bytes) {
        return toBitmap(bytes, -1, -1);
    }

    /**
     * convert Drawable to Bitmap
     * 参考：http://kylines.iteye.com/blog/1660184
     *
     * @param drawable the drawable
     * @return bitmap bitmap
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
     * convert Bitmap to Drawable
     *
     * @param bitmap the bitmap
     * @return drawable drawable
     */
    public static Drawable toDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(null, bitmap);
    }

    /**
     * convert Drawable to byte array
     *
     * @param drawable the drawable
     * @return byte [ ]
     */
    public static byte[] toByteArray(Drawable drawable) {
        return toByteArray(toBitmap(drawable));
    }

    /**
     * convert byte array to Drawable
     *
     * @param bytes the bytes
     * @return drawable drawable
     */
    public static Drawable toDrawable(byte[] bytes) {
        return toDrawable(toBitmap(bytes));
    }

    /**
     * dp转换为px
     *
     * @param context the context
     * @param dpValue the dp value
     * @return int int
     */
    public static int toPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pxValue = (int) (dpValue * scale + 0.5f);
        LogUtils.verbose(dpValue + " dp == " + pxValue + " px");
        return pxValue;
    }

    /**
     * To px int.
     *
     * @param dpValue the dp value
     * @return the int
     */
    public static int toPx(float dpValue) {
        Resources resources = Resources.getSystem();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
        return (int) px;
    }

    /**
     * px转换为dp
     *
     * @param context the context
     * @param pxValue the px value
     * @return int int
     */
    public static int toDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int dpValue = (int) (pxValue / scale + 0.5f);
        LogUtils.verbose(pxValue + " px == " + dpValue + " dp");
        return dpValue;
    }

    /**
     * px转换为sp
     *
     * @param context the context
     * @param pxValue the px value
     * @return int int
     */
    public static int toSp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        int spValue = (int) (pxValue / fontScale + 0.5f);
        LogUtils.verbose(pxValue + " px == " + spValue + " sp");
        return spValue;
    }

    /**
     * To gbk string.
     *
     * @param str the str
     * @return the string
     */
    public static String toGbk(String str) {
        try {
            return new String(str.getBytes("utf-8"), "gbk");
        } catch (UnsupportedEncodingException e) {
            LogUtils.warn(e.getMessage());
            return str;
        }
    }

    /**
     * To file size string string.
     *
     * @param fileSize the file size
     * @return the string
     */
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

    /**
     * To string string.
     *
     * @param is the is
     * @return the string
     */
    public static String toString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return sb.toString();
    }

    /**
     * To round drawable shape drawable.
     *
     * @param color  the color
     * @param radius the radius
     * @return the shape drawable
     */
    public static ShapeDrawable toRoundDrawable(int color, int radius) {
        int r = toPx(radius);
        float[] outerR = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape shape = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(color);
        return drawable;
    }

    /**
     * 对TextView、Button等设置不同状态时其文字颜色。
     * 参见：http://blog.csdn.net/sodino/article/details/6797821
     * Modified by liyujiang at 2015.08.13
     *
     * @param normalColor  the normal color
     * @param pressedColor the pressed color
     * @param focusedColor the focused color
     * @param unableColor  the unable color
     * @return the color state list
     */
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

    /**
     * To color state list color state list.
     *
     * @param normalColor  the normal color
     * @param pressedColor the pressed color
     * @return the color state list
     */
    public static ColorStateList toColorStateList(int normalColor, int pressedColor) {
        return toColorStateList(normalColor, pressedColor, pressedColor, normalColor);
    }

    /**
     * To state list drawable state list drawable.
     *
     * @param normal  the normal
     * @param pressed the pressed
     * @param focused the focused
     * @param unable  the unable
     * @return the state list drawable
     */
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

    /**
     * To state list drawable state list drawable.
     *
     * @param normalColor  the normal color
     * @param pressedColor the pressed color
     * @param focusedColor the focused color
     * @param unableColor  the unable color
     * @return the state list drawable
     */
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

    /**
     * To state list drawable state list drawable.
     *
     * @param normal  the normal
     * @param pressed the pressed
     * @return the state list drawable
     */
    public static StateListDrawable toStateListDrawable(Drawable normal, Drawable pressed) {
        return toStateListDrawable(normal, pressed, pressed, normal);
    }

    /**
     * To state list drawable state list drawable.
     *
     * @param normalColor  the normal color
     * @param pressedColor the pressed color
     * @return the state list drawable
     */
    public static StateListDrawable toStateListDrawable(int normalColor, int pressedColor) {
        return toStateListDrawable(normalColor, pressedColor, pressedColor, normalColor);
    }

}
