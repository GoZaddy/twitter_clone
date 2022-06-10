package com.faruq.apps.twitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.faruq.apps.twitter.databinding.ActivityComposeBinding;
import com.faruq.apps.twitter.models.Tweet;

import org.parceler.Parcels;

import java.util.Objects;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    private EditText tweetText;
    private TwitterClient twitterClient;
    private static final String TAG = "ComposeActivity";
    private MenuItem progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComposeBinding binding = ActivityComposeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button tweetButton = binding.button;
        tweetText = binding.tweetText;

        String action = getIntent().getStringExtra("action");
        if (Objects.equals(action, "compose")){
            String username = getIntent().getStringExtra("metadata_user_name");
            tweetText.setText(getString(R.string.compose_tweet_edit_text_reply_format, username));
            tweetText.requestFocus();
        }



        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweetText.getText().length() == 0){
                    Toast.makeText(ComposeActivity.this, "Tweet cannot be empty!", Toast.LENGTH_SHORT).show();
                } else if (tweetText.getText().length() > 280) {
                    Toast.makeText(ComposeActivity.this, "Tweet cannot be longer than 280 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar();
                    twitterClient = TwitterApp.getRestClient(ComposeActivity.this);
                    System.out.println("tweet text: "+tweetText.getText().toString());
                    twitterClient.postTweet(tweetText.getText().toString(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Tweet newTweet = new Tweet(json.jsonObject);
                            Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                            intent.putExtra("newTweet", Parcels.wrap(newTweet));
                            ComposeActivity.this.setResult(Activity.RESULT_OK, intent);
                            hideProgressBar();
                            ComposeActivity.this.finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            hideProgressBar();
                            Log.e(TAG, "Error while posting tweet: "+response, throwable);
                        }
                    });
                }
            }
        });


    }

    public void showProgressBar() {
        // Show progress item
        progressBar.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        progressBar.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        progressBar = menu.findItem(R.id.progress_bar);

        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}