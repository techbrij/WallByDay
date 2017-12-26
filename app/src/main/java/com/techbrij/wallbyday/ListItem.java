package com.techbrij.wallbyday;

import android.graphics.Bitmap;
/**
 * Created by Administrator on 4/11/2015.
 */
public class ListItem {
    Bitmap image;
    String title;

    public ListItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}