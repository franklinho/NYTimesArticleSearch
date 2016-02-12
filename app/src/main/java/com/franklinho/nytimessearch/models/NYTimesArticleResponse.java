package com.franklinho.nytimessearch.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franklinho on 2/11/16.
 */
public class NYTimesArticleResponse {
    public List<Article> getArticles() {
        return docs;
    }

    List<Article> docs;

    public NYTimesArticleResponse() {
        docs = new ArrayList<Article>();
    }

    public static NYTimesArticleResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        NYTimesArticleResponse nyTimesArticleResponse = gson.fromJson(response, NYTimesArticleResponse.class);
        return nyTimesArticleResponse;
    }
}
