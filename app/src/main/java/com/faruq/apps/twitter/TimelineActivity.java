package com.faruq.apps.twitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.faruq.apps.twitter.adapters.TweetsAdapter;
import com.faruq.apps.twitter.databinding.ActivityTimelineBinding;
import com.faruq.apps.twitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = "TimelineActivity";

    TwitterClient client;
    TimelineActivity context;
    ActivityTimelineBinding activityTimelineBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTimelineBinding = ActivityTimelineBinding.inflate(LayoutInflater.from(this));
        setContentView(activityTimelineBinding.getRoot());

        client = TwitterApp.getRestClient(this);
        context = this;
        populateHomeTimeline();

    }

    private void populateHomeTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                List<Tweet> tweets = Tweet.fromJsonArray(json.jsonArray);

                TweetsAdapter tweetsAdapter = new TweetsAdapter(context, tweets);

                RecyclerView tweetsRecyclerView = activityTimelineBinding.recylerView;
                tweetsRecyclerView.setAdapter(tweetsAdapter);
                tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(context));



//                Log.i(TAG, "onSuccess: "+json.toString());

                for(Tweet tweet: tweets){
                    tweet.print();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure", throwable);
            }
        });
    }
}