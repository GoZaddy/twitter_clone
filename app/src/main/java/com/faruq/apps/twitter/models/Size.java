package com.faruq.apps.twitter.models;

import org.parceler.Parcel;

@Parcel
public class Size {
    private Integer height;
    private Integer width;
    private String resize;

    public Size(){}

    public Size(Integer height, Integer width, String resize) {
        this.height = height;
        this.width = width;
        this.resize = resize;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public String getResize() {
        return resize;
    }
}
