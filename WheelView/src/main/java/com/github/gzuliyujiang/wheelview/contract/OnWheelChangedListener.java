/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.wheelview.contract;

import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

/**
 * 滚轮滑动接口
 *
 * @author Florent Champigny
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 20:03
 */
public interface OnWheelChangedListener {

    /**
     * Invoke when scroll stopped
     * Will return a distance offset which between current scroll position and
     * initial position, this offset is a positive or a negative, positive means
     * scrolling from bottom to top, negative means scrolling from top to bottom
     *
     * @param view   wheel view
     * @param offset Distance offset which between current scroll position and initial position
     */
    void onWheelScrolled(WheelView view, int offset);

    /**
     * Invoke when scroll stopped
     * This method will be called when wheel stop and return current selected item data's
     * position in list
     *
     * @param view     wheel view
     * @param position Current selected item data's position in list
     */
    void onWheelSelected(WheelView view, int position);

    /**
     * Invoke when scroll state changed
     * The state always between idle, dragging, and scrolling, this method will
     * be called when they switch
     *
     * @param view  wheel view
     * @param state {@link ScrollState#IDLE}
     *              {@link ScrollState#DRAGGING}
     *              {@link ScrollState#SCROLLING}
     *              <p>
     *              State only one of the following
     *              {@link ScrollState#IDLE}
     *              Express WheelPicker in state of idle
     *              {@link ScrollState#DRAGGING}
     *              Express WheelPicker in state of dragging
     *              {@link ScrollState#SCROLLING}
     *              Express WheelPicker in state of scrolling
     */
    void onWheelScrollStateChanged(WheelView view, @ScrollState int state);

    /**
     * Invoke when loop finished
     *
     * @param view wheel view
     */
    void onWheelLoopFinished(WheelView view);

}
