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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Arrays;

/**
 * A custom View representing the crop window and the shaded background outside the crop window.
 */
public class CropOverlayView extends View {

    //region: Fields and Consts

    /**
     * Gesture detector used for multi touch box scaling
     */
    private ScaleGestureDetector mScaleDetector;

    /**
     * Boolean to see if multi touch is enabled for the crop rectangle
     */
    private boolean mMultiTouchEnabled;

    /**
     * Handler from crop window stuff, moving and knowing possition.
     */
    private final CropWindowHandler mCropWindowHandler = new CropWindowHandler();

    /**
     * Listener to publicj crop window changes
     */
    private CropWindowChangeListener mCropWindowChangeListener;

    /**
     * Rectangle used for drawing
     */
    private final RectF mDrawRect = new RectF();

    /**
     * The Paint used to draw the white rectangle around the crop area.
     */
    private Paint mBorderPaint;

    /**
     * The Paint used to draw the corners of the Border
     */
    private Paint mBorderCornerPaint;

    /**
     * The Paint used to draw the guidelines within the crop area when pressed.
     */
    private Paint mGuidelinePaint;

    /**
     * The Paint used to darken the surrounding areas outside the crop area.
     */
    private Paint mBackgroundPaint;

    /**
     * Used for oval crop window shape or non-straight rotation drawing.
     */
    private final Path mPath = new Path();

    /**
     * The bounding box around the Bitmap that we are cropping.
     */
    private final float[] mBoundsPoints = new float[8];

    /**
     * The bounding box around the Bitmap that we are cropping.
     */
    private final RectF mCalcBounds = new RectF();

    /**
     * The bounding image view width used to know the crop overlay is at view edges.
     */
    private int mViewWidth;

    /**
     * The bounding image view height used to know the crop overlay is at view edges.
     */
    private int mViewHeight;

    /**
     * The offset to draw the border corener from the border
     */
    private float mBorderCornerOffset;

    /**
     * the length of the border corner to draw
     */
    private float mBorderCornerLength;

    /**
     * The initial crop window padding from image borders
     */
    private float mInitialCropWindowPaddingRatio;

    /**
     * The radius of the touch zone (in pixels) around a given Handle.
     */
    private float mTouchRadius;

    /**
     * An edge of the crop window will snap to the corresponding edge of a specified bounding box
     * when the crop window edge is less than or equal to this distance (in pixels) away from the bounding box edge.
     */
    private float mSnapRadius;

    /**
     * The Handle that is currently pressed; null if no Handle is pressed.
     */
    private CropWindowMoveHandler mMoveHandler;

    /**
     * Flag indicating if the crop area should always be a certain aspect ratio (indicated by mTargetAspectRatio).
     */
    private boolean mFixAspectRatio;

    /**
     * save the current aspect ratio of the image
     */
    private int mAspectRatioX;

    /**
     * save the current aspect ratio of the image
     */
    private int mAspectRatioY;

    /**
     * The aspect ratio that the crop area should maintain;
     * this variable is only used when mMaintainAspectRatio is true.
     */
    private float mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

    /**
     * Instance variables for customizable attributes
     */
    private CropImageView.Guidelines mGuidelines;

    /**
     * The shape of the cropping area - rectangle/circular.
     */
    private CropImageView.CropShape mCropShape;

    /**
     * the initial crop window rectangle to set
     */
    private final Rect mInitialCropWindowRect = new Rect();

    /**
     * Whether the Crop View has been initialized for the first time
     */
    private boolean initializedCropWindow;

    /**
     * Used to set back LayerType after changing to software.
     */
    private Integer mOriginalLayerType;
    //endregion

    public CropOverlayView(Context context) {
        this(context, null);
    }

    public CropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Set the crop window change listener.
     */
    public void setCropWindowChangeListener(CropWindowChangeListener listener) {
        mCropWindowChangeListener = listener;
    }

    /**
     * Get the left/top/right/bottom coordinates of the crop window.
     */
    public RectF getCropWindowRect() {
        return mCropWindowHandler.getRect();
    }

    /**
     * Set the left/top/right/bottom coordinates of the crop window.
     */
    public void setCropWindowRect(RectF rect) {
        mCropWindowHandler.setRect(rect);
    }

