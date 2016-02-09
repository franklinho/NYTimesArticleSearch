package com.franklinho.nytimessearch.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
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

import com.franklinho.nytimessearch.Article;
import com.franklinho.nytimessearch.ArticleArrayAdapter;
import com.franklinho.nytimessearch.EditSettingsDialog;
import com.franklinho.nytimessearch.R;
import com.franklinho.nytimessearch.SpacesItemDecoration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.rvResults) RecyclerView rvResults;
    String NYTIMES_API_KEY = "3c6aa034b9301a603f43fdc6ce4ef667:5:74335560";
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rvResults.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(new StaggeredGridLayoutManager(2,1));
//        rvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // create an intent to display the article
//                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
//                //get the article to display
//                Article article = articles.get(position);
//                // pass article into intent
//                i.putExtra("article", article);
//                // launch the activity
//                startActivity(i);
//            }
//        });

        requestArticles(0, "");
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

                requestArticles(0,query);
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

    public void requestArticles(int page, String query) {
        if (page == 0) {
            articles.clear();
        }
//        String query = etQuery.getText().toString();
//        Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.add("api-key", NYTIMES_API_KEY);
        params.add("page", "0");
        if (preferences.getInt("newest", 0) != 0) {
            params.add("sort","oldest");
        }
        String dateString = preferences.getString("beginDate","MM/DD/YYYY");
        if (!dateString.equals("MM/DD/YYYY")) {
            String[] separated = dateString.split("/");
            String queryString = separated[2] + separated[0] + separated[1];
            Log.d("DEBUG",queryString);
            params.add("begin_date",queryString);
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





        if (query.length() > 0) {
            params.add("q", query);
        }
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.d("DEBUG", articles.toString());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void onArticleSearch(View view) {

        requestArticles(0, "");
    }




}
