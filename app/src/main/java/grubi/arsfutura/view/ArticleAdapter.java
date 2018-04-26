package grubi.arsfutura.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grubi.arsfutura.R;
import grubi.arsfutura.model.data.Article;
import grubi.arsfutura.model.data.OrientationResource;
import grubi.arsfutura.util.TimeUtil;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TOP_HEADLINES = 1;
    public static final int LATEST_NEWS = 2;

    private Context context;
    private List<OrientationResource> articles;
    private AdapterListener listener;
    private boolean paginationEnabled;
    private String title;

    public ArticleAdapter(Context context) {
        this.paginationEnabled = false;
        this.context = context;
        this.articles = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case TOP_HEADLINES:
                view = inflater.inflate(R.layout.article_list_top_headlines, parent, false);
                holder = new TopHeadlinesViewHolder(view);
                break;
            case LATEST_NEWS:
                view = inflater.inflate(R.layout.article_item_latest_news, parent, false);
                holder = new VerticalViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.article_item_latest_news, parent, false);
                holder = new VerticalViewHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TOP_HEADLINES) {
            OrientationResource<List<Article>> article = this.articles.get(position);
            ArticleHorizontalAdapter adapter = new ArticleHorizontalAdapter(article.getResource());
            ((TopHeadlinesViewHolder) holder).recyclerView.setLayoutManager(new
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ((TopHeadlinesViewHolder) holder).recyclerView.setAdapter(adapter);
        } else if (holder.getItemViewType() == LATEST_NEWS) {
            OrientationResource<Article> orientationResource = this.articles.get(position);
            Article article = orientationResource.getResource();
            if (position == 1 & this.title.equals("Latest News")) {
                ((VerticalViewHolder) holder).bigTitle.setText(this.title);
                ((VerticalViewHolder) holder).bigTitle.setVisibility(View.VISIBLE);
            } else if (position == 0 & this.title.equals("Search Result")) {
                ((VerticalViewHolder) holder).bigTitle.setText(this.title);
                ((VerticalViewHolder) holder).bigTitle.setVisibility(View.VISIBLE);
            } else {
                ((VerticalViewHolder) holder).bigTitle.setVisibility(View.GONE);
            }
            ((VerticalViewHolder) holder).title.setText(article.getTitle());
            ((VerticalViewHolder) holder).description.setText(article.getDescription());
            ((VerticalViewHolder) holder).publishTime.setText(TimeUtil.getElapsedTime(article
                    .getPublishedAt()) + " by ");
            ((VerticalViewHolder) holder).sourceName.setText(article.getSource().getName());

            ifEndOfPage(position);
        }
    }

    private void ifEndOfPage(int position) {
        if (position == this.articles.size() - 1 & this.paginationEnabled) {
            this.listener.onBottomReached();
            this.paginationEnabled = false;
        }
    }

    public void addArticles(List<OrientationResource> articles, boolean enablePagination, String
            title) {
        this.title = title;
        this.paginationEnabled = enablePagination;
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (this.articles.get(position).getResource() instanceof Article) {
            return LATEST_NEWS;
        } else if (this.articles.get(position).getResource() instanceof ArrayList) {
            return TOP_HEADLINES;
        } else {
            return -1;
        }
    }

    public class TopHeadlinesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        TopHeadlinesViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_top_headlines);
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, publishTime, sourceName, bigTitle;

        public VerticalViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_view_title);
            bigTitle = itemView.findViewById(R.id.text_view_big_title);
            description = itemView.findViewById(R.id.text_view_description);
            publishTime = itemView.findViewById(R.id.text_view_publish_time);
            sourceName = itemView.findViewById(R.id.text_view_source_name);
        }
    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }
}