    /**
     * Fix the current crop window rectangle if it is outside of cropping image or view bounds.
     */
    public void fixCurrentCropWindowRect() {
        RectF rect = getCropWindowRect();
        fixCropWindowRectByRules(rect);
        mCropWindowHandler.setRect(rect);
    }

    /**
     * Informs the CropOverlayView of the image's position relative to the
     * ImageView. This is necessary to call in order to draw the crop window.
     *
     * @param boundsPoints the image's bounding points
     * @param viewWidth The bounding image view width.
     * @param viewHeight The bounding image view height.
     */
    public void setBounds(float[] boundsPoints, int viewWidth, int viewHeight) {
        if (boundsPoints == null || !Arrays.equals(mBoundsPoints, boundsPoints)) {
            if (boundsPoints == null) {
                Arrays.fill(mBoundsPoints, 0);
            } else {
                System.arraycopy(boundsPoints, 0, mBoundsPoints, 0, boundsPoints.length);
            }
            mViewWidth = viewWidth;
            mViewHeight = viewHeight;
            RectF cropRect = mCropWindowHandler.getRect();
            if (cropRect.width() == 0 || cropRect.height() == 0) {
                initCropWindow();
            }
        }
    }

    /**
     * Resets the crop overlay view.
     */
    public void resetCropOverlayView() {
        if (initializedCropWindow) {
            setCropWindowRect(BitmapUtils.EMPTY_RECT_F);
            initCropWindow();
            invalidate();
        }
    }

    /**
     * The shape of the cropping area - rectangle/circular.
     */
    public CropImageView.CropShape getCropShape() {
        return mCropShape;
    }

    /**
     * The shape of the cropping area - rectangle/circular.
     */
    public void setCropShape(CropImageView.CropShape cropShape) {
        if (mCropShape != cropShape) {
            mCropShape = cropShape;
            invalidate();
        }
    }

    /**
     * Get the current guidelines option set.
     */
    public CropImageView.Guidelines getGuidelines() {
        return mGuidelines;
    }

    /**
     * Sets the guidelines for the CropOverlayView to be either on, off, or to show when resizing the application.
     */
    public void setGuidelines(CropImageView.Guidelines guidelines) {
        if (mGuidelines != guidelines) {
            mGuidelines = guidelines;
            if (initializedCropWindow) {
                invalidate();
            }
        }
    }

    /**
     * whether the aspect ratio is fixed or not; true fixes the aspect ratio, while false allows it to be changed.
     */
    public boolean isFixAspectRatio() {
        return mFixAspectRatio;
    }

    /**
     * Sets whether the aspect ratio is fixed or not; true fixes the aspect ratio, while false allows it to be changed.
     */
    public void setFixedAspectRatio(boolean fixAspectRatio) {
        if (mFixAspectRatio != fixAspectRatio) {
            mFixAspectRatio = fixAspectRatio;
            if (initializedCropWindow) {
                initCropWindow();
                invalidate();
            }
        }
    }

    /**
     * the X value of the aspect ratio;
     */
    public int getAspectRatioX() {
        return mAspectRatioX;
    }

    /**
     * Sets the X value of the aspect ratio; is defaulted to 1.
     */
    public void setAspectRatioX(int aspectRatioX) {
        if (aspectRatioX <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        } else if (mAspectRatioX != aspectRatioX) {
            mAspectRatioX = aspectRatioX;
            mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

            if (initializedCropWindow) {
                initCropWindow();
                invalidate();
            }
        }
    }

    /**
     * the Y value of the aspect ratio;
     */
    public int getAspectRatioY() {
        return mAspectRatioY;
    }

    /**
     * Sets the Y value of the aspect ratio; is defaulted to 1.
     *
     * @param aspectRatioY int that specifies the new Y value of the aspect
     * ratio
     */
    public void setAspectRatioY(int aspectRatioY) {
        if (aspectRatioY <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        } else if (mAspectRatioY != aspectRatioY) {
            mAspectRatioY = aspectRatioY;
            mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

            if (initializedCropWindow) {
                initCropWindow();
                invalidate();
            }
        }
    }

    /**
     * An edge of the crop window will snap to the corresponding edge of a
     * specified bounding box when the crop window edge is less than or equal to
     * this distance (in pixels) away from the bounding box edge. (default: 3)
     */
    public void setSnapRadius(float snapRadius) {
        mSnapRadius = snapRadius;
    }

