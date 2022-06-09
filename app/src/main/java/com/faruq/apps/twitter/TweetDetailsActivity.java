package com.faruq.apps.twitter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.faruq.apps.twitter.databinding.ActivityTweetDetailsBinding;
import com.faruq.apps.twitter.models.Tweet;
import com.faruq.apps.twitter.utils.DateFormatter;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {
    private static String TAG = "TweetDetailsActivity";
    private TextView nameTextView;
    private TextView usernameTextView;
    private ImageView profilePictureIV;
    private ImageView mediaImageView;
    private TextView bodyTextView;
    private TextView timeTV;
    private TextView dateTV;
    private TextView sourceTV;
    private TextView retweetCountTV;
    private TextView favoriteCountTV;
    private ImageButton replyButton;
    private ImageButton retweetButton;
    private ImageButton favoriteButton;
    
    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTweetDetailsBinding binding = ActivityTweetDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        // initialize View objects
        nameTextView = binding.nameTextView;
        usernameTextView = binding.usernameTextView;
        profilePictureIV = binding.profilePictureImageView;
        mediaImageView = binding.mediaImageView;
        bodyTextView = binding.bodyTextView;
        timeTV = binding.createdAtTimeTextView;
        dateTV = binding.createdAtDateTextView;
        sourceTV = binding.tweetSourceTextView;
        retweetCountTV = binding.retweetCountTextView;
        favoriteCountTV = binding.favoriteCountTextView;
        replyButton = binding.replyButton;
        retweetButton = binding.retweetButton;
        favoriteButton = binding.favoriteButton;


        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(TweetDetailsActivity.class.getSimpleName()));

        if (tweet.getMedia() != null){
            Glide.with(this)
                    .load(tweet.getMedia().getLarge())
                    .centerCrop()
                    .transform(new RoundedCorners(30))
                    .into(mediaImageView);
        } else {
            mediaImageView.setVisibility(View.GONE);
        }

        nameTextView.setText(tweet.getUser().getName());
        usernameTextView.setText("@"+tweet.getUser().getUserID());
        Glide.with(this)
                .load(tweet.getUser().getProfileImage())
                .centerCrop()
                .transform(new RoundedCorners(100))
                .into(profilePictureIV);
        bodyTextView.setText(tweet.getBody());
        timeTV.setText(DateFormatter.getTime(tweet.getCreatedAt()));
        dateTV.setText(DateFormatter.getDate(tweet.getCreatedAt()));
        sourceTV.setText(Html.fromHtml(tweet.getSource()));

        if (tweet.getTweetLiked() == true){
            renderFavoriteTweet();
        } else {
            renderUnfavoriteTweet();
        }

        if (tweet.getTweetRetweeted() == true){
            renderRetweetTweet();
        } else {
            renderUnretweetTweet();
        }

        TwitterClient twitterClient = TwitterApp.getRestClient(this);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        retweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.getTweetRetweeted() == true){
                    tweet.unretweetTweet();
                    renderUnretweetTweet();
                    twitterClient.unretweetTweet(tweet.getId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            tweet.retweetTweet();
                            renderRetweetTweet();
                            Log.e(TAG, "error occurred while unretweeting tweet: "+response, throwable);
                        }
                    });
                } else {
                    tweet.retweetTweet();
                    renderRetweetTweet();
                    twitterClient.retweetTweet(tweet.getId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            tweet.unretweetTweet();
                            renderUnretweetTweet();
                            Log.e(TAG, "error occurred while retweeting tweet: "+response, throwable);
                        }
                    });
                }
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.getTweetLiked() == true){
                    tweet.unfavoriteTweet();
                    renderUnfavoriteTweet();
                    twitterClient.unfavoriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            tweet.favoriteTweet();
                            renderFavoriteTweet();
                            Log.e(TAG, "error occurred while unfavoriting tweet: "+response, throwable);
                        }
                    });
                } else {
                    tweet.favoriteTweet();
                    renderFavoriteTweet();
                    twitterClient.favoriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            tweet.unfavoriteTweet();
                            renderUnfavoriteTweet();
                            Log.e(TAG, "error occurred while favoriting tweet: "+response, throwable);
                        }
                    });
                }
            }
        });


    }

    public void setRetweetCountTV(String count){
        retweetCountTV.setText(Html.fromHtml(String.format("<b>%s</b> Retweets", count)));
    }

    public void setFavoriteCountTV(String count){
        favoriteCountTV.setText(Html.fromHtml(String.format("<b>%s</b> Likes", count)));
    }

    // sets text for favorite count and image for favorite button
    public void renderFavoriteTweet(){
        // update button
        setFavoriteCountTV(tweet.getLikeCount().toString());
        favoriteButton.setImageResource(R.drawable.ic_vector_heart);
    }

    public void renderUnfavoriteTweet(){
        setFavoriteCountTV(tweet.getLikeCount().toString());
        favoriteButton.setImageResource(R.drawable.ic_vector_heart_stroke);
    }

    public void renderRetweetTweet(){
        setRetweetCountTV(tweet.getRetweetCount().toString());
        retweetButton.setImageResource(R.drawable.ic_vector_retweet_stroke_high);
    }

    public void renderUnretweetTweet(){
        setRetweetCountTV(tweet.getRetweetCount().toString());
        retweetButton.setImageResource(R.drawable.ic_vector_retweet_stroke);
    }
}