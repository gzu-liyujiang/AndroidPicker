package com.github.gzuliyujiang.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Builder used for creating Image Crop Activity by user request.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ActivityBuilder {

    /**
     * The image to crop source Android uri.
     */
    private final Uri mSource;

    /**
     * Options for image crop UX
     */
    private final CropImageOptions mOptions;

    public ActivityBuilder(Uri source) {
        mSource = source;
        mOptions = new CropImageOptions();
    }

    /**
     * Get {@link CropImageActivity} intent to start the activity.
     */
    public Intent getCropIntent(@NonNull Context context) {
        mOptions.validate();
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.putExtra(CropImageConsts.CROP_IMAGE_EXTRA_SOURCE, mSource);
        intent.putExtra(CropImageConsts.CROP_IMAGE_EXTRA_OPTIONS, mOptions);
        return intent;
    }

    /**
     * Start {@link CropImageActivity}.
     *
     * @param activity activity to receive result
     */
    public void start(@NonNull Activity activity) {
        mOptions.validate();
        activity.startActivityForResult(getCropIntent(activity), CropImageConsts.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Start {@link CropImageActivity}.
     *
     * @param fragment fragment to receive result
     */
    public void start(@NonNull Fragment fragment) {
        fragment.startActivityForResult(getCropIntent(fragment.requireActivity()), CropImageConsts.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * The shape of the cropping window.<br>
     * To set square/circle crop shape set aspect ratio to 1:1.<br>
     * <i>Default: RECTANGLE</i>
     */
    public ActivityBuilder setCropShape(@NonNull CropImageView.CropShape cropShape) {
        mOptions.cropShape = cropShape;
        return this;
    }

    /**
     * An edge of the crop window will snap to the corresponding edge of a specified bounding box
     * when the crop window edge is less than or equal to this distance (in pixels) away from the bounding box
     * edge (in pixels).<br>
     * <i>Default: 3dp</i>
     */
    public ActivityBuilder setSnapRadius(float snapRadius) {
        mOptions.snapRadius = snapRadius;
        return this;
    }

    /**
     * The radius of the touchable area around the handle (in pixels).<br>
     * We are basing this value off of the recommended 48dp Rhythm.<br>
     * See: http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm<br>
     * <i>Default: 48dp</i>
     */
    public ActivityBuilder setTouchRadius(float touchRadius) {
        mOptions.touchRadius = touchRadius;
        return this;
    }

    /**
     * whether the guidelines should be on, off, or only showing when resizing.<br>
     * <i>Default: ON_TOUCH</i>
     */
    public ActivityBuilder setGuidelines(@NonNull CropImageView.Guidelines guidelines) {
        mOptions.guidelines = guidelines;
        return this;
    }

    /**
     * The initial scale type of the image in the crop image view<br>
     * <i>Default: FIT_CENTER</i>
     */
    public ActivityBuilder setScaleType(@NonNull CropImageView.ScaleType scaleType) {
        mOptions.scaleType = scaleType;
        return this;
    }

    /**
     * if to show crop overlay UI what contains the crop window UI surrounded by background over the cropping
     * image.<br>
     * <i>default: true, may disable for animation or frame transition.</i>
     */
    public ActivityBuilder setShowCropOverlay(boolean showCropOverlay) {
        mOptions.showCropOverlay = showCropOverlay;
        return this;
    }

    /**
     * if auto-zoom functionality is enabled.<br>
     * default: true.
     */
    public ActivityBuilder setAutoZoomEnabled(boolean autoZoomEnabled) {
        mOptions.autoZoomEnabled = autoZoomEnabled;
        return this;
    }

    /**
     * if multi touch functionality is enabled.<br>
     * default: true.
     */
    public ActivityBuilder setMultiTouchEnabled(boolean multiTouchEnabled) {
        mOptions.multiTouchEnabled = multiTouchEnabled;
        return this;
    }

    /**
     * The max zoom allowed during cropping.<br>
     * <i>Default: 4</i>
     */
    public ActivityBuilder setMaxZoom(int maxZoom) {
        mOptions.maxZoom = maxZoom;
        return this;
    }

    /**
     * The initial crop window padding from image borders in percentage of the cropping image dimensions.<br>
     * <i>Default: 0.1</i>
     */
    public ActivityBuilder setInitialCropWindowPaddingRatio(float initialCropWindowPaddingRatio) {
        mOptions.initialCropWindowPaddingRatio = initialCropWindowPaddingRatio;
        return this;
    }

    /**
     * whether the width to height aspect ratio should be maintained or free to change.<br>
     * <i>Default: false</i>
     */
    public ActivityBuilder setFixAspectRatio(boolean fixAspectRatio) {
        mOptions.fixAspectRatio = fixAspectRatio;
        return this;
    }

    /**
     * the X,Y value of the aspect ratio.<br>
     * Also sets fixes aspect ratio to TRUE.<br>
     * <i>Default: 1/1</i>
     *
     * @param aspectRatioX the width
     * @param aspectRatioY the height
     */
    public ActivityBuilder setAspectRatio(int aspectRatioX, int aspectRatioY) {
        mOptions.aspectRatioX = aspectRatioX;
        mOptions.aspectRatioY = aspectRatioY;
        mOptions.fixAspectRatio = true;
        return this;
    }

    /**
     * the thickness of the guidelines lines (in pixels).<br>
     * <i>Default: 3dp</i>
     */
    public ActivityBuilder setBorderLineThickness(float borderLineThickness) {
        mOptions.borderLineThickness = borderLineThickness;
        return this;
    }

    /**
     * the color of the guidelines lines.<br>
     * <i>Default: Color.argb(170, 255, 255, 255)</i>
     */
    public ActivityBuilder setBorderLineColor(int borderLineColor) {
        mOptions.borderLineColor = borderLineColor;
        return this;
    }

    /**
     * thickness of the corner line (in pixels).<br>
     * <i>Default: 2dp</i>
     */
    public ActivityBuilder setBorderCornerThickness(float borderCornerThickness) {
        mOptions.borderCornerThickness = borderCornerThickness;
        return this;
    }

    /**
     * the offset of corner line from crop window border (in pixels).<br>
     * <i>Default: 5dp</i>
     */
    public ActivityBuilder setBorderCornerOffset(float borderCornerOffset) {
        mOptions.borderCornerOffset = borderCornerOffset;
        return this;
    }

    /**
     * the length of the corner line away from the corner (in pixels).<br>
     * <i>Default: 14dp</i>
     */
    public ActivityBuilder setBorderCornerLength(float borderCornerLength) {
        mOptions.borderCornerLength = borderCornerLength;
        return this;
    }

    /**
     * the color of the corner line.<br>
     * <i>Default: WHITE</i>
     */
    public ActivityBuilder setBorderCornerColor(int borderCornerColor) {
        mOptions.borderCornerColor = borderCornerColor;
        return this;
    }

    /**
     * the thickness of the guidelines lines (in pixels).<br>
     * <i>Default: 1dp</i>
     */
    public ActivityBuilder setGuidelinesThickness(float guidelinesThickness) {
        mOptions.guidelinesThickness = guidelinesThickness;
        return this;
    }

    /**
     * the color of the guidelines lines.<br>
     * <i>Default: Color.argb(170, 255, 255, 255)</i>
     */
    public ActivityBuilder setGuidelinesColor(int guidelinesColor) {
        mOptions.guidelinesColor = guidelinesColor;
        return this;
    }

    /**
     * the color of the overlay background around the crop window cover the image parts not in the crop window.<br>
     * <i>Default: Color.argb(119, 0, 0, 0)</i>
     */
    public ActivityBuilder setBackgroundColor(int backgroundColor) {
        mOptions.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * the min size the crop window is allowed to be (in pixels).<br>
     * <i>Default: 42dp, 42dp</i>
     */
    public ActivityBuilder setMinCropWindowSize(int minCropWindowWidth, int minCropWindowHeight) {
        mOptions.minCropWindowWidth = minCropWindowWidth;
        mOptions.minCropWindowHeight = minCropWindowHeight;
        return this;
    }

    /**
     * the min size the resulting cropping image is allowed to be, affects the cropping window limits
     * (in pixels).<br>
     * <i>Default: 40px, 40px</i>
     */
    public ActivityBuilder setMinCropResultSize(int minCropResultWidth, int minCropResultHeight) {
        mOptions.minCropResultWidth = minCropResultWidth;
        mOptions.minCropResultHeight = minCropResultHeight;
        return this;
    }

    /**
     * the max size the resulting cropping image is allowed to be, affects the cropping window limits
     * (in pixels).<br>
     * <i>Default: 99999, 99999</i>
     */
    public ActivityBuilder setMaxCropResultSize(int maxCropResultWidth, int maxCropResultHeight) {
        mOptions.maxCropResultWidth = maxCropResultWidth;
        mOptions.maxCropResultHeight = maxCropResultHeight;
        return this;
    }

    /**
     * the Android Uri to save the cropped image to.<br>
     * <i>Default: NONE, will create a temp file</i>
     */
    public ActivityBuilder setOutputUri(Uri outputUri) {
        mOptions.outputUri = outputUri;
        return this;
    }

    /**
     * the compression format to use when writting the image.<br>
     * <i>Default: JPEG</i>
     */
    public ActivityBuilder setOutputCompressFormat(Bitmap.CompressFormat outputCompressFormat) {
        mOptions.outputCompressFormat = outputCompressFormat;
        return this;
    }

    /**
     * the quility (if applicable) to use when writting the image (0 - 100).<br>
     * <i>Default: 90</i>
     */
    public ActivityBuilder setOutputCompressQuality(int outputCompressQuality) {
        mOptions.outputCompressQuality = outputCompressQuality;
        return this;
    }

    /**
     * the size to resize the cropped image to.<br>
     * Uses {@link CropImageView.RequestSizeOptions#RESIZE_INSIDE} option.<br>
     * <i>Default: 0, 0 - not set, will not resize</i>
     */
    public ActivityBuilder setRequestedSize(int reqWidth, int reqHeight) {
        return setRequestedSize(reqWidth, reqHeight, CropImageView.RequestSizeOptions.RESIZE_INSIDE);
    }

    /**
     * the size to resize the cropped image to.<br>
     * <i>Default: 0, 0 - not set, will not resize</i>
     */
    public ActivityBuilder setRequestedSize(int reqWidth, int reqHeight, CropImageView.RequestSizeOptions options) {
        mOptions.outputRequestWidth = reqWidth;
        mOptions.outputRequestHeight = reqHeight;
        mOptions.outputRequestSizeOptions = options;
        return this;
    }

    /**
     * if the result of crop image activity should not save the cropped image bitmap.<br>
     * Used if you want to crop the image manually and need only the crop rectangle and rotation data.<br>
     * <i>Default: false</i>
     */
    public ActivityBuilder setNoOutputImage(boolean noOutputImage) {
        mOptions.noOutputImage = noOutputImage;
        return this;
    }

    /**
     * the initial rectangle to set on the cropping image after loading.<br>
     * <i>Default: NONE - will initialize using initial crop window padding ratio</i>
     */
    public ActivityBuilder setInitialCropWindowRectangle(Rect initialCropWindowRectangle) {
        mOptions.initialCropWindowRectangle = initialCropWindowRectangle;
        return this;
    }

    /**
     * the initial rotation to set on the cropping image after loading (0-360 degrees clockwise).<br>
     * <i>Default: NONE - will read image exif data</i>
     */
    public ActivityBuilder setInitialRotation(int initialRotation) {
        mOptions.initialRotation = initialRotation;
        return this;
    }

    /**
     * if to allow rotation during cropping.<br>
     * <i>Default: true</i>
     */
    public ActivityBuilder setAllowRotation(boolean allowRotation) {
        mOptions.allowRotation = allowRotation;
        return this;
    }

    /**
     * The amount of degreees to rotate clockwise or counter-clockwise (0-360).<br>
     * <i>Default: 90</i>
     */
    public ActivityBuilder setRotationDegrees(int rotationDegrees) {
        mOptions.rotationDegrees = rotationDegrees;
        return this;
    }

}
