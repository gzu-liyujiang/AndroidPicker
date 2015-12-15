package cn.qqtheme.framework.helper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 操作安装包中的“assets”目录下的文件
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2013-11-2
 */
public class AssetsUtils {

    /**
     * read file content
     *
     * @param assetPath
     * @return String
     */
    public static String readText(Context context, String assetPath) {
        LogUtils.debug("read assets file as text: " + assetPath);
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = context.getAssets().open(assetPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            while (br.ready()) {
                String line = br.readLine();
                if (line != null) {
                    // 读出来文件末尾多了“null”?
                    sb.append(line).append("\n");
                }
            }
            br.close();
            is.close();
            return sb.toString();
        } catch (Exception e) {
            LogUtils.error(e);
            return "";
        }
    }

}
