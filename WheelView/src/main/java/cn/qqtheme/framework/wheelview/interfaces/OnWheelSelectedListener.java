package cn.qqtheme.framework.wheelview.interfaces;

import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 滚轮条目选择监听
 *
 * @author liyujiang
 * @date 2019/5/14 20:04
 */
public interface OnWheelSelectedListener<Item> {
    /**
     * 条目已选择
     *
     * @param wheelView 滚轮视图
     * @param position  选中项的索引
     * @param item      选中项的值
     */
    void onItemSelected(WheelView wheelView, int position, Item item);

    /**
     * 当前条目滚动
     *
     * @param wheelView 滚轮视图
     * @param position  当前条目的索引
     */
    void onCurrentItemOfScroll(WheelView wheelView, int position);
}

