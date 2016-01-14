package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import cn.qqtheme.framework.entity.FileItem;
import cn.qqtheme.framework.R;
import cn.qqtheme.framework.util.CompatUtils;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.FileUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * The type File adapter.
 */
public class FileAdapter extends BaseAdapter {
    /**
     * The constant DIR_ROOT.
     */
    public static final String DIR_ROOT = "..";
    /**
     * The constant DIR_PARENT.
     */
    public static final String DIR_PARENT = ".";
    private Context context;
    private ArrayList<FileItem> data = new ArrayList<FileItem>();
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

    /**
     * Instantiates a new File adapter.
     *
     * @param context the context
     */
    public FileAdapter(Context context) {
        this.context = context;
    }

    /**
     * Gets current path.
     *
     * @return the current path
     */
    public String getCurrentPath() {
        return currentPath;
    }

    /**
     * Sets allow extensions.
     *
     * @param allowExtensions the allow extensions
     */
    public void setAllowExtensions(String[] allowExtensions) {
        this.allowExtensions = allowExtensions;
    }

    /**
     * Sets only list dir.
     *
     * @param onlyListDir the only list dir
     */
    public void setOnlyListDir(boolean onlyListDir) {
        this.onlyListDir = onlyListDir;
    }

    /**
     * Sets show home dir.
     *
     * @param showHomeDir the show home dir
     */
    public void setShowHomeDir(boolean showHomeDir) {
        this.showHomeDir = showHomeDir;
    }

    /**
     * Sets show up dir.
     *
     * @param showUpDir the show up dir
     */
    public void setShowUpDir(boolean showUpDir) {
        this.showUpDir = showUpDir;
    }

    /**
     * Sets show hide dir.
     *
     * @param showHideDir the show hide dir
     */
    public void setShowHideDir(boolean showHideDir) {
        this.showHideDir = showHideDir;
    }

    /**
     * Load data array list.
     *
     * @param path the path
     */
    public void loadData(String path) {
        if (path == null) {
            LogUtils.warn("current directory is null");
            return;
        }
        ArrayList<FileItem> datas = new ArrayList<FileItem>();
        if (rootPath == null) {
            rootPath = path;
        }
        LogUtils.debug("current directory path: " + path);
        currentPath = path;
        if (showHomeDir) {
            //添加“返回主目录”
            FileItem fileRoot = new FileItem();
            fileRoot.setDirectory(true);
            fileRoot.setIcon(homeIcon);
            fileRoot.setName(DIR_ROOT);
            fileRoot.setSize(0);
            fileRoot.setPath(rootPath);
            datas.add(fileRoot);
        }
        if (showUpDir && !path.equals("/")) {
            //添加“返回上一级目录”
            FileItem fileParent = new FileItem();
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
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FileItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, null);
            CompatUtils.setBackground(convertView, ConvertUtils.toStateListDrawable(Color.WHITE, Color.LTGRAY));
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(android.R.id.icon);
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileItem item = data.get(position);
        holder.imageView.setImageResource(item.getIcon());
        holder.textView.setText(item.getName());
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
