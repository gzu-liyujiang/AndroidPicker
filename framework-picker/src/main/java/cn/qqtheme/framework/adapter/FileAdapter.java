package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Color;

import cn.qqtheme.framework.entity.FileItem;
import cn.qqtheme.framework.helper.FileUtils;
import cn.qqtheme.framework.helper.Logger;
import cn.qqtheme.framework.picker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends IconTextAdapter<FileItem> {
    public static final String DIR_ROOT = "..";
    public static final String DIR_PARENT = ".";
    private String rootPath = null;
    private String[] allowExtensions = null;//允许的扩展名
    private boolean showUpOrRoot = true;//是否显示返回上一级或根目录
    private boolean showHiddenDir = true;//是否显示隐藏的目录（以“.”开头）
    private int homeIcon = R.drawable.file_picker_home;
    private int updirIcon = R.drawable.file_picker_updir;
    private int folderIcon = R.drawable.file_picker_folder;
    private int fileIcon = R.drawable.file_picker_file;

    public FileAdapter(Context context) {
        super(context);
    }

    public FileAdapter(Context context, List<FileItem> data) {
        super(context, data);
    }

    public FileAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public FileAdapter(Context context, int layoutRes, List<FileItem> data) {
        super(context, layoutRes, data);
    }

    @Override
    protected void convert(AdapterHelper helper, FileItem item) {
        super.convert(helper, item);
        helper.setTextColor(R.id.text, Color.BLACK);
    }

    public void setAllowExtensions(String[] allowExtensions) {
        this.allowExtensions = allowExtensions;
    }

    public void setShowUpOrRoot(boolean showUpOrRoot) {
        this.showUpOrRoot = showUpOrRoot;
    }

    public void setShowHiddenDir(boolean showHiddenDir) {
        this.showHiddenDir = showHiddenDir;
    }

    public ArrayList<FileItem> loadData(String path) {
        return loadData(path, true);
    }

    public ArrayList<FileItem> loadData(String path, boolean notifyDataSetChanged) {
        ArrayList<FileItem> datas = new ArrayList<FileItem>();
        if (path == null) {
            Logger.warn("current directory is null");
            return datas;
        }
        if (rootPath == null) {
            rootPath = path;
        }
        Logger.debug("current directory path: " + path);
        if (showUpOrRoot) {
            //添加“返回主目录”及“返回上一级”
            FileItem fileRoot = new FileItem();
            fileRoot.setDirectory(true);
            fileRoot.setIcon(homeIcon);
            fileRoot.setName(DIR_ROOT);
            fileRoot.setSize(0);
            fileRoot.setPath(rootPath);
            datas.add(fileRoot);
            if (!path.equals("/")) {
                FileItem fileParent = new FileItem();
                fileParent.setDirectory(true);
                fileParent.setIcon(updirIcon);
                fileParent.setName(DIR_PARENT);
                fileRoot.setSize(0);
                fileParent.setPath(new File(path).getParent());
                datas.add(fileParent);
            }
        }
        File[] files;
        if (allowExtensions == null) {
            files = FileUtils.listDirsAndFiles(path);
        } else {
            files = FileUtils.listDirsAndFiles(path, allowExtensions);
        }
        if (files != null) {
            for (File file : files) {
                if (!showHiddenDir && file.getName().startsWith(".")) {
                    continue;
                }
                FileItem fileItem = new FileItem();
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
