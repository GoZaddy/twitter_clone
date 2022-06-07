package com.faruq.apps.twitter.models;

import androidx.room.ColumnInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    String name;

    String profileImage;

    // normally this field would be annotated @PrimaryKey because this is an embedded object
    // it is not needed
    String id;

    public User(){

    }

    public static User fromJSON(JSONObject tweetJson) throws JSONException {

        User user = new User();
        user.id = tweetJson.getString("name");
        user.name = tweetJson.getString("screen_name");
        user.profileImage = tweetJson.getString("profile_image_url_https");

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
}

