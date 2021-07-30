package com.github.gzuliyujiang.imagepicker;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Result data of Crop Image Activity.
 */
public class ActivityResult extends CropImageView.CropResult implements Parcelable {
    public static final Creator<ActivityResult> CREATOR = new Creator<ActivityResult>() {
        @Override
        public ActivityResult createFromParcel(Parcel in) {
            return new ActivityResult(in);
        }

        @Override
        public ActivityResult[] newArray(int size) {
            return new ActivityResult[size];
        }
    };

    public ActivityResult(Bitmap bitmap, Uri uri, Exception error, float[] cropPoints, Rect cropRect, int rotation, int sampleSize) {
        super(bitmap, uri, error, cropPoints, cropRect, rotation, sampleSize);
    }

    protected ActivityResult(Parcel in) {
        super(null,
                in.readParcelable(Uri.class.getClassLoader()),
                (Exception) in.readSerializable(),
                in.createFloatArray(),
                in.readParcelable(Rect.class.getClassLoader()),
                in.readInt(), in.readInt());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getUri(), flags);
        dest.writeSerializable(getError());
        dest.writeFloatArray(getCropPoints());
        dest.writeParcelable(getCropRect(), flags);
        dest.writeInt(getRotation());
        dest.writeInt(getSampleSize());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
