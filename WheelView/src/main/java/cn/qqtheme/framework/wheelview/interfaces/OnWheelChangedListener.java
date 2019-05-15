package cn.qqtheme.framework.wheelview.interfaces;

import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 滚轮滑动监听
 *
 * @author liyujiang
 * @date 2019/5/14 20:03
 */
public interface OnWheelChangedListener {
    /**
     * <p>
     * Invoke when WheelPicker scroll stopped
     * WheelPicker will return a distance offset which between current scroll position and
     * initial position, this offset is a positive or a negative, positive means WheelPicker is
     * scrolling from bottom to top, negative means WheelPicker is scrolling from top to bottom
     *
     * @param offset <p>
     *               Distance offset which between current scroll position and initial position
     */
    void onWheelScrolled(int offset);

    /**
     * <p>
     * Invoke when WheelPicker scroll stopped
     * This method will be called when WheelPicker stop and return current selected item data's
     * position in list
     *
     * @param position <p>
     *                 Current selected item data's position in list
     */
    void onWheelSelected(int position);

    /**
     * <p>
     * Invoke when WheelPicker's scroll state changed
     * The state of WheelPicker always between idle, dragging, and scrolling, this method will
     * be called when they switch
     *
     * @param state {@link WheelView#SCROLL_STATE_IDLE}
     *              {@link WheelView#SCROLL_STATE_DRAGGING}
     *              {@link WheelView#SCROLL_STATE_SCROLLING}
     *              <p>
     *              State of WheelPicker, only one of the following
     *              {@link WheelView#SCROLL_STATE_IDLE}
     *              Express WheelPicker in state of idle
     *              {@link WheelView#SCROLL_STATE_DRAGGING}
     *              Express WheelPicker in state of dragging
     *              {@link WheelView#SCROLL_STATE_SCROLLING}
     *              Express WheelPicker in state of scrolling
     */
    void onWheelScrollStateChanged(int state);
}
