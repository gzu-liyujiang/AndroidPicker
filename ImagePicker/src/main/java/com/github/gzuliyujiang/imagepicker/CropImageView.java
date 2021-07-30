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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.exifinterface.media.ExifInterface;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Custom view that provides cropping capabilities to an image.
 */
public class CropImageView extends FrameLayout {

    //region: Fields and Consts

    /**
     * Image view widget used to show the image for cropping.
     */
    private final ImageView mImageView;

    /**
     * Overlay over the image view to show cropping UI.
     */
    private final CropOverlayView mCropOverlayView;

    /**
     * The matrix used to transform the cropping image in the image view
     */
    private final Matrix mImageMatrix = new Matrix();

    /**
     * Reusing matrix instance for reverse matrix calculations.
     */
    private final Matrix mImageInverseMatrix = new Matrix();

    /**
     * Progress bar widget to show progress bar on async image loading and cropping.
     */
    private final ProgressBar mProgressBar;

    /**
     * Rectengale used in image matrix transformation calculation (reusing rect instance)
     */
    private final float[] mImagePoints = new float[8];

    /**
     * Animation class to smooth animate zoom-in/out
     */
    private CropImageAnimation mAnimation;

    private Bitmap mBitmap;

    private int mDegreesRotated;

    private int mLayoutWidth;

    private int mLayoutHeight;

    private int mImageResource;

    /**
     * The initial scale type of the image in the crop image view
     */
    private ScaleType mScaleType;

    /**
     * if to show crop overlay UI what contains the crop window UI surrounded by background over the cropping
     * image.<br>
     * default: true, may disable for animation or frame transition.
     */
    private boolean mShowCropOverlay = true;

    /**
     * if to show progress bar when image async loading/cropping is in progress.<br>
     * default: true, disable to provide custom progress bar UI.
     */
    private boolean mShowProgressBar = true;

    /**
     * if auto-zoom functionality is enabled.<br>
     * default: true.
     */
    private boolean mAutoZoomEnabled;

    /**
     * The max zoom allowed during cropping
     */
    private int mMaxZoom;

    /**
     * callback to be invoked when image async loading is complete.
     */
    private OnSetImageUriCompleteListener mOnSetImageUriCompleteListener;

    /**
     * callback to be invoked when image async cropping is complete.
     */
    private OnCropImageCompleteListener mOnCropImageCompleteListener;

    /**
     * The URI that the image was loaded from (if loaded from URI)
     */
    private Uri mLoadedImageUri;

    /**
     * The sample size the image was loaded by if was loaded by URI
     */
    private int mLoadedSampleSize = 1;

    /**
     * The current zoom level to to scale the cropping image
     */
    private float mZoom = 1;

    /**
     * The X offset that the cropping image was translated after zooming
     */
    private float mZoomOffsetX;

    /**
     * The Y offset that the cropping image was translated after zooming
     */
    private float mZoomOffsetY;

    /**
     * Used to restore the cropping windows rectangle after state restore
     */
    private RectF mRestoreCropWindowRect;

    /**
     * Used to detect size change to handle auto-zoom using {@link #handleCropWindowChanged(boolean, boolean)} in
     * {@link #layout(int, int, int, int)}.
     */
    private boolean mSizeChanged;

    /**
     * Task used to load bitmap async from UI thread
     */
    private WeakReference<BitmapLoadingTask> mBitmapLoadingWorkerTask;

