package com.faruq.apps.twitter.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.faruq.apps.twitter.ComposeActivity;
import com.faruq.apps.twitter.R;
import com.faruq.apps.twitter.TweetDetailsActivity;
import com.faruq.apps.twitter.TwitterApp;
import com.faruq.apps.twitter.TwitterClient;
import com.faruq.apps.twitter.databinding.ItemTweetBinding;
import com.faruq.apps.twitter.models.Tweet;
import com.faruq.apps.twitter.utils.FormatNumbers;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    private final Context context;
    private final List<Tweet> tweets;


    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // make sure to specify parent of the recyclerView item so the root element at item_tweet.xml has access to its parent
        ItemTweetBinding itemTweetBinding = ItemTweetBinding.inflate(LayoutInflater.from(this.context), parent, false);
        return new ViewHolder(itemTweetBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position, context);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView profileImage;
        private final TextView tvBody;
        private final TextView tvScreenName;
        private final ImageView mediaIV;
        private final TextView usernameRelativeTime;
        private final TextView retweetCountTextView;
        private final TextView favoriteCountTextView;

        private final ImageButton replyButton;
        private final ImageButton retweetButton;
        private final ImageButton favoriteButton;

        private final TextView retweetStatusTV;

        private final ConstraintLayout constraintLayout;
        private final ImageView retweetStatusIcon;

        private static final String TAG = "TweetsAdapterViewHolder";


        public ViewHolder(@NonNull ItemTweetBinding itemView) {
            super(itemView.getRoot());

            profileImage = itemView.imageView;
            tvBody = itemView.tweetBody;
            tvScreenName = itemView.screenName;
            mediaIV = itemView.mediaIV;
            usernameRelativeTime = itemView.usernameRelativeTime;

            retweetCountTextView = itemView.retweetCount;
            favoriteCountTextView = itemView.favoriteCount;

            replyButton = itemView.replyButton;
            retweetButton = itemView.retweetButton;
            favoriteButton = itemView.favoriteButton;

            retweetStatusTV = itemView.retweetStatusTextView;

            constraintLayout = itemView.constraintLayout;
            retweetStatusIcon = itemView.retweetStatusRetweetIcon;
        }

        public void bind(int position, Context context){
            Tweet initialTweet = tweets.get(position);
            String message = null;

            if (initialTweet.getRetweetedStatus() != null){
                message = initialTweet.getUser().getName()+" Retweeted";
                initialTweet = initialTweet.getRetweetedStatus();

            }
            Tweet tweet = initialTweet;
            if (message != null){
                // constrain top of profile image to bottom of retweet icon
                retweetStatusIcon.setVisibility(View.VISIBLE);
                retweetStatusTV.setVisibility(View.VISIBLE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(profileImage.getId(),ConstraintSet.TOP,retweetStatusIcon.getId(),ConstraintSet.BOTTOM,40);
                constraintSet.applyTo(constraintLayout);

                retweetStatusTV.setText(message);
            } else {
                retweetStatusIcon.setVisibility(View.GONE);
                retweetStatusTV.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(profileImage.getId(),ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP,0);
                constraintSet.applyTo(constraintLayout);
                // constrain top of profile image to top of constraint layout
            }
            tvBody.setText(tweet.getBody());
            tvScreenName.setText(tweet.getUser().getName());
            usernameRelativeTime.setText(context.getString(R.string.username_relative_time_format, tweet.getUser().getUserID(), tweet.getRelativeTime()));
            Glide.with(context)
                    .load(tweet.getUser().getProfileImage())
                    .centerCrop()
                    .transform(new RoundedCorners(100))
                    .into(profileImage);


            if (tweet.getMedia() != null){
                mediaIV.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.getMedia().getThumb()).placeholder(R.drawable.placeholder_image)
                        .into(mediaIV);
            } else {
                mediaIV.setVisibility(View.GONE);
            }

            TwitterClient twitterClient = TwitterApp.getRestClient(context);
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tweet.getTweetLiked() == true){
                        tweet.unfavoriteTweet();
                        tweets.set(position, tweet);
                        notifyItemChanged(position);
                        twitterClient.unfavoriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {

                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                tweet.favoriteTweet();
                                tweets.set(position, tweet);
                                notifyItemChanged(position);
                                Log.e(TAG, "error occurred while unfavoriting tweet: "+response, throwable);
                            }
                        });
                    } else {
                        tweet.favoriteTweet();
                        tweets.set(position, tweet);
                        notifyItemChanged(position);
                        twitterClient.favoriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                tweet.unfavoriteTweet();
                                tweets.set(position, tweet);
                                notifyItemChanged(position);
                                Log.e(TAG, "error occurred while favoriting tweet: "+response, throwable);
                            }
                        });
                    }

                }
            });

            retweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tweet.getTweetRetweeted() == true){
                        tweet.unretweetTweet();
                        tweets.set(position, tweet);
                        notifyItemChanged(position);
                        twitterClient.unretweetTweet(tweet.getId(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                tweet.retweetTweet();
                                tweets.set(position, tweet);
                                notifyItemChanged(position);
                                Log.e(TAG, "error occurred while unretweeting tweet: "+response, throwable);
                            }
                        });
                    } else {
                        tweet.retweetTweet();
                        tweets.set(position, tweet);
                        notifyItemChanged(position);
                        twitterClient.retweetTweet(tweet.getId(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                tweet.unretweetTweet();
                                tweets.set(position, tweet);
                                notifyItemChanged(position);
                                Log.e(TAG, "error occurred while retweeting tweet: "+response, throwable);
                            }
                        });
                    }

                }
            });

            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("action", "compose");
                    intent.putExtra("metadata_user_name", tweet.getUser().getUserID());
                    context.startActivity(intent);
                }
            });



            if (tweet.getRetweetCount() > 0){
                retweetCountTextView.setText(FormatNumbers.ShortenNumber(tweet.getRetweetCount()));
            } else {
                retweetCountTextView.setText("");
            }

            if (tweet.getLikeCount() > 0){
                favoriteCountTextView.setText(FormatNumbers.ShortenNumber(tweet.getLikeCount()));
            } else {
                favoriteCountTextView.setText("");
            }






            if(tweet.getTweetLiked() == true){
                favoriteButton.setImageResource(R.drawable.ic_vector_heart);
                favoriteCountTextView.setTextColor(context.getResources().getColor(R.color.favoritedTweetRed));
            } else {
                favoriteButton.setImageResource(R.drawable.ic_vector_heart_stroke);
                favoriteCountTextView.setTextColor(context.getResources().getColor(R.color.defaultTweetActionsTextColor));
            }

            if (tweet.getTweetRetweeted() == true){
                retweetButton.setImageResource(R.drawable.ic_vector_retweet_stroke_high);
                retweetCountTextView.setTextColor(context.getResources().getColor(R.color.retweetedTweetGreen));
            } else {
                retweetButton.setImageResource(R.drawable.ic_vector_retweet_stroke);
                retweetCountTextView.setTextColor(context.getResources().getColor(R.color.defaultTweetActionsTextColor));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TweetDetailsActivity.class);
                    intent.putExtra(TweetDetailsActivity.class.getSimpleName(), Parcels.wrap(tweet));
                    intent.putExtra("tweet_position", position);
                    context.startActivity(intent);
                }
            });

        }
    }
}
