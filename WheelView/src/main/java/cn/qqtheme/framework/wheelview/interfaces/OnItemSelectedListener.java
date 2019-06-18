package cn.qqtheme.framework.wheelview.interfaces;

/**
 * 单项条目选择回调
 *
 * @author liyujiang
 * @date 2019/5/14 20:00
 */
public interface OnItemSelectedListener<T> {
    void onItemSelected(int position, T item);
}

