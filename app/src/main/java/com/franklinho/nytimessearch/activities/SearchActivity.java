package com.franklinho.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.franklinho.nytimessearch.adapters.ArticleArrayAdapter;
import com.franklinho.nytimessearch.fragments.EditSettingsDialog;
import com.franklinho.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import com.franklinho.nytimessearch.R;
import com.franklinho.nytimessearch.interfaces.SpacesItemDecoration;
import com.franklinho.nytimessearch.models.NYTimesArticleResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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
    EditSettingsDialog settingsDialog;
    public String dateType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //Set NYTimes Icon
        actionBar.setLogo(R.drawable.space_between_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Prepare sharedpreferences for filters
        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        //set up spacing for Recyclerview
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rvResults.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvResults.setAdapter(adapter);
        //Set up layout type for RecyclerView
        final StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(2,1);

        staggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvResults.setLayoutManager(staggeredLayoutManager);
        //Prepare RecyclerView for infinite scroll
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
        //Implement search bar
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

        // Handle clearing search
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.search_src_text);
                et.setText("");
                sharedQuery = "";
                requestArticles(0,sharedQuery);
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

    //Shows filters
    private void showSettingsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        settingsDialog = EditSettingsDialog.newInstance();
        settingsDialog.show(fm, "fragment_edit_settings");
    }

    public void requestArticles(final int page, String query) {
        // Check for network connectivity
        if (isNetworkAvailable() && isOnline()) {
            alertLayout.setVisibility(View.GONE);
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
            RequestParams params = new RequestParams();

            if (query.length() > 0) {
                params.add("q", query);
            }

            // Clear articles for new queries
            if (page == 0) {
                articles.clear();

            }
            params.add("api-key", NYTIMES_API_KEY);
            params.add("page",String.valueOf(page));
            if (preferences.getInt("newest", 0) != 0) {
                params.add("sort","oldest");
            }

            // Add filters for begin date and end date
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

            // Ad filter for news desk items
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

            //Make request
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

//                    JSONArray articleJsonResults = null;
                    JSONArray articleJsonResults = null;
                    String articleJsonResultsString;
                    try {
                        int curSize = articles.size();

                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        articleJsonResultsString = response.getJSONObject("response").toString();

                        if (articleJsonResults.length() > 0 && articleJsonResults != null) {
//                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            //Parse JSON with GSON Class
                            NYTimesArticleResponse articleResponse = NYTimesArticleResponse.parseJSON(articleJsonResultsString);
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


    //Checks if network is available
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // Check if user is connected to the internet
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

    //Shows alert if not connected to internet
    private void setAlertToNetworkConnectionError() {
        alertLayout.setBackgroundColor(R.color.yellow);
        tvAlertText.setText("Network Connection Error");
        ivAlertImage.setVisibility(View.VISIBLE);
        alertLayout.setVisibility(View.VISIBLE);
    }

    //Shows alert if there are no items
    private void setAlertToNoItemsError() {
        alertLayout.setBackgroundColor(R.color.white);
        tvAlertText.setText("Search Returned No Items");
        ivAlertImage.setVisibility(View.INVISIBLE);
        alertLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (dateType == "beginDate") {
            settingsDialog.setBeginDate(format.format(c.getTime()));
        } else if (dateType == "endDate") {
            settingsDialog.setEndDate(format.format(c.getTime()));
        }

    }
}
