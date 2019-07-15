package cn.qqtheme.framework.wheelview.contract;

import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 滚轮滑动监听
 *
 * @author Florent Champigny
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 20:03
 */
public interface OnWheelChangedListener {
    /**
     * <p>
     * Invoke when scroll stopped
     * Will return a distance offset which between current scroll position and
     * initial position, this offset is a positive or a negative, positive means
     * scrolling from bottom to top, negative means scrolling from top to bottom
     *
     * @param wheelView wheel view
     * @param offset    Distance offset which between current scroll position and initial position
     */
    void onWheelScrolled(WheelView wheelView, int offset);

    /**
     * <p>
     * Invoke when scroll stopped
     * This method will be called when WheelPicker stop and return current selected item data's
     * position in list
     *
     * @param wheelView wheel view
     * @param position  Current selected item data's position in list
     */
    void onWheelSelected(WheelView wheelView, int position);

    /**
     * <p>
     * Invoke when scroll state changed
     * The state always between idle, dragging, and scrolling, this method will
     * be called when they switch
     *
     * @param wheelView wheel view
     * @param state     {@link WheelView#SCROLL_STATE_IDLE}
     *                  {@link WheelView#SCROLL_STATE_DRAGGING}
     *                  {@link WheelView#SCROLL_STATE_SCROLLING}
     *                  <p>
     *                  State only one of the following
     *                  {@link WheelView#SCROLL_STATE_IDLE}
     *                  Express WheelPicker in state of idle
     *                  {@link WheelView#SCROLL_STATE_DRAGGING}
     *                  Express WheelPicker in state of dragging
     *                  {@link WheelView#SCROLL_STATE_SCROLLING}
     *                  Express WheelPicker in state of scrolling
     */
    void onWheelScrollStateChanged(WheelView wheelView, int state);
}
