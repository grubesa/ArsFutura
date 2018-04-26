package grubi.arsfutura.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import grubi.arsfutura.R;
import grubi.arsfutura.model.data.OrientationResource;

public class ArticleSearchActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.image_view_search_icon)
    ImageView imageViewSearchIcon;

    @BindView(R.id.edit_text_search)
    EditText editTextSearch;

    private ArticleViewModel articleViewModel;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        adapter = new ArticleAdapter(this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setListener(new AdapterListener() {
            @Override
            public void onBottomReached() {
                articleViewModel.setNextPage();
            }
        });
        this.recyclerView.setAdapter(adapter);
        this.articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);

        this.articleViewModel.getArticles().observe(this, new Observer<List<OrientationResource>>
                () {
            @Override
            public void onChanged(@Nullable List<OrientationResource> orientationResources) {
                adapter.addArticles(orientationResources, false, "Latest News");
            }
        });

        this.articleViewModel.getSearchArticles().observe(this, new
                Observer<List<OrientationResource>>() {
            @Override
            public void onChanged(@Nullable List<OrientationResource> orientationResources) {
                adapter.addArticles(orientationResources, true, "Search Result");
            }
        });
    }

    @OnClick(R.id.image_view_search_icon)
    public void onClearSearchText(View view) {
        this.editTextSearch.setText(null);
    }

    @OnTextChanged(value = R.id.edit_text_search, callback = OnTextChanged.Callback
            .AFTER_TEXT_CHANGED)
    public void onTypeSearchText(Editable editable) {
        if (editable.length() > 0) {
            this.imageViewSearchIcon.setImageResource(R.mipmap.ic_clear_black_24dp);
            this.articleViewModel.setSearchQuery(editable.toString());
        } else {
            this.imageViewSearchIcon.setImageResource(R.mipmap.ic_search_black_24dp);
            this.adapter.addArticles(this.articleViewModel.getArticles().getValue(), false,
                    "Latest News");
        }
    }
}
