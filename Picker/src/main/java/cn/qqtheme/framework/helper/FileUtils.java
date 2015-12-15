package cn.qqtheme.framework.helper;

import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 文件处理
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2014-4-18
 */
public class FileUtils {

    private FileUtils() {
        super();
    }

    /**
     * 为目录结尾添加“/”
     *
     * @param path
     * @return
     */
    public static String separator(String path) {
        path = path.replace('\\', '/');
        if (!path.endsWith("/"))
            path = path + "/";
        return path;
    }

    /**
     * 列出指定目录下的所有子目录
     *
     * @param startDirPath
     * @param excludeDirs
     * @param sortType
     * @return
     */
    public static File[] listDirs(String startDirPath, String[] excludeDirs, SortType sortType) {
        LogUtils.debug(String.format("list dir %s", startDirPath));
        ArrayList<File> dirList = new ArrayList<File>();
        File startDir = new File(startDirPath);
        if (!startDir.isDirectory()) {
            return new File[0];
        }
        File[] dirs = startDir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (f == null) {
                    return false;
                }
                if (f.isDirectory()) {
                    return true;
                }
                return false;
            }
        });
        if (dirs == null) {
            return new File[0];
        }
        if (excludeDirs == null) {
            excludeDirs = new String[0];
        }
        for (File dir : dirs) {
            File file = dir.getAbsoluteFile();
            if (!Arrays.deepToString(excludeDirs).contains(file.getName())) {
                dirList.add(file);
            }
        }
        if (sortType.equals(SortType.BY_NAME_ASC)) {
            Collections.sort(dirList, new SortByName());
        } else if (sortType.equals(SortType.BY_NAME_DESC)) {
            Collections.sort(dirList, new SortByName());
            Collections.reverse(dirList);
        } else if (sortType.equals(SortType.BY_TIME_ASC)) {
            Collections.sort(dirList, new SortByTime());
        } else if (sortType.equals(SortType.BY_TIME_DESC)) {
            Collections.sort(dirList, new SortByTime());
            Collections.reverse(dirList);
        } else if (sortType.equals(SortType.BY_SIZE_ASC)) {
            Collections.sort(dirList, new SortBySize());
        } else if (sortType.equals(SortType.BY_SIZE_DESC)) {
            Collections.sort(dirList, new SortBySize());
            Collections.reverse(dirList);
        } else if (sortType.equals(SortType.BY_EXTENSION_ASC)) {
            Collections.sort(dirList, new SortByExtension());
        } else if (sortType.equals(SortType.BY_EXTENSION_DESC)) {
            Collections.sort(dirList, new SortByExtension());
            Collections.reverse(dirList);
        }
        return dirList.toArray(new File[dirList.size()]);
    }

    /**
     * 列出指定目录下的所有子目录
     *
     * @param startDirPath
     * @param excludeDirs
     * @return
     */
    public static File[] listDirs(String startDirPath, String[] excludeDirs) {
        return listDirs(startDirPath, excludeDirs, SortType.BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有子目录
     */
    public static File[] listDirs(String startDirPath) {
        return listDirs(startDirPath, null, SortType.BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有子目录及所有文件
     *
     * @param startDirPath
     * @return
     */
    public static File[] listDirsAndFiles(String startDirPath, String[] allowExtensions) {
        File[] dirs, files, dirsAndFiles;
        dirs = listDirs(startDirPath);
        if (allowExtensions == null) {
            files = listFiles(startDirPath);
        } else {
            files = listFiles(startDirPath, allowExtensions);
        }
        if (dirs == null || files == null) {
            return null;
        }
        dirsAndFiles = new File[dirs.length + files.length];
        System.arraycopy(dirs, 0, dirsAndFiles, 0, dirs.length);
        System.arraycopy(files, 0, dirsAndFiles, dirs.length, files.length);
        return dirsAndFiles;
    }

    /**
     * 列出指定目录下的所有子目录及所有文件
     *
     * @param startDirPath
     * @return
     */
    public static File[] listDirsAndFiles(String startDirPath) {
        return listDirsAndFiles(startDirPath, null);
    }

    /**
     * 列出指定目录下的所有文件
     */
    public static File[] listFiles(String startDirPath, final Pattern filterPattern, SortType sortType) {
        LogUtils.debug(String.format("list file %s", startDirPath));
        ArrayList<File> fileList = new ArrayList<File>();
        File f = new File(startDirPath);
        if (!f.isDirectory()) {
            return new File[0];
        }
        File[] files = f.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (f == null) {
                    return false;
                }
                if (f.isDirectory()) {
                    return false;
                }
                if (filterPattern == null) {
                    return true;
                }
                return filterPattern.matcher(f.getName()).find();
            }
        });
        if (files == null) {
            return new File[0];
        }
        for (File file : files) {
            fileList.add(file.getAbsoluteFile());
        }
        if (sortType.equals(SortType.BY_NAME_ASC)) {
            Collections.sort(fileList, new SortByName());
        } else if (sortType.equals(SortType.BY_NAME_DESC)) {
            Collections.sort(fileList, new SortByName());
            Collections.reverse(fileList);
        } else if (sortType.equals(SortType.BY_TIME_ASC)) {
            Collections.sort(fileList, new SortByTime());
        } else if (sortType.equals(SortType.BY_TIME_DESC)) {
            Collections.sort(fileList, new SortByTime());
            Collections.reverse(fileList);
        } else if (sortType.equals(SortType.BY_SIZE_ASC)) {
            Collections.sort(fileList, new SortBySize());
        } else if (sortType.equals(SortType.BY_SIZE_DESC)) {
            Collections.sort(fileList, new SortBySize());
            Collections.reverse(fileList);
        } else if (sortType.equals(SortType.BY_EXTENSION_ASC)) {
            Collections.sort(fileList, new SortByExtension());
        } else if (sortType.equals(SortType.BY_EXTENSION_DESC)) {
            Collections.sort(fileList, new SortByExtension());
            Collections.reverse(fileList);
        }
        return fileList.toArray(new File[fileList.size()]);
    }

    /**
     * 列出指定目录下的所有文件
     */
    public static File[] listFiles(String startDirPath, Pattern filterPattern) {
        return listFiles(startDirPath, filterPattern, SortType.BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有文件
     */
    public static File[] listFiles(String startDirPath) {
        return listFiles(startDirPath, null, SortType.BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有文件
     */
    public static File[] listFiles(String startDirPath, final String[] allowExtensions) {
        LogUtils.debug(String.format("list file %s", startDirPath));
        File file = new File(startDirPath);
        return file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                //返回当前目录所有以某些扩展名结尾的文件
                String extension = FileUtils.getExtension(name);
                return Arrays.deepToString(allowExtensions).contains(extension);
            }

        });
    }

    /**
     * 列出指定目录下的所有文件
     */
    public static File[] listFiles(String startDirPath, String allowExtension) {
        return listFiles(startDirPath, new String[]{allowExtension});
    }

    /**
     * 判断文件或目录是否存在
     *
     * @param path
     * @return
     */
    public static boolean exist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件或目录
     */
    public static boolean delete(File file, boolean deleteRootDir) {
        LogUtils.debug(String.format("delete file %s", file.getAbsolutePath()));
        boolean result = false;
        if (file.isFile()) {
            //是文件
            result = deleteResolveEBUSY(file);
        } else {
            //是目录
            File[] files = file.listFiles();
            if (files == null) {
                return false;
            }
            if (files.length == 0) {
                result = deleteRootDir && deleteResolveEBUSY(file);
            } else {
                for (File f : files) {
                    delete(f, deleteRootDir);
                    result = deleteResolveEBUSY(f);
                }
            }
            if (deleteRootDir) {
                result = deleteResolveEBUSY(file);
            }
        }
        return result;
    }

    /**
     * bug: open failed: EBUSY (Device or resource busy)
     * fix: http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
     */
    private static boolean deleteResolveEBUSY(File file) {
        // Before you delete a Directory or File: rename it!
        final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);
        return to.delete();
    }

    /**
     * 删除文件或目录
     */
    public static boolean delete(String path, boolean deleteRootDir) {
        File file = new File(path);
        if (file.exists()) {
            return delete(file, deleteRootDir);
        }
        return false;
    }

    /**
     * 删除文件或目录, 不删除最顶层目录
     */
    public static boolean delete(String path) {
        return delete(path, false);
    }

    /**
     * 删除文件或目录, 不删除最顶层目录
     */
    public static boolean delete(File file) {
        return delete(file, false);
    }

    /**
     * 复制文件为另一个文件，或复制某目录下的所有文件及目录到另一个目录下
     */
    public static boolean copy(String src, String tar) {
        File srcFile = new File(src);
        if (!srcFile.exists()) {
            return false;
        }
        return copy(srcFile, new File(tar));
    }

    /**
     * 复制文件或目录
     */
    public static boolean copy(File src, File tar) {
        try {
            LogUtils.debug(String.format("copy %s to %s", src.getAbsolutePath(), tar.getAbsolutePath()));
            if (src.isFile()) {
                InputStream is = new FileInputStream(src);
                OutputStream op = new FileOutputStream(tar);
                BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(op);
                byte[] bt = new byte[1024 * 8];
                int len = bis.read(bt);
                while (len != -1) {
                    bos.write(bt, 0, len);
                    len = bis.read(bt);
                }
                bis.close();
                bos.close();
            } else if (src.isDirectory()) {
                File[] files = src.listFiles();
                //noinspection ResultOfMethodCallIgnored
                tar.mkdirs();
                for (File file : files) {
                    copy(file.getAbsoluteFile(), new File(tar.getAbsoluteFile()
                            + File.separator + file.getName()));
                }
            }
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        }
    }

    /**
     * 移动文件或目录
     */
    public static boolean move(String src, String tar) {
        return move(new File(src), new File(tar));
    }

    /**
     * 移动文件或目录
     */
    public static boolean move(File src, File tar) {
        return rename(src, tar);
    }

    public static boolean rename(String oldPath, String newPath) {
        return rename(new File(oldPath), new File(newPath));
    }

    public static boolean rename(File src, File tar) {
        try {
            LogUtils.debug(String.format("rename %s to %s", src.getAbsolutePath(), tar.getAbsolutePath()));
            return src.renameTo(tar);
        } catch (Exception e) {
            LogUtils.warn(e);
            return false;
        }
    }

    /**
     * 读取文本文件, 失败将返回空串
     */
    public static String readText(String filepath, String charset) {
        LogUtils.debug(String.format("read %s use %s", filepath, charset));
        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(filepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, charset));
            while (br.ready()) {
                String line = br.readLine();
                if (line != null) {
                    // 读出来文件末尾多了“null”?
                    sb.append(line).append("\n");
                }
            }
            br.close();
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            LogUtils.error(e);
            return "";
        }
    }

    /**
     * 读取文本文件, 失败将返回空串
     */
    public static String readText(String filepath) {
        return readText(filepath, "utf-8");
    }

    /**
     * 读取文件内容, 失败将返回空串
     */
    public static byte[] readByte(String filepath) {
        LogUtils.debug(String.format("read %s", filepath));
        try {
            FileInputStream fis = new FileInputStream(filepath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] data = baos.toByteArray();
            baos.close();
            fis.close();
            return data;
        } catch (Exception e) {
            LogUtils.error(e);
            return null;
        }
    }

    /**
     * 保存文本内容
     *
     * @param filepath
     * @param content
     */
    public static boolean writeText(String filepath, String content, String charset) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filepath), charset));
            writer.write(content);
            writer.close();
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        }
    }

    public static boolean writeText(String filepath, String content) {
        return writeText(filepath, content, "utf-8");
    }

    /**
     * 保存文件内容
     *
     * @param filepath
     * @param data
     */
    public static boolean writeByte(String filepath, byte[] data) {
        LogUtils.debug(String.format("write %s", filepath));
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        }
    }

    /**
     * 追加文本内容
     *
     * @param path
     * @param content
     */
    public static boolean appendText(String path, String content) {
        LogUtils.debug(String.format("append %s", path));
        File file = new File(path);
        try {
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, true);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            LogUtils.error(e);
            return false;
        }
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     */
    public static long getLength(String path) {
        File file = new File(path);
        if (!file.isFile() || !file.exists()) {
            return 0;
        }
        return file.length();
    }

    /**
     * 获取文件名（包括扩展名）
     *
     * @param pathOrUrl
     * @return
     */
    public static String getName(String pathOrUrl) {
        return getName(pathOrUrl, false);
    }

    public static String getName(String pathOrUrl, boolean useHash) {
        if (useHash) {
            return pathOrUrl.replace("/","_") + "." + getExtension(pathOrUrl);
        }
        int pos = pathOrUrl.lastIndexOf('/');
        if (0 <= pos) {
            return pathOrUrl.substring(pos + 1);
        } else {
            return String.valueOf(System.currentTimeMillis()) + "." + getExtension(pathOrUrl);
        }
    }

    /**
     * 获取文件名（不包括扩展名）
     *
     * @param pathOrUrl
     * @return
     */
    public static String getNameExcludeExtension(String pathOrUrl) {
        try {
            String name = getName(pathOrUrl);
            return name.substring(0, name.lastIndexOf('.'));
        } catch (Exception e) {
            LogUtils.warn(e.toString());
            return "";
        }
    }

    /**
     * 获取文件后缀,不包括“.”
     *
     * @param pathOrUrl
     * @return
     */
    public static String getExtension(String pathOrUrl) {
        int dotPos = pathOrUrl.lastIndexOf('.');
        if (0 <= dotPos) {
            return pathOrUrl.substring(dotPos + 1);
        } else {
            return "ext";
        }
    }

    /**
     * 获取文件的MIME类型
     *
     * @param pathOrUrl
     * @return
     */
    public static String getMimeType(String pathOrUrl) {
        String ext = getExtension(pathOrUrl);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String mimeType;
        if (map.hasExtension(ext)) {
            mimeType = map.getMimeTypeFromExtension(ext);
        } else {
            mimeType = "*/*";
        }
        LogUtils.debug(pathOrUrl + ": " + mimeType);
        return mimeType;
    }

    /**
     * 获取格式化后的文件/目录创建或最后修改时间
     *
     * @param path
     * @return
     */
    public static String getDateTime(String path) {
        return getDateTime(path, "yyyy年MM月dd日HH:mm");
    }

    /**
     * 获取格式化后的文件/目录创建或最后修改时间
     *
     * @param path
     * @param format
     * @return
     */
    public static String getDateTime(String path, String format) {
        File file = new File(path);
        return getDateTime(file, format);
    }

    /**
     * 获取格式化后的文件/目录创建或最后修改时间
     *
     * @param file
     * @param format
     * @return
     */
    public static String getDateTime(File file, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(file.lastModified());
        SimpleDateFormat chineseDateFormat = new SimpleDateFormat(format,
                Locale.CHINA);
        return chineseDateFormat.format(cal.getTime());
    }

    /**
     * 比较两个文件的最后修改时间
     *
     * @param path1
     * @param path2
     * @return
     */
    public static int compareLastModified(String path1, String path2) {
        long stamp1 = (new File(path1)).lastModified();
        long stamp2 = (new File(path2)).lastModified();
        if (stamp1 > stamp2) {
            return 1;
        } else if (stamp1 < stamp2) {
            return -1;
        } else {
            return 0;
        }
    }

    public static boolean makeDirs(String path) {
        return (new File(path)).mkdirs();
    }

    public enum SortType {
        BY_NAME_ASC,
        BY_NAME_DESC,
        BY_TIME_ASC,
        BY_TIME_DESC,
        BY_SIZE_ASC,
        BY_SIZE_DESC,
        BY_EXTENSION_ASC,
        BY_EXTENSION_DESC,
    }

    public static class SortByExtension implements Comparator<File> {

        public SortByExtension() {
            super();
        }

        public int compare(File f1, File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (f1.isDirectory() && f2.isFile()) {
                    return -1;
                } else if (f1.isFile() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            }
        }

    }

    public static class SortByName implements Comparator<File> {
        private boolean caseSensitive;

        public SortByName(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        public SortByName() {
            this.caseSensitive = false;
        }

        public int compare(File f1, File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (f1.isDirectory() && f2.isFile()) {
                    return -1;
                } else if (f1.isFile() && f2.isDirectory()) {
                    return 1;
                } else {
                    String s1 = f1.getName();
                    String s2 = f2.getName();
                    if (caseSensitive) {
                        return s1.compareTo(s2);
                    } else {
                        return s1.compareToIgnoreCase(s2);
                    }
                }
            }
        }

    }

    public static class SortBySize implements Comparator<File> {

        public SortBySize() {
            super();
        }

        public int compare(File f1, File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (f1.isDirectory() && f2.isFile()) {
                    return -1;
                } else if (f1.isFile() && f2.isDirectory()) {
                    return 1;
                } else {
                    if (f1.length() < f2.length()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }

    }

    public static class SortByTime implements Comparator<File> {

        public SortByTime() {
            super();
        }

        public int compare(File f1, File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (f1.isDirectory() && f2.isFile()) {
                    return -1;
                } else if (f1.isFile() && f2.isDirectory()) {
                    return 1;
                } else {
                    if (f1.lastModified() > f2.lastModified()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }

    }

}