    /**
     * Set multi touch functionality to enabled/disabled.
     */
    public boolean setMultiTouchEnabled(boolean multiTouchEnabled) {
        if (mMultiTouchEnabled != multiTouchEnabled) {
            mMultiTouchEnabled = multiTouchEnabled;
            if (mMultiTouchEnabled && mScaleDetector == null) {
                mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
            }
            return true;
        }
        return false;
    }

    /**
     * the min size the resulting cropping image is allowed to be, affects the cropping window limits
     * (in pixels).<br>
     */
    public void setMinCropResultSize(int minCropResultWidth, int minCropResultHeight) {
        mCropWindowHandler.setMinCropResultSize(minCropResultWidth, minCropResultHeight);
    }

    /**
     * the max size the resulting cropping image is allowed to be, affects the cropping window limits
     * (in pixels).<br>
     */
    public void setMaxCropResultSize(int maxCropResultWidth, int maxCropResultHeight) {
        mCropWindowHandler.setMaxCropResultSize(maxCropResultWidth, maxCropResultHeight);
    }

    /**
     * set the max width/height and scale factor of the shown image to original image to scale the limits
     * appropriately.
     */
    public void setCropWindowLimits(float maxWidth, float maxHeight, float scaleFactorWidth, float scaleFactorHeight) {
        mCropWindowHandler.setCropWindowLimits(maxWidth, maxHeight, scaleFactorWidth, scaleFactorHeight);
    }

    /**
     * Get crop window initial rectangle.
     */
    public Rect getInitialCropWindowRect() {
        return mInitialCropWindowRect;
    }

    /**
     * Set crop window initial rectangle to be used instead of default.
     */
    public void setInitialCropWindowRect(Rect rect) {
        mInitialCropWindowRect.set(rect != null ? rect : BitmapUtils.EMPTY_RECT);
        if (initializedCropWindow) {
            initCropWindow();
            invalidate();
            callOnCropWindowChanged(false);
        }
    }

    /**
     * Reset crop window to initial rectangle.
     */
    public void resetCropWindowRect() {
        if (initializedCropWindow) {
            initCropWindow();
            invalidate();
            callOnCropWindowChanged(false);
        }
    }

    /**
     * Sets all initial values, but does not call initCropWindow to reset the views.<br>
     * Used once at the very start to initialize the attributes.
     */
    public void setInitialAttributeValues(CropImageOptions options) {

        mCropWindowHandler.setInitialAttributeValues(options);

        setCropShape(options.cropShape);

        setSnapRadius(options.snapRadius);

        setGuidelines(options.guidelines);

        setFixedAspectRatio(options.fixAspectRatio);

        setAspectRatioX(options.aspectRatioX);

        setAspectRatioY(options.aspectRatioY);

        setMultiTouchEnabled(options.multiTouchEnabled);

        mTouchRadius = options.touchRadius;

        mInitialCropWindowPaddingRatio = options.initialCropWindowPaddingRatio;

        mBorderPaint = getNewPaintOrNull(options.borderLineThickness, options.borderLineColor);

        mBorderCornerOffset = options.borderCornerOffset;
        mBorderCornerLength = options.borderCornerLength;
        mBorderCornerPaint = getNewPaintOrNull(options.borderCornerThickness, options.borderCornerColor);

        mGuidelinePaint = getNewPaintOrNull(options.guidelinesThickness, options.guidelinesColor);

        mBackgroundPaint = getNewPaint(options.backgroundColor);
    }

    //region: Private methods

