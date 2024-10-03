package com.nrh.myinstitutetit;

// for storing data in the feeds section of the app

public class DataClass {

    private String imageUrl;
    private String caption;
    private String key;

    // No-argument constructor (required by Firebase)
    public DataClass() {
        // Firebase needs this for deserialization
    }

    // Constructor with parameters
    public DataClass(String imageUrl, String caption, String key) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.key = key;
    }

    // Getter and setter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter and setter for caption
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    // Getter and setter for key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
