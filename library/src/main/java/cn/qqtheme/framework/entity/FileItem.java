package cn.qqtheme.framework.entity;

/**
 * 文件项信息
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2014 -05-23 18:02
 */
public class FileItem {
    private int icon;
    private String name;
    private String path = "/";
    private long size = 0;
    private boolean isDirectory = false;

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Is directory boolean.
     *
     * @return the boolean
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Sets directory.
     *
     * @param isDirectory the is directory
     */
    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

}