    /**
     * Set the initial crop window size and position. This is dependent on the
     * size and position of the image being cropped.
     */
    private void initCropWindow() {

        float leftLimit = Math.max(BitmapUtils.getRectLeft(mBoundsPoints), 0);
        float topLimit = Math.max(BitmapUtils.getRectTop(mBoundsPoints), 0);
        float rightLimit = Math.min(BitmapUtils.getRectRight(mBoundsPoints), getWidth());
        float bottomLimit = Math.min(BitmapUtils.getRectBottom(mBoundsPoints), getHeight());

        if (rightLimit <= leftLimit || bottomLimit <= topLimit) {
            return;
        }

        RectF rect = new RectF();

        // Tells the attribute functions the crop window has already been initialized
        initializedCropWindow = true;

        float horizontalPadding = mInitialCropWindowPaddingRatio * (rightLimit - leftLimit);
        float verticalPadding = mInitialCropWindowPaddingRatio * (bottomLimit - topLimit);

        if (mInitialCropWindowRect.width() > 0 && mInitialCropWindowRect.height() > 0) {
            // Get crop window position relative to the displayed image.
            rect.left = leftLimit + mInitialCropWindowRect.left / mCropWindowHandler.getScaleFactorWidth();
            rect.top = topLimit + mInitialCropWindowRect.top / mCropWindowHandler.getScaleFactorHeight();
            rect.right = rect.left + mInitialCropWindowRect.width() / mCropWindowHandler.getScaleFactorWidth();
            rect.bottom = rect.top + mInitialCropWindowRect.height() / mCropWindowHandler.getScaleFactorHeight();

            // Correct for floating point errors. Crop rect boundaries should not exceed the source Bitmap bounds.
            rect.left = Math.max(leftLimit, rect.left);
            rect.top = Math.max(topLimit, rect.top);
            rect.right = Math.min(rightLimit, rect.right);
            rect.bottom = Math.min(bottomLimit, rect.bottom);

        } else if (mFixAspectRatio) {

            // If the image aspect ratio is wider than the crop aspect ratio,
            // then the image height is the determining initial length. Else, vice-versa.
            float bitmapAspectRatio = (rightLimit - leftLimit) / (bottomLimit - topLimit);
            if (bitmapAspectRatio > mTargetAspectRatio) {

                rect.top = topLimit + verticalPadding;
                rect.bottom = bottomLimit - verticalPadding;

                float centerX = getWidth() / 2f;

                //dirty fix for wrong crop overlay aspect ratio when using fixed aspect ratio
                mTargetAspectRatio = (float) mAspectRatioX / mAspectRatioY;

                // Limits the aspect ratio to no less than 40 wide or 40 tall
                float cropWidth = Math.max(mCropWindowHandler.getMinCropWidth(), rect.height() * mTargetAspectRatio);

                float halfCropWidth = cropWidth / 2f;
                rect.left = centerX - halfCropWidth;
                rect.right = centerX + halfCropWidth;

            } else {

                rect.left = leftLimit + horizontalPadding;
                rect.right = rightLimit - horizontalPadding;

                float centerY = getHeight() / 2f;

                // Limits the aspect ratio to no less than 40 wide or 40 tall
                float cropHeight = Math.max(mCropWindowHandler.getMinCropHeight(), rect.width() / mTargetAspectRatio);

                float halfCropHeight = cropHeight / 2f;
                rect.top = centerY - halfCropHeight;
                rect.bottom = centerY + halfCropHeight;
            }
        } else {
            // Initialize crop window to have 10% padding w/ respect to image.
            rect.left = leftLimit + horizontalPadding;
            rect.top = topLimit + verticalPadding;
            rect.right = rightLimit - horizontalPadding;
            rect.bottom = bottomLimit - verticalPadding;
        }

        fixCropWindowRectByRules(rect);

        mCropWindowHandler.setRect(rect);
    }

