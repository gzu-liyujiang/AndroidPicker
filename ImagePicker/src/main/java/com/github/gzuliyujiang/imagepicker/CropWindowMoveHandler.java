// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.github.gzuliyujiang.imagepicker;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Handler to update crop window edges by the move type - Horizontal, Vertical, Corner or Center.<br/>
 */
final class CropWindowMoveHandler {

    //region: Fields and Consts

    /**
     * Minimum width in pixels that the crop window can get.
     */
    private final float mMinCropWidth;

    /**
     * Minimum width in pixels that the crop window can get.
     */
    private final float mMinCropHeight;

    /**
     * Maximum height in pixels that the crop window can get.
     */
    private final float mMaxCropWidth;

    /**
     * Maximum height in pixels that the crop window can get.
     */
    private final float mMaxCropHeight;

    /**
     * The type of crop window move that is handled.
     */
    private final Type mType;

    /**
     * Holds the x and y offset between the exact touch location and the exact handle location that is activated.
     * There may be an offset because we allow for some leeway (specified by mHandleRadius) in activating a handle.
     * However, we want to maintain these offset values while the handle is being dragged so that the handle
     * doesn't jump.
     */
    private final PointF mTouchOffset = new PointF();
    //endregion

    /**
     * @param type the type of move this handler is executing
     * @param cropWindowHandler main crop window handle to get and update the crop window edges
     * @param touchX the location of the initial toch possition to measure move distance
     * @param touchY the location of the initial toch possition to measure move distance
     */
    public CropWindowMoveHandler(Type type, CropWindowHandler cropWindowHandler, float touchX, float touchY) {
        mType = type;
        mMinCropWidth = cropWindowHandler.getMinCropWidth();
        mMinCropHeight = cropWindowHandler.getMinCropHeight();
        mMaxCropWidth = cropWindowHandler.getMaxCropWidth();
        mMaxCropHeight = cropWindowHandler.getMaxCropHeight();
        calculateTouchOffset(cropWindowHandler.getRect(), touchX, touchY);
    }

    /**
     * Updates the crop window by change in the toch location.<br>
     * Move type handled by this instance, as initialized in creation, affects how the change in toch location
     * changes the crop window position and size.<br>
     * After the crop window position/size is changed by toch move it may result in values that vialate contraints:
     * outside the bounds of the shown bitmap, smaller/larger than min/max size or missmatch in aspect ratio.
     * So a series of fixes is executed on "secondary" edges to adjust it by the "primary" edge movement.<br>
     * Primary is the edge directly affected by move type, secondary is the other edge.<br>
     * The crop window is changed by directly setting the Edge coordinates.
     *
     * @param x the new x-coordinate of this handle
     * @param y the new y-coordinate of this handle
     * @param bounds the bounding rectangle of the image
     * @param viewWidth The bounding image view width used to know the crop overlay is at view edges.
     * @param viewHeight The bounding image view height used to know the crop overlay is at view edges.
     * @param snapMargin the maximum distance (in pixels) at which the crop window should snap to the image
     * @param fixedAspectRatio is the aspect ration fixed and 'targetAspectRatio' should be used
     * @param aspectRatio the aspect ratio to maintain
     */
    public void move(RectF rect, float x, float y, RectF bounds, int viewWidth, int viewHeight, float snapMargin, boolean fixedAspectRatio, float aspectRatio) {

        // Adjust the coordinates for the finger position's offset (i.e. the
        // distance from the initial touch to the precise handle location).
        // We want to maintain the initial touch's distance to the pressed
        // handle so that the crop window size does not "jump".
        float adjX = x + mTouchOffset.x;
        float adjY = y + mTouchOffset.y;

        if (mType == Type.CENTER) {
            moveCenter(rect, adjX, adjY, bounds, viewWidth, viewHeight, snapMargin);
        } else {
            if (fixedAspectRatio) {
                moveSizeWithFixedAspectRatio(rect, adjX, adjY, bounds, viewWidth, viewHeight, snapMargin, aspectRatio);
            } else {
                moveSizeWithFreeAspectRatio(rect, adjX, adjY, bounds, viewWidth, viewHeight, snapMargin);
            }
        }
    }

    //region: Private methods

