/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.gzuliyujiang.calendarpicker.listener;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.util.Locale;

/**
 * Recy页面滑动的监控
 * <p>
 * 改自ViewPager2
 * </p>
 */
public final class ScrollEventAdapter extends RecyclerView.OnScrollListener {

    @Retention(SOURCE)
    @IntDef({STATE_IDLE, STATE_IN_PROGRESS_MANUAL_DRAG, STATE_IN_PROGRESS_SMOOTH_SCROLL,
            STATE_IN_PROGRESS_IMMEDIATE_SCROLL, STATE_IN_PROGRESS_FAKE_DRAG})
    private @interface AdapterState {
    }

    private static final int STATE_IDLE = 0;
    private static final int STATE_IN_PROGRESS_MANUAL_DRAG = 1;
    private static final int STATE_IN_PROGRESS_SMOOTH_SCROLL = 2;
    private static final int STATE_IN_PROGRESS_IMMEDIATE_SCROLL = 3;
    private static final int STATE_IN_PROGRESS_FAKE_DRAG = 4;

    private static final int NO_POSITION = -1;

    private OnPageChangeCallback mCallback;
    private final @NonNull RecyclerView mRecyclerView;
    private final @NonNull LinearLayoutManager mLayoutManager;

    // state related fields
    private @AdapterState int mAdapterState;
    private int mScrollState;
    private ScrollEventValues mScrollValues;
    private int mDragStartPosition;
    private int mTarget;
    private boolean mDispatchSelected;
    private boolean mScrollHappened;
    private boolean mDataSetChangeHappened;
    private boolean mFakeDragging;

