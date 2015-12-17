package cn.qqtheme.framework.bean;

/**
 * 类描述
 *
 * @author 李玉江[QQ:1032694760]
 * @version 2014-05-23 18:02
 * @builder Created By IntelliJ IDEA
 */
public class FileItem<ICON> extends IconText<ICON> {
    private String path = "/";
    private long size = 0;
    private boolean isDirectory = false;

    public String getName() {
        return getText().toString();
    }

    public void setName(String name) {
        setText(name);
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
