package com.franklinho.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.franklinho.nytimessearch.activities.ArticleActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        public ImageView ivImage;
        public TextView tvTitle;
        private Context context;
        public Article article;

        public ViewHolder(View itemView) {
            super (itemView);

            context=itemView.getContext();
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Intent i = new Intent(context, ArticleActivity.class);
            //get the article to display
            // pass article into intent
            i.putExtra("article", article);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticles.get(position);


        TextView textView = holder.tvTitle;
        ImageView imageView = holder.ivImage;
        holder.article = article;

        imageView.setImageResource(0);
        textView.setText(article.getHeadline());

        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(holder.context).load(thumbnail).into(imageView);

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
