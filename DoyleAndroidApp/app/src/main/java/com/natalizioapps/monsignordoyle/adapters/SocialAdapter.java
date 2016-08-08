package com.natalizioapps.monsignordoyle.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.objects.Tweet;

import java.util.List;

//TODO: COMMENT

public class SocialAdapter extends ArrayAdapter<Tweet> {
    private final Activity context;
    private final List<Tweet> tweets;

    static class ViewHolder {
        public TextView content;
        public TextView date;
    }

    public SocialAdapter(Activity context, List<Tweet> tweets) {
        super(context, R.layout.social_tweet, tweets);
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View tweetView = convertView;
        // reuse views
        if (tweetView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            tweetView = inflater.inflate(R.layout.social_tweet, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.content = (TextView) tweetView.findViewById(R.id.social_tweet_content);
            viewHolder.date = (TextView) tweetView.findViewById(R.id.social_tweet_date);
            tweetView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) tweetView.getTag();
        Tweet p = tweets.get(position);
        holder.content.setText(p.getContent());
        holder.date.setText(p.getTime());

        return tweetView;
    }
} 
