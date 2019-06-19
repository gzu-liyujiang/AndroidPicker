package cn.qqtheme.framework.wheelview.contract;

import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 滚轮条目选择监听
 *
 * @author Florent Champigny
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 20:04
 */
public interface OnWheelSelectedListener<T> {
    /**
     * 条目已选择
     *
     * @param wheelView 滚轮视图
     * @param position  选中项的索引
     * @param item      选中项的值
     */
    void onItemSelected(WheelView wheelView, int position, T item);
}