    /**
     * Calculates the offset of the touch point from the precise location of the specified handle.<br>
     * Save these values in a member variable since we want to maintain this offset as we drag the handle.
     */
    private void calculateTouchOffset(RectF rect, float touchX, float touchY) {

        float touchOffsetX = 0;
        float touchOffsetY = 0;

        // Calculate the offset from the appropriate handle.
        switch (mType) {
            case TOP_LEFT:
                touchOffsetX = rect.left - touchX;
                touchOffsetY = rect.top - touchY;
                break;
            case TOP_RIGHT:
                touchOffsetX = rect.right - touchX;
                touchOffsetY = rect.top - touchY;
                break;
            case BOTTOM_LEFT:
                touchOffsetX = rect.left - touchX;
                touchOffsetY = rect.bottom - touchY;
                break;
            case BOTTOM_RIGHT:
                touchOffsetX = rect.right - touchX;
                touchOffsetY = rect.bottom - touchY;
                break;
            case LEFT:
                touchOffsetX = rect.left - touchX;
                touchOffsetY = 0;
                break;
            case TOP:
                touchOffsetX = 0;
                touchOffsetY = rect.top - touchY;
                break;
            case RIGHT:
                touchOffsetX = rect.right - touchX;
                touchOffsetY = 0;
                break;
            case BOTTOM:
                touchOffsetX = 0;
                touchOffsetY = rect.bottom - touchY;
                break;
            case CENTER:
                touchOffsetX = rect.centerX() - touchX;
                touchOffsetY = rect.centerY() - touchY;
                break;
            default:
                break;
        }

        mTouchOffset.x = touchOffsetX;
        mTouchOffset.y = touchOffsetY;
    }

    /**
     * Center move only changes the position of the crop window without changing the size.
     */
    private void moveCenter(RectF rect, float x, float y, RectF bounds, int viewWidth, int viewHeight, float snapRadius) {
        float dx = x - rect.centerX();
        float dy = y - rect.centerY();
        if (rect.left + dx < 0 || rect.right + dx > viewWidth || rect.left + dx < bounds.left || rect.right + dx > bounds.right) {
            dx /= 1.05f;
            mTouchOffset.x -= dx / 2;
        }
        if (rect.top + dy < 0 || rect.bottom + dy > viewHeight || rect.top + dy < bounds.top || rect.bottom + dy > bounds.bottom) {
            dy /= 1.05f;
            mTouchOffset.y -= dy / 2;
        }
        rect.offset(dx, dy);
        snapEdgesToBounds(rect, bounds, snapRadius);
    }

    /**
     * Change the size of the crop window on the required edge (or edges for corner size move) without
     * affecting "secondary" edges.<br>
     * Only the primary edge(s) are fixed to stay within limits.
     */
    private void moveSizeWithFreeAspectRatio(RectF rect, float x, float y, RectF bounds, int viewWidth, int viewHeight, float snapMargin) {
        switch (mType) {
            case TOP_LEFT:
                adjustTop(rect, y, bounds, snapMargin, 0, false, false);
                adjustLeft(rect, x, bounds, snapMargin, 0, false, false);
                break;
            case TOP_RIGHT:
                adjustTop(rect, y, bounds, snapMargin, 0, false, false);
                adjustRight(rect, x, bounds, viewWidth, snapMargin, 0, false, false);
                break;
            case BOTTOM_LEFT:
                adjustBottom(rect, y, bounds, viewHeight, snapMargin, 0, false, false);
                adjustLeft(rect, x, bounds, snapMargin, 0, false, false);
                break;
            case BOTTOM_RIGHT:
                adjustBottom(rect, y, bounds, viewHeight, snapMargin, 0, false, false);
                adjustRight(rect, x, bounds, viewWidth, snapMargin, 0, false, false);
                break;
            case LEFT:
                adjustLeft(rect, x, bounds, snapMargin, 0, false, false);
                break;
            case TOP:
                adjustTop(rect, y, bounds, snapMargin, 0, false, false);
                break;
            case RIGHT:
                adjustRight(rect, x, bounds, viewWidth, snapMargin, 0, false, false);
                break;
            case BOTTOM:
                adjustBottom(rect, y, bounds, viewHeight, snapMargin, 0, false, false);
                break;
            default:
                break;
        }
    }

