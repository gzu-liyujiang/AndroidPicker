package cn.qqtheme.framework.adapter;

import android.content.Context;

import cn.qqtheme.framework.bean.JavaBean;

import java.util.List;

/**
 * JavaBean数据适配
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
