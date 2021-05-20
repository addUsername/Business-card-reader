package com.addusername.cardparser.interfaces;

import android.graphics.Bitmap;

import com.addusername.cardparser.model.rest.Rect;

import java.util.Collection;

public interface ViewOps {
    void loadImg(Bitmap bitmap, Collection<Rect> rects);
}