    /**
     * Change the size of the crop window on the required "primary" edge WITH affect to relevant "secondary"
     * edge via aspect ratio.<br>
     * Example: change in the left edge (primary) will affect top and bottom edges (secondary) to preserve the
     * given aspect ratio.
     */
    private void moveSizeWithFixedAspectRatio(RectF rect, float x, float y, RectF bounds, int viewWidth, int viewHeight, float snapMargin, float aspectRatio) {
        switch (mType) {
            case TOP_LEFT:
                if (calculateAspectRatio(x, y, rect.right, rect.bottom) < aspectRatio) {
                    adjustTop(rect, y, bounds, snapMargin, aspectRatio, true, false);
                    adjustLeftByAspectRatio(rect, aspectRatio);
                } else {
                    adjustLeft(rect, x, bounds, snapMargin, aspectRatio, true, false);
                    adjustTopByAspectRatio(rect, aspectRatio);
                }
                break;
            case TOP_RIGHT:
                if (calculateAspectRatio(rect.left, y, x, rect.bottom) < aspectRatio) {
                    adjustTop(rect, y, bounds, snapMargin, aspectRatio, false, true);
                    adjustRightByAspectRatio(rect, aspectRatio);
                } else {
                    adjustRight(rect, x, bounds, viewWidth, snapMargin, aspectRatio, true, false);
                    adjustTopByAspectRatio(rect, aspectRatio);
                }
                break;
            case BOTTOM_LEFT:
                if (calculateAspectRatio(x, rect.top, rect.right, y) < aspectRatio) {
                    adjustBottom(rect, y, bounds, viewHeight, snapMargin, aspectRatio, true, false);
                    adjustLeftByAspectRatio(rect, aspectRatio);
                } else {
                    adjustLeft(rect, x, bounds, snapMargin, aspectRatio, false, true);
                    adjustBottomByAspectRatio(rect, aspectRatio);
                }
                break;
            case BOTTOM_RIGHT:
                if (calculateAspectRatio(rect.left, rect.top, x, y) < aspectRatio) {
                    adjustBottom(rect, y, bounds, viewHeight, snapMargin, aspectRatio, false, true);
                    adjustRightByAspectRatio(rect, aspectRatio);
                } else {
                    adjustRight(rect, x, bounds, viewWidth, snapMargin, aspectRatio, false, true);
                    adjustBottomByAspectRatio(rect, aspectRatio);
                }
                break;
            case LEFT:
                adjustLeft(rect, x, bounds, snapMargin, aspectRatio, true, true);
                adjustTopBottomByAspectRatio(rect, bounds, aspectRatio);
                break;
            case TOP:
                adjustTop(rect, y, bounds, snapMargin, aspectRatio, true, true);
                adjustLeftRightByAspectRatio(rect, bounds, aspectRatio);
                break;
            case RIGHT:
                adjustRight(rect, x, bounds, viewWidth, snapMargin, aspectRatio, true, true);
                adjustTopBottomByAspectRatio(rect, bounds, aspectRatio);
                break;
            case BOTTOM:
                adjustBottom(rect, y, bounds, viewHeight, snapMargin, aspectRatio, true, true);
                adjustLeftRightByAspectRatio(rect, bounds, aspectRatio);
                break;
            default:
                break;
        }
    }

    /**
     * Check if edges have gone out of bounds (including snap margin), and fix if needed.
     */
    private void snapEdgesToBounds(RectF edges, RectF bounds, float margin) {
        if (edges.left < bounds.left + margin) {
            edges.offset(bounds.left - edges.left, 0);
        }
        if (edges.top < bounds.top + margin) {
            edges.offset(0, bounds.top - edges.top);
        }
        if (edges.right > bounds.right - margin) {
            edges.offset(bounds.right - edges.right, 0);
        }
        if (edges.bottom > bounds.bottom - margin) {
            edges.offset(0, bounds.bottom - edges.bottom);
        }
    }

