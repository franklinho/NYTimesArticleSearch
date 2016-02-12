package com.franklinho.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.franklinho.nytimessearch.utils.DynamicHeightImageView;
import com.franklinho.nytimessearch.R;
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
    private List<com.franklinho.nytimessearch.models.Article> mArticles;
    private final int WITH_IMAGE = 0, TEXT_ONLY=1;
    public int viewWidth;

    public ArticleArrayAdapter(List<com.franklinho.nytimessearch.models.Article> articles) {
        mArticles = articles;
    }

    public class ViewHolderText extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.tvTitle) TextView tvTitle;
        private Context context;
        public com.franklinho.nytimessearch.models.Article article;


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
//            i.putExtra("article", Parcels.wrap(article));
            i.putExtra("article", article);
            // launch the activity
            context.startActivity(i);

        }
    }

    public static class ViewHolderImageText extends  RecyclerView.ViewHolder  implements  View.OnClickListener {
        private Context context;
        public com.franklinho.nytimessearch.models.Article article;
        @Bind(R.id.tvTitle) TextView tvTitle;
        @Bind(R.id.ivImage)
        DynamicHeightImageView ivImage;
//        DynamicHeightImageView ivImage;


        public ViewHolderImageText(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        com.franklinho.nytimessearch.models.Article article = mArticles.get(position);

        final String thumbnail;
        if (article.getMultimedia() != null && article.getMultimedia().size() > 0) {
            thumbnail = article.getMultimedia().get(0).getUrl();
            if (TextUtils.isEmpty(thumbnail) || thumbnail == null) {
                return TEXT_ONLY;
            } else {
                return WITH_IMAGE;
            }
        } else {
            return TEXT_ONLY;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        if (viewType == WITH_IMAGE) {
            View articleViewImageText = inflater.inflate(R.layout.item_article_result, parent, false);
            ViewHolderImageText vhImageText = new ViewHolderImageText(articleViewImageText);
            return vhImageText;
        } else {
            View articleViewText = inflater.inflate(R.layout.item_article_no_image, parent, false);
            ViewHolderText vhText = new ViewHolderText(articleViewText);
            return vhText;
        }

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
        com.franklinho.nytimessearch.models.Article article = mArticles.get(position);
        TextView textView = vhImageText.tvTitle;
        vhImageText.article = article;
        final DynamicHeightImageView imageView;
        final String thumbnail;

        if (article.getHeadline().getMain() != null) {
            if (article.getSnippet() == null) {
                textView.setText(article.getHeadline().getMain());
            } else {
                textView.setText(Html.fromHtml("<b>" + article.getHeadline().getMain() + "</b><br>" + " " + article.getSnippet()));
            }
        }
        imageView = vhImageText.ivImage;

        imageView.setImageResource(0);
        thumbnail = article.getMultimedia().get(0).getUrl();


        if (!TextUtils.isEmpty(thumbnail)) {
            imageView.setHeightRatio((double) article.getMultimedia().get(0).getHeight() / article.getMultimedia().get(0).getWidth());
            Picasso.with(vhImageText.context).load(thumbnail).fit().into(imageView);
        }

    }

    private void configureTextOnlyViewHolder(ViewHolderText vhText, int position) {
        com.franklinho.nytimessearch.models.Article article = mArticles.get(position);
        TextView textView = vhText.tvTitle;
        vhText.article = article;

        if (article.getHeadline().getMain() != null) {
            if (article.getSnippet() == null) {
                textView.setText(article.getHeadline().getMain());
            } else {
                textView.setText(Html.fromHtml("<b>" + article.getHeadline().getMain() + "</b><br>" + " " + article.getSnippet()));
            }
        }
    }

}
