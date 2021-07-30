package com.github.gzuliyujiang.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 图片选择器（相机拍照+相册选取+图片裁剪）
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2020/4/3 17:18
 */
public class ImagePicker {
    private Uri cropImageUri;
    private boolean cropEnabled = true;
    private PickCallback callback;

    private static class Holder {
        private static final ImagePicker INSTANCE = new ImagePicker();
    }

    public static ImagePicker getInstance() {
        return Holder.INSTANCE;
    }

    private ImagePicker() {
        super();
    }

    /**
     * 启动照相机
     */
    public void startCamera(Activity activity, boolean cropEnabled, @NonNull PickCallback callback) {
        this.cropEnabled = cropEnabled;
        this.callback = callback;
        if (HybridityUtils.isExplicitCameraPermissionRequired(activity)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    CropImageConsts.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
        } else {
            activity.startActivityForResult(HybridityUtils.getCameraIntent(activity), CropImageConsts.PICK_IMAGE_CHOOSER_REQUEST_CODE);
        }
    }

    /**
     * 启动照相机
     */
    public void startCamera(Fragment fragment, boolean cropEnabled, @NonNull PickCallback callback) {
        this.cropEnabled = cropEnabled;
        this.callback = callback;
        if (HybridityUtils.isExplicitCameraPermissionRequired(fragment.requireActivity())) {
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImageConsts.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
        } else {
            fragment.startActivityForResult(HybridityUtils.getCameraIntent(fragment.requireActivity()), CropImageConsts.PICK_IMAGE_CHOOSER_REQUEST_CODE);
        }
    }

    /**
     * 启动图库选择器
     */
    public void startGallery(Activity activity, boolean cropEnabled, @NonNull PickCallback callback) {
        this.cropEnabled = cropEnabled;
        this.callback = callback;
        activity.startActivityForResult(HybridityUtils.getGalleryIntent(), CropImageConsts.PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动图库选择器
     */
    public void startGallery(Fragment fragment, boolean cropEnabled, @NonNull PickCallback callback) {
        this.cropEnabled = cropEnabled;
        this.callback = callback;
        fragment.startActivityForResult(HybridityUtils.getGalleryIntent(), CropImageConsts.PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 图片选择/裁剪结果回调，在 {@link Activity#onActivityResult(int, int, Intent)} 中调用
     */
    @SuppressWarnings("JavadocReference")
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        onActivityResultInner(activity, null, requestCode, resultCode, data);
    }

    /**
     * 图片选择/裁剪结果回调，在 {@link Fragment#onActivityResult(int, int, Intent)} 中调用
     */
    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        onActivityResultInner(null, fragment, requestCode, resultCode, data);
    }

    private void onActivityResultInner(Activity activity, Fragment fragment, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Context context;
            if (activity != null) {
                context = activity;
            } else {
                context = fragment.getActivity();
            }

            if (context != null && requestCode == CropImageConsts.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri pickImageUri = HybridityUtils.getPickImageResultUri(context, data);
                // 检查读取文件权限
                if (HybridityUtils.isReadExternalStoragePermissionsRequired(context, pickImageUri)) {
                    if (activity != null) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                CropImageConsts.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                    } else {
                        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                CropImageConsts.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                    }
                } else {
                    // 选择图片回调
                    if (activity != null) {
                        handlePickImage(activity, null, pickImageUri);
                    } else {
                        handlePickImage(null, fragment, pickImageUri);
                    }
                }
            } else if (requestCode == CropImageConsts.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                // 裁剪图片回调
                handleCropResult(context, data);
            }
        }
    }

    /**
     * 授权结果回调，在 {@link Activity#onRequestPermissionsResult(int, String[], int[])} 中调用
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                           int[] grantResults) {
        onRequestPermissionsResultInner(activity, null, requestCode, permissions, grantResults);
    }

    /**
     * 授权结果回调，在 {@link Fragment#onRequestPermissionsResult(int, String[], int[])} 中调用
     */
    public void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                           int[] grantResults) {
        onRequestPermissionsResultInner(null, fragment, requestCode, permissions, grantResults);
    }

    private void onRequestPermissionsResultInner(Activity activity, Fragment fragment, int requestCode,
                                                 String[] permissions, int[] grantResults) {
        if (requestCode == CropImageConsts.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (activity != null) {
                    startCamera(activity, cropEnabled, callback);
                } else {
                    startCamera(fragment, cropEnabled, callback);
                }
            } else {
                if (callback != null) {
                    callback.onPermissionDenied(permissions, "没有相机权限");
                }
            }
        } else if (requestCode == CropImageConsts.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (cropImageUri != null
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (activity != null) {
                    handlePickImage(activity, null, cropImageUri);
                } else {
                    handlePickImage(null, fragment, cropImageUri);
                }
            } else {
                if (callback != null) {
                    callback.onPermissionDenied(permissions, "没有外部存储权限");
                }
            }
        }
    }

    /**
     * 裁剪图片结果回调
     */
    private void handleCropResult(Context context, Intent data) {
        ActivityResult result = null;
        if (data != null) {
            result = data.getParcelableExtra(CropImageConsts.CROP_IMAGE_EXTRA_RESULT);
        }
        if (result == null) {
            return;
        }
        Exception error = result.getError();
        if (error == null) {
            cropImageUri = result.getUri();
            if (callback != null) {
                callback.onCropImage(handleUri(context, cropImageUri));
            }
        } else {
            if (callback != null) {
                callback.onCropError(error);
            }
        }
    }

    /**
     * 选择图片结果回调
     */
    private void handlePickImage(Activity activity, Fragment fragment, Uri imageUri) {
        if (callback != null) {
            Context context;
            if (activity != null) {
                context = activity;
            } else {
                context = fragment.getContext();
            }
            callback.onPickImage(handleUri(context, imageUri));
        }
        if (!cropEnabled) {
            return;
        }
        if (imageUri == null || imageUri.equals(Uri.EMPTY)) {
            if (callback != null) {
                callback.onCropError(new IllegalArgumentException("Uri is null or empty"));
            }
            return;
        }
        ActivityBuilder builder = new ActivityBuilder(imageUri);
        if (callback != null) {
            callback.cropConfig(builder);
        }
        if (activity != null) {
            builder.start(activity);
        } else {
            builder.start(fragment);
        }
    }

    /**
     * 处理返回图片的 uri，content 协议自动转换 file 协议，避免 {@link FileNotFoundException}
     */
    private Uri handleUri(Context context, Uri imageUri) {
        if ("content".equals(imageUri.getScheme())) {
            String realPath = HybridityUtils.getRealPathFromUri(context, imageUri);
            if (!TextUtils.isEmpty(realPath)) {
                return Uri.fromFile(new File(realPath));
            }
        }
        return imageUri;
    }

}

