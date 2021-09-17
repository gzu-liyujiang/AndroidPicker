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

package com.github.gzuliyujiang.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.DrawableRes;

/**
 * 颜色选择面板。Adapted from https://github.com/jbruchanov/AndroidColorPicker
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2015/7/20
 */
@SuppressWarnings("unused")
public class ColorGradientView extends View {
    private static final int[] GRAD_COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
    private static final int[] GRAD_ALPHA = new int[]{Color.WHITE, Color.TRANSPARENT};

    private ColorGradientView gradientView;
    private Shader mShader;
    private Drawable mPointerDrawable;
    private Paint mPaint;
    private Paint mPaintBackground;
    private final RectF mGradientRect = new RectF();

    private final float[] mHSV = new float[]{1f, 1f, 1f};

    private final int[] mSelectedColorGradient = new int[]{0, Color.BLACK};
    private float mRadius = 0;
    private int mSelectedColor = 0;
    private boolean brightnessGradient = false;
    private int mLastX = Integer.MIN_VALUE;
    private int mLastY;
    private int mPointerHeight;
    private int mPointerWidth;
    private boolean mLockPointerInBounds = false;
    private OnColorChangedListener mOnColorChangedListener;

    public ColorGradientView(Context context) {
        super(context);
        init();
    }

    public ColorGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackground.setColor(Color.WHITE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, isInEditMode() ? null : mPaint);
        setPointerDrawable(R.mipmap.color_picker_pointer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 0;
        int desiredHeight = 0;

        if (mPointerDrawable != null) {
            desiredHeight = mPointerDrawable.getIntrinsicHeight();
            //this is nonsense, but at least have something than 0
            desiredWidth = mPointerDrawable.getIntrinsicWidth();
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShader != null) {
            canvas.drawRoundRect(mGradientRect, mRadius, mRadius, mPaintBackground);
            canvas.drawRoundRect(mGradientRect, mRadius, mRadius, mPaint);
        }

        onDrawPointer(canvas);

    }

