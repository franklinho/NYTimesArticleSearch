package com.franklinho.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    public ArticleArrayAdapter(Context context, List<Article> articles) {
//        super(context, android.R.layout.simple_list_item_1, articles);
//    }
    private List<Article> mArticles;
    private final int WITH_IMAGE = 0, TEXT_ONLY=1;

    public ArticleArrayAdapter(List<Article> articles) {
        mArticles = articles;
    }

    public class ViewHolderText extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.tvTitle) TextView tvTitle;
        private Context context;
        public Article article;

        public ViewHolderText(View itemView) {
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

    public static class ViewHolderImageText extends  RecyclerView.ViewHolder  implements  View.OnClickListener {
        private Context context;
        public Article article;
        @Bind(R.id.tvTitle) TextView tvTitle;
        @Bind(R.id.ivImage) ImageView ivImage;

        public ViewHolderImageText(View itemView) {
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
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article article = mArticles.get(position);
        final String thumbnail = article.getThumbNail();
        if (TextUtils.isEmpty(thumbnail) || thumbnail == null) {
            return TEXT_ONLY;
        } else {
            return WITH_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        switch (viewType) {
            case WITH_IMAGE:
                View articleViewImageText = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolderImageText(articleViewImageText);
                break;
            case TEXT_ONLY:
                View articleViewText = inflater.inflate(R.layout.item_article_no_image, parent, false);
                viewHolder = new ViewHolderText(articleViewText);
                break;
            default:
                View articleViewDefault = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolderImageText(articleViewDefault);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case WITH_IMAGE:
                ViewHolderImageText vhImageText = (ViewHolderImageText) holder;
                configureImageTextViewHolder(vhImageText,position);
                break;
            case TEXT_ONLY:
                ViewHolderText vhText = (ViewHolderText) holder;
                configureTextOnlyViewHolder(vhText,position);
                break;
            default:
                ViewHolderImageText vhDefault = (ViewHolderImageText) holder;
                configureImageTextViewHolder(vhDefault,position);
                break;
        }



    }

    private void configureImageTextViewHolder(final ViewHolderImageText vhImageText, int position) {
        Article article = mArticles.get(position);
        TextView textView = vhImageText.tvTitle;
        vhImageText.article = article;
        final ImageView imageView;
        final String thumbnail;

        if (article.getHeadline() != null) {
            if (article.getSnippet() == null) {
                textView.setText(article.getHeadline());
            } else {
                textView.setText(Html.fromHtml("<b>" + article.getHeadline() + "</b><br>" + " " + article.getSnippet()));
            }
        }
        imageView = vhImageText.ivImage;
        imageView.setImageResource(0);
        thumbnail = article.getThumbNail();
        imageView.setImageResource(0);
        if (!TextUtils.isEmpty(thumbnail)) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Picasso.with(vhImageText.context).load(thumbnail).resize(imageView.getWidth(), 0).into(imageView);
                    // Decided not to go with Glide due to automatic resizing
//                    Glide.with(holder.context).load(thumbnail).into(imageView);
                }
            });
        }

    }

    private void configureTextOnlyViewHolder(ViewHolderText vhText, int position) {
        Article article = mArticles.get(position);
        TextView textView = vhText.tvTitle;
        vhText.article = article;

        if (article.getHeadline() != null) {
            if (article.getSnippet() == null) {
                textView.setText(article.getHeadline());
            } else {
                textView.setText(Html.fromHtml("<b>" + article.getHeadline() + "</b><br>" + " " + article.getSnippet()));
            }
        }
    }

}
