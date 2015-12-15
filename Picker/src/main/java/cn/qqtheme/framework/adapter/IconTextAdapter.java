package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.qqtheme.framework.entity.IconText;
import cn.qqtheme.framework.helper.Common;

public class IconTextAdapter<T extends IconText> extends BeanAdapter<T> {

    public IconTextAdapter(Context context) {
        super(context, android.R.layout.activity_list_item);
    }

    public IconTextAdapter(Context context, List<T> data) {
        super(context, android.R.layout.activity_list_item, data);
    }

    public IconTextAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public IconTextAdapter(Context context, int layoutRes, List<T> data) {
        super(context, layoutRes, data);
    }

    protected int getIconViewId() {
        return android.R.id.icon;
    }

    protected int getTextViewId() {
        return android.R.id.text1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Common.setBackground(view, Common.toStateListDrawable(Color.WHITE, Color.LTGRAY));
        return view;
    }

    @Override
    protected void convert(AdapterHelper helper, T item) {
        Object data = item.getIcon();
        if (data != null) {
            if (data instanceof Bitmap) {
                helper.setImageBitmap(getIconViewId(), (Bitmap) data);
            } else if (data instanceof Drawable) {
                helper.setImageDrawable(getIconViewId(), (Drawable) data);
            } else {
                Integer resId = (Integer) data;
                if (resId == IconText.NO_ICON) {
                    helper.setVisible(getIconViewId(), false);
                } else {
                    helper.setImageResource(getIconViewId(), resId);
                }
            }
        }
        helper.setText(getTextViewId(), item.getText());
    }

}
