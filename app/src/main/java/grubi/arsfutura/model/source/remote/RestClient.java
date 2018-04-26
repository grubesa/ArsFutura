package grubi.arsfutura.model.source.remote;

public class RestClient {

    public static final String BASE_URL = "https://newsapi.org";

    public static ArticleService getArticleService() {
        return RetrofitClient.getClient(BASE_URL).create(ArticleService.class);
    }

}
