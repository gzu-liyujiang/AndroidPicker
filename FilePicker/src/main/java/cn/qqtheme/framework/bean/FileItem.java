package cn.qqtheme.framework.bean;

/**
 * 类描述
 *
 * @param <ICON> the type parameter
 * @author 李玉江[QQ :1032694760]
 * @version 2014-05-23 18:02
 */
public class FileItem<ICON> extends IconText<ICON> {
    private String path = "/";
    private long size = 0;
    private boolean isDirectory = false;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return getText().toString();
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        setText(name);
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
