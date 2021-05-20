package com.addusername.cardparser.interfaces;

import com.addusername.cardparser.model.rest.Rect;

public interface ModelOps {
    void parseImg(byte[] bytes);

    void parseImgAndGetPrediction(byte[] bytes);
}
