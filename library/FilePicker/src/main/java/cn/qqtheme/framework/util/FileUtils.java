package cn.qqtheme.framework.util;

import android.Manifest;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 文件处理
 *
 * @author 李玉江[QQ :1023694760]
 * @version 2014 -4-18
 */
public final class FileUtils {
    /**
     * The constant BY_NAME_ASC.
     */
    public static final int BY_NAME_ASC = 0;
    /**
     * The constant BY_NAME_DESC.
     */
    public static final int BY_NAME_DESC = 1;
    /**
     * The constant BY_TIME_ASC.
     */
    public static final int BY_TIME_ASC = 2;
    /**
     * The constant BY_TIME_DESC.
     */
    public static final int BY_TIME_DESC = 3;
    /**
     * The constant BY_SIZE_ASC.
     */
    public static final int BY_SIZE_ASC = 4;
    /**
     * The constant BY_SIZE_DESC.
     */
    public static final int BY_SIZE_DESC = 5;
    /**
     * The constant BY_EXTENSION_ASC.
     */
    public static final int BY_EXTENSION_ASC = 6;
    /**
     * The constant BY_EXTENSION_DESC.
     */
    public static final int BY_EXTENSION_DESC = 7;

    /**
     * The interface Sort type.
     */
    @IntDef(flag = false, value = {
            BY_NAME_ASC,
            BY_NAME_DESC,
            BY_TIME_ASC,
            BY_TIME_DESC,
            BY_SIZE_ASC,
            BY_SIZE_DESC,
            BY_EXTENSION_ASC,
            BY_EXTENSION_DESC
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortType {
    }

    /**
     * 为目录结尾添加“/”
     *
     * @param path the path
     * @return string
     */
    public static String separator(String path) {
        String separator = File.separator;
        path = path.replace("\\", separator);
        if (!path.endsWith(separator)) {
            path += separator;
        }
        return path;
    }

    /**
     * Close silently.
     *
     * @param c the c
     */
    public static void closeSilently(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
            LogUtils.warn(t);
        }
    }

    /**
     * 列出指定目录下的所有子目录
     *
     * @param startDirPath the start dir path
     * @param excludeDirs  the exclude dirs
     * @param sortType     the sort type
     * @return file [ ]
     */
    public static File[] listDirs(String startDirPath, String[] excludeDirs, @SortType int sortType) {
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
        int len = dirs.length;
        if (excludeDirs == null) {
            excludeDirs = new String[0];
        }
        for (File dir : dirs) {
            File file = dir.getAbsoluteFile();
            if (!ConvertUtils.toString(excludeDirs).contains(file.getName())) {
                dirList.add(file);
            }
        }
        if (sortType == BY_NAME_ASC) {
            Collections.sort(dirList, new SortByName());
        } else if (sortType == BY_NAME_DESC) {
            Collections.sort(dirList, new SortByName());
            Collections.reverse(dirList);
        } else if (sortType == BY_TIME_ASC) {
            Collections.sort(dirList, new SortByTime());
        } else if (sortType == BY_TIME_DESC) {
            Collections.sort(dirList, new SortByTime());
            Collections.reverse(dirList);
        } else if (sortType == BY_SIZE_ASC) {
            Collections.sort(dirList, new SortBySize());
        } else if (sortType == BY_SIZE_DESC) {
            Collections.sort(dirList, new SortBySize());
            Collections.reverse(dirList);
        } else if (sortType == BY_EXTENSION_ASC) {
            Collections.sort(dirList, new SortByExtension());
        } else if (sortType == BY_EXTENSION_DESC) {
            Collections.sort(dirList, new SortByExtension());
            Collections.reverse(dirList);
        }
        return dirList.toArray(new File[dirList.size()]);
    }

    /**
     * 列出指定目录下的所有子目录
     *
     * @param startDirPath the start dir path
     * @param excludeDirs  the exclude dirs
     * @return file [ ]
     */
    public static File[] listDirs(String startDirPath, String[] excludeDirs) {
        return listDirs(startDirPath, excludeDirs, BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有子目录
     *
     * @param startDirPath the start dir path
     * @return the file [ ]
     */
    public static File[] listDirs(String startDirPath) {
        return listDirs(startDirPath, null, BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有子目录及所有文件
     *
     * @param startDirPath    the start dir path
     * @param allowExtensions the allow extensions
     * @return file [ ]
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
     * @param startDirPath the start dir path
     * @return file [ ]
     */
    public static File[] listDirsAndFiles(String startDirPath) {
        return listDirsAndFiles(startDirPath, null);
    }

    /**
     * 列出指定目录下的所有文件
     *
     * @param startDirPath  the start dir path
     * @param filterPattern the filter pattern
     * @param sortType      the sort type
     * @return the file [ ]
     */
    public static File[] listFiles(String startDirPath, final Pattern filterPattern, @SortType int sortType) {
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
        if (sortType == BY_NAME_ASC) {
            Collections.sort(fileList, new SortByName());
        } else if (sortType == BY_NAME_DESC) {
            Collections.sort(fileList, new SortByName());
            Collections.reverse(fileList);
        } else if (sortType == BY_TIME_ASC) {
            Collections.sort(fileList, new SortByTime());
        } else if (sortType == BY_TIME_DESC) {
            Collections.sort(fileList, new SortByTime());
            Collections.reverse(fileList);
        } else if (sortType == BY_SIZE_ASC) {
            Collections.sort(fileList, new SortBySize());
        } else if (sortType == BY_SIZE_DESC) {
            Collections.sort(fileList, new SortBySize());
            Collections.reverse(fileList);
        } else if (sortType == BY_EXTENSION_ASC) {
            Collections.sort(fileList, new SortByExtension());
        } else if (sortType == BY_EXTENSION_DESC) {
            Collections.sort(fileList, new SortByExtension());
            Collections.reverse(fileList);
        }
        return fileList.toArray(new File[fileList.size()]);
    }

    /**
     * 列出指定目录下的所有文件
     *
     * @param startDirPath  the start dir path
     * @param filterPattern the filter pattern
     * @return the file [ ]
     */
    public static File[] listFiles(String startDirPath, Pattern filterPattern) {
        return listFiles(startDirPath, filterPattern, BY_NAME_ASC)
                ;
    }

    /**
     * 列出指定目录下的所有文件
     *
     * @param startDirPath the start dir path
     * @return the file [ ]
     */
    public static File[] listFiles(String startDirPath) {
        return listFiles(startDirPath, null, BY_NAME_ASC);
    }

    /**
     * 列出指定目录下的所有文件
     *
     * @param startDirPath    the start dir path
     * @param allowExtensions the allow extensions
     * @return the file [ ]
     */
    public static File[] listFiles(String startDirPath, final String[] allowExtensions) {
        LogUtils.debug(String.format("list file %s", startDirPath));
        File file = new File(startDirPath);
        return file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                //返回当前目录所有以某些扩展名结尾的文件
                String extension = FileUtils.getExtension(name);
                return ConvertUtils.toString(allowExtensions).contains(extension);
            }

        });
    }

    /**
     * 列出指定目录下的所有文件
     *
     * @param startDirPath   the start dir path
     * @param allowExtension the allow extension
     * @return the file [ ]
     */
    public static File[] listFiles(String startDirPath, String allowExtension) {
        return listFiles(startDirPath, new String[]{allowExtension});
    }

    /**
     * 判断文件或目录是否存在
     *
     * @param path the path
     * @return boolean
     */
    public static boolean exist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件或目录
     *
     * @param file          the file
     * @param deleteRootDir the delete root dir
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                    //noinspection MissingPermission
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
        //noinspection ResultOfMethodCallIgnored
        file.renameTo(to);
        return to.delete();
    }

    /**
     * 删除文件或目录
     *
     * @param path          the path
     * @param deleteRootDir the delete root dir
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean delete(String path, boolean deleteRootDir) {
        File file = new File(path);
        if (file.exists()) {
            //noinspection MissingPermission
            return delete(file, deleteRootDir);
        }
        return false;
    }

    /**
     * 删除文件或目录, 不删除最顶层目录
     *
     * @param path the path
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean delete(String path) {
        //noinspection MissingPermission
        return delete(path, false);
    }

    /**
     * 删除文件或目录, 不删除最顶层目录
     *
     * @param file the file
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean delete(File file) {
        //noinspection MissingPermission
        return delete(file, false);
    }

    /**
     * 复制文件为另一个文件，或复制某目录下的所有文件及目录到另一个目录下
     *
     * @param src the src
     * @param tar the tar
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean copy(String src, String tar) {
        File srcFile = new File(src);
        //noinspection MissingPermission
        return srcFile.exists() && copy(srcFile, new File(tar));
    }

    /**
     * 复制文件或目录
     *
     * @param src the src
     * @param tar the tar
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                    //noinspection MissingPermission
                    copy(file.getAbsoluteFile(), new File(tar.getAbsoluteFile(), file.getName()));
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
     *
     * @param src the src
     * @param tar the tar
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean move(String src, String tar) {
        //noinspection MissingPermission
        return move(new File(src), new File(tar));
    }

    /**
     * 移动文件或目录
     *
     * @param src the src
     * @param tar the tar
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean move(File src, File tar) {
        //noinspection MissingPermission
        return rename(src, tar);
    }

    /**
     * Rename boolean.
     *
     * @param oldPath the old path
     * @param newPath the new path
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean rename(String oldPath, String newPath) {
        //noinspection MissingPermission
        return rename(new File(oldPath), new File(newPath));
    }

    /**
     * Rename boolean.
     *
     * @param src the src
     * @param tar the tar
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
     *
     * @param filepath the filepath
     * @param charset  the charset
     * @return the string
     */
    public static String readText(String filepath, String charset) {
        LogUtils.debug(String.format("read %s use %s", filepath, charset));
        try {
            byte[] data = readByte(filepath);
            if (data != null) {
                return new String(data, charset);
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return "";
    }

    /**
     * 读取文本文件, 失败将返回空串
     *
     * @param filepath the filepath
     * @return the string
     */
    public static String readText(String filepath) {
        return readText(filepath, "utf-8");
    }

    /**
     * 读取文件内容, 失败将返回空串
     *
     * @param filepath the filepath
     * @return the byte [ ]
     */
    public static byte[] readByte(String filepath) {
        LogUtils.debug(String.format("read %s", filepath));
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filepath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] data = baos.toByteArray();
            baos.close();
            return data;
        } catch (Exception e) {
            LogUtils.error(e);
            return null;
        } finally {
            closeSilently(fis);
        }
    }

    /**
     * 保存文本内容
     *
     * @param filepath the filepath
     * @param content  the content
     * @param charset  the charset
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean writeText(String filepath, String content, String charset) {
        try {
            //noinspection MissingPermission
            writeByte(filepath, content.getBytes(charset));
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        }
    }

    /**
     * Write text boolean.
     *
     * @param filepath the filepath
     * @param content  the content
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean writeText(String filepath, String content) {
        //noinspection MissingPermission
        return writeText(filepath, content, "utf-8");
    }

    /**
     * 保存文件内容
     *
     * @param filepath the filepath
     * @param data     the data
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean writeByte(String filepath, byte[] data) {
        LogUtils.debug(String.format("write %s", filepath));
        File file = new File(filepath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            fos = new FileOutputStream(filepath);
            fos.write(data);
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        } finally {
            closeSilently(fos);
        }
    }

    /**
     * 追加文本内容
     *
     * @param path    the path
     * @param content the content
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean appendText(String path, String content) {
        LogUtils.debug(String.format("append %s", path));
        File file = new File(path);
        FileWriter writer = null;
        try {
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            writer = new FileWriter(file, true);
            writer.write(content);
            return true;
        } catch (IOException e) {
            LogUtils.error(e);
            return false;
        } finally {
            closeSilently(writer);
        }
    }

    /**
     * 获取文件大小
     *
     * @param path the path
     * @return length
     */
    public static long getLength(String path) {
        File file = new File(path);
        if (!file.isFile() || !file.exists()) {
            return 0;
        }
        return file.length();
    }

    /**
     * 获取格式化后的文件大小
     *
     * @param path the path
     * @return size
     */
    public static String getSize(String path) {
        long fileSize = getLength(path);
        return ConvertUtils.toFileSizeString(fileSize);
    }

    /**
     * 获取文件后缀,不包括“.”
     *
     * @param pathOrUrl the path or url
     * @return extension
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
     * @param pathOrUrl the path or url
     * @return mime type
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
     * @param path the path
     * @return date time
     */
    public static String getDateTime(String path) {
        return getDateTime(path, "yyyy年MM月dd日HH:mm");
    }

    /**
     * 获取格式化后的文件/目录创建或最后修改时间
     *
     * @param path   the path
     * @param format the format
     * @return date time
     */
    public static String getDateTime(String path, String format) {
        File file = new File(path);
        return getDateTime(file, format);
    }

    /**
     * 获取格式化后的文件/目录创建或最后修改时间
     *
     * @param file   the file
     * @param format the format
     * @return date time
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
     * @param path1 the path 1
     * @param path2 the path 2
     * @return int
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

    /**
     * Make dirs boolean.
     *
     * @param path the path
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean makeDirs(String path) {
        //noinspection MissingPermission
        return makeDirs(new File(path));
    }

    /**
     * Make dirs boolean.
     *
     * @param file the file
     * @return the boolean
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static boolean makeDirs(File file) {
        return file.mkdirs();
    }

    /**
     * The type Sort by extension.
     */
    public static class SortByExtension implements Comparator<File> {

        /**
         * Instantiates a new Sort by extension.
         */
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

    /**
     * The type Sort by name.
     */
    public static class SortByName implements Comparator<File> {
        private boolean caseSensitive;

        /**
         * Instantiates a new Sort by name.
         *
         * @param caseSensitive the case sensitive
         */
        public SortByName(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        /**
         * Instantiates a new Sort by name.
         */
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

    /**
     * The type Sort by size.
     */
    public static class SortBySize implements Comparator<File> {

        /**
         * Instantiates a new Sort by size.
         */
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

    /**
     * The type Sort by time.
     */
    public static class SortByTime implements Comparator<File> {

        /**
         * Instantiates a new Sort by time.
         */
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