    /**
     * Fix the given rect to fit into bitmap rect and follow min, max and aspect ratio rules.
     */
    private void fixCropWindowRectByRules(RectF rect) {
        if (rect.width() < mCropWindowHandler.getMinCropWidth()) {
            float adj = (mCropWindowHandler.getMinCropWidth() - rect.width()) / 2;
            rect.left -= adj;
            rect.right += adj;
        }
        if (rect.height() < mCropWindowHandler.getMinCropHeight()) {
            float adj = (mCropWindowHandler.getMinCropHeight() - rect.height()) / 2;
            rect.top -= adj;
            rect.bottom += adj;
        }
        if (rect.width() > mCropWindowHandler.getMaxCropWidth()) {
            float adj = (rect.width() - mCropWindowHandler.getMaxCropWidth()) / 2;
            rect.left += adj;
            rect.right -= adj;
        }
        if (rect.height() > mCropWindowHandler.getMaxCropHeight()) {
            float adj = (rect.height() - mCropWindowHandler.getMaxCropHeight()) / 2;
            rect.top += adj;
            rect.bottom -= adj;
        }

        calculateBounds(rect);
        if (mCalcBounds.width() > 0 && mCalcBounds.height() > 0) {
            float leftLimit = Math.max(mCalcBounds.left, 0);
            float topLimit = Math.max(mCalcBounds.top, 0);
            float rightLimit = Math.min(mCalcBounds.right, getWidth());
            float bottomLimit = Math.min(mCalcBounds.bottom, getHeight());
            if (rect.left < leftLimit) {
                rect.left = leftLimit;
            }
            if (rect.top < topLimit) {
                rect.top = topLimit;
            }
            if (rect.right > rightLimit) {
                rect.right = rightLimit;
            }
            if (rect.bottom > bottomLimit) {
                rect.bottom = bottomLimit;
            }
        }
        if (mFixAspectRatio && Math.abs(rect.width() - rect.height() * mTargetAspectRatio) > 0.1) {
            if (rect.width() > rect.height() * mTargetAspectRatio) {
                float adj = Math.abs(rect.height() * mTargetAspectRatio - rect.width()) / 2;
                rect.left += adj;
                rect.right -= adj;
            } else {
                float adj = Math.abs(rect.width() / mTargetAspectRatio - rect.height()) / 2;
                rect.top += adj;
                rect.bottom -= adj;
            }
        }
    }

    /**
     * Draw crop overview by drawing background over image not in the cripping area, then borders and guidelines.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // Draw translucent background for the cropped area.
        drawBackground(canvas);

        if (mCropWindowHandler.showGuidelines()) {
            // Determines whether guidelines should be drawn or not
            if (mGuidelines == CropImageView.Guidelines.ON) {
                drawGuidelines(canvas);
            } else if (mGuidelines == CropImageView.Guidelines.ON_TOUCH && mMoveHandler != null) {
                // Draw only when resizing
                drawGuidelines(canvas);
            }
        }

        drawBorders(canvas);

        drawCorners(canvas);
    }

    /**
     * Draw shadow background over the image not including the crop area.
     */
    private void drawBackground(Canvas canvas) {

        RectF rect = mCropWindowHandler.getRect();

        float left = Math.max(BitmapUtils.getRectLeft(mBoundsPoints), 0);
        float top = Math.max(BitmapUtils.getRectTop(mBoundsPoints), 0);
        float right = Math.min(BitmapUtils.getRectRight(mBoundsPoints), getWidth());
        float bottom = Math.min(BitmapUtils.getRectBottom(mBoundsPoints), getHeight());

        if (mCropShape == CropImageView.CropShape.RECTANGLE) {
            if (!isNonStraightAngleRotated()) {
                canvas.drawRect(left, top, right, rect.top, mBackgroundPaint);
                canvas.drawRect(left, rect.bottom, right, bottom, mBackgroundPaint);
                canvas.drawRect(left, rect.top, rect.left, rect.bottom, mBackgroundPaint);
                canvas.drawRect(rect.right, rect.top, right, rect.bottom, mBackgroundPaint);
            } else {
                mPath.reset();
                mPath.moveTo(mBoundsPoints[0], mBoundsPoints[1]);
                mPath.lineTo(mBoundsPoints[2], mBoundsPoints[3]);
                mPath.lineTo(mBoundsPoints[4], mBoundsPoints[5]);
                mPath.lineTo(mBoundsPoints[6], mBoundsPoints[7]);
                mPath.close();

                canvas.save();
                canvas.clipPath(mPath, Region.Op.INTERSECT);
                canvas.clipRect(rect, Region.Op.DIFFERENCE);
                canvas.drawRect(left, top, right, bottom, mBackgroundPaint);
                canvas.restore();
            }
        } else {
            mPath.reset();
            mDrawRect.set(rect.left, rect.top, rect.right, rect.bottom);
            mPath.addOval(mDrawRect, Path.Direction.CW);
            canvas.save();
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            canvas.drawRect(left, top, right, bottom, mBackgroundPaint);
            canvas.restore();
        }
    }

