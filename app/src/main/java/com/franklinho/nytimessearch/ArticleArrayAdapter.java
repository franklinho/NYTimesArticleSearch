package com.franklinho.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.franklinho.nytimessearch.activities.ArticleActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by franklinho on 2/8/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {
//    public ArticleArrayAdapter(Context context, List<Article> articles) {
//        super(context, android.R.layout.simple_list_item_1, articles);
//    }
    private List<Article> mArticles;

    public ArticleArrayAdapter(List<Article> articles) {
        mArticles = articles;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder  implements  View.OnClickListener {
        private Context context;
        public Article article;
        @Bind(R.id.tvTitle) TextView tvTitle;
        @Bind(R.id.ivImage) ImageView ivImage;

        public ViewHolder(View itemView) {
            super (itemView);
            ButterKnife.bind(this,itemView);


            context=itemView.getContext();


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Intent i = new Intent(context, ArticleActivity.class);
            //get the article to display
            // pass article into intent
            i.putExtra("article", Parcels.wrap(article));
            // launch the activity
            context.startActivity(i);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article article = mArticles.get(position);


        TextView textView = holder.tvTitle;
        final ImageView imageView = holder.ivImage;
        holder.article = article;

        imageView.setImageResource(0);
        if (article.getHeadline() != null) {
            textView.setText(article.getHeadline());
        }


        final String thumbnail = article.getThumbNail();
        imageView.setImageResource(0);
        if (!TextUtils.isEmpty(thumbnail)) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Picasso.with(holder.context).load(thumbnail).resize(imageView.getWidth(), 0).into(imageView);
                }


            });


        }

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // get data item for position
//        Article article = this.getItem(position);
//        // check to see if existing view is being reused
//        // not using recycled view -> inflate the layout
//        if (convertView == null) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
//        }
//
//
//        // find the image view
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
//
//
//        //clear out the recycled image from convertview
//        imageView.setImageResource(0);
//        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//        tvTitle.setText(article.getHeadline());
//
//        //populate the thumbnail image
//        //remote download the image in the background
//
//        String thumbnail = article.getThumbNail();
//        if (!TextUtils.isEmpty(thumbnail)) {
//            Picasso.with(getContext()).load(thumbnail).into(imageView);
//
//        }
//
//        return convertView;
//    }
}
