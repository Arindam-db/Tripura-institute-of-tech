package com.nrh.myinstitutetit;

public class Feed {
    private String caption;
    private String imageUrl; // URL or path for the image

    // Constructor
    public Feed(String caption, String imageUrl) {
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
