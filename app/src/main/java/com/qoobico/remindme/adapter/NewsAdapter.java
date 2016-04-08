package com.qoobico.remindme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qoobico.remindme.R;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Winner on 28.03.2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private android.content.Context Context;
    private ArrayList<News> NewsArrayList;
    private NetworkImageView newsImg;

    public TextView newsTitle;
    public TextView newsDescription;
    public TextView createNews;
    private static String today;

    ImageLoader newsImage = MyApplication.getInstance().getImageLoader();


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            newsImage = MyApplication.getInstance().getImageLoader();
            newsImg = (NetworkImageView) itemView
                    .findViewById(R.id.news_image);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsDescription = (TextView) itemView.findViewById(R.id.news_description);
            createNews = (TextView) itemView.findViewById(R.id.news_create);


        }

    }

    public NewsAdapter(android.content.Context Context, ArrayList<News> NewsArrayList) {
        this.Context = Context;
        this.NewsArrayList = NewsArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_news, parent, false);
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        News news = NewsArrayList.get(position);

        newsImg.setImageUrl(news.getNews_image(), newsImage);

        newsTitle.setText(news.getNewsTitle());
        newsDescription.setText(news.getNewsDescription());
        createNews.setText(getTimeStamp(news.getCreateNews()));

    }

    @Override
    public int getItemCount() {
        return NewsArrayList.size();
    }
    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;


    }
}