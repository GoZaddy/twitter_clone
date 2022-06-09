package com.faruq.apps.twitter.models;


import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
public class Media {
    private String id;
    private String mediaURL;
    private String type;
    private TweetIndices tweetIndices;
    private Sizes sizes;
    private String jsonString;

    public Media(){}

    public Media(String id, String mediaURL, String type, TweetIndices tweetIndices, Sizes sizes, String string) {
        this.id = id;
        this.mediaURL = mediaURL;
        this.type = type;
        this.tweetIndices = tweetIndices;
        this.sizes = sizes;
        this.jsonString = string;
    }

    public static Media fromJSON(JSONObject jsonObject) throws JSONException {
        return new Media(
                jsonObject.getString("id_str"),
                jsonObject.getString("media_url_https"),
                jsonObject.getString("type"),
                TweetIndices.fromJSON(jsonObject.getJSONArray("indices")),
                Sizes.fromJSON(jsonObject.getJSONObject("sizes")),
                jsonObject.toString()
        );
    }


    @NonNull
    public String toString(){
        return this.jsonString;
    }

    public String getId() {
        return id;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public String getType() {
        return type;
    }

    public TweetIndices getTweetIndices() {
        return tweetIndices;
    }

    public Sizes getSizes() {
        return sizes;
    }

    public String getSmall(){
        if (sizes.getSmallSize() != null){
            return this.mediaURL+"?format=jpg&name=small";
        }
        return null;
    }

    public String getLarge(){
        if (sizes.getLargeSize() != null){
            return this.mediaURL+"?format=jpg&name=large";
        }
        return null;
    }

    public String getMedium(){
        if (sizes.getMediumSize() != null){
            return this.mediaURL+"?format=jpg&name=medium";
        }
        return null;
    }

    public String getThumb(){
        if (sizes.getThumbSize() != null){
            return this.mediaURL+"?format=jpg&name=thumb";
        }
        return null;
    }


}
