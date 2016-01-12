package cn.qqtheme.framework.bean;

/**
 * The type Icon text.
 *
 * @param <ICON> the type parameter
 */
public class IconText<ICON> extends JavaBean {
    /**
     * The constant NO_ICON.
     */
    public static final int NO_ICON = -1;
    private ICON icon;
    private CharSequence text;

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(ICON icon) {
        this.icon = icon;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public ICON getIcon() {
        return icon;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(CharSequence text) {
        this.text = text;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public CharSequence getText() {
        return text;
    }

}
