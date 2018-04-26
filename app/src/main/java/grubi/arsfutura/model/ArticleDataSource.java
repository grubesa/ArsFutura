package grubi.arsfutura.model;

import android.arch.lifecycle.LiveData;

import java.util.List;

import grubi.arsfutura.model.data.Article;

public interface ArticleDataSource {

    public LiveData<List<Article>> getTopHeadlines(String sources, Integer pageSize, Integer page);

    public LiveData<List<Article>> getLatestNews(String country);

    public LiveData<List<Article>> getEverything(String q, Integer pageSize, Integer page);
}