    private void onDrawPointer(Canvas canvas) {
        if (mPointerDrawable != null) {
            int vh = getHeight();
            int pwh = mPointerWidth >> 1;
            int phh = mPointerHeight >> 1;
            float tx, ty;
            if (!brightnessGradient) {
                // fixed: 17-1-7 Math operands should be cast before assignment
                tx = (float) (mLastX - pwh);
                ty = (float) (mLastY - phh);
                if (mLockPointerInBounds) {
                    tx = Math.max(mGradientRect.left, Math.min(tx, mGradientRect.right - mPointerWidth));
                    ty = Math.max(mGradientRect.top, Math.min(ty, mGradientRect.bottom - mPointerHeight));
                } else {
                    tx = Math.max(mGradientRect.left - pwh, Math.min(tx, mGradientRect.right - pwh));
                    ty = Math.max(mGradientRect.top - pwh, Math.min(ty, mGradientRect.bottom - phh));
                }
            } else {//vertical lock
                tx = (float) (mLastX - pwh);
                ty = mPointerHeight != mPointerDrawable.getIntrinsicHeight() ? (vh >> 1) - phh : 0;
                if (mLockPointerInBounds) {
                    tx = Math.max(mGradientRect.left, Math.min(tx, mGradientRect.right - mPointerWidth));
                    ty = Math.max(mGradientRect.top, Math.min(ty, mGradientRect.bottom - mPointerHeight));
                } else {
                    tx = Math.max(mGradientRect.left - pwh, Math.min(tx, mGradientRect.right - pwh));
                    ty = Math.max(mGradientRect.top - pwh, Math.min(ty, mGradientRect.bottom - phh));
                }
            }
            canvas.translate(tx, ty);
            mPointerDrawable.draw(canvas);
            canvas.translate(-tx, -ty);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mGradientRect.set(getPaddingLeft(), getPaddingTop(), right - left - getPaddingRight(), bottom - top - getPaddingBottom());
        if (changed) {
            buildShader();
        }

        if (mPointerDrawable != null) {
            int h = (int) mGradientRect.height();
            int ph = mPointerDrawable.getIntrinsicHeight();
            int pw = mPointerDrawable.getIntrinsicWidth();
            mPointerHeight = ph;
            mPointerWidth = pw;
            if (h < ph) {
                mPointerHeight = h;
                mPointerWidth = (int) (pw * (h / (float) ph));
            }
            mPointerDrawable.setBounds(0, 0, mPointerWidth, mPointerHeight);
            updatePointerPosition();
        }
    }

    private void buildShader() {
        if (brightnessGradient) {
            mShader = new LinearGradient(mGradientRect.left, mGradientRect.top, mGradientRect.right, mGradientRect.top, mSelectedColorGradient, null, Shader.TileMode.CLAMP);
        } else {
            LinearGradient gradientShader = new LinearGradient(mGradientRect.left, mGradientRect.top, mGradientRect.right, mGradientRect.top, GRAD_COLORS, null, Shader.TileMode.CLAMP);
            LinearGradient alphaShader = new LinearGradient(0, mGradientRect.top + (mGradientRect.height() / 3), 0, mGradientRect.bottom, GRAD_ALPHA, null, Shader.TileMode.CLAMP);
            mShader = new ComposeShader(alphaShader, gradientShader, PorterDuff.Mode.MULTIPLY);
        }
        mPaint.setShader(mShader);
    }

    public void setRadius(float radius) {
        // fixed: 17-1-7  Equality tests should not be made with floating point values.
        if ((int) radius != (int) mRadius) {
            mRadius = radius;
            invalidate();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLastX = (int) event.getX();
        mLastY = (int) event.getY();
        onUpdateColorSelection(mLastX, mLastY);
        invalidate();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    protected void onUpdateColorSelection(int x, int y) {
        x = (int) Math.max(mGradientRect.left, Math.min(x, mGradientRect.right));
        y = (int) Math.max(mGradientRect.top, Math.min(y, mGradientRect.bottom));
        if (brightnessGradient) {
            float b = pointToValueBrightness(x);
            mHSV[2] = b;
            mSelectedColor = Color.HSVToColor(mHSV);
        } else {
            float hue = pointToHue(x);
            float sat = pointToSaturation(y);
            mHSV[0] = hue;
            mHSV[1] = sat;
            mHSV[2] = 1f;
            mSelectedColor = Color.HSVToColor(mHSV);
        }
        dispatchColorChanged(mSelectedColor);
    }

    protected void dispatchColorChanged(int color) {
        if (gradientView != null) {
            gradientView.setColor(color, false);
        }
        if (mOnColorChangedListener != null) {
            mOnColorChangedListener.onColorChanged(this, color);
        }
    }

    public final void asBrightnessGradient() {
        this.brightnessGradient = true;
    }

    public void setBrightnessGradientView(ColorGradientView gradientView) {
        if (this.gradientView != gradientView) {
            this.gradientView = gradientView;
            if (this.gradientView != null) {
                this.gradientView.asBrightnessGradient();
                this.gradientView.setColor(mSelectedColor);
            }
        }
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setColor(int selectedColor) {
        setColor(selectedColor, true);
    }

    protected void setColor(int selectedColor, boolean updatePointers) {
        Color.colorToHSV(selectedColor, mHSV);
        if (brightnessGradient) {
            mSelectedColorGradient[0] = getColorForGradient(mHSV);
            mSelectedColor = Color.HSVToColor(mHSV);
            buildShader();
            if (mLastX != Integer.MIN_VALUE) {
                mHSV[2] = pointToValueBrightness(mLastX);
            }
            selectedColor = Color.HSVToColor(mHSV);
        }
        if (updatePointers) {
            updatePointerPosition();
        }
        mSelectedColor = selectedColor;
        invalidate();
        dispatchColorChanged(mSelectedColor);
    }

    private int getColorForGradient(float[] hsv) {
        // fixed: 17-1-7  Equality tests should not be made with floating point values.
        if ((int) hsv[2] != 1) {
            float oldV = hsv[2];
            hsv[2] = 1;
            int color = Color.HSVToColor(hsv);
            hsv[2] = oldV;
            return color;
        } else {
            return Color.HSVToColor(hsv);
        }
    }

    private void updatePointerPosition() {
        if (mGradientRect.width() != 0 && mGradientRect.height() != 0) {
            if (!brightnessGradient) {
                mLastX = hueToPoint(mHSV[0]);
                mLastY = saturationToPoint(mHSV[1]);
            } else {
                mLastX = brightnessToPoint(mHSV[2]);
            }
        }
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        mOnColorChangedListener = onColorChangedListener;
    }

    //region HSL math

    private float pointToHue(float x) {
        x = x - mGradientRect.left;
        return x * 360f / mGradientRect.width();
    }

    private int hueToPoint(float hue) {
        return (int) (mGradientRect.left + ((hue * mGradientRect.width()) / 360));
    }

    private float pointToSaturation(float y) {
        y = y - mGradientRect.top;
        return 1 - (1.f / mGradientRect.height() * y);
    }

    private int saturationToPoint(float sat) {
        sat = 1 - sat;
        return (int) (mGradientRect.top + (mGradientRect.height() * sat));
    }

    private float pointToValueBrightness(float x) {
        x = x - mGradientRect.left;
        return 1 - (1.f / mGradientRect.width() * x);
    }

    private int brightnessToPoint(float val) {
        val = 1 - val;
        return (int) (mGradientRect.left + (mGradientRect.width() * val));
    }
    //endregion HSL math

    public void setPointerDrawable(Drawable pointerDrawable) {
        if (pointerDrawable == null) {
            return;
        }
        if (mPointerDrawable != pointerDrawable) {
            mPointerDrawable = pointerDrawable;
            requestLayout();
        }
    }

    public void setPointerDrawable(@DrawableRes int pointerDrawable) {
        setPointerDrawable(getResources().getDrawable(pointerDrawable));
    }

    public void recycle() {
        mPaint = null;
        mPaintBackground = null;
        if (mPointerDrawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) mPointerDrawable).getBitmap();
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public void setLockPointerInBounds(boolean lockPointerInBounds) {
        if (lockPointerInBounds != mLockPointerInBounds) {
            mLockPointerInBounds = lockPointerInBounds;
            invalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isBrightnessGradient = brightnessGradient;
        ss.color = mSelectedColor;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        brightnessGradient = ss.isBrightnessGradient;
        setColor(ss.color, true);
    }

}