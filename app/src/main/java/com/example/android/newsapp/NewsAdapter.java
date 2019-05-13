package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    List<News> mNewsList;

    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
        mNewsList = newsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        News currentNews = mNewsList.get(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        String dateCorrected = currentNews.getDate().substring(0, 10);

        TextView titleTextView = view.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNews.getTitle());

        TextView dateTextView = view.findViewById(R.id.date_text_view);
        dateTextView.setText(dateCorrected);

        TextView typeTextView = view.findViewById(R.id.type_text_view);
        typeTextView.setText(currentNews.getSection());

        TextView authorTextView = view.findViewById(R.id.author_text_view);
        authorTextView.setText(currentNews.getAuthor());

        return view;
    }
}