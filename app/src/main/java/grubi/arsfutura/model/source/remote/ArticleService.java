package grubi.arsfutura.model.source.remote;

import grubi.arsfutura.model.data.ArticleResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleService {

    @GET("/v2/top-headlines")
    public Call<ArticleResource> getTopHeadlines(@Query("sources") String sources, @Query
            ("pageSize") Integer pageSize, @Query("page") Integer page);

    @GET("/v2/top-headlines")
    public Call<ArticleResource> getLatestNews(@Query("country") String country);

    @GET("/v2/everything")
    public Call<ArticleResource> getEverything(@Query("q") String q, @Query("pageSize") Integer
            pageSize, @Query("page") Integer page);

}
