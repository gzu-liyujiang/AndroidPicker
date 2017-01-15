package cn.qqtheme.framework.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import cn.qqtheme.framework.util.LogUtils;

/**
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <p>
 * 集成第三方图片加载框架（如：Glide、Picasso、Universal-Image-Loader、Fresco），
 * 遵循“高内聚低耦合”原则，实现本类即可更换框架内核，降低各模块之间的依赖。
 * <p>
 * UIL：https://github.com/nostra13/Android-Universal-Image-Loader
 * Picasso：https://github.com/square/picasso
 * Glide：http://github.com/bumptech/glide
 * Fresco：https://github.com/facebook/fresco
 *
 * @author 李玉江[QQ:1032694760]
 * @see ImageLoadEngine
 * @since 2015/12/9
 */
public class ImageHelper implements ImageLoadEngine, AbsListView.OnScrollListener {
    public static final int PLACEHOLDER_LOADING = android.R.drawable.ic_menu_report_image;
    public static final int PLACEHOLDER_FAILURE = android.R.drawable.ic_menu_report_image;
    private static ImageHelper instance;
    private Context context;

    private ImageHelper() {
    }

    public static ImageHelper getInstance() {
        if (instance == null) {
            instance = new ImageHelper();
        }
        return instance;
    }

    @Override
    public void display(String urlOrPath, ImageView view) {
        display(urlOrPath, view, -1, -1);
    }

    @Override
    public void display(String urlOrPath, ImageView view, int width, int height) {
        LogUtils.verbose("Image>>>" + urlOrPath);
        if (null == context) {
            context = view.getContext();
        }
        RequestCreator creator = Picasso.with(context).load(urlOrPath);
        creator.placeholder(PLACEHOLDER_LOADING);
        creator.error(PLACEHOLDER_FAILURE);
        creator.config(urlOrPath.endsWith(".png") ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        if (width > 0 && height > 0) {
            creator.resize(width, height);
        }
        creator.into(view);
    }

    @Override
    public void onScrollFling(AbsListView view) {
        if (null == context) {
            context = view.getContext();
        }
        Picasso.with(context).pauseTag(view);
    }

    @Override
    public void onScrollFinish(AbsListView view) {
        if (null == context) {
            context = view.getContext();
        }
        Picasso.with(context).resumeTag(view);
    }

    @Override
    public final void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_FLING) {
            onScrollFling(view);
        } else {
            onScrollFinish(view);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItems, int totalItems) {

    }

}
