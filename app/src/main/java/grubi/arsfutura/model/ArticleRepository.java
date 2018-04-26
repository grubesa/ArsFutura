package grubi.arsfutura.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;

import java.util.List;

import grubi.arsfutura.model.data.Article;
import grubi.arsfutura.model.data.ArticleResource;
import grubi.arsfutura.model.source.remote.ArticleService;
import grubi.arsfutura.model.source.remote.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository implements ArticleDataSource {

    private volatile static ArticleRepository INSTANCE = null;

    private ArticleService remoteDataSource;
    private Call<ArticleResource> callInProgress;

    private final MediatorLiveData<List<Article>> searchArticles = new MediatorLiveData<>();
    private final MediatorLiveData<List<Article>> topHeadlinesArticles = new MediatorLiveData<>();
    private final MediatorLiveData<List<Article>> latestNewsArticles = new MediatorLiveData<>();

    private ArticleRepository(Context context) {
        this.remoteDataSource = RestClient.getArticleService();
    }

    public static ArticleRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ArticleRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticleRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<Article>> getTopHeadlines(String sources, Integer pageSize, Integer page) {
        this.remoteDataSource.getTopHeadlines(sources, pageSize, page).enqueue(new Callback<ArticleResource>() {
            @Override
            public void onResponse(Call<ArticleResource> call, Response<ArticleResource> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        topHeadlinesArticles.postValue(response.body().getArticles());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArticleResource> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return this.topHeadlinesArticles;
    }

    @Override
    public LiveData<List<Article>> getLatestNews(String country) {
        Call<ArticleResource> call = this.remoteDataSource.getLatestNews(country);
        if (this.callInProgress != null) {
            this.callInProgress.cancel();
            this.callInProgress = null;
        }
        call.enqueue(new Callback<ArticleResource>() {
            @Override
            public void onResponse(Call<ArticleResource> call, Response<ArticleResource> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        latestNewsArticles.setValue(response.body().getArticles());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArticleResource> call, Throwable t) {
                t.printStackTrace();
            }
        });
        this.callInProgress = call;
        return this.latestNewsArticles;
    }

    @Override
    public LiveData<List<Article>> getEverything(String q, Integer pageSize, Integer page) {
        this.remoteDataSource.getEverything(q, pageSize, page).enqueue(new Callback<ArticleResource>() {
            @Override
            public void onResponse(Call<ArticleResource> call, Response<ArticleResource> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        searchArticles.postValue(response.body().getArticles());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArticleResource> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return this.searchArticles;
    }
}