    public ScrollEventAdapter(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        } else {
            throw new RuntimeException("need RecyclerView use LinearLayoutManager!!!!!!!!!!!!");
        }
        mScrollValues = new ScrollEventValues();
        resetState();
    }

    private void resetState() {
        mAdapterState = STATE_IDLE;
        mScrollState = SCROLL_STATE_IDLE;
        mScrollValues.reset();
        mDragStartPosition = NO_POSITION;
        mTarget = NO_POSITION;
        mDispatchSelected = false;
        mScrollHappened = false;
        mFakeDragging = false;
        mDataSetChangeHappened = false;
    }

    /**
     * This method only deals with some cases of {@link AdapterState} transitions. The rest of
     * the state transition implementation is in the {@link #onScrolled} method.
     */
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        // User started a drag (not dragging -> dragging)
        if ((mAdapterState != STATE_IN_PROGRESS_MANUAL_DRAG
                || mScrollState != SCROLL_STATE_DRAGGING)
                && newState == SCROLL_STATE_DRAGGING) {
            startDrag(false);
            return;
        }

        // Drag is released, RecyclerView is snapping to page (dragging -> settling)
        // Note that mAdapterState is not updated, to remember we were dragging when settling
        if (isInAnyDraggingState() && newState == SCROLL_STATE_SETTLING) {
            // Only go through the settling phase if the drag actually moved the page
            if (mScrollHappened) {
                dispatchStateChanged(SCROLL_STATE_SETTLING);
                // Determine target page and dispatch onPageSelected on next scroll event
                mDispatchSelected = true;
            }
            return;
        }

        // Drag is finished (dragging || settling -> idle)
        if (isInAnyDraggingState() && newState == SCROLL_STATE_IDLE) {
            boolean dispatchIdle = false;
            updateScrollEventValues();
            if (!mScrollHappened) {
                // Pages didn't move during drag, so either we're at the start or end of the list,
                // or there are no pages at all.
                // In the first case, ViewPager's contract requires at least one scroll event.
                // In the second case, don't send that scroll event
                if (mScrollValues.mPosition != RecyclerView.NO_POSITION) {
                    dispatchScrolled(mScrollValues.mPosition, 0f, 0);
                }
                dispatchIdle = true;
            } else if (mScrollValues.mOffsetPx == 0) {
                // Normally we dispatch the selected page and go to idle in onScrolled when
                // mOffsetPx == 0, but in this case the drag was still ongoing when onScrolled was
                // called, so that didn't happen. And since mOffsetPx == 0, there will be no further
                // scroll events, so fire the onPageSelected event and go to idle now.
                // Note that if we _did_ go to idle in that last onScrolled event, this code will
                // not be executed because mAdapterState has been reset to STATE_IDLE.
                dispatchIdle = true;
                if (mDragStartPosition != mScrollValues.mPosition) {
                    dispatchSelected(mScrollValues.mPosition);
                }
            }
            if (dispatchIdle) {
                // Normally idle is fired in last onScrolled call, but either onScrolled was never
                // called, or we were still dragging when the last onScrolled was called
                dispatchStateChanged(SCROLL_STATE_IDLE);
                resetState();
            }
        }

        if (mAdapterState == STATE_IN_PROGRESS_SMOOTH_SCROLL
                && newState == SCROLL_STATE_IDLE && mDataSetChangeHappened) {
            updateScrollEventValues();
            if (mScrollValues.mOffsetPx == 0) {
                if (mTarget != mScrollValues.mPosition) {
                    dispatchSelected(
                            mScrollValues.mPosition == NO_POSITION ? 0 : mScrollValues.mPosition);
                }
                dispatchStateChanged(SCROLL_STATE_IDLE);
                resetState();
            }
        }
    }

    /**
     * This method only deals with some cases of {@link AdapterState} transitions. The rest of
     * the state transition implementation is in the {@link #onScrollStateChanged} method.
     */
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        mScrollHappened = true;
        updateScrollEventValues();

        if (mDispatchSelected) {
            // Drag started settling, need to calculate target page and dispatch onPageSelected now
            mDispatchSelected = false;
            boolean scrollingForward = dy > 0 || (dy == 0 && dx < 0 == (mLayoutManager.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL));

            // "&& values.mOffsetPx != 0": filters special case where we're scrolling forward and
            // the first scroll event after settling already got us at the target
            mTarget = scrollingForward && mScrollValues.mOffsetPx != 0
                    ? mScrollValues.mPosition + 1 : mScrollValues.mPosition;
            if (mDragStartPosition != mTarget) {
                dispatchSelected(mTarget);
            }
        } else if (mAdapterState == STATE_IDLE) {
            // onScrolled while IDLE means RV has just been populated after an adapter has been set.
            // Contract requires us to fire onPageSelected as well.
            int position = mScrollValues.mPosition;
            // Contract forbids us to send position = -1 though
            dispatchSelected(position == NO_POSITION ? 0 : position);
        }

        // If position = -1, there are no items. Contract says to send position = 0 instead.
        dispatchScrolled(mScrollValues.mPosition == NO_POSITION ? 0 : mScrollValues.mPosition,
                mScrollValues.mOffset, mScrollValues.mOffsetPx);

        // Dispatch idle in onScrolled instead of in onScrollStateChanged because RecyclerView
        // doesn't send IDLE event when using setCurrentItem(x, false)
        if ((mScrollValues.mPosition == mTarget || mTarget == NO_POSITION)
                && mScrollValues.mOffsetPx == 0 && !(mScrollState == SCROLL_STATE_DRAGGING)) {
            // When the target page is reached and the user is not dragging anymore, we're settled,
            // so go to idle.
            // Special case and a bit of a hack when mTarget == NO_POSITION: RecyclerView is being
            // initialized and fires a single scroll event. This flags mScrollHappened, so we need
            // to reset our state. However, we don't want to dispatch idle. But that won't happen;
            // because we were already idle.
            dispatchStateChanged(SCROLL_STATE_IDLE);
            resetState();
        }
    }

    /**
     * Calculates the current position and the offset (as a percentage and in pixels) of that
     * position from the center.
     */
    private void updateScrollEventValues() {
        ScrollEventValues values = mScrollValues;

        values.mPosition = mLayoutManager.findFirstVisibleItemPosition();
        if (values.mPosition == RecyclerView.NO_POSITION) {
            values.reset();
            return;
        }
        View firstVisibleView = mLayoutManager.findViewByPosition(values.mPosition);
        if (firstVisibleView == null) {
            values.reset();
            return;
        }

        int leftDecorations = mLayoutManager.getLeftDecorationWidth(firstVisibleView);
        int rightDecorations = mLayoutManager.getRightDecorationWidth(firstVisibleView);
        int topDecorations = mLayoutManager.getTopDecorationHeight(firstVisibleView);
        int bottomDecorations = mLayoutManager.getBottomDecorationHeight(firstVisibleView);

        LayoutParams params = firstVisibleView.getLayoutParams();
        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams margin = (MarginLayoutParams) params;
            leftDecorations += margin.leftMargin;
            rightDecorations += margin.rightMargin;
            topDecorations += margin.topMargin;
            bottomDecorations += margin.bottomMargin;
        }

        int decoratedHeight = firstVisibleView.getHeight() + topDecorations + bottomDecorations;
        int decoratedWidth = firstVisibleView.getWidth() + leftDecorations + rightDecorations;


        boolean isHorizontal = mLayoutManager.getOrientation() == RecyclerView.HORIZONTAL;
        int start, sizePx;
        if (isHorizontal) {
            sizePx = decoratedWidth;
            start = firstVisibleView.getLeft() - leftDecorations - mRecyclerView.getPaddingLeft();
            if (mLayoutManager.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
                start = -start;
            }
        } else {
            sizePx = decoratedHeight;
            start = firstVisibleView.getTop() - topDecorations - mRecyclerView.getPaddingTop();
        }

        values.mOffsetPx = -start;
        if (values.mOffsetPx < 0) {
/*            // We're in an error state. Figure out if this might have been caused
            // by animateLayoutChanges and throw a descriptive exception if so
            if (new AnimateLayoutChangeDetector(mLayoutManager).mayHaveInterferingAnimations()) {
                throw new IllegalStateException("Page(s) contain a ViewGroup with a "
                        + "LayoutTransition (or animateLayoutChanges=\"true\"), which interferes "
                        + "with the scrolling animation. Make sure to call getLayoutTransition()"
                        + ".setAnimateParentHierarchy(false) on all ViewGroups with a "
                        + "LayoutTransition before an animation is started.");
            }*/

            // Throw a generic exception otherwise
            throw new IllegalStateException(String.format(Locale.US, "Page can only be offset by a "
                    + "positive amount, not by %d", values.mOffsetPx));
        }
        values.mOffset = sizePx == 0 ? 0 : (float) values.mOffsetPx / sizePx;
    }

    private void startDrag(boolean isFakeDrag) {
        mFakeDragging = isFakeDrag;
        mAdapterState = isFakeDrag ? STATE_IN_PROGRESS_FAKE_DRAG : STATE_IN_PROGRESS_MANUAL_DRAG;
        if (mTarget != NO_POSITION) {
            // Target was set means we were settling to that target
            // Update "drag start page" to reflect the page that ViewPager2 thinks it is at
            mDragStartPosition = mTarget;
            // Reset target because drags have no target until released
            mTarget = NO_POSITION;
        } else if (mDragStartPosition == NO_POSITION) {
            // ViewPager2 was at rest, set "drag start page" to current page
            mDragStartPosition = getPosition();
        }
        dispatchStateChanged(SCROLL_STATE_DRAGGING);
    }

    void notifyDataSetChangeHappened() {
        mDataSetChangeHappened = true;
    }

    /**
     * Let the adapter know a programmatic scroll was initiated.
     */
    void notifyProgrammaticScroll(int target, boolean smooth) {
        mAdapterState = smooth
                ? STATE_IN_PROGRESS_SMOOTH_SCROLL
                : STATE_IN_PROGRESS_IMMEDIATE_SCROLL;
        // mFakeDragging is true when a fake drag is interrupted by an a11y command
        // set it to false so endFakeDrag won't fling the RecyclerView
        mFakeDragging = false;
        boolean hasNewTarget = mTarget != target;
        mTarget = target;
        dispatchStateChanged(SCROLL_STATE_SETTLING);
        if (hasNewTarget) {
            dispatchSelected(target);
        }
    }

    /**
     * Let the adapter know that a fake drag has started.
     */
    void notifyBeginFakeDrag() {
        mAdapterState = STATE_IN_PROGRESS_FAKE_DRAG;
        startDrag(true);
    }

    /**
     * Let the adapter know that a fake drag has ended.
     */
    void notifyEndFakeDrag() {
        if (isDragging() && !mFakeDragging) {
            // Real drag has already taken over, no need to post process the fake drag
            return;
        }
        mFakeDragging = false;
        updateScrollEventValues();
        if (mScrollValues.mOffsetPx == 0) {
            // We're snapped, so dispatch an IDLE event
            if (mScrollValues.mPosition != mDragStartPosition) {
                dispatchSelected(mScrollValues.mPosition);
            }
            dispatchStateChanged(SCROLL_STATE_IDLE);
            resetState();
        } else {
            // We're not snapped, so dispatch a SETTLING event
            dispatchStateChanged(SCROLL_STATE_SETTLING);
        }
    }

    public void setOnPageChangeCallback(OnPageChangeCallback callback) {
        mCallback = callback;
    }

    int getScrollState() {
        return mScrollState;
    }

    /**
     * @return {@code true} if there is no known scroll in progress
     */
    boolean isIdle() {
        return mScrollState == SCROLL_STATE_IDLE;
    }

    /**
     * @return {@code true} if the ViewPager2 is being dragged. Returns {@code false} from the
     * moment the ViewPager2 starts settling or goes idle.
     */
    boolean isDragging() {
        return mScrollState == SCROLL_STATE_DRAGGING;
    }

    boolean isFakeDragging() {
        return mFakeDragging;
    }

    /**
     * Checks if the adapter state (not the scroll state) is in the manual or fake dragging state.
     *
     * @return {@code true} if {@link #mAdapterState} is either {@link
     * #STATE_IN_PROGRESS_MANUAL_DRAG} or {@link #STATE_IN_PROGRESS_FAKE_DRAG}
     */
    private boolean isInAnyDraggingState() {
        return mAdapterState == STATE_IN_PROGRESS_MANUAL_DRAG
                || mAdapterState == STATE_IN_PROGRESS_FAKE_DRAG;
    }

    /**
     * Calculates the scroll position of the currently visible item of the ViewPager relative to its
     * width. Calculated by adding the fraction by which the first visible item is off screen to its
     * adapter position. E.g., if the ViewPager is currently scrolling from the second to the third
     * page, the returned value will be between 1 and 2. Thus, non-integral values mean that the
     * the ViewPager is settling towards its {ViewPager2#getCurrentItem() current item}, or
     * the user may be dragging it.
     *
     * @return The current scroll position of the ViewPager, relative to its width
     */
    double getRelativeScrollPosition() {
        updateScrollEventValues();
        return mScrollValues.mPosition + (double) mScrollValues.mOffset;
    }

    private void dispatchStateChanged(int state) {
        // Callback contract for immediate-scroll requires not having state change notifications,
        // but only when there was no smooth scroll in progress.
        // By putting a suppress statement in here (rather than next to dispatch calls) we are
        // simplifying the code of the class and enforcing the contract in one place.
        if (mAdapterState == STATE_IN_PROGRESS_IMMEDIATE_SCROLL
                && mScrollState == SCROLL_STATE_IDLE) {
            return;
        }
        if (mScrollState == state) {
            return;
        }

        mScrollState = state;
        if (mCallback != null) {
            mCallback.onPageScrollStateChanged(state);
        }
    }

    private void dispatchSelected(int target) {
        if (mCallback != null) {
            mCallback.onPageSelected(target);
        }
    }

    private void dispatchScrolled(int position, float offset, int offsetPx) {
        if (mCallback != null) {
            mCallback.onPageScrolled(position, offset, offsetPx);
        }
    }

    private int getPosition() {
        return mLayoutManager.findFirstVisibleItemPosition();
    }

    private static final class ScrollEventValues {
        int mPosition;
        float mOffset;
        int mOffsetPx;

        // to avoid a synthetic accessor
        ScrollEventValues() {
        }

        void reset() {
            mPosition = RecyclerView.NO_POSITION;
            mOffset = 0f;
            mOffsetPx = 0;
        }
    }
}
