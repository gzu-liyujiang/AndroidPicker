package cn.qqtheme.framework.entity;

public class IconText<ICON> extends JavaBean {
    public static final int NO_ICON = -1;
    private ICON icon;
    private CharSequence text;

    public void setIcon(ICON icon) {
        this.icon = icon;
    }

    public ICON getIcon() {
        return icon;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }

}
