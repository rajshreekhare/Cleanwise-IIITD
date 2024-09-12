package com.zuccessful.cleanwise.pojo;

/**
 * Created by spark on 11/3/18.
 */

public class UploadImage_MT17010 {

    private String name;
    private String url;

    public UploadImage_MT17010() {
        // Empty constructor
    }

    public UploadImage_MT17010(String name, String url) {
        if (name.trim().equals("")) {
            name = "No Image";
        }
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