    /**
     * Get the resulting x-position of the left edge of the crop window given
     * the handle's position and the image's bounding box and snap radius.
     *
     * @param left the position that the left edge is dragged to
     * @param bounds the bounding box of the image that is being cropped
     * @param snapMargin the snap distance to the image edge (in pixels)
     */
    private void adjustLeft(RectF rect, float left, RectF bounds, float snapMargin, float aspectRatio, boolean topMoves, boolean bottomMoves) {

        float newLeft = left;

        if (newLeft < 0) {
            newLeft /= 1.05f;
            mTouchOffset.x -= newLeft / 1.1f;
        }

        if (newLeft < bounds.left) {
            mTouchOffset.x -= (newLeft - bounds.left) / 2f;
        }

        if (newLeft - bounds.left < snapMargin) {
            newLeft = bounds.left;
        }

        // Checks if the window is too small horizontally
        if (rect.right - newLeft < mMinCropWidth) {
            newLeft = rect.right - mMinCropWidth;
        }

        // Checks if the window is too large horizontally
        if (rect.right - newLeft > mMaxCropWidth) {
            newLeft = rect.right - mMaxCropWidth;
        }

        if (newLeft - bounds.left < snapMargin) {
            newLeft = bounds.left;
        }

        // check vertical bounds if aspect ratio is in play
        if (aspectRatio > 0) {
            float newHeight = (rect.right - newLeft) / aspectRatio;

            // Checks if the window is too small vertically
            if (newHeight < mMinCropHeight) {
                newLeft = Math.max(bounds.left, rect.right - mMinCropHeight * aspectRatio);
                newHeight = (rect.right - newLeft) / aspectRatio;
            }

            // Checks if the window is too large vertically
            if (newHeight > mMaxCropHeight) {
                newLeft = Math.max(bounds.left, rect.right - mMaxCropHeight * aspectRatio);
                newHeight = (rect.right - newLeft) / aspectRatio;
            }

            // if top AND bottom edge moves by aspect ratio check that it is within full height bounds
            if (topMoves && bottomMoves) {
                newLeft = Math.max(newLeft, Math.max(bounds.left, rect.right - bounds.height() * aspectRatio));
            } else {
                // if top edge moves by aspect ratio check that it is within bounds
                if (topMoves && rect.bottom - newHeight < bounds.top) {
                    newLeft = Math.max(bounds.left, rect.right - (rect.bottom - bounds.top) * aspectRatio);
                    newHeight = (rect.right - newLeft) / aspectRatio;
                }

                // if bottom edge moves by aspect ratio check that it is within bounds
                if (bottomMoves && rect.top + newHeight > bounds.bottom) {
                    newLeft = Math.max(newLeft, Math.max(bounds.left, rect.right - (bounds.bottom - rect.top) * aspectRatio));
                }
            }
        }

        rect.left = newLeft;
    }

    /**
     * Get the resulting x-position of the right edge of the crop window given
     * the handle's position and the image's bounding box and snap radius.
     *
     * @param right the position that the right edge is dragged to
     * @param bounds the bounding box of the image that is being cropped
     * @param viewWidth view width
     * @param snapMargin the snap distance to the image edge (in pixels)
     */
    private void adjustRight(RectF rect, float right, RectF bounds, int viewWidth, float snapMargin, float aspectRatio, boolean topMoves, boolean bottomMoves) {

        float newRight = right;

        if (newRight > viewWidth) {
            newRight = viewWidth + (newRight - viewWidth) / 1.05f;
            mTouchOffset.x -= (newRight - viewWidth) / 1.1f;
        }

        if (newRight > bounds.right) {
            mTouchOffset.x -= (newRight - bounds.right) / 2f;
        }

        // If close to the edge
        if (bounds.right - newRight < snapMargin) {
            newRight = bounds.right;
        }

        // Checks if the window is too small horizontally
        if (newRight - rect.left < mMinCropWidth) {
            newRight = rect.left + mMinCropWidth;
        }

        // Checks if the window is too large horizontally
        if (newRight - rect.left > mMaxCropWidth) {
            newRight = rect.left + mMaxCropWidth;
        }

        // If close to the edge
        if (bounds.right - newRight < snapMargin) {
            newRight = bounds.right;
        }

        // check vertical bounds if aspect ratio is in play
        if (aspectRatio > 0) {
            float newHeight = (newRight - rect.left) / aspectRatio;

            // Checks if the window is too small vertically
            if (newHeight < mMinCropHeight) {
                newRight = Math.min(bounds.right, rect.left + mMinCropHeight * aspectRatio);
                newHeight = (newRight - rect.left) / aspectRatio;
            }

            // Checks if the window is too large vertically
            if (newHeight > mMaxCropHeight) {
                newRight = Math.min(bounds.right, rect.left + mMaxCropHeight * aspectRatio);
                newHeight = (newRight - rect.left) / aspectRatio;
            }

            // if top AND bottom edge moves by aspect ratio check that it is within full height bounds
            if (topMoves && bottomMoves) {
                newRight = Math.min(newRight, Math.min(bounds.right, rect.left + bounds.height() * aspectRatio));
            } else {
                // if top edge moves by aspect ratio check that it is within bounds
                if (topMoves && rect.bottom - newHeight < bounds.top) {
                    newRight = Math.min(bounds.right, rect.left + (rect.bottom - bounds.top) * aspectRatio);
                    newHeight = (newRight - rect.left) / aspectRatio;
                }

                // if bottom edge moves by aspect ratio check that it is within bounds
                if (bottomMoves && rect.top + newHeight > bounds.bottom) {
                    newRight = Math.min(newRight, Math.min(bounds.right, rect.left + (bounds.bottom - rect.top) * aspectRatio));
                }
            }
        }

        rect.right = newRight;
    }

