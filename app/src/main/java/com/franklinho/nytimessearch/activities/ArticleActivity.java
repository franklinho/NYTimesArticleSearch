package com.franklinho.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;

import com.franklinho.nytimessearch.R;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {
    @Bind(R.id.wvArticle) WebView wvArticle;
    com.franklinho.nytimessearch.models.Article article;
    private ShareActionProvider miShareAction;
    @Bind(R.id.pB1) ProgressBar pB1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);



        article = (com.franklinho.nytimessearch.models.Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(article.getHeadline().getMain());
        setSupportActionBar(toolbar);


        wvArticle.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && pB1.getVisibility() == ProgressBar.GONE){
                    pB1.setVisibility(ProgressBar.VISIBLE);
                }
                pB1.setProgress(progress);
                if(progress == 100) {
                    pB1.setVisibility(ProgressBar.GONE);
                }
            }
        });
        wvArticle.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        wvArticle.loadUrl(article.getWebUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        android.support.v7.widget.ShareActionProvider miShare = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
        miShare.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }
}
