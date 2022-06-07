package com.faruq.apps.twitter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.faruq.apps.twitter.adapters.TweetsAdapter;
import com.faruq.apps.twitter.databinding.ActivityTimelineBinding;
import com.faruq.apps.twitter.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = "TimelineActivity";

    TwitterClient client;
    TimelineActivity context;
    ActivityTimelineBinding activityTimelineBinding;
    ActivityResultLauncher<Intent> newTweetLauncher;
    List<Tweet> tweets;
    TweetsAdapter tweetsAdapter;
    RecyclerView tweetsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTimelineBinding = ActivityTimelineBinding.inflate(LayoutInflater.from(this));
        setContentView(activityTimelineBinding.getRoot());

        tweets = new ArrayList<Tweet>();

        tweetsAdapter = new TweetsAdapter(this, tweets);

        tweetsRecyclerView = activityTimelineBinding.recylerView;
        tweetsRecyclerView.setAdapter(tweetsAdapter);
        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

         newTweetLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    if (result.getData() != null) {
                        Tweet newTweet = (Tweet) Parcels.unwrap(result.getData().getParcelableExtra("newTweet"));
                        tweets.add(0, newTweet);
                        tweetsAdapter.notifyDataSetChanged();
                        tweetsRecyclerView.smoothScrollToPosition(0);
                    }
                }


            }
        });

        activityTimelineBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTweetLauncher.launch(new Intent(TimelineActivity.this, ComposeActivity.class));
            }
        });

        client = TwitterApp.getRestClient(this);
        populateHomeTimeline();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.log_out) {
            client.clearAccessToken();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
            startActivity(i);
            return true;
        } else if (id == R.id.compose_tweet){
            newTweetLauncher.launch(new Intent(TimelineActivity.this, ComposeActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateHomeTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                tweetsAdapter.notifyDataSetChanged();
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