package cn.qqtheme.framework.logger.impl;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 格式化日志，加入边框，支持打印任意对象
 * <p>
 * Adapted from https://github.com/fodroid/XDroid-Base/.../LogFormat.java
 *
 * @author wanglei
 * @date 2016/11/29
 * @date 2018/11/30 18:12
 * @date 2019/5/23 23:50
 */
@SuppressWarnings("WeakerAccess")
@RestrictTo(RestrictTo.Scope.LIBRARY)
class LogFormatter {
    private static final int JSON_INDENT = 4;
    private static final int XML_INDENT = 4;
    private static final int BORDER_LENGTH = 88;
    private static final char VERTICAL_BORDER_CHAR = '║';
    private static String TOP_HORIZONTAL_BORDER = "╔";
    private static String DIVIDER_HORIZONTAL_BORDER = "╟";
    private static String BOTTOM_HORIZONTAL_BORDER = "╚";

    static {
        StringBuilder sbTopBottom = new StringBuilder();
        StringBuilder sbMiddle = new StringBuilder();
        for (int i = 0; i < BORDER_LENGTH; i++) {
            sbTopBottom.append("═");
            sbMiddle.append("─");
        }
        TOP_HORIZONTAL_BORDER += sbTopBottom.toString();
        DIVIDER_HORIZONTAL_BORDER += sbMiddle.toString();
        BOTTOM_HORIZONTAL_BORDER += sbTopBottom.toString();
    }

