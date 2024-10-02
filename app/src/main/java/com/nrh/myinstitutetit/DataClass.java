package com.nrh.myinstitutetit;

public class DataClass {
    private String ImageURL, caption;

    public DataClass() {

    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public DataClass(String imageURL, String caption) {
        ImageURL = imageURL;
        this.caption = caption;
    }
}
