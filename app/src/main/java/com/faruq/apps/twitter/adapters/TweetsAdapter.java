package com.faruq.apps.twitter.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faruq.apps.twitter.databinding.ItemTweetBinding;
import com.faruq.apps.twitter.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    Context context;
    List<Tweet> tweets;
    ItemTweetBinding itemTweetBinding;

    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;

        this.tweets = tweets;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemTweetBinding = ItemTweetBinding.inflate(LayoutInflater.from(this.context));
        return new ViewHolder(itemTweetBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView tvBody;
        TextView tvScreenName;

        public ViewHolder(@NonNull ItemTweetBinding itemView) {
            super(itemView.getRoot());

            profileImage = itemView.imageView;
            tvBody = itemView.tweetBody;
            tvScreenName = itemView.screenName;


        }

        public void bind(Tweet tweet){
            tvBody.setText(tweet.getBody());
            tvScreenName.setText(tweet.getUser().getName());
            Glide.with(context).load(tweet.getUser().getProfileImage()).into(profileImage);
        }
    }
}
