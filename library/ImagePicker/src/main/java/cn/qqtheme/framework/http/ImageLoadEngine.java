package cn.qqtheme.framework.http;

import android.widget.AbsListView;
import android.widget.ImageView;

/**
 * 图片加载引擎接口
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/12/31 15:35
 * Builder:Android Studio
 *
 * @see ImageHelper
 */
public interface ImageLoadEngine {

    void display(String urlOrPath, ImageView view);

    void display(String urlOrPath, ImageView view, int width, int height);

    void onScrollFling(AbsListView view);

    void onScrollFinish(AbsListView view);

}
