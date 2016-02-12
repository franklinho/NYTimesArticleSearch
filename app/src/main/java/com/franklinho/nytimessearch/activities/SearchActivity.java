package com.franklinho.nytimessearch.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.franklinho.nytimessearch.Article;
import com.franklinho.nytimessearch.ArticleArrayAdapter;
import com.franklinho.nytimessearch.EditSettingsDialog;
import com.franklinho.nytimessearch.EndlessRecyclerViewScrollListener;
import com.franklinho.nytimessearch.R;
import com.franklinho.nytimessearch.SpacesItemDecoration;
import com.franklinho.nytimessearch.models.NYTimesArticleResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.rvResults) RecyclerView rvResults;
    String NYTIMES_API_KEY = "3c6aa034b9301a603f43fdc6ce4ef667:5:74335560";
    ArrayList<com.franklinho.nytimessearch.models.Article> articles;
    ArticleArrayAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public String sharedQuery = "";
    @Bind(R.id.alertLayout) RelativeLayout alertLayout;
    @Bind(R.id.tvAlertText) TextView tvAlertText;
    @Bind(R.id.ivAlertImage) ImageView ivAlertImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.space_between_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rvResults.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvResults.setAdapter(adapter);
        final StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2,1);

        staggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvResults.setLayoutManager(staggeredLayoutManager);
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestArticles(page, sharedQuery);
            }
        });

        requestArticles(0,sharedQuery);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                sharedQuery = query;
                requestArticles(0, query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditSettingsDialog settingsDialog = EditSettingsDialog.newInstance();
        settingsDialog.show(fm, "fragment_edit_settings");
    }

//    private void updateFilterCount() {
//        int filterCount = 0;
//        if (preferences.getInt("newest", 0) != 0) {
//            filterCount += 1;
//        }
//        if (!preferences.getString("beginDate","MM/DD/YYYY").equals("MM/DD/YYYY")) {
//            filterCount += 1;
//        }
//        if (preferences.getBoolean("arts", false)) {
//            filterCount += 1;
//        }
//        if (preferences.getBoolean("fashion", false)) {
//            filterCount += 1;
//        }
//        if (preferences.getBoolean("sports", false)) {
//            filterCount += 1;
//        }
//
//    }
    public void requestArticles(final int page, String query) {
        if (isNetworkAvailable() && isOnline()) {
            alertLayout.setVisibility(View.GONE);
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
            RequestParams params = new RequestParams();

            if (query.length() > 0) {
                params.add("q", query);
            }
//        final int curSize;
            if (page == 0) {
                articles.clear();

//            curSize = 0;
            }
            params.add("api-key", NYTIMES_API_KEY);
            params.add("page",String.valueOf(page));
            if (preferences.getInt("newest", 0) != 0) {
                params.add("sort","oldest");
            }
            String beingDateString = preferences.getString("beginDate","MM/DD/YYYY");
            if (!beingDateString.equals("MM/DD/YYYY")) {
                String[] separated = beingDateString.split("/");
                String queryString = separated[2] + separated[0] + separated[1];
                Log.d("DEBUG", queryString);
                params.add("begin_date",queryString);
            }

            String endDateString = preferences.getString("endDate","MM/DD/YYYY");
            if (!endDateString.equals("MM/DD/YYYY")) {
                String[] separated = endDateString.split("/");
                String queryString = separated[2] + separated[0] + separated[1];
                Log.d("DEBUG", queryString);
                params.add("end_date",queryString);
            }

            Boolean arts = preferences.getBoolean("arts", false);
            Boolean fashion = preferences.getBoolean("fashion", false);
            Boolean sports = preferences.getBoolean("sports", false);

            if (arts || fashion || sports) {
                String newsDeskString = "";
                if (arts) {
                    newsDeskString = newsDeskString + "\"Arts\"";
                }
                if (fashion) {
                    newsDeskString = newsDeskString + "\"Fashion & Style\"";
                }
                if (sports) {
                    newsDeskString = newsDeskString + "\"Sports\"";
                }
                params.add("fq","news_desk:(" + newsDeskString + ")");
                Log.d("DEBUG","news_desk:(" + newsDeskString + ")");
            }

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

//                    JSONArray articleJsonResults = null;
                    String articleJsonResults = null;
                    try {
                        int curSize = articles.size();
                        articleJsonResults = response.getJSONObject("response").toString();
                        if (articleJsonResults.length() > 0 && articleJsonResults != null) {
//                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            NYTimesArticleResponse articleResponse = NYTimesArticleResponse.parseJSON(articleJsonResults);
                            articles.addAll(articleResponse.getArticles());
                            Log.d("DEBUG", articles.toString());
                            if (page > 0) {
                                adapter.notifyItemRangeInserted(curSize, articles.size()-1);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            setAlertToNoItemsError();
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            setAlertToNetworkConnectionError();
        }
    }

    public void onArticleSearch(View view) {

        requestArticles(0, sharedQuery);
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    private void setAlertToNetworkConnectionError() {
        alertLayout.setBackgroundColor(R.color.yellow);
        tvAlertText.setText("Network Connection Error");
        ivAlertImage.setVisibility(View.VISIBLE);
        alertLayout.setVisibility(View.VISIBLE);
    }

    private void setAlertToNoItemsError() {
        alertLayout.setBackgroundColor(R.color.white);
        tvAlertText.setText("Search Returned No Items");
        ivAlertImage.setVisibility(View.INVISIBLE);
        alertLayout.setVisibility(View.VISIBLE);
    }

}