    /**
     * Get the resulting y-position of the top edge of the crop window given the
     * handle's position and the image's bounding box and snap radius.
     *
     * @param top the x-position that the top edge is dragged to
     * @param bounds the bounding box of the image that is being cropped
     * @param snapMargin the snap distance to the image edge (in pixels)
     */
    private void adjustTop(RectF rect, float top, RectF bounds, float snapMargin, float aspectRatio, boolean leftMoves, boolean rightMoves) {

        float newTop = top;

        if (newTop < 0) {
            newTop /= 1.05f;
            mTouchOffset.y -= newTop / 1.1f;
        }

        if (newTop < bounds.top) {
            mTouchOffset.y -= (newTop - bounds.top) / 2f;
        }

        if (newTop - bounds.top < snapMargin) {
            newTop = bounds.top;
        }

        // Checks if the window is too small vertically
        if (rect.bottom - newTop < mMinCropHeight) {
            newTop = rect.bottom - mMinCropHeight;
        }

        // Checks if the window is too large vertically
        if (rect.bottom - newTop > mMaxCropHeight) {
            newTop = rect.bottom - mMaxCropHeight;
        }

        if (newTop - bounds.top < snapMargin) {
            newTop = bounds.top;
        }

        // check horizontal bounds if aspect ratio is in play
        if (aspectRatio > 0) {
            float newWidth = (rect.bottom - newTop) * aspectRatio;

            // Checks if the crop window is too small horizontally due to aspect ratio adjustment
            if (newWidth < mMinCropWidth) {
                newTop = Math.max(bounds.top, rect.bottom - (mMinCropWidth / aspectRatio));
                newWidth = (rect.bottom - newTop) * aspectRatio;
            }

            // Checks if the crop window is too large horizontally due to aspect ratio adjustment
            if (newWidth > mMaxCropWidth) {
                newTop = Math.max(bounds.top, rect.bottom - (mMaxCropWidth / aspectRatio));
                newWidth = (rect.bottom - newTop) * aspectRatio;
            }

            // if left AND right edge moves by aspect ratio check that it is within full width bounds
            if (leftMoves && rightMoves) {
                newTop = Math.max(newTop, Math.max(bounds.top, rect.bottom - bounds.width() / aspectRatio));
            } else {
                // if left edge moves by aspect ratio check that it is within bounds
                if (leftMoves && rect.right - newWidth < bounds.left) {
                    newTop = Math.max(bounds.top, rect.bottom - (rect.right - bounds.left) / aspectRatio);
                    newWidth = (rect.bottom - newTop) * aspectRatio;
                }

                // if right edge moves by aspect ratio check that it is within bounds
                if (rightMoves && rect.left + newWidth > bounds.right) {
                    newTop = Math.max(newTop, Math.max(bounds.top, rect.bottom - (bounds.right - rect.left) / aspectRatio));
                }
            }
        }

        rect.top = newTop;
    }

