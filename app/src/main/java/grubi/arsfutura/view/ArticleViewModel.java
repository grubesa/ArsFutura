package grubi.arsfutura.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import grubi.arsfutura.model.ArticleDataSource;
import grubi.arsfutura.model.ArticleRepository;
import grubi.arsfutura.model.data.Article;
import grubi.arsfutura.model.data.OrientationResource;
import grubi.arsfutura.util.SearchLiveData;

public class ArticleViewModel extends AndroidViewModel {

    private final static String COUNTRY = "us";
    private final static String SOURCES = "bbc-news";
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static int DEFAULT_PAGE = 1;

    private ArticleDataSource dataSource;

    private final MediatorLiveData<List<OrientationResource>> articles = new MediatorLiveData<>();
    private LiveData<List<Article>> topHeadlineArticles;
    private LiveData<List<Article>> latestNewArticles;

    private final MediatorLiveData<List<OrientationResource>> searchArticles = new
            MediatorLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final MutableLiveData<Integer> searchPage = new MutableLiveData<>();
    private final SearchLiveData searchTrigger = new SearchLiveData(searchQuery, searchPage);
    private final LiveData<List<Article>> searchResultArticles = Transformations.switchMap(this
            .searchTrigger, trigger -> this.dataSource.getEverything(trigger.first,
            DEFAULT_PAGE_SIZE, trigger.second));

    private boolean searchQueryChanged;

    public ArticleViewModel(@NonNull Application application) {
        super(application);

        this.searchPage.setValue(DEFAULT_PAGE);
        this.dataSource = ArticleRepository.getInstance(this.getApplication());

        this.topHeadlineArticles = this.dataSource.getTopHeadlines(SOURCES, DEFAULT_PAGE_SIZE,
                DEFAULT_PAGE);
        this.latestNewArticles = this.dataSource.getLatestNews(COUNTRY);

        this.articles.addSource(this.topHeadlineArticles, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> data) {
                List<OrientationResource> articleList = new ArrayList<>();
                articleList.add(new OrientationResource<>(data));
                if (latestNewArticles.getValue() != null) {
                    articleList.add(new OrientationResource<>(latestNewArticles.getValue()));
                }
                articles.setValue(articleList);
            }
        });
        this.articles.addSource(this.latestNewArticles, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> data) {
                List<OrientationResource> articleList = new ArrayList<>();
                if (topHeadlineArticles.getValue() != null) {
                    articleList.add(new OrientationResource<>(topHeadlineArticles.getValue()));
                }
                for (Article article : data) {
                    articleList.add(new OrientationResource(article));
                }
                articles.setValue(articleList);
            }
        });
        this.searchArticles.addSource(this.searchResultArticles, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> data) {
                List<OrientationResource> articleList = new ArrayList<>();
                if (!searchQueryChanged) {
                    articleList.addAll(searchArticles.getValue());
                }
                for (Article article : data) {
                    articleList.add(new OrientationResource(article));
                }
                searchArticles.setValue(articleList);
            }
        });
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQueryChanged = true;
        this.searchPage.setValue(DEFAULT_PAGE);
        this.searchQuery.setValue(searchQuery);
    }

    public void setNextPage() {
        this.searchQueryChanged = false;
        this.searchPage.setValue(this.searchPage.getValue() + 1);
    }

    public MediatorLiveData<List<OrientationResource>> getArticles() {
        return this.articles;
    }

    public MediatorLiveData<List<OrientationResource>> getSearchArticles() {
        return this.searchArticles;
    }
}
