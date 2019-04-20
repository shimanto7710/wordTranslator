package com.example.shimantoahmed.learnvocabulary.others;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrManager {
    TessBaseAPI baseAPI;

    public void intAPI() {
        baseAPI = new TessBaseAPI();
        String dataPath = MainApplication.instance.getTessDataParentDirectory();
        baseAPI.init(dataPath, "eng");
    }

    public String startRecognize(Bitmap bitmap) {
        if (baseAPI == null) intAPI();
        baseAPI.setImage(bitmap);
        return baseAPI.getUTF8Text();
    }
}