    static String formatString(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof String) {
            String s = object.toString().trim();
            String prefixObj = "{";
            String prefixArr = "[";
            String prefixXml = "<";
            if (s.startsWith(prefixObj) || s.startsWith(prefixArr)) {
                return formatJson(s);
            } else if (s.startsWith(prefixXml)) {
                return formatXml(s);
            }
            return s;
        } else if (object instanceof JSONObject || object instanceof JSONArray) {
            return formatJson(object.toString());
        } else if (object instanceof Collection) {
            return formatCollection((Collection) object);
        } else if (object instanceof Map) {
            return formatMap((Map) object);
        } else if (object instanceof Bundle) {
            return formatBundle((Bundle) object);
        } else if (object instanceof Intent) {
            return formatIntent((Intent) object);
        } else if (object instanceof Throwable) {
            return formatThrowable((Throwable) object);
        }
        return formatObject(object);
    }

    private static String formatJson(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        String formatted = json;
        try {
            String prefixObj = "{";
            String prefixArr = "[";
            if (json.startsWith(prefixObj)) {
                JSONObject jo = new JSONObject(json);
                formatted = jo.toString(JSON_INDENT);
            } else if (json.startsWith(prefixArr)) {
                JSONArray ja = new JSONArray(json);
                formatted = ja.toString(JSON_INDENT);
            }
        } catch (Exception ignore) {
        }

        return formatted;
    }

    private static String formatXml(String xml) {
        String formatted = null;
        if (xml == null || xml.trim().length() == 0) {
            return null;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                    String.valueOf(XML_INDENT));
            transformer.transform(xmlInput, xmlOutput);
            formatted = xmlOutput.getWriter().toString().replaceFirst(">", ">"
                    + lineSeparator());
        } catch (Exception ignore) {

        }
        return formatted;
    }

    private static String formatCollection(Collection collection) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("size", collection.size());
            jsonObject.put("data", collection);
            return formatJson(jsonObject.toString());
        } catch (JSONException ignore) {
            return collection.toString();
        }
    }

    private static String formatMap(Map map) {
        JSONObject jsonObject = new JSONObject(map);
        return formatJson(jsonObject.toString());
    }

    private static String formatBundle(Bundle bundle) {
        JSONObject jsonObject = new JSONObject();
        try {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                if (value instanceof Serializable) {
                    jsonObject.put(key, value);
                } else {
                    jsonObject.put(key, formatObject(value));
                }

            }
        } catch (JSONException ignore) {
        }
        return formatJson(jsonObject.toString());
    }

    private static String formatIntent(Intent intent) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Scheme", intent.getScheme());
            jsonObject.put("Action", intent.getAction());
            jsonObject.put("DataString", intent.getDataString());
            jsonObject.put("Type", intent.getType());
            jsonObject.put("Package", intent.getPackage());
            jsonObject.put("ComponentInfo", intent.getComponent());
            jsonObject.put("Categories", intent.getCategories());
            Bundle extras = intent.getExtras();
            if (extras != null) {
                jsonObject.put("Extras", formatBundle(extras));
            }
        } catch (JSONException ignore) {
        }
        return formatJson(jsonObject.toString());
    }

    private static String formatThrowable(Throwable throwable) {
        String stackTraceString = Log.getStackTraceString(throwable);
        //128 KB - 1
        final int maxStackTraceSize = 131071;
        if (stackTraceString.length() > maxStackTraceSize) {
            String disclaimer = " [stack trace too large]";
            stackTraceString = stackTraceString.substring(0, maxStackTraceSize
                    - disclaimer.length()) + disclaimer;
        }
        return stackTraceString;
    }

    private static String formatObject(Object object) {
        if (object == null) {
            return "null";
        }
        Class<?> clazz = object.getClass();
        //得到所有的字段
        List<Field> tmp = Arrays.asList(clazz.getDeclaredFields());
        //UnsupportedOperationException at java.util.AbstractList
        ArrayList<Field> list = new ArrayList<>(tmp);
        while (clazz != null && clazz != Object.class) {
            //得到继承自父类的字段
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    int modifier = field.getModifiers();
                    if (Modifier.isPublic(modifier)) {
                        list.add(field);
                    }
                }
            }
        }
        Field[] a = new Field[list.size()];
        Field[] fields = list.toArray(a);
        JSONObject jsonObject = new JSONObject();
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                Object obj = field.get(object);
                jsonObject.put(fieldName, obj);
            } catch (Exception ignore) {
            }
        }
        return formatJson(jsonObject.toString());
    }

    public static String formatArgs(String format, Object... args) {
        if (format != null) {
            return String.format(format, args);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, n = args.length; i < n; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            return sb.toString();
        }
    }

    public static String formatBorder(String[] segments) {
        if (segments == null || segments.length == 0) {
            return "";
        }
        String[] nonNullSegments = new String[segments.length];
        int nonNullCount = 0;
        for (String segment : segments) {
            if (segment != null) {
                nonNullSegments[nonNullCount++] = segment;
            }
        }
        if (nonNullCount == 0) {
            return "";
        }

        StringBuilder msgBuilder = new StringBuilder(">>>>_<<<<");
        msgBuilder.append(lineSeparator());
        msgBuilder.append(TOP_HORIZONTAL_BORDER);
        msgBuilder.append(lineSeparator());
        for (int i = 0; i < nonNullCount; i++) {
            msgBuilder.append(appendVerticalBorder(nonNullSegments[i]));
            if (i != nonNullCount - 1) {
                msgBuilder.append(lineSeparator());
                msgBuilder.append(DIVIDER_HORIZONTAL_BORDER);
                msgBuilder.append(lineSeparator());
            } else {
                msgBuilder.append(lineSeparator());
                msgBuilder.append(BOTTOM_HORIZONTAL_BORDER);
            }
        }
        return msgBuilder.toString();
    }

    private static String appendVerticalBorder(String msg) {
        StringBuilder borderedMsgBuilder = new StringBuilder(msg.length() + 10);
        String[] lines = msg.split(lineSeparator());
        for (int i = 0, n = lines.length; i < n; i++) {
            if (i != 0) {
                borderedMsgBuilder.append(lineSeparator());
            }
            String line = lines[i];
            borderedMsgBuilder.append(VERTICAL_BORDER_CHAR).append(line);
        }
        return borderedMsgBuilder.toString();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String lineSeparator() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return System.lineSeparator();
            }
        } catch (Exception ignored) {
        }
        return "\n";
    }

}
