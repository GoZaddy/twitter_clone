package com.faruq.apps.twitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class TweetIndices{
    private Integer startIndex;
    private Integer endIndex;

    public TweetIndices(){}

    public TweetIndices(Integer startIndex, Integer endIndex){
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static TweetIndices fromJSON(JSONArray jsonArray) throws JSONException {
        return new TweetIndices(
                jsonArray.getInt(0),
                jsonArray.getInt(1)
        );
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }
}