package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final int NEWS_LOADER_ID = 1;
    public static final String NEWS_LOADER =
            "http://content.guardianapis.com/search?";
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView mListView;
    TextView mTextView;
    NewsAdapter adapter;
    ProgressBar mProgressBar;
    private TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);

        textViewEmpty = findViewById(R.id.no_text_view);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mListView = findViewById(R.id.list_view);
        mTextView = findViewById(R.id.text_view);

        adapter = new NewsAdapter(this, new ArrayList<News>());
        mProgressBar.setVisibility(View.GONE);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News currentNews = adapter.getItem(i);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(R.string.no_internet_text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(NEWS_LOADER);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("show-references", "author");
        uriBuilder.appendQueryParameter("q", "android");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", "15");
        uriBuilder.appendQueryParameter("api-key", "1ddbb4cd-d24f-46b8-a943-9314b673df71");
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        textViewEmpty.setText(R.string.no_internet_text);
        adapter.clear();

        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}