    /**
     * Draw 2 veritcal and 2 horizontal guidelines inside the cropping area to split it into 9 equal parts.
     */
    private void drawGuidelines(Canvas canvas) {
        if (mGuidelinePaint != null) {
            float sw = mBorderPaint != null ? mBorderPaint.getStrokeWidth() : 0;
            RectF rect = mCropWindowHandler.getRect();
            rect.inset(sw, sw);

            float oneThirdCropWidth = rect.width() / 3;
            float oneThirdCropHeight = rect.height() / 3;

            if (mCropShape == CropImageView.CropShape.OVAL) {

                float w = rect.width() / 2 - sw;
                float h = rect.height() / 2 - sw;

                // Draw vertical guidelines.
                float x1 = rect.left + oneThirdCropWidth;
                float x2 = rect.right - oneThirdCropWidth;
                float yv = (float) (h * Math.sin(Math.acos((w - oneThirdCropWidth) / w)));
                canvas.drawLine(x1, rect.top + h - yv, x1, rect.bottom - h + yv, mGuidelinePaint);
                canvas.drawLine(x2, rect.top + h - yv, x2, rect.bottom - h + yv, mGuidelinePaint);

                // Draw horizontal guidelines.
                float y1 = rect.top + oneThirdCropHeight;
                float y2 = rect.bottom - oneThirdCropHeight;
                float xv = (float) (w * Math.cos(Math.asin((h - oneThirdCropHeight) / h)));
                canvas.drawLine(rect.left + w - xv, y1, rect.right - w + xv, y1, mGuidelinePaint);
                canvas.drawLine(rect.left + w - xv, y2, rect.right - w + xv, y2, mGuidelinePaint);
            } else {

                // Draw vertical guidelines.
                float x1 = rect.left + oneThirdCropWidth;
                float x2 = rect.right - oneThirdCropWidth;
                canvas.drawLine(x1, rect.top, x1, rect.bottom, mGuidelinePaint);
                canvas.drawLine(x2, rect.top, x2, rect.bottom, mGuidelinePaint);

                // Draw horizontal guidelines.
                float y1 = rect.top + oneThirdCropHeight;
                float y2 = rect.bottom - oneThirdCropHeight;
                canvas.drawLine(rect.left, y1, rect.right, y1, mGuidelinePaint);
                canvas.drawLine(rect.left, y2, rect.right, y2, mGuidelinePaint);
            }
        }
    }

    /**
     * Draw borders of the crop area.
     */
    private void drawBorders(Canvas canvas) {
        if (mBorderPaint != null) {
            float w = mBorderPaint.getStrokeWidth();
            RectF rect = mCropWindowHandler.getRect();
            rect.inset(w / 2, w / 2);

            if (mCropShape == CropImageView.CropShape.RECTANGLE) {
                // Draw rectangle crop window border.
                canvas.drawRect(rect, mBorderPaint);
            } else {
                // Draw circular crop window border
                canvas.drawOval(rect, mBorderPaint);
            }
        }
    }

    /**
     * Draw the corner of crop overlay.
     */
    private void drawCorners(Canvas canvas) {
        if (mBorderCornerPaint != null) {

            float lineWidth = mBorderPaint != null ? mBorderPaint.getStrokeWidth() : 0;
            float cornerWidth = mBorderCornerPaint.getStrokeWidth();
            float w = cornerWidth / 2 + mBorderCornerOffset;
            RectF rect = mCropWindowHandler.getRect();
            rect.inset(w, w);

            float cornerOffset = (cornerWidth - lineWidth) / 2;
            float cornerExtension = cornerWidth / 2 + cornerOffset;

            // Top left
            canvas.drawLine(rect.left - cornerOffset, rect.top - cornerExtension, rect.left - cornerOffset, rect.top + mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.left - cornerExtension, rect.top - cornerOffset, rect.left + mBorderCornerLength, rect.top - cornerOffset, mBorderCornerPaint);

            // Top right
            canvas.drawLine(rect.right + cornerOffset, rect.top - cornerExtension, rect.right + cornerOffset, rect.top + mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.right + cornerExtension, rect.top - cornerOffset, rect.right - mBorderCornerLength, rect.top - cornerOffset, mBorderCornerPaint);

            // Bottom left
            canvas.drawLine(rect.left - cornerOffset, rect.bottom + cornerExtension, rect.left - cornerOffset, rect.bottom - mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.left - cornerExtension, rect.bottom + cornerOffset, rect.left + mBorderCornerLength, rect.bottom + cornerOffset, mBorderCornerPaint);

            // Bottom left
            canvas.drawLine(rect.right + cornerOffset, rect.bottom + cornerExtension, rect.right + cornerOffset, rect.bottom - mBorderCornerLength, mBorderCornerPaint);
            canvas.drawLine(rect.right + cornerExtension, rect.bottom + cornerOffset, rect.right - mBorderCornerLength, rect.bottom + cornerOffset, mBorderCornerPaint);
        }
    }

