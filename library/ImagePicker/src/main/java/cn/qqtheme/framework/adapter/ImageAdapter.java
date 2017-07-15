package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

import cn.qqtheme.framework.http.ImageHelper;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 图片数据适配。强烈建议设置图片宽高，不然加载网络图片时会比较杂乱
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/22
 */
public final class ImageAdapter extends BaseAdapter {
    private static final int STRING = 0;
    private static final int INTEGER = 1;
    private static final int DRAWABLE = 2;
    private static final int BITMAP = 3;
    private static final int TAG_KEY_AVOID_CONFLICT_WITH_GLIDE = 0x20161023;
    private Context context;
    private int mode = STRING;
    private ArrayList<?> data = new ArrayList();
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
    private OnImageClickListener onImageClickListener;
    private int width = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int height = ViewGroup.LayoutParams.WRAP_CONTENT;

    private ImageAdapter(Context context) {
        this.context = context;
    }

    public static ImageAdapter with(Context context) {
        return new ImageAdapter(context);
    }

    public ImageAdapter resources(Integer[] data) {
        this.mode = INTEGER;
        this.data = new ArrayList<Integer>(Arrays.asList(data));
        return this;
    }

    public ImageAdapter resources(ArrayList<Integer> data) {
        this.mode = INTEGER;
        this.data = data;
        return this;
    }

    public ImageAdapter urls(String[] data) {
        this.mode = STRING;
        this.data = new ArrayList<String>(Arrays.asList(data));
        return this;
    }

    public ImageAdapter urls(ArrayList<String> data) {
        this.mode = STRING;
        this.data = data;
        return this;
    }

    public ImageAdapter bitmaps(ArrayList<Bitmap> data) {
        this.mode = BITMAP;
        this.data = data;
        return this;
    }

    public ImageAdapter drawables(ArrayList<Drawable> data) {
        this.mode = DRAWABLE;
        this.data = data;
        return this;
    }

    public ImageAdapter scaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    /**
     * 单位为dp
     */
    public ImageAdapter size(int width, int height) {
        this.width = ConvertUtils.toPx(context, width);
        this.height = ConvertUtils.toPx(context, height);
        return this;
    }

    public ImageAdapter itemClick(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
        return this;
    }

    public void replaceAll(ArrayList data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addAll(ArrayList data) {
        //noinspection unchecked
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = new ImageView(context);
            ViewGroup.LayoutParams params;
            if (parent instanceof AbsListView) {
                //java.lang.ClassCastException:
                // android.view.ViewGroup$LayoutParams cannot be cast to android.widget.AbsListView$LayoutParams
                params = new AbsListView.LayoutParams(width, height);
            } else {
                params = new ViewGroup.LayoutParams(width, height);
            }
            convertView.setLayoutParams(params);
            viewHolder.imageView = (ImageView) convertView;
            viewHolder.imageView.setScaleType(scaleType);
            //加上key，避免和Glide框架冲突: You must not call setTag() on a view Glide is targeting
            convertView.setTag(TAG_KEY_AVOID_CONFLICT_WITH_GLIDE, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(TAG_KEY_AVOID_CONFLICT_WITH_GLIDE);
        }
        final Object item = data.get(position);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(position, item);
                }
            }
        });
        switch (mode) {
            case INTEGER:
                viewHolder.imageView.setImageResource((Integer) item);
                break;
            case DRAWABLE:
                viewHolder.imageView.setImageDrawable((Drawable) item);
                break;
            case BITMAP:
                viewHolder.imageView.setImageBitmap((Bitmap) item);
                break;
            default:
                ImageHelper.getInstance().display(item.toString(), viewHolder.imageView, width, height);
                break;
        }
        return convertView;
    }

    public interface OnImageClickListener {

        void onImageClick(int position, Object item);

    }

    private class ViewHolder {
        ImageView imageView;
    }

}