    /**
     * Get the resulting y-position of the bottom edge of the crop window given
     * the handle's position and the image's bounding box and snap radius.
     *
     * @param bottom the position that the bottom edge is dragged to
     * @param bounds the bounding box of the image that is being cropped
     * @param viewHeight view height
     * @param snapMargin the snap distance to the image edge (in pixels)
     */
    private void adjustBottom(RectF rect, float bottom, RectF bounds, int viewHeight, float snapMargin, float aspectRatio, boolean leftMoves, boolean rightMoves) {

        float newBottom = bottom;

        if (newBottom > viewHeight) {
            newBottom = viewHeight + (newBottom - viewHeight) / 1.05f;
            mTouchOffset.y -= (newBottom - viewHeight) / 1.1f;
        }

        if (newBottom > bounds.bottom) {
            mTouchOffset.y -= (newBottom - bounds.bottom) / 2f;
        }

        if (bounds.bottom - newBottom < snapMargin) {
            newBottom = bounds.bottom;
        }

        // Checks if the window is too small vertically
        if (newBottom - rect.top < mMinCropHeight) {
            newBottom = rect.top + mMinCropHeight;
        }

        // Checks if the window is too small vertically
        if (newBottom - rect.top > mMaxCropHeight) {
            newBottom = rect.top + mMaxCropHeight;
        }

        if (bounds.bottom - newBottom < snapMargin) {
            newBottom = bounds.bottom;
        }

        // check horizontal bounds if aspect ratio is in play
        if (aspectRatio > 0) {
            float newWidth = (newBottom - rect.top) * aspectRatio;

            // Checks if the window is too small horizontally
            if (newWidth < mMinCropWidth) {
                newBottom = Math.min(bounds.bottom, rect.top + mMinCropWidth / aspectRatio);
                newWidth = (newBottom - rect.top) * aspectRatio;
            }

            // Checks if the window is too large horizontally
            if (newWidth > mMaxCropWidth) {
                newBottom = Math.min(bounds.bottom, rect.top + mMaxCropWidth / aspectRatio);
                newWidth = (newBottom - rect.top) * aspectRatio;
            }

            // if left AND right edge moves by aspect ratio check that it is within full width bounds
            if (leftMoves && rightMoves) {
                newBottom = Math.min(newBottom, Math.min(bounds.bottom, rect.top + bounds.width() / aspectRatio));
            } else {
                // if left edge moves by aspect ratio check that it is within bounds
                if (leftMoves && rect.right - newWidth < bounds.left) {
                    newBottom = Math.min(bounds.bottom, rect.top + (rect.right - bounds.left) / aspectRatio);
                    newWidth = (newBottom - rect.top) * aspectRatio;
                }

                // if right edge moves by aspect ratio check that it is within bounds
                if (rightMoves && rect.left + newWidth > bounds.right) {
                    newBottom = Math.min(newBottom, Math.min(bounds.bottom, rect.top + (bounds.right - rect.left) / aspectRatio));
                }
            }
        }

        rect.bottom = newBottom;
    }

    /**
     * Adjust left edge by current crop window height and the given aspect ratio,
     * the right edge remains in possition while the left adjusts to keep aspect ratio to the height.
     */
    private void adjustLeftByAspectRatio(RectF rect, float aspectRatio) {
        rect.left = rect.right - rect.height() * aspectRatio;
    }

    /**
     * Adjust top edge by current crop window width and the given aspect ratio,
     * the bottom edge remains in possition while the top adjusts to keep aspect ratio to the width.
     */
    private void adjustTopByAspectRatio(RectF rect, float aspectRatio) {
        rect.top = rect.bottom - rect.width() / aspectRatio;
    }

    /**
     * Adjust right edge by current crop window height and the given aspect ratio,
     * the left edge remains in possition while the left adjusts to keep aspect ratio to the height.
     */
    private void adjustRightByAspectRatio(RectF rect, float aspectRatio) {
        rect.right = rect.left + rect.height() * aspectRatio;
    }

    /**
     * Adjust bottom edge by current crop window width and the given aspect ratio,
     * the top edge remains in possition while the top adjusts to keep aspect ratio to the width.
     */
    private void adjustBottomByAspectRatio(RectF rect, float aspectRatio) {
        rect.bottom = rect.top + rect.width() / aspectRatio;
    }

    /**
     * Adjust left and right edges by current crop window height and the given aspect ratio,
     * both right and left edges adjusts equally relative to center to keep aspect ratio to the height.
     */
    private void adjustLeftRightByAspectRatio(RectF rect, RectF bounds, float aspectRatio) {
        rect.inset((rect.width() - rect.height() * aspectRatio) / 2, 0);
        if (rect.left < bounds.left) {
            rect.offset(bounds.left - rect.left, 0);
        }
        if (rect.right > bounds.right) {
            rect.offset(bounds.right - rect.right, 0);
        }
    }

    /**
     * Adjust top and bottom edges by current crop window width and the given aspect ratio,
     * both top and bottom edges adjusts equally relative to center to keep aspect ratio to the width.
     */
    private void adjustTopBottomByAspectRatio(RectF rect, RectF bounds, float aspectRatio) {
        rect.inset(0, (rect.height() - rect.width() / aspectRatio) / 2);
        if (rect.top < bounds.top) {
            rect.offset(0, bounds.top - rect.top);
        }
        if (rect.bottom > bounds.bottom) {
            rect.offset(0, bounds.bottom - rect.bottom);
        }
    }

    /**
     * Calculates the aspect ratio given a rectangle.
     */
    private static float calculateAspectRatio(float left, float top, float right, float bottom) {
        return (right - left) / (bottom - top);
    }
    //endregion

    //region: Inner class: Type

    /**
     * The type of crop window move that is handled.
     */
    public enum Type {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        CENTER
    }
    //endregion
}