    /**
     * Task used to crop bitmap async from UI thread
     */
    private WeakReference<BitmapCroppingTask> mBitmapCroppingWorkerTask;
    //endregion

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CropImageOptions options = new CropImageOptions();
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropImageView, 0, 0);
            try {
                options.fixAspectRatio = ta.getBoolean(R.styleable.CropImageView_cropFixAspectRatio, options.fixAspectRatio);
                options.aspectRatioX = ta.getInteger(R.styleable.CropImageView_cropAspectRatioX, options.aspectRatioX);
                options.aspectRatioY = ta.getInteger(R.styleable.CropImageView_cropAspectRatioY, options.aspectRatioY);
                options.scaleType = ScaleType.values()[ta.getInt(
                        R.styleable.CropImageView_cropScaleType, options.scaleType.ordinal())];
                options.autoZoomEnabled = ta.getBoolean(R.styleable.CropImageView_cropAutoZoomEnabled, options.autoZoomEnabled);
                options.multiTouchEnabled = ta.getBoolean(R.styleable.CropImageView_cropMultiTouchEnabled, options.multiTouchEnabled);
                options.maxZoom = ta.getInteger(R.styleable.CropImageView_cropMaxZoom, options.maxZoom);
                options.cropShape = CropShape.values()[ta.getInt(
                        R.styleable.CropImageView_cropShape, options.cropShape.ordinal())];
                options.guidelines = Guidelines.values()[ta.getInt(
                        R.styleable.CropImageView_cropGuidelines, options.guidelines.ordinal())];
                options.snapRadius = ta.getDimension(R.styleable.CropImageView_cropSnapRadius, options.snapRadius);
                options.touchRadius = ta.getDimension(R.styleable.CropImageView_cropTouchRadius, options.touchRadius);
                options.initialCropWindowPaddingRatio = ta.getFloat(
                        R.styleable.CropImageView_cropInitialCropWindowPaddingRatio, options.initialCropWindowPaddingRatio);
                options.borderLineThickness = ta.getDimension(R.styleable.CropImageView_cropBorderLineThickness, options.borderLineThickness);
                options.borderLineColor = ta.getInteger(R.styleable.CropImageView_cropBorderLineColor, options.borderLineColor);
                options.borderCornerThickness = ta.getDimension(
                        R.styleable.CropImageView_cropBorderCornerThickness, options.borderCornerThickness);
                options.borderCornerOffset = ta.getDimension(R.styleable.CropImageView_cropBorderCornerOffset, options.borderCornerOffset);
                options.borderCornerLength = ta.getDimension(R.styleable.CropImageView_cropBorderCornerLength, options.borderCornerLength);
                options.borderCornerColor = ta.getInteger(R.styleable.CropImageView_cropBorderCornerColor, options.borderCornerColor);
                options.guidelinesThickness = ta.getDimension(R.styleable.CropImageView_cropGuidelinesThickness, options.guidelinesThickness);
                options.guidelinesColor = ta.getInteger(R.styleable.CropImageView_cropGuidelinesColor, options.guidelinesColor);
                options.backgroundColor = ta.getInteger(R.styleable.CropImageView_cropBackgroundColor, options.backgroundColor);
                options.showCropOverlay = ta.getBoolean(R.styleable.CropImageView_cropShowCropOverlay, mShowCropOverlay);
                options.showProgressBar = ta.getBoolean(R.styleable.CropImageView_cropShowProgressBar, mShowProgressBar);
                options.borderCornerThickness = ta.getDimension(
                        R.styleable.CropImageView_cropBorderCornerThickness, options.borderCornerThickness);
                options.minCropWindowWidth = (int) ta.getDimension(
                        R.styleable.CropImageView_cropMinCropWindowWidth, options.minCropWindowWidth);
                options.minCropWindowHeight = (int) ta.getDimension(
                        R.styleable.CropImageView_cropMinCropWindowHeight, options.minCropWindowHeight);
                options.minCropResultWidth = (int) ta.getFloat(R.styleable.CropImageView_cropMinCropResultWidthPX, options.minCropResultWidth);
                options.minCropResultHeight = (int) ta.getFloat(
                        R.styleable.CropImageView_cropMinCropResultHeightPX, options.minCropResultHeight);
                options.maxCropResultWidth = (int) ta.getFloat(R.styleable.CropImageView_cropMaxCropResultWidthPX, options.maxCropResultWidth);
                options.maxCropResultHeight = (int) ta.getFloat(
                        R.styleable.CropImageView_cropMaxCropResultHeightPX, options.maxCropResultHeight);

                // if aspect ratio is set then set fixed to true
                if (ta.hasValue(R.styleable.CropImageView_cropAspectRatioX) &&
                        ta.hasValue(R.styleable.CropImageView_cropAspectRatioX) &&
                        !ta.hasValue(R.styleable.CropImageView_cropFixAspectRatio)) {
                    options.fixAspectRatio = true;
                }
            } finally {
                ta.recycle();
            }
        }
        options.validate();

        mScaleType = options.scaleType;
        mAutoZoomEnabled = options.autoZoomEnabled;
        mMaxZoom = options.maxZoom;
        mShowCropOverlay = options.showCropOverlay;
        mShowProgressBar = options.showProgressBar;

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.crop_image_view, this, true);

        mImageView = v.findViewById(R.id.ImageView_image);
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);

        mCropOverlayView = v.findViewById(R.id.CropOverlayView);
        mCropOverlayView.setCropWindowChangeListener(new CropOverlayView.CropWindowChangeListener() {
            @Override
            public void onCropWindowChanged(boolean inProgress) {
                handleCropWindowChanged(inProgress, true);
            }
        });
        mCropOverlayView.setInitialAttributeValues(options);

        mProgressBar = v.findViewById(R.id.CropProgressBar);
        setProgressBarVisibility();
    }

    public void setCropImageOptions(CropImageOptions options) {
        options.validate();
        mScaleType = options.scaleType;
        mAutoZoomEnabled = options.autoZoomEnabled;
        mMaxZoom = options.maxZoom;
        mShowCropOverlay = options.showCropOverlay;
        mShowProgressBar = options.showProgressBar;
        mCropOverlayView.setInitialAttributeValues(options);
    }

    /**
     * Get the scale type of the image in the crop view.
     */
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * Set the scale type of the image in the crop view
     */
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != mScaleType) {
            mScaleType = scaleType;
            mZoom = 1;
            mZoomOffsetX = mZoomOffsetY = 0;
            mCropOverlayView.resetCropOverlayView();
            requestLayout();
        }
    }

    /**
     * The shape of the cropping area - rectangle/circular.
     */
    public CropShape getCropShape() {
        return mCropOverlayView.getCropShape();
    }

    /**
     * The shape of the cropping area - rectangle/circular.<br>
     * To set square/circle crop shape set aspect ratio to 1:1.
     */
    public void setCropShape(CropShape cropShape) {
        mCropOverlayView.setCropShape(cropShape);
    }

    /**
     * if auto-zoom functionality is enabled. default: true.
     */
    public boolean isAutoZoomEnabled() {
        return mAutoZoomEnabled;
    }

    /**
     * Set auto-zoom functionality to enabled/disabled.
     */
    public void setAutoZoomEnabled(boolean autoZoomEnabled) {
        if (mAutoZoomEnabled != autoZoomEnabled) {
            mAutoZoomEnabled = autoZoomEnabled;
            handleCropWindowChanged(false, false);
            mCropOverlayView.invalidate();
        }
    }

    /**
     * Set multi touch functionality to enabled/disabled.
     */
    public void setMultiTouchEnabled(boolean multiTouchEnabled) {
        if (mCropOverlayView.setMultiTouchEnabled(multiTouchEnabled)) {
            handleCropWindowChanged(false, false);
            mCropOverlayView.invalidate();
        }
    }

    /**
     * The max zoom allowed during cropping.
     */
    public int getMaxZoom() {
        return mMaxZoom;
    }

    /**
     * The max zoom allowed during cropping.
     */
    public void setMaxZoom(int maxZoom) {
        if (mMaxZoom != maxZoom && maxZoom > 0) {
            mMaxZoom = maxZoom;
            handleCropWindowChanged(false, false);
            mCropOverlayView.invalidate();
        }
    }

    /**
     * the min size the resulting cropping image is allowed to be, affects the cropping window limits
     * (in pixels).<br>
     */
    public void setMinCropResultSize(int minCropResultWidth, int minCropResultHeight) {
        mCropOverlayView.setMinCropResultSize(minCropResultWidth, minCropResultHeight);

    }

    /**
     * the max size the resulting cropping image is allowed to be, affects the cropping window limits
     * (in pixels).<br>
     */
    public void setMaxCropResultSize(int maxCropResultWidth, int maxCropResultHeight) {
        mCropOverlayView.setMaxCropResultSize(maxCropResultWidth, maxCropResultHeight);
    }

    /**
     * Get the amount of degrees the cropping image is rotated cloackwise.<br>
     *
     * @return 0-360
     */
    public int getRotatedDegrees() {
        return mDegreesRotated;
    }

    /**
     * Set the amount of degrees the cropping image is rotated cloackwise.<br>
     *
     * @param degrees 0-360
     */
    public void setRotatedDegrees(int degrees) {
        if (mDegreesRotated != degrees) {
            rotateImage(degrees - mDegreesRotated);
        }
    }

    /**
     * whether the aspect ratio is fixed or not; true fixes the aspect ratio, while false allows it to be changed.
     */
    public boolean isFixAspectRatio() {
        return mCropOverlayView.isFixAspectRatio();
    }

    /**
     * Sets whether the aspect ratio is fixed or not; true fixes the aspect ratio, while false allows it to be changed.
     */
    public void setFixedAspectRatio(boolean fixAspectRatio) {
        mCropOverlayView.setFixedAspectRatio(fixAspectRatio);
    }

    /**
     * Get the current guidelines option set.
     */
    public Guidelines getGuidelines() {
        return mCropOverlayView.getGuidelines();
    }

    /**
     * Sets the guidelines for the CropOverlayView to be either on, off, or to show when resizing the application.
     */
    public void setGuidelines(Guidelines guidelines) {
        mCropOverlayView.setGuidelines(guidelines);
    }

    /**
     * both the X and Y values of the aspectRatio.
     */
    public Pair<Integer, Integer> getAspectRatio() {
        return new Pair<>(mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY());
    }

    /**
     * Sets the both the X and Y values of the aspectRatio.<br>
     * Sets fixed aspect ratio to TRUE.
     *
     * @param aspectRatioX int that specifies the new X value of the aspect ratio
     * @param aspectRatioY int that specifies the new Y value of the aspect ratio
     */
    public void setAspectRatio(int aspectRatioX, int aspectRatioY) {
        mCropOverlayView.setAspectRatioX(aspectRatioX);
        mCropOverlayView.setAspectRatioY(aspectRatioY);
        setFixedAspectRatio(true);
    }

    /**
     * Clears set aspect ratio values and sets fixed aspect ratio to FALSE.
     */
    public void clearAspectRatio() {
        mCropOverlayView.setAspectRatioX(1);
        mCropOverlayView.setAspectRatioY(1);
        setFixedAspectRatio(false);
    }

    /**
     * An edge of the crop window will snap to the corresponding edge of a
     * specified bounding box when the crop window edge is less than or equal to
     * this distance (in pixels) away from the bounding box edge. (default: 3dp)
     */
    public void setSnapRadius(float snapRadius) {
        if (snapRadius >= 0) {
            mCropOverlayView.setSnapRadius(snapRadius);
        }
    }

    /**
     * if to show progress bar when image async loading/cropping is in progress.<br>
     * default: true, disable to provide custom progress bar UI.
     */
    public boolean isShowProgressBar() {
        return mShowProgressBar;
    }

    /**
     * if to show progress bar when image async loading/cropping is in progress.<br>
     * default: true, disable to provide custom progress bar UI.
     */
    public void setShowProgressBar(boolean showProgressBar) {
        if (mShowProgressBar != showProgressBar) {
            mShowProgressBar = showProgressBar;
            setProgressBarVisibility();
        }
    }

    /**
     * if to show crop overlay UI what contains the crop window UI surrounded by background over the cropping
     * image.<br>
     * default: true, may disable for animation or frame transition.
     */
    public boolean isShowCropOverlay() {
        return mShowCropOverlay;
    }

    /**
     * if to show crop overlay UI what contains the crop window UI surrounded by background over the cropping
     * image.<br>
     * default: true, may disable for animation or frame transition.
     */
    public void setShowCropOverlay(boolean showCropOverlay) {
        if (mShowCropOverlay != showCropOverlay) {
            mShowCropOverlay = showCropOverlay;
            setCropOverlayVisibility();
        }
    }

    /**
     * Returns the integer of the imageResource
     */
    public int getImageResource() {
        return mImageResource;
    }

    /**
     * Get the URI of an image that was set by URI, null otherwise.
     */
    public Uri getImageUri() {
        return mLoadedImageUri;
    }

    /**
     * Gets the crop window's position relative to the source Bitmap (not the image
     * displayed in the CropImageView) using the original image rotation.
     *
     * @return a Rect instance containing cropped area boundaries of the source Bitmap
     */
    public Rect getCropRect() {
        if (mBitmap != null) {

            // get the points of the crop rectangle adjusted to source bitmap
            float[] points = getCropPoints();

            int orgWidth = mBitmap.getWidth() * mLoadedSampleSize;
            int orgHeight = mBitmap.getHeight() * mLoadedSampleSize;

            // get the rectangle for the points (it may be larger than original if rotation is not stright)
            return BitmapUtils.getRectFromPoints(points, orgWidth, orgHeight,
                    mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY());
        } else {
            return null;
        }
    }

    /**
     * Gets the 4 points of crop window's position relative to the source Bitmap (not the image
     * displayed in the CropImageView) using the original image rotation.<br>
     * Note: the 4 points may not be a rectangle if the image was rotates to NOT stright angle (!= 90/180/270).
     *
     * @return 4 points (x0,y0,x1,y1,x2,y2,x3,y3) of cropped area boundaries
     */
    public float[] getCropPoints() {

        // Get crop window position relative to the displayed image.
        RectF cropWindowRect = mCropOverlayView.getCropWindowRect();

        float[] points = new float[]{
                cropWindowRect.left,
                cropWindowRect.top,
                cropWindowRect.right,
                cropWindowRect.top,
                cropWindowRect.right,
                cropWindowRect.bottom,
                cropWindowRect.left,
                cropWindowRect.bottom
        };

        mImageMatrix.invert(mImageInverseMatrix);
        mImageInverseMatrix.mapPoints(points);

        for (int i = 0; i < points.length; i++) {
            points[i] *= mLoadedSampleSize;
        }

        return points;
    }

    /**
     * Set the crop window position and size to the given rectangle.<br>
     * Image to crop must be first set before invoking this, for async - after complete callback.
     *
     * @param rect window rectangle (position and size) relative to source bitmap
     */
    public void setCropRect(Rect rect) {
        mCropOverlayView.setInitialCropWindowRect(rect);
    }

    /**
     * Reset crop window to initial rectangle.
     */
    public void resetCropRect() {
        mZoom = 1;
        mZoomOffsetX = 0;
        mZoomOffsetY = 0;
        mDegreesRotated = 0;
        applyImageMatrix(getWidth(), getHeight(), false, false);
        mCropOverlayView.resetCropWindowRect();
    }

    /**
     * Gets the cropped image based on the current crop window.
     *
     * @return a new Bitmap representing the cropped image
     */
    public Bitmap getCroppedImage() {
        return getCroppedImage(0, 0, RequestSizeOptions.NONE);
    }

    /**
     * Gets the cropped image based on the current crop window.<br>
     * Uses {@link RequestSizeOptions#RESIZE_INSIDE} option.
     *
     * @param reqWidth  the width to resize the cropped image to
     * @param reqHeight the height to resize the cropped image to
     * @return a new Bitmap representing the cropped image
     */
    public Bitmap getCroppedImage(int reqWidth, int reqHeight) {
        return getCroppedImage(reqWidth, reqHeight, RequestSizeOptions.RESIZE_INSIDE);
    }

    /**
     * Gets the cropped image based on the current crop window.<br>
     *
     * @param reqWidth  the width to resize the cropped image to (see options)
     * @param reqHeight the height to resize the cropped image to (see options)
     * @param options   the resize method to use, see its documentation
     * @return a new Bitmap representing the cropped image
     */
    public Bitmap getCroppedImage(int reqWidth, int reqHeight, RequestSizeOptions options) {
        Bitmap croppedBitmap = null;
        if (mBitmap != null) {
            mImageView.clearAnimation();

            reqWidth = options != RequestSizeOptions.NONE ? reqWidth : 0;
            reqHeight = options != RequestSizeOptions.NONE ? reqHeight : 0;

            if (mLoadedImageUri != null && (mLoadedSampleSize > 1 || options == RequestSizeOptions.SAMPLING)) {
                int orgWidth = mBitmap.getWidth() * mLoadedSampleSize;
                int orgHeight = mBitmap.getHeight() * mLoadedSampleSize;
                BitmapUtils.BitmapSampled bitmapSampled =
                        BitmapUtils.cropBitmap(getContext(), mLoadedImageUri, getCropPoints(),
                                mDegreesRotated, orgWidth, orgHeight,
                                mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(),
                                reqWidth, reqHeight);
                croppedBitmap = bitmapSampled.bitmap;
            } else {
                croppedBitmap = BitmapUtils.cropBitmapObjectHandleOOM(mBitmap, getCropPoints(), mDegreesRotated,
                        mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY()).bitmap;
            }

            croppedBitmap = BitmapUtils.resizeBitmap(croppedBitmap, reqWidth, reqHeight, options);
        }

        return croppedBitmap;
    }

    /**
     * Gets the cropped image based on the current crop window.<br>
     */
    public void getCroppedImageAsync() {
        getCroppedImageAsync(0, 0, RequestSizeOptions.NONE);
    }

    /**
     * Gets the cropped image based on the current crop window.<br>
     * Uses {@link RequestSizeOptions#RESIZE_INSIDE} option.<br>
     * The result will be invoked to listener set by {@link #setOnCropImageCompleteListener(OnCropImageCompleteListener)}.
     *
     * @param reqWidth  the width to resize the cropped image to
     * @param reqHeight the height to resize the cropped image to
     */
    public void getCroppedImageAsync(int reqWidth, int reqHeight) {
        getCroppedImageAsync(reqWidth, reqHeight, RequestSizeOptions.RESIZE_INSIDE);
    }

    /**
     * Gets the cropped image based on the current crop window.<br>
     * The result will be invoked to listener set by {@link #setOnCropImageCompleteListener(OnCropImageCompleteListener)}.
     *
     * @param reqWidth  the width to resize the cropped image to (see options)
     * @param reqHeight the height to resize the cropped image to (see options)
     * @param options   the resize method to use, see its documentation
     */
    public void getCroppedImageAsync(int reqWidth, int reqHeight, RequestSizeOptions options) {
        if (mOnCropImageCompleteListener == null) {
            throw new IllegalArgumentException("mOnCropImageCompleteListener is not set");
        }
        startCropWorkerTask(reqWidth, reqHeight, options, null, null, 0);
    }

    /**
     * Save the cropped image based on the current crop window to the given uri.<br>
     * Uses JPEG image compression with 90 compression quality.<br>
     *
     * @param saveUri the Android Uri to save the cropped image to
     */
    public void saveCroppedImageAsync(Uri saveUri) {
        saveCroppedImageAsync(saveUri, Bitmap.CompressFormat.JPEG, 90, 0, 0, RequestSizeOptions.NONE);
    }

    /**
     * Save the cropped image based on the current crop window to the given uri.<br>
     *
     * @param saveUri             the Android Uri to save the cropped image to
     * @param saveCompressFormat  the compression format to use when writing the image
     * @param saveCompressQuality the quality (if applicable) to use when writing the image (0 - 100)
     */
    public void saveCroppedImageAsync(Uri saveUri, Bitmap.CompressFormat saveCompressFormat, int saveCompressQuality) {
        saveCroppedImageAsync(saveUri, saveCompressFormat, saveCompressQuality, 0, 0, RequestSizeOptions.NONE);
    }

    /**
     * Save the cropped image based on the current crop window to the given uri.<br>
     * Uses {@link RequestSizeOptions#RESIZE_INSIDE} option.<br>
     *
     * @param saveUri             the Android Uri to save the cropped image to
     * @param saveCompressFormat  the compression format to use when writing the image
     * @param saveCompressQuality the quality (if applicable) to use when writing the image (0 - 100)
     * @param reqWidth            the width to resize the cropped image to
     * @param reqHeight           the height to resize the cropped image to
     */
    public void saveCroppedImageAsync(Uri saveUri, Bitmap.CompressFormat saveCompressFormat, int saveCompressQuality, int reqWidth, int reqHeight) {
        saveCroppedImageAsync(saveUri, saveCompressFormat, saveCompressQuality, reqWidth, reqHeight, RequestSizeOptions.RESIZE_INSIDE);
    }

    /**
     * Save the cropped image based on the current crop window to the given uri.<br>
     *
     * @param saveUri             the Android Uri to save the cropped image to
     * @param saveCompressFormat  the compression format to use when writing the image
     * @param saveCompressQuality the quality (if applicable) to use when writing the image (0 - 100)
     * @param reqWidth            the width to resize the cropped image to (see options)
     * @param reqHeight           the height to resize the cropped image to (see options)
     * @param options             the resize method to use, see its documentation
     */
    public void saveCroppedImageAsync(Uri saveUri, Bitmap.CompressFormat saveCompressFormat, int saveCompressQuality, int reqWidth, int reqHeight, RequestSizeOptions options) {
        if (mOnCropImageCompleteListener == null) {
            throw new IllegalArgumentException("mOnCropImageCompleteListener is not set");
        }
        startCropWorkerTask(reqWidth, reqHeight, options, saveUri, saveCompressFormat, saveCompressQuality);
    }

    /**
     * Set the callback to be invoked when image async loading ({@link #setImageUriAsync(Uri)})
     * is complete (successful or failed).
     */
    public void setOnSetImageUriCompleteListener(OnSetImageUriCompleteListener listener) {
        mOnSetImageUriCompleteListener = listener;
    }

    /**
     * Set the callback to be invoked when image async cropping image ({@link #getCroppedImageAsync()} or
     * {@link #saveCroppedImageAsync(Uri)}) is complete (successful or failed).
     */
    public void setOnCropImageCompleteListener(OnCropImageCompleteListener listener) {
        mOnCropImageCompleteListener = listener;
    }

    /**
     * Sets a Bitmap as the content of the CropImageView.
     *
     * @param bitmap the Bitmap to set
     */
    public void setImageBitmap(Bitmap bitmap) {
        mCropOverlayView.setInitialCropWindowRect(null);
        setBitmap(bitmap);
    }

    /**
     * Sets a Bitmap and initializes the image rotation according to the EXIT data.<br>
     * <br>
     * The EXIF can be retrieved by doing the following:
     * <code>ExifInterface exif = new ExifInterface(path);</code>
     *
     * @param bitmap the original bitmap to set; if null, this
     * @param exif   the EXIF information about this bitmap; may be null
     */
    public void setImageBitmap(Bitmap bitmap, ExifInterface exif) {
        Bitmap setBitmap;
        if (bitmap != null && exif != null) {
            BitmapUtils.RotateBitmapResult result = BitmapUtils.rotateBitmapByExif(bitmap, exif);
            setBitmap = result.bitmap;
            mDegreesRotated = result.degrees;
        } else {
            setBitmap = bitmap;
        }
        mCropOverlayView.setInitialCropWindowRect(null);
        setBitmap(setBitmap);
    }

    /**
     * Sets a Drawable as the content of the CropImageView.
     *
     * @param resId the drawable resource ID to set
     */
    public void setImageResource(int resId) {
        if (resId != 0) {
            mCropOverlayView.setInitialCropWindowRect(null);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            setBitmap(bitmap, resId);
        }
    }

    /**
     * Sets a bitmap loaded from the given Android URI as the content of the CropImageView.<br>
     * Can be used with URI from gallery or camera source.<br>
     * Will rotate the image by exif data.<br>
     *
     * @param uri the URI to load the image from
     */
    public void setImageUriAsync(Uri uri) {
        if (uri != null) {
            BitmapLoadingTask task = mBitmapLoadingWorkerTask != null ? mBitmapLoadingWorkerTask.get() : null;
            if (task != null) {
                // cancel previous loading (no check if the same URI because camera URI can be the same for different images)
                task.cancel(true);
            }

            // either no existing task is working or we canceled it, need to load new URI
            clearImageInt();
            mCropOverlayView.setInitialCropWindowRect(null);
            mBitmapLoadingWorkerTask = new WeakReference<>(new BitmapLoadingTask(this, uri));
            mBitmapLoadingWorkerTask.get().execute();
            setProgressBarVisibility();
        }
    }

    /**
     * Clear the current image set for cropping.
     */
    public void clearImage() {
        clearImageInt();
        mCropOverlayView.setInitialCropWindowRect(null);
    }

    /**
     * Rotates image by the specified number of degrees clockwise.<br>
     * Negative values represent counter-clockwise rotations.
     *
     * @param degrees Integer specifying the number of degrees to rotate.
     */
    public void rotateImage(int degrees) {
        if (mBitmap != null) {
            // Force degrees to be a non-zero value between 0 and 360 (inclusive)
            if (degrees < 0) {
                degrees = (degrees % 360) + 360;
            } else {
                degrees = degrees % 360;
            }

            boolean flipAxes = !mCropOverlayView.isFixAspectRatio() && ((degrees > 45 && degrees < 135) || (degrees > 215 && degrees < 305));
            BitmapUtils.RECT.set(mCropOverlayView.getCropWindowRect());
            float halfWidth = (flipAxes ? BitmapUtils.RECT.height() : BitmapUtils.RECT.width()) / 2f;
            float halfHeight = (flipAxes ? BitmapUtils.RECT.width() : BitmapUtils.RECT.height()) / 2f;

            mImageMatrix.invert(mImageInverseMatrix);

            BitmapUtils.POINTS[0] = BitmapUtils.RECT.centerX();
            BitmapUtils.POINTS[1] = BitmapUtils.RECT.centerY();
            BitmapUtils.POINTS[2] = 0;
            BitmapUtils.POINTS[3] = 0;
            BitmapUtils.POINTS[4] = 1;
            BitmapUtils.POINTS[5] = 0;
            mImageInverseMatrix.mapPoints(BitmapUtils.POINTS);

            // This is valid because degrees is not negative.
            mDegreesRotated = (mDegreesRotated + degrees) % 360;

            applyImageMatrix(getWidth(), getHeight(), true, false);

            // adjust the zoom so the crop window size remains the same even after image scale change
            mImageMatrix.mapPoints(BitmapUtils.POINTS2, BitmapUtils.POINTS);
            mZoom /= Math.sqrt(Math.pow(BitmapUtils.POINTS2[4] - BitmapUtils.POINTS2[2], 2) + Math.pow(
                    BitmapUtils.POINTS2[5] - BitmapUtils.POINTS2[3], 2));
            mZoom = Math.max(mZoom, 1);

            applyImageMatrix(getWidth(), getHeight(), true, false);

            mImageMatrix.mapPoints(BitmapUtils.POINTS2, BitmapUtils.POINTS);

            // adjust the width/height by the changes in scaling to the image
            double change = Math.sqrt(
                    Math.pow(BitmapUtils.POINTS2[4] - BitmapUtils.POINTS2[2], 2) + Math.pow(
                            BitmapUtils.POINTS2[5] - BitmapUtils.POINTS2[3], 2));
            halfWidth *= change;
            halfHeight *= change;

            // calculate the new crop window rectangle to center in the same location and have proper width/height
            BitmapUtils.RECT.set(BitmapUtils.POINTS2[0] - halfWidth, BitmapUtils.POINTS2[1] - halfHeight,
                    BitmapUtils.POINTS2[0] + halfWidth, BitmapUtils.POINTS2[1] + halfHeight);

            mCropOverlayView.resetCropOverlayView();
            mCropOverlayView.setCropWindowRect(BitmapUtils.RECT);
            applyImageMatrix(getWidth(), getHeight(), true, false);
            handleCropWindowChanged(false, false);

            // make sure the crop window rectangle is within the cropping image bounds after all the changes
            mCropOverlayView.fixCurrentCropWindowRect();
        }
    }

    //region: Private methods

    /**
     * On complete of the async bitmap loading by {@link #setImageUriAsync(Uri)} set the result
     * to the widget if still relevant and call listener if set.
     *
     * @param result the result of bitmap loading
     */
    void onSetImageUriAsyncComplete(BitmapLoadingTask.Result result) {

        mBitmapLoadingWorkerTask = null;
        setProgressBarVisibility();

        if (result.error == null) {
            setBitmap(result.bitmap, result.uri, result.loadSampleSize, result.degreesRotated);
        }

        OnSetImageUriCompleteListener listener = mOnSetImageUriCompleteListener;
        if (listener != null) {
            listener.onSetImageUriComplete(this, result.uri, result.error);
        }
    }

    /**
     * On complete of the async bitmap cropping by {@link #getCroppedImageAsync()} call listener if set.
     *
     * @param result the result of bitmap cropping
     */
    void onImageCroppingAsyncComplete(BitmapCroppingTask.Result result) {

        mBitmapCroppingWorkerTask = null;
        setProgressBarVisibility();

        OnCropImageCompleteListener listener = mOnCropImageCompleteListener;
        if (listener != null) {
            CropResult cropResult = new CropResult(result.bitmap, result.uri, result.error, getCropPoints(), getCropRect(), getRotatedDegrees(), result.sampleSize);
            listener.onCropImageComplete(this, cropResult);
        }
    }

    /**
     * {@link #setBitmap(Bitmap, int, Uri, int, int)}}
     */
    private void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, 0, null, 1, 0);
    }

    /**
     * {@link #setBitmap(Bitmap, int, Uri, int, int)}}
     */
    private void setBitmap(Bitmap bitmap, int imageResource) {
        setBitmap(bitmap, imageResource, null, 1, 0);
    }

    /**
     * {@link #setBitmap(Bitmap, int, Uri, int, int)}}
     */
    private void setBitmap(Bitmap bitmap, Uri imageUri, int loadSampleSize, int degreesRotated) {
        setBitmap(bitmap, 0, imageUri, loadSampleSize, degreesRotated);
    }

    /**
     * Set the given bitmap to be used in for cropping<br>
     * Optionally clear full if the bitmap is new, or partial clear if the bitmap has been manipulated.
     */
    private void setBitmap(Bitmap bitmap, int imageResource, Uri imageUri, int loadSampleSize, int degreesRotated) {
        if (mBitmap == null || !mBitmap.equals(bitmap)) {

            mImageView.clearAnimation();

            clearImageInt();

            mBitmap = bitmap;
            mImageView.setImageBitmap(mBitmap);

            mLoadedImageUri = imageUri;
            mImageResource = imageResource;
            mLoadedSampleSize = loadSampleSize;
            mDegreesRotated = degreesRotated;

            applyImageMatrix(getWidth(), getHeight(), true, false);

            if (mCropOverlayView != null) {
                mCropOverlayView.resetCropOverlayView();
                setCropOverlayVisibility();
            }
        }
    }

    /**
     * Clear the current image set for cropping.<br>
     * Full clear will also clear the data of the set image like Uri or Resource id while partial clear
     * will only clear the bitmap and recycle if required.
     */
    private void clearImageInt() {

        // if we allocated the bitmap, release it as fast as possible
        if (mBitmap != null && (mImageResource > 0 || mLoadedImageUri != null)) {
            mBitmap.recycle();
        }
        mBitmap = null;

        // clean the loaded image flags for new image
        mImageResource = 0;
        mLoadedImageUri = null;
        mLoadedSampleSize = 1;
        mDegreesRotated = 0;
        mZoom = 1;
        mZoomOffsetX = 0;
        mZoomOffsetY = 0;
        mImageMatrix.reset();

        mImageView.setImageBitmap(null);

        setCropOverlayVisibility();
    }

    /**
     * Gets the cropped image based on the current crop window.<br>
     * If (reqWidth,reqHeight) is given AND image is loaded from URI cropping will try to use sample size to fit in
     * the requested width and height down-sampling if possible - optimization to get best size to quality.<br>
     *
     * @param reqWidth            the width to resize the cropped image to (see options)
     * @param reqHeight           the height to resize the cropped image to (see options)
     * @param options             the resize method to use on the cropped bitmap
     * @param saveUri             optional: to save the cropped image to
     * @param saveCompressFormat  if saveUri is given, the given compression will be used for saving the image
     * @param saveCompressQuality if saveUri is given, the given quality will be used for the compression.
     */
    public void startCropWorkerTask(int reqWidth, int reqHeight, RequestSizeOptions options, Uri saveUri, Bitmap.CompressFormat saveCompressFormat, int saveCompressQuality) {
        if (mBitmap != null) {
            mImageView.clearAnimation();

            BitmapCroppingTask
                    currentTask = mBitmapCroppingWorkerTask != null ? mBitmapCroppingWorkerTask.get() : null;
            if (currentTask != null) {
                // cancel previous cropping
                currentTask.cancel(true);
            }

            reqWidth = options != RequestSizeOptions.NONE ? reqWidth : 0;
            reqHeight = options != RequestSizeOptions.NONE ? reqHeight : 0;

            int orgWidth = mBitmap.getWidth() * mLoadedSampleSize;
            int orgHeight = mBitmap.getHeight() * mLoadedSampleSize;
            if (mLoadedImageUri != null && (mLoadedSampleSize > 1 || options == RequestSizeOptions.SAMPLING)) {
                mBitmapCroppingWorkerTask = new WeakReference<>(new BitmapCroppingTask(this, mLoadedImageUri, getCropPoints(),
                        mDegreesRotated, orgWidth, orgHeight,
                        mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(),
                        reqWidth, reqHeight, options,
                        saveUri, saveCompressFormat, saveCompressQuality));
            } else {
                mBitmapCroppingWorkerTask = new WeakReference<>(new BitmapCroppingTask(this, mBitmap, getCropPoints(), mDegreesRotated,
                        mCropOverlayView.isFixAspectRatio(), mCropOverlayView.getAspectRatioX(), mCropOverlayView.getAspectRatioY(),
                        reqWidth, reqHeight, options,
                        saveUri, saveCompressFormat, saveCompressQuality));
            }
            mBitmapCroppingWorkerTask.get().execute();
            setProgressBarVisibility();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putParcelable("LOADED_IMAGE_URI", mLoadedImageUri);
        bundle.putInt("LOADED_IMAGE_RESOURCE", mImageResource);
        if (mLoadedImageUri == null && mImageResource < 1) {
            bundle.putParcelable("SET_BITMAP", mBitmap);
        }
        if (mLoadedImageUri != null && mBitmap != null) {
            String key = UUID.randomUUID().toString();
            BitmapUtils.mStateBitmap = new Pair<>(key, new WeakReference<>(mBitmap));
            bundle.putString("LOADED_IMAGE_STATE_BITMAP_KEY", key);
        }
        if (mBitmapLoadingWorkerTask != null) {
            BitmapLoadingTask task = mBitmapLoadingWorkerTask.get();
            if (task != null) {
                bundle.putParcelable("LOADING_IMAGE_URI", task.getUri());
            }
        }
        bundle.putInt("LOADED_SAMPLE_SIZE", mLoadedSampleSize);
        bundle.putInt("DEGREES_ROTATED", mDegreesRotated);
        bundle.putParcelable("INITIAL_CROP_RECT", mCropOverlayView.getInitialCropWindowRect());

        BitmapUtils.RECT.set(mCropOverlayView.getCropWindowRect());

        mImageMatrix.invert(mImageInverseMatrix);
        mImageInverseMatrix.mapRect(BitmapUtils.RECT);

        bundle.putParcelable("CROP_WINDOW_RECT", BitmapUtils.RECT);
        bundle.putString("CROP_SHAPE", mCropOverlayView.getCropShape().name());
        bundle.putBoolean("CROP_AUTO_ZOOM_ENABLED", mAutoZoomEnabled);
        bundle.putInt("CROP_MAX_ZOOM", mMaxZoom);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            // prevent restoring state if already set by outside code
            if (mBitmapLoadingWorkerTask == null && mLoadedImageUri == null && mBitmap == null && mImageResource == 0) {

                Uri uri = bundle.getParcelable("LOADED_IMAGE_URI");
                if (uri != null) {
                    String key = bundle.getString("LOADED_IMAGE_STATE_BITMAP_KEY");
                    if (key != null) {
                        Bitmap stateBitmap = BitmapUtils.mStateBitmap != null && BitmapUtils.mStateBitmap.first.equals(key)
                                ? BitmapUtils.mStateBitmap.second.get() : null;
                        if (stateBitmap != null && !stateBitmap.isRecycled()) {
                            BitmapUtils.mStateBitmap = null;
                            setBitmap(stateBitmap, uri, bundle.getInt("LOADED_SAMPLE_SIZE"), 0);
                        }
                    }
                    if (mLoadedImageUri == null) {
                        setImageUriAsync(uri);
                    }
                } else {
                    int resId = bundle.getInt("LOADED_IMAGE_RESOURCE");
                    if (resId > 0) {
                        setImageResource(resId);
                    } else {
                        Bitmap bitmap = bundle.getParcelable("SET_BITMAP");
                        if (bitmap != null) {
                            setBitmap(bitmap);
                        } else {
                            uri = bundle.getParcelable("LOADING_IMAGE_URI");
                            if (uri != null) {
                                setImageUriAsync(uri);
                            }
                        }
                    }
                }

                mDegreesRotated = bundle.getInt("DEGREES_ROTATED");

                mCropOverlayView.setInitialCropWindowRect(bundle.getParcelable("INITIAL_CROP_RECT"));

                mRestoreCropWindowRect = bundle.getParcelable("CROP_WINDOW_RECT");

                mCropOverlayView.setCropShape(CropShape.valueOf(bundle.getString("CROP_SHAPE")));

                mAutoZoomEnabled = bundle.getBoolean("CROP_AUTO_ZOOM_ENABLED");
                mMaxZoom = bundle.getInt("CROP_MAX_ZOOM");
            }

            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mBitmap != null) {

            // Bypasses a baffling bug when used within a ScrollView, where heightSize is set to 0.
            if (heightSize == 0) {
                heightSize = mBitmap.getHeight();
            }

            int desiredWidth;
            int desiredHeight;

            double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
            double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;

            // Checks if either width or height needs to be fixed
            if (widthSize < mBitmap.getWidth()) {
                viewToBitmapWidthRatio = (double) widthSize / (double) mBitmap.getWidth();
            }
            if (heightSize < mBitmap.getHeight()) {
                viewToBitmapHeightRatio = (double) heightSize / (double) mBitmap.getHeight();
            }

            // If either needs to be fixed, choose smallest ratio and calculate from there
            if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY) {
                if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                    desiredWidth = widthSize;
                    desiredHeight = (int) (mBitmap.getHeight() * viewToBitmapWidthRatio);
                } else {
                    desiredHeight = heightSize;
                    desiredWidth = (int) (mBitmap.getWidth() * viewToBitmapHeightRatio);
                }
            } else {
                // Otherwise, the picture is within frame layout bounds. Desired width is simply picture size
                desiredWidth = mBitmap.getWidth();
                desiredHeight = mBitmap.getHeight();
            }

            int width = getOnMeasureSpec(widthMode, widthSize, desiredWidth);
            int height = getOnMeasureSpec(heightMode, heightSize, desiredHeight);

            mLayoutWidth = width;
            mLayoutHeight = height;

            setMeasuredDimension(mLayoutWidth, mLayoutHeight);

        } else {
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        if (mLayoutWidth > 0 && mLayoutHeight > 0) {
            // Gets original parameters, and creates the new parameters
            ViewGroup.LayoutParams origParams = this.getLayoutParams();
            origParams.width = mLayoutWidth;
            origParams.height = mLayoutHeight;
            setLayoutParams(origParams);

            if (mBitmap != null) {
                applyImageMatrix(r - l, b - t, true, false);

                // after state restore we want to restore the window crop, possible only after widget size is known
                if (mRestoreCropWindowRect != null) {
                    mImageMatrix.mapRect(mRestoreCropWindowRect);
                    mCropOverlayView.setCropWindowRect(mRestoreCropWindowRect);
                    handleCropWindowChanged(false, false);
                    mCropOverlayView.fixCurrentCropWindowRect();
                    mRestoreCropWindowRect = null;
                } else if (mSizeChanged) {
                    mSizeChanged = false;
                    handleCropWindowChanged(false, false);
                }
            } else {
                updateImageBounds(true);
            }
        } else {
            updateImageBounds(true);
        }
    }

    /**
     * Detect size change to handle auto-zoom using {@link #handleCropWindowChanged(boolean, boolean)} in
     * {@link #layout(int, int, int, int)}.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSizeChanged = oldw > 0 && oldh > 0;
    }

    /**
     * Handle crop window change to:<br>
     * 1. Execute auto-zoom-in/out depending on the area covered of cropping window relative to the
     * available view area.<br>
     * 2. Slide the zoomed sub-area if the cropping window is outside of the visible view sub-area.<br>
     *
     * @param inProgress is the crop window change is still in progress by the user
     * @param animate    if to animate the change to the image matrix, or set it directly
     */
    private void handleCropWindowChanged(boolean inProgress, boolean animate) {
        int width = getWidth();
        int height = getHeight();
        if (mBitmap != null && width > 0 && height > 0) {

            RectF cropRect = mCropOverlayView.getCropWindowRect();
            if (inProgress) {
                if (cropRect.left < 0 || cropRect.top < 0 || cropRect.right > width || cropRect.bottom > height) {
                    applyImageMatrix(width, height, false, false);
                }
            } else if (mAutoZoomEnabled || mZoom > 1) {
                float newZoom = 0;
                // keep the cropping window covered area to 50%-65% of zoomed sub-area
                if (mZoom < mMaxZoom && cropRect.width() < width * 0.5f && cropRect.height() < height * 0.5f) {
                    newZoom = Math.min(mMaxZoom, Math.min(width / (cropRect.width() / mZoom / 0.64f), height / (cropRect.height() / mZoom / 0.64f)));
                }
                if (mZoom > 1 && (cropRect.width() > width * 0.65f || cropRect.height() > height * 0.65f)) {
                    newZoom = Math.max(1, Math.min(width / (cropRect.width() / mZoom / 0.51f), height / (cropRect.height() / mZoom / 0.51f)));
                }
                if (!mAutoZoomEnabled) {
                    newZoom = 1;
                }

                if (newZoom > 0 && newZoom != mZoom) {
                    if (animate) {
                        if (mAnimation == null) {
                            // lazy create animation single instance
                            mAnimation = new CropImageAnimation(mImageView, mCropOverlayView);
                        }
                        // set the state for animation to start from
                        mAnimation.setStartState(mImagePoints, mImageMatrix);
                    }

                    mZoom = newZoom;

                    applyImageMatrix(width, height, true, animate);
                }
            }
        }
    }

    /**
     * Apply matrix to handle the image inside the image view.
     *
     * @param width  the width of the image view
     * @param height the height of the image view
     */
    private void applyImageMatrix(float width, float height, boolean center, boolean animate) {
        if (mBitmap != null && width > 0 && height > 0) {

            mImageMatrix.invert(mImageInverseMatrix);
            RectF cropRect = mCropOverlayView.getCropWindowRect();
            mImageInverseMatrix.mapRect(cropRect);

            mImageMatrix.reset();

            // move the image to the center of the image view first so we can manipulate it from there
            mImageMatrix.postTranslate((width - mBitmap.getWidth()) / 2, (height - mBitmap.getHeight()) / 2);
            mapImagePointsByImageMatrix();

            // rotate the image the required degrees from center of image
            if (mDegreesRotated > 0) {
                mImageMatrix.postRotate(mDegreesRotated, BitmapUtils.getRectCenterX(mImagePoints), BitmapUtils
                        .getRectCenterY(mImagePoints));
                mapImagePointsByImageMatrix();
            }

            // scale the image to the image view, image rect transformed to know new width/height
            float scale = Math.min(width / BitmapUtils.getRectWidth(mImagePoints), height / BitmapUtils
                    .getRectHeight(mImagePoints));
            if (mScaleType == ScaleType.FIT_CENTER || (mScaleType == ScaleType.CENTER_INSIDE && scale < 1) || (scale > 1 && mAutoZoomEnabled)) {
                mImageMatrix.postScale(scale, scale, BitmapUtils.getRectCenterX(mImagePoints), BitmapUtils
                        .getRectCenterY(mImagePoints));
                mapImagePointsByImageMatrix();
            }

            // scale by the current zoom level
            mImageMatrix.postScale(mZoom, mZoom, BitmapUtils.getRectCenterX(mImagePoints), BitmapUtils
                    .getRectCenterY(mImagePoints));
            mapImagePointsByImageMatrix();

            mImageMatrix.mapRect(cropRect);

            if (center) {
                // set the zoomed area to be as to the center of cropping window as possible
                mZoomOffsetX = width > BitmapUtils.getRectWidth(mImagePoints) ? 0
                        : Math.max(Math.min(width / 2 - cropRect.centerX(), -BitmapUtils.getRectLeft(mImagePoints)), getWidth() - BitmapUtils
                        .getRectRight(mImagePoints)) / mZoom;
                mZoomOffsetY = height > BitmapUtils.getRectHeight(mImagePoints) ? 0
                        : Math.max(Math.min(height / 2 - cropRect.centerY(), -BitmapUtils.getRectTop(mImagePoints)), getHeight() - BitmapUtils
                        .getRectBottom(mImagePoints)) / mZoom;
            } else {
                // adjust the zoomed area so the crop window rectangle will be inside the area in case it was moved outside
                mZoomOffsetX = Math.min(
                        Math.max(mZoomOffsetX * mZoom, -cropRect.left), -cropRect.right + width) / mZoom;
                mZoomOffsetY = Math.min(
                        Math.max(mZoomOffsetY * mZoom, -cropRect.top), -cropRect.bottom + height) / mZoom;
            }

            // apply to zoom offset translate and update the crop rectangle to offset correctly
            mImageMatrix.postTranslate(mZoomOffsetX * mZoom, mZoomOffsetY * mZoom);
            cropRect.offset(mZoomOffsetX * mZoom, mZoomOffsetY * mZoom);
            mCropOverlayView.setCropWindowRect(cropRect);
            mapImagePointsByImageMatrix();

            // set matrix to apply
            if (animate) {
                // set the state for animation to end in, start animation now
                mAnimation.setEndState(mImagePoints, mImageMatrix);
                mImageView.startAnimation(mAnimation);
            } else {
                mImageView.setImageMatrix(mImageMatrix);
            }

            // update the image rectangle in the crop overlay
            updateImageBounds(false);
        }
    }

    /**
     * Adjust the given image rectangle by image transformation matrix to know the final rectangle of the image.<br>
     * To get the proper rectangle it must be first reset to orginal image rectangle.
     */
    private void mapImagePointsByImageMatrix() {
        mImagePoints[0] = 0;
        mImagePoints[1] = 0;
        mImagePoints[2] = mBitmap.getWidth();
        mImagePoints[3] = 0;
        mImagePoints[4] = mBitmap.getWidth();
        mImagePoints[5] = mBitmap.getHeight();
        mImagePoints[6] = 0;
        mImagePoints[7] = mBitmap.getHeight();
        mImageMatrix.mapPoints(mImagePoints);
    }

    /**
     * Determines the specs for the onMeasure function. Calculates the width or height
     * depending on the mode.
     *
     * @param measureSpecMode The mode of the measured width or height.
     * @param measureSpecSize The size of the measured width or height.
     * @param desiredSize     The desired size of the measured width or height.
     * @return The final size of the width or height.
     */
    private static int getOnMeasureSpec(int measureSpecMode, int measureSpecSize, int desiredSize) {

        // Measure Width
        int spec;
        if (measureSpecMode == MeasureSpec.EXACTLY) {
            // Must be this size
            spec = measureSpecSize;
        } else if (measureSpecMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...; match_parent value
            spec = Math.min(desiredSize, measureSpecSize);
        } else {
            // Be whatever you want; wrap_content
            spec = desiredSize;
        }

        return spec;
    }

    /**
     * Set visibility of crop overlay to hide it when there is no image or specificly set by client.
     */
    private void setCropOverlayVisibility() {
        if (mCropOverlayView != null) {
            mCropOverlayView.setVisibility(mShowCropOverlay && mBitmap != null ? VISIBLE : INVISIBLE);
        }
    }

    /**
     * Set visibility of progress bar when async loading/cropping is in process and show is enabled.
     */
    private void setProgressBarVisibility() {
        boolean visible = mShowProgressBar &&
                (mBitmap == null && mBitmapLoadingWorkerTask != null || mBitmapCroppingWorkerTask != null);
        mProgressBar.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    /**
     * Update the scale factor between the actual image bitmap and the shown image.<br>
     */
    private void updateImageBounds(boolean clear) {
        if (mBitmap != null && !clear) {

            // Get the scale factor between the actual Bitmap dimensions and the displayed dimensions for width/height.
            float scaleFactorWidth = mBitmap.getWidth() * mLoadedSampleSize / BitmapUtils.getRectWidth(mImagePoints);
            float scaleFactorHeight = mBitmap.getHeight() * mLoadedSampleSize / BitmapUtils.getRectHeight(mImagePoints);
            mCropOverlayView.setCropWindowLimits(getWidth(), getHeight(), scaleFactorWidth, scaleFactorHeight);
        }

        // set the bitmap rectangle and update the crop window after scale factor is set
        mCropOverlayView.setBounds(clear ? null : mImagePoints, getWidth(), getHeight());
    }
    //endregion

    //region: Inner class: CropShape

    /**
     * The possible cropping area shape.<br>
     * To set square/circle crop shape set aspect ratio to 1:1.
     */
    public enum CropShape {
        RECTANGLE,
        OVAL
    }
    //endregion

    //region: Inner class: ScaleType

    /**
     * Options for scaling the bounds of cropping image to the bounds of Crop Image View.<br>
     * Note: Some options are affected by auto-zoom, if enabled.
     */
    public enum ScaleType {

        /**
         * Scale the image uniformly (maintain the image's aspect ratio) to fit in crop image view.<br>
         * The largest dimension will be equals to crop image view and the second dimension will be smaller.
         */
        FIT_CENTER,

        /**
         * Center the image in the view, but perform no scaling.<br>
         * Note: If auto-zoom is enabled and the source image is smaller than crop image view then it will be
         * scaled uniformly to fit the crop image view.
         */
        CENTER,

        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so that both
         * dimensions (width and height) of the image will be equal to or <b>larger</b> than the
         * corresponding dimension of the view (minus padding).<br>
         * The image is then centered in the view.
         */
        CENTER_CROP,

        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so that both
         * dimensions (width and height) of the image will be equal to or <b>less</b> than the
         * corresponding dimension of the view (minus padding).<br>
         * The image is then centered in the view.<br>
         * Note: If auto-zoom is enabled and the source image is smaller than crop image view then it will be
         * scaled uniformly to fit the crop image view.
         */
        CENTER_INSIDE
    }
    //endregion

    //region: Inner class: Guidelines

    /**
     * The possible guidelines showing types.
     */
    public enum Guidelines {
        /**
         * Never show
         */
        OFF,

        /**
         * Show when crop move action is live
         */
        ON_TOUCH,

        /**
         * Always show
         */
        ON
    }
    //endregion

    //region: Inner class: RequestSizeOptions

    /**
     * Possible options for handling requested width/height for cropping.
     */
    public enum RequestSizeOptions {

        /**
         * No resize/sampling is done unless required for memory management (OOM).
         */
        NONE,

        /**
         * Only sample the image during loading (if image set using URI) so the smallest of the image
         * dimensions will be between the requested size and x2 requested size.<br>
         * NOTE: resulting image will not be exactly requested width/height
         * see: <a href="http://developer.android.com/training/displaying-bitmaps/load-bitmap.html">Loading Large
         * Bitmaps Efficiently</a>.
         */
        SAMPLING,

        /**
         * Resize the image uniformly (maintain the image's aspect ratio) so that both
         * dimensions (width and height) of the image will be equal to or <b>less</b> than the
         * corresponding requested dimension.<br>
         * If the image is smaller than the requested size it will NOT change.
         */
        RESIZE_INSIDE,

        /**
         * Resize the image uniformly (maintain the image's aspect ratio) to fit in the given width/height.<br>
         * The largest dimension will be equals to the requested and the second dimension will be smaller.<br>
         * If the image is smaller than the requested size it will enlarge it.
         */
        RESIZE_FIT,

        /**
         * Resize the image to fit exactly in the given width/height.<br>
         * This resize method does NOT preserve aspect ratio.<br>
         * If the image is smaller than the requested size it will enlarge it.
         */
        RESIZE_EXACT
    }
    //endregion

    //region: Inner class: OnSetImageUriCompleteListener

    /**
     * Interface definition for a callback to be invoked when image async loading is complete.
     */
    public interface OnSetImageUriCompleteListener {

        /**
         * Called when a crop image view has completed loading image for cropping.<br>
         * If loading failed error parameter will contain the error.
         *
         * @param view  The crop image view that loading of image was complete.
         * @param uri   the URI of the image that was loading
         * @param error if error occurred during loading will contain the error, otherwise null.
         */
        void onSetImageUriComplete(CropImageView view, Uri uri, Exception error);
    }
    //endregion

    //region: Inner class: OnGetCroppedImageCompleteListener

    /**
     * Interface definition for a callback to be invoked when image async crop is complete.
     */
    public interface OnCropImageCompleteListener {

        /**
         * Called when a crop image view has completed cropping image.<br>
         * Result object contains the cropped bitmap, saved cropped image uri, crop points data or
         * the error occured during cropping.
         *
         * @param view   The crop image view that cropping of image was complete.
         * @param result the crop image result data (with cropped image or error)
         */
        void onCropImageComplete(CropImageView view, CropResult result);
    }
    //endregion

    //region: Inner class: OnSaveCroppedImageCompleteListener

    //region: Inner class: ActivityResult

    /**
     * Result data of crop image.
     */
    public static class CropResult {

        /**
         * The cropped image bitmap result.<br>
         * Null if save cropped image was executed, no output requested or failure.
         */
        private final Bitmap mBitmap;

        /**
         * The Android uri of the saved cropped image result.<br>
         * Null if get cropped image was executed, no output requested or failure.
         */
        private final Uri mUri;

        /**
         * The error that failed the loading/cropping (null if successful)
         */
        private final Exception mError;

        /**
         * The 4 points of the cropping window in the source image
         */
        private final float[] mCropPoints;

        /**
         * The rectangle of the cropping window in the source image
         */
        private final Rect mCropRect;

        /**
         * The final rotation of the cropped image relative to source
         */
        private final int mRotation;

        /**
         * sample size used creating the crop bitmap to lower its size
         */
        private final int mSampleSize;

        public CropResult(Bitmap bitmap, Uri uri, Exception error, float[] cropPoints, Rect cropRect, int rotation, int sampleSize) {
            mBitmap = bitmap;
            mUri = uri;
            mError = error;
            mCropPoints = cropPoints;
            mCropRect = cropRect;
            mRotation = rotation;
            mSampleSize = sampleSize;
        }

        /**
         * Is the result is success or error.
         */
        public boolean isSuccessful() {
            return mError == null;
        }

        /**
         * The cropped image bitmap result.<br>
         * Null if save cropped image was executed, no output requested or failure.
         */
        public Bitmap getBitmap() {
            return mBitmap;
        }

        /**
         * The Android uri of the saved cropped image result
         * Null if get cropped image was executed, no output requested or failure.
         */
        public Uri getUri() {
            return mUri;
        }

        /**
         * The error that failed the loading/cropping (null if successful)
         */
        public Exception getError() {
            return mError;
        }

        /**
         * The 4 points of the cropping window in the source image
         */
        public float[] getCropPoints() {
            return mCropPoints;
        }

        /**
         * The rectangle of the cropping window in the source image
         */
        public Rect getCropRect() {
            return mCropRect;
        }

        /**
         * The final rotation of the cropped image relative to source
         */
        public int getRotation() {
            return mRotation;
        }

        /**
         * sample size used creating the crop bitmap to lower its size
         */
        public int getSampleSize() {
            return mSampleSize;
        }
    }
    //endregion
}
