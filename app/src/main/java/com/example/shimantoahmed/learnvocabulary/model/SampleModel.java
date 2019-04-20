package com.example.shimantoahmed.learnvocabulary.model;

import android.graphics.Bitmap;
import android.net.Uri;

public class SampleModel {
    private int id;
    private String name;
    private String posting;
    private String date;
    private Bitmap bitmap;



//    public SampleModel(String name, String posting) {
//        this.name = name;
//        this.posting = posting;
//    }


    public SampleModel(int id, String name, String posting, String date, Bitmap bitmap) {
        this.id = id;
        this.name = name;
        this.posting = posting;
        this.date = date;
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
