package com.masterexcels.excelreader.model;

import android.graphics.Bitmap;

public class MySlide {
    private Bitmap bitmap;
    private boolean isSelected;

    public MySlide(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
