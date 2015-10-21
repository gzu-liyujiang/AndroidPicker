package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 万能数据适配器。
 *
 * @param <T> The type of the items in the list.
 * @link https://github.com/JoanZapata/base-adapter-helper
 * <p/>
 * Abstraction class of a BaseAdapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, AdapterHelper> {

    /**
     * Create a QuickAdapter.
     *
     * @param context   The context.
     * @param layoutRes The layout resource id of each item.
     */
    public QuickAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context   The context.
     * @param layoutRes The layout resource id of each item.
     * @param data      A new list is created out of this one to avoid mutable list
     */
    public QuickAdapter(Context context, int layoutRes, List<T> data) {
        super(context, layoutRes, data);
    }

    protected AdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
        return AdapterHelper.get(context, convertView, parent, layoutResId, position);
    }

}
