package com.github.gzuliyujiang.calendarpicker.listener;

import androidx.annotation.Px;

public abstract class OnPageChangeCallback {
    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     *                 Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    public void onPageScrolled(int position, float positionOffset,
                               @Px int positionOffsetPixels) {
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    public void onPageSelected(int position) {
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user begins
     * dragging, when a fake drag is started, when the pager is automatically settling to the
     * current page, or when it is fully stopped/idle. {@code state} can be one of {@link
     */
    public void onPageScrollStateChanged(int state) {
    }
}
