package com.hoony.thriftapp;

import android.graphics.Bitmap;

/*
This class is to save data of images of a item.
 */
public class ItemImages {

    private Bitmap bitmap;

    public ItemImages(Bitmap bitmap) {

        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
