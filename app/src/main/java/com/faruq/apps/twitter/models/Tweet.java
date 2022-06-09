package com.faruq.apps.twitter.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import com.faruq.apps.twitter.models.User;

@Parcel
public class Tweet {
    // Define database columns and associated fields
    private String id;

    private String body;

    private String createdAt;

    // Use @Embedded to keep the column entries as part of the same table while still
    // keeping the logical separation between the two objects.
    private User user;

    private Media media;

    private Boolean isTweetLiked;

    private Boolean isTweetRetweeted;

    private Integer retweetCount;

    private Integer likeCount;


    // tweet that was retweeted if this tweet is a retweet
    private Tweet retweetedStatus;

    private String source;



    public Tweet(){}


    public Tweet(JSONObject object){
        try {
            this.id = object.getString("id");
            this.user = User.fromJSON(object.getJSONObject("user"));
            this.body = object.getString("text");
            this.createdAt = object.getString("created_at");
            this.isTweetLiked = object.getBoolean("favorited");
            this.isTweetRetweeted = object.getBoolean("retweeted");
            this.retweetCount = object.getInt("retweet_count");
            this.likeCount = object.getInt("favorite_count");
            this.source = object.getString("source");

            try {
                this.retweetedStatus = new Tweet(object.getJSONObject("retweetedStatus"));
            } catch (JSONException jsonException){
                this.retweetedStatus = null;
            }


            try{
                this.media = Media.fromJSON(object.getJSONObject("entities").getJSONArray("media").getJSONObject(0));
            } catch (JSONException jsonException){
                this.media = null;
            }
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

    private class GetRelativeTime {
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
        private static final String TAG = "GetRelativeTime";

        public GetRelativeTime() {
        }

        public String getRelativeTimeAgo() {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            try {
                long time = sf.parse(Tweet.this.createdAt).getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "1 m";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + " m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "1 h";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + " h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + " d";
                }
            } catch (ParseException e) {
                Log.i(TAG, "getRelativeTimeAgo failed");
                e.printStackTrace();
            }

            return "";
        }
    }

    public String getRelativeTime(){
        GetRelativeTime getRelativeTime = new GetRelativeTime();
        return getRelativeTime.getRelativeTimeAgo();
    }

    public void print(){
        System.out.println("id: "+id);
        System.out.println("body: "+body);
        System.out.println("createdAt: "+createdAt);
        System.out.println("like count: "+this.getLikeCount());
        System.out.println("retweet count: "+this.getRetweetCount());
        System.out.println("is retweeted: "+this.getTweetRetweeted());
        System.out.println("is liked: "+this.getTweetLiked());
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

    public Media getMedia() {
        return media;
    }

    public Boolean getTweetLiked() {
        return isTweetLiked;
    }

    public Boolean getTweetRetweeted() {
        return isTweetRetweeted;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Tweet getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setTweetLiked(Boolean tweetLiked) {
        isTweetLiked = tweetLiked;
    }

    public void setTweetRetweeted(Boolean tweetRetweeted) {
        isTweetRetweeted = tweetRetweeted;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void favoriteTweet(){
        this.setTweetLiked(true);
        this.setLikeCount(this.likeCount+1);
    }

    public void unfavoriteTweet(){
        this.setTweetLiked(false);
        this.setLikeCount(this.likeCount-1);
    }

    public void retweetTweet(){
        this.setTweetRetweeted(true);
        this.setRetweetCount(this.retweetCount+1);
    }

    public void unretweetTweet(){
        this.setTweetRetweeted(false);
        this.setRetweetCount(this.retweetCount-1);
    }

    public String getSource() {
        if (Objects.equals(this.source, "web")){
            return "Twitter Web App";
        }
        return source;
    }
}

