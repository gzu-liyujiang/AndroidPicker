package cn.qqtheme.framework.entity;

/**
 * 文件项信息
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2014-05-23 18:02
 */
public class FileItem {
    private int icon;
    private String name;
    private String path = "/";
    private long size = 0;
    private boolean isDirectory = false;

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

}
