package cn.qqtheme.framework.wheelview.contract;

/**
 * 单项条目选择回调
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 20:00
 */
public interface OnSingleSelectedListener<T> {
    void onItemSelected(int position, T item);
}

