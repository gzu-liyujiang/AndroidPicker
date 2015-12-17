package cn.qqtheme.framework.adapter;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.bean.FileItem;
import cn.qqtheme.framework.picker.file.R;
import cn.qqtheme.framework.util.FileUtils;
import cn.qqtheme.framework.util.LogUtils;

public class FileAdapter extends IconTextAdapter<FileItem<Integer>> {
    public static final String DIR_ROOT = "..";
    public static final String DIR_PARENT = ".";
    private String rootPath = null;
    private String currentPath = null;
    private String[] allowExtensions = null;//允许的扩展名
    private boolean onlyListDir = false;//是否仅仅读取目录
    private boolean showHomeDir = false;//是否显示返回主目录
    private boolean showUpDir = true;//是否显示返回上一级
    private boolean showHideDir = true;//是否显示隐藏的目录（以“.”开头）
    private int homeIcon = R.drawable.file_picker_home;
    private int upIcon = R.drawable.file_picker_updir;
    private int folderIcon = R.drawable.file_picker_folder;
    private int fileIcon = R.drawable.file_picker_file;

    public FileAdapter(Context context) {
        super(context);
    }

    public FileAdapter(Context context, List<FileItem<Integer>> data) {
        super(context, data);
    }

    public FileAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public FileAdapter(Context context, int layoutRes, List<FileItem<Integer>> data) {
        super(context, layoutRes, data);
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setAllowExtensions(String[] allowExtensions) {
        this.allowExtensions = allowExtensions;
    }

    public void setOnlyListDir(boolean onlyListDir) {
        this.onlyListDir = onlyListDir;
    }

    public void setShowHomeDir(boolean showHomeDir) {
        this.showHomeDir = showHomeDir;
    }

    public void setShowUpDir(boolean showUpDir) {
        this.showUpDir = showUpDir;
    }

    public void setShowHideDir(boolean showHideDir) {
        this.showHideDir = showHideDir;
    }

    public ArrayList<FileItem<Integer>> loadData(String path) {
        return loadData(path, true);
    }

    public ArrayList<FileItem<Integer>> loadData(String path, boolean notifyDataSetChanged) {
        ArrayList<FileItem<Integer>> datas = new ArrayList<FileItem<Integer>>();
        if (path == null) {
            LogUtils.warn("current directory is null");
            return datas;
        }
        if (rootPath == null) {
            rootPath = path;
        }
        LogUtils.debug("current directory path: " + path);
        currentPath = path;
        if (showHomeDir) {
            //添加“返回主目录”
            FileItem<Integer> fileRoot = new FileItem<Integer>();
            fileRoot.setDirectory(true);
            fileRoot.setIcon(homeIcon);
            fileRoot.setName(DIR_ROOT);
            fileRoot.setSize(0);
            fileRoot.setPath(rootPath);
            datas.add(fileRoot);
        }
        if (showUpDir && !path.equals("/")) {
            //添加“返回上一级目录”
            FileItem<Integer> fileParent = new FileItem<Integer>();
            fileParent.setDirectory(true);
            fileParent.setIcon(upIcon);
            fileParent.setName(DIR_PARENT);
            fileParent.setSize(0);
            fileParent.setPath(new File(path).getParent());
            datas.add(fileParent);
        }
        File[] files;
        if (allowExtensions == null) {
            if (onlyListDir) {
                files = FileUtils.listDirs(path);
            } else {
                files = FileUtils.listDirsAndFiles(path);
            }
        } else {
            if (onlyListDir) {
                files = FileUtils.listDirs(path, allowExtensions);
            } else {
                files = FileUtils.listDirsAndFiles(path, allowExtensions);
            }
        }
        if (files != null) {
            for (File file : files) {
                if (!showHideDir && file.getName().startsWith(".")) {
                    continue;
                }
                FileItem<Integer> fileItem = new FileItem<Integer>();
                boolean isDirectory = file.isDirectory();
                fileItem.setDirectory(isDirectory);
                if (isDirectory) {
                    fileItem.setIcon(folderIcon);
                    fileItem.setSize(0);
                } else {
                    fileItem.setIcon(fileIcon);
                    fileItem.setSize(file.length());
                }
                fileItem.setName(file.getName());
                fileItem.setPath(file.getAbsolutePath());
                datas.add(fileItem);
            }
        }
        if (notifyDataSetChanged) {
            replaceAll(datas);
        }
        return datas;
    }

}
