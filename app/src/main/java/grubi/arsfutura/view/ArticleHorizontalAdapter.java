package grubi.arsfutura.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import grubi.arsfutura.R;
import grubi.arsfutura.model.data.Article;
import grubi.arsfutura.util.TimeUtil;

public class ArticleHorizontalAdapter extends RecyclerView.Adapter<ArticleHorizontalAdapter
        .MyViewHolder> {

    List<Article> articleList = new ArrayList<>();

    public ArticleHorizontalAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .article_item_top_headlines, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article article = this.articleList.get(position);
        Picasso.get().load(article.getUrlToImage()).into(holder.image);
        holder.title.setText(article.getTitle());
        holder.sourceName.setText(article.getSource().getName());
        holder.publishTime.setText(TimeUtil.getElapsedTime(article.getPublishedAt()) + " by ");
    }

    @Override
    public int getItemCount() {
        return this.articleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, sourceName, publishTime;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.text_view_title);
            sourceName = itemView.findViewById(R.id.text_view_source_name);
            publishTime = itemView.findViewById(R.id.text_view_publish_time);
        }
    }

}