    /**
     * Creates the Paint object for drawing.
     */
    private static Paint getNewPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        return paint;
    }

    /**
     * Creates the Paint object for given thickness and color, if thickness < 0 return null.
     */
    private static Paint getNewPaintOrNull(float thickness, int color) {
        if (thickness > 0) {
            Paint borderPaint = new Paint();
            borderPaint.setColor(color);
            borderPaint.setStrokeWidth(thickness);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setAntiAlias(true);
            return borderPaint;
        } else {
            return null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If this View is not enabled, don't allow for touch interactions.
        if (isEnabled()) {
            if (mMultiTouchEnabled) {
                mScaleDetector.onTouchEvent(event);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onActionDown(event.getX(), event.getY());
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    onActionUp();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    onActionMove(event.getX(), event.getY());
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * On press down start crop window movment depending on the location of the press.<br>
     * if press is far from crop window then no move handler is returned (null).
     */
    private void onActionDown(float x, float y) {
        mMoveHandler = mCropWindowHandler.getMoveHandler(x, y, mTouchRadius, mCropShape);
        if (mMoveHandler != null) {
            invalidate();
        }
    }

    /**
     * Clear move handler starting in {@link #onActionDown(float, float)} if exists.
     */
    private void onActionUp() {
        if (mMoveHandler != null) {
            mMoveHandler = null;
            callOnCropWindowChanged(false);
            invalidate();
        }
    }

    /**
     * Handle move of crop window using the move handler created in {@link #onActionDown(float, float)}.<br>
     * The move handler will do the proper move/resize of the crop window.
     */
    private void onActionMove(float x, float y) {
        if (mMoveHandler != null) {
            float snapRadius = mSnapRadius;
            RectF rect = mCropWindowHandler.getRect();

            if (calculateBounds(rect)) {
                snapRadius = 0;
            }

            mMoveHandler.move(rect, x, y, mCalcBounds, mViewWidth, mViewHeight, snapRadius, mFixAspectRatio, mTargetAspectRatio);
            mCropWindowHandler.setRect(rect);
            callOnCropWindowChanged(true);
            invalidate();
        }
    }

    /**
     * Calculate the bounding rectangle for current crop window, handle non-straight rotation angles.<br>
     * If the rotation angle is straight then the bounds rectangle is the bitmap rectangle,
     * otherwsie we find the max rectangle that is within the image bounds starting from the crop window rectangle.
     *
     * @param rect the crop window rectangle to start finsing bounded rectangle from
     * @return true - non straight rotation in place, false - otherwise.
     */
    private boolean calculateBounds(RectF rect) {

        float left = BitmapUtils.getRectLeft(mBoundsPoints);
        float top = BitmapUtils.getRectTop(mBoundsPoints);
        float right = BitmapUtils.getRectRight(mBoundsPoints);
        float bottom = BitmapUtils.getRectBottom(mBoundsPoints);

        if (!isNonStraightAngleRotated()) {
            mCalcBounds.set(left, top, right, bottom);
            return false;
        } else {
            float x0 = mBoundsPoints[0];
            float y0 = mBoundsPoints[1];
            float x2 = mBoundsPoints[4];
            float y2 = mBoundsPoints[5];
            float x3 = mBoundsPoints[6];
            float y3 = mBoundsPoints[7];

            if (mBoundsPoints[7] < mBoundsPoints[1]) {
                if (mBoundsPoints[1] < mBoundsPoints[3]) {
                    x0 = mBoundsPoints[6];
                    y0 = mBoundsPoints[7];
                    x2 = mBoundsPoints[2];
                    y2 = mBoundsPoints[3];
                    x3 = mBoundsPoints[4];
                    y3 = mBoundsPoints[5];
                } else {
                    x0 = mBoundsPoints[4];
                    y0 = mBoundsPoints[5];
                    x2 = mBoundsPoints[0];
                    y2 = mBoundsPoints[1];
                    x3 = mBoundsPoints[2];
                    y3 = mBoundsPoints[3];
                }
            } else if (mBoundsPoints[1] > mBoundsPoints[3]) {
                x0 = mBoundsPoints[2];
                y0 = mBoundsPoints[3];
                x2 = mBoundsPoints[6];
                y2 = mBoundsPoints[7];
                x3 = mBoundsPoints[0];
                y3 = mBoundsPoints[1];
            }

            float a0 = (y3 - y0) / (x3 - x0);
            float a1 = -1f / a0;
            float b0 = y0 - a0 * x0;
            float b1 = y0 - a1 * x0;
            float b2 = y2 - a0 * x2;
            float b3 = y2 - a1 * x2;

            float c0 = (rect.centerY() - rect.top) / (rect.centerX() - rect.left);
            float c1 = -c0;
            float d0 = rect.top - c0 * rect.left;
            float d1 = rect.top - c1 * rect.right;

            left = Math.max(left, (d0 - b0) / (a0 - c0) < rect.right ? (d0 - b0) / (a0 - c0) : left);
            left = Math.max(left, (d0 - b1) / (a1 - c0) < rect.right ? (d0 - b1) / (a1 - c0) : left);
            left = Math.max(left, (d1 - b3) / (a1 - c1) < rect.right ? (d1 - b3) / (a1 - c1) : left);
            right = Math.min(right, (d1 - b1) / (a1 - c1) > rect.left ? (d1 - b1) / (a1 - c1) : right);
            right = Math.min(right, (d1 - b2) / (a0 - c1) > rect.left ? (d1 - b2) / (a0 - c1) : right);
            right = Math.min(right, (d0 - b2) / (a0 - c0) > rect.left ? (d0 - b2) / (a0 - c0) : right);

            top = Math.max(top, Math.max(a0 * left + b0, a1 * right + b1));
            bottom = Math.min(bottom, Math.min(a1 * left + b3, a0 * right + b2));

            mCalcBounds.left = left;
            mCalcBounds.top = top;
            mCalcBounds.right = right;
            mCalcBounds.bottom = bottom;
            return true;
        }
    }

    /**
     * Is the cropping image has been rotated by NOT 0,90,180 or 270 degrees.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isNonStraightAngleRotated() {
        return mBoundsPoints[0] != mBoundsPoints[6] && mBoundsPoints[1] != mBoundsPoints[7];
    }

    /**
     * Invoke on crop change listener safe, don't let the app crash on exception.
     */
    private void callOnCropWindowChanged(boolean inProgress) {
        try {
            if (mCropWindowChangeListener != null) {
                mCropWindowChangeListener.onCropWindowChanged(inProgress);
            }
        } catch (Exception e) {
            Log.e("AIC", "Exception in crop window changed", e);
        }
    }
    //endregion

    //region: Inner class: CropWindowChangeListener

    /**
     * Interface definition for a callback to be invoked when crop window rectangle is changing.
     */
    public interface CropWindowChangeListener {

        /**
         * Called after a change in crop window rectangle.
         *
         * @param inProgress is the crop window change operation is still in progress by user touch
         */
        void onCropWindowChanged(boolean inProgress);
    }
    //endregion

    //region: Inner class: ScaleListener

    /**
     * Handle scaling the rectangle based on two finger input
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public boolean onScale(ScaleGestureDetector detector) {
            RectF rect = mCropWindowHandler.getRect();

            float x = detector.getFocusX();
            float y = detector.getFocusY();
            float dY = detector.getCurrentSpanY() / 2;
            float dX = detector.getCurrentSpanX() / 2;

            float newTop = y - dY;
            float newLeft = x - dX;
            float newRight = x + dX;
            float newBottom = y + dY;

            if (newLeft < newRight &&
                    newTop <= newBottom &&
                    newLeft >= 0 &&
                    newRight <= mCropWindowHandler.getMaxCropWidth() &&
                    newTop >= 0 &&
                    newBottom <= mCropWindowHandler.getMaxCropHeight()) {

                rect.set(newLeft, newTop, newRight, newBottom);
                mCropWindowHandler.setRect(rect);
                invalidate();
            }

            return true;
        }
    }
    //endregion
}