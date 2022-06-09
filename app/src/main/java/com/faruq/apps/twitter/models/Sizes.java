package com.faruq.apps.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Sizes {
    private Size thumbSize;
    private Size largeSize;
    private Size mediumSize;
    private Size smallSize;

    public Sizes(){}

    public Sizes(Size thumbSize, Size largeSize, Size mediumSize, Size smallSize) {
        this.thumbSize = thumbSize;
        this.largeSize = largeSize;
        this.mediumSize = mediumSize;
        this.smallSize = smallSize;
    }

    public static Sizes fromJSON(JSONObject jsonObject){
        Size thumb;
        Size large;
        Size medium;
        Size small;
        try{
            thumb = new Size(
                    jsonObject.getJSONObject("thumb").getInt("h"),
                    jsonObject.getJSONObject("thumb").getInt("w"),
                    jsonObject.getJSONObject("thumb").getString("resize")
            );
        } catch(JSONException exception){
            thumb = null;
        }

        try{
            large = new Size(
                    jsonObject.getJSONObject("large").getInt("h"),
                    jsonObject.getJSONObject("large").getInt("w"),
                    jsonObject.getJSONObject("large").getString("resize")
            );
        } catch(JSONException exception){
            large = null;
        }

        try{
            medium = new Size(
                    jsonObject.getJSONObject("medium").getInt("h"),
                    jsonObject.getJSONObject("medium").getInt("w"),
                    jsonObject.getJSONObject("medium").getString("resize")
            );
        } catch(JSONException exception){
            medium = null;
        }

        try{
            small = new Size(
                    jsonObject.getJSONObject("small").getInt("h"),
                    jsonObject.getJSONObject("small").getInt("w"),
                    jsonObject.getJSONObject("small").getString("resize")
            );
        } catch(JSONException exception){
            small = null;
        }


        return new Sizes(
                thumb,
                large,
                medium,
                small
        );
    }

    public Size getThumbSize() {
        return thumbSize;
    }

    public Size getLargeSize() {
        return largeSize;
    }

    public Size getMediumSize() {
        return mediumSize;
    }

    public Size getSmallSize() {
        return smallSize;
    }
}
