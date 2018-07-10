package com.ys.recorder.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


public class AppInfomation implements Parcelable {


    public String appName;
    String downUrl;
    public Drawable drawable;
    int imageId;
    boolean isChoice;
    public String packageName;

    public AppInfomation() {
    }

    public AppInfomation(Parcel paramParcel) {
        this.appName = paramParcel.readString();
        this.packageName = paramParcel.readString();
        this.drawable = new BitmapDrawable((Bitmap) paramParcel.readParcelable(Bitmap.class.getClassLoader()));
        this.downUrl = paramParcel.readString();
        this.imageId = paramParcel.readInt();
        if (paramParcel.readInt() == 0) {
        }
        for (boolean bool = false; ; bool = true) {
            this.isChoice = bool;
            return;
        }
    }

    public AppInfomation(String paramString) {
        this.appName = paramString;
    }

    public AppInfomation(String paramString, int paramInt) {
        this.appName = paramString;
        this.imageId = paramInt;
    }

    public AppInfomation(String paramString1, int paramInt, String paramString2, String paramString3) {
        setAppName(paramString1);
        setDownUrl(paramString3);
        setImageId(paramInt);
        setPackageName(paramString2);
        setChoice(this.isChoice);
    }

    public AppInfomation(String paramString, Drawable paramDrawable) {
        this.appName = paramString;
        this.drawable = paramDrawable;
    }

    public AppInfomation(String paramString1, String paramString2) {
        this.appName = paramString1;
        this.packageName = paramString2;
    }

    public AppInfomation(String paramString1, String paramString2, Drawable paramDrawable) {
        this.appName = paramString1;
        this.packageName = paramString2;
        this.drawable = paramDrawable;
    }

    public int describeContents() {
        return 0;
    }

    public String getAppName() {
        if (this.appName == null) {
            return "";
        }
        return this.appName;
    }

    public String getDownUrl() {
        return this.downUrl;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getImageId() {
        return this.imageId;
    }

    public String getPackageName() {
        if (this.packageName == null) {
            return "";
        }
        return this.packageName;
    }

    public boolean isChoice() {
        return this.isChoice;
    }

    public void setAppName(String paramString) {
        this.appName = paramString;
    }

    public void setChoice(boolean paramBoolean) {
        this.isChoice = paramBoolean;
    }

    public void setDownUrl(String paramString) {
        this.downUrl = paramString;
    }

    public void setDrawable(Drawable paramDrawable) {
        this.drawable = paramDrawable;
    }

    public void setImageId(int paramInt) {
        this.imageId = paramInt;
    }

    public void setPackageName(String paramString) {
        this.packageName = paramString;
    }

    public String toString() {
        return "AppInfo [appName=" + this.appName + ", packageName=" + this.packageName + "]";
    }

    public static final Creator<AppInfomation> CREATOR = new Creator() {
        public AppInfomation createFromParcel(Parcel parcel) {
            return new AppInfomation(parcel);
        }

        public AppInfomation[] newArray(int paramAnonymousInt) {
            return new AppInfomation[paramAnonymousInt];
        }
    };

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramInt = 1;
        paramParcel.writeString(appName);
        paramParcel.writeString(packageName);
        paramParcel.writeParcelable(drawableToBitmap(drawable), Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        paramParcel.writeString(downUrl);
        paramParcel.writeInt(imageId);
        paramParcel.writeInt(isChoice ? 1 : 0);
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
