package cn.qqtheme.framework.adapter;

import android.content.Context;

import java.util.List;

import cn.qqtheme.framework.entity.JavaBean;

/**
 * 通用的JavaBean的适配器
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2014-1-19
 */
public abstract class BeanAdapter<T extends JavaBean> extends QuickAdapter<T> {

    public BeanAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public BeanAdapter(Context context, int layoutRes, List<T> data) {
        super(context, layoutRes, data);
    }

}
