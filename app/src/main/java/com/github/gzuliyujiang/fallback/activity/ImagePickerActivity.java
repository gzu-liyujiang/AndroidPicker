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

package com.github.gzuliyujiang.fallback.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.imagepicker.ActivityBuilder;
import com.github.gzuliyujiang.imagepicker.CropImageView;
import com.github.gzuliyujiang.imagepicker.ImagePicker;
import com.github.gzuliyujiang.imagepicker.PickCallback;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/7/30 12:44
 */
public class ImagePickerActivity extends FragmentActivity implements OnSelectListener {
    private final PickCallback pickCallback = new PickCallback() {
        @Override
        public void onPermissionDenied(String[] permissions, String message) {
            Toast.makeText(ImagePickerActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPickImage(@Nullable Uri imageUri) {
            Toast.makeText(ImagePickerActivity.this, String.valueOf(imageUri), Toast.LENGTH_SHORT).show();
        }
    };
    private final PickCallback cropCallback = new PickCallback() {
        @Override
        public void onPermissionDenied(String[] permissions, String message) {
            Toast.makeText(ImagePickerActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void cropConfig(ActivityBuilder builder) {
            builder.setMultiTouchEnabled(true)
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setRequestedSize(400, 400)
                    .setFixAspectRatio(true)
                    .setAspectRatio(1, 1);
        }

        @Override
        public void onCropImage(@Nullable Uri imageUri) {
            Toast.makeText(ImagePickerActivity.this, String.valueOf(imageUri), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePicker.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ImagePicker.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onSelect(int position, String text) {
        switch (position) {
            case 0:
                ImagePicker.getInstance().startCamera(this, true, pickCallback);
                break;
            case 1:
                ImagePicker.getInstance().startGallery(this, true, pickCallback);
                break;
            default:
                break;
        }
    }

    public void onCamera(View view) {
        ImagePicker.getInstance().startCamera(this, true, cropCallback);
    }

    public void onGallery(View view) {
        ImagePicker.getInstance().startGallery(this, true, cropCallback);
    }

    public void onXPopup(View view) {
        new XPopup.Builder(view.getContext())
                .asBottomList(null, new String[]{"拍照", "从相册选取"}, this)
                .show();
    }

}
