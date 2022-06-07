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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.faruq.apps.twitter.R;
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
        holder.bind(tweet, context);
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
        ImageView profileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView mediaIV;
        TextView usernameRelativeTime;

        public ViewHolder(@NonNull ItemTweetBinding itemView) {
            super(itemView.getRoot());

            profileImage = itemView.imageView;
            tvBody = itemView.tweetBody;
            tvScreenName = itemView.screenName;
            mediaIV = itemView.mediaIV;
            usernameRelativeTime = itemView.usernameRelativeTime;

        }

        public void bind(Tweet tweet, Context context){
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

        }
    }
}
