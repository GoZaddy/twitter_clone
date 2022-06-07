package com.faruq.apps.twitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.sql.Date;
import java.util.ArrayList;

import com.faruq.apps.twitter.models.User;

@Parcel
public class Tweet {
    // Define database columns and associated fields
    String id;

    String body;

    String createdAt;

    // Use @Embedded to keep the column entries as part of the same table while still
    // keeping the logical separation between the two objects.
    User user;

    public Tweet(){}


    public Tweet(JSONObject object){
        try {
            this.id = object.getString("id");
            this.user = User.fromJSON(object.getJSONObject("user"));
            this.body = object.getString("text");
            this.createdAt = object.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweets.add(tweet);
        }

        return tweets;
    }

    public void print(){
        System.out.println("id: "+id);
        System.out.println("body: "+body);
        System.out.println("createdAt: "+createdAt);
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}

