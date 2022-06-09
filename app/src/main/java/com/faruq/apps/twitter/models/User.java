package com.faruq.apps.twitter.models;

import androidx.room.ColumnInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    private String id;

    private String userID;

    private String name;

    private String profileImage;

    private Boolean isVerified;

    // normally this field would be annotated @PrimaryKey because this is an embedded object
    // it is not needed


    public User(){

    }

    public static User fromJSON(JSONObject tweetJson) throws JSONException {

        User user = new User();
        user.id = tweetJson.getString("id");
        user.userID = tweetJson.getString("screen_name");
        user.name = tweetJson.getString("name");
        user.profileImage = tweetJson.getString("profile_image_url_https");
        user.isVerified = tweetJson.getBoolean("verified");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public Boolean getVerified() {
        return isVerified;
    }
}

