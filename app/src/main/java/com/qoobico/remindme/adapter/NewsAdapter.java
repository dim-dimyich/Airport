package com.qoobico.remindme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qoobico.remindme.R;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.model.FlightItem;
import com.qoobico.remindme.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Winner on 28.03.2016.
 */
public class NewsAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private android.content.Context Context;
    private ArrayList<News> NewsArrayList;

    private static String today;

    ImageLoader newsImage = MyApplication.getInstance().getImageLoader();


    public class ViewHolderNews extends android.support.v7.widget.RecyclerView.ViewHolder {

        @Bind(R.id.news_create)
        TextView createNews;

        @Bind(R.id.news_description)
        TextView newsDescription;

        @Bind(R.id.news_title)
        TextView newsTitle;

        @Bind(R.id.news_image)
        NetworkImageView newsImg;



        public ViewHolderNews(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        public void bindViewHolder(News news) {

            newsImg.setImageUrl(news.getNews_image(), newsImage);
            newsTitle.setText(news.getId());
            newsDescription.setText(news.getNewsDescription());
            createNews.setText(getTimeStamp(news.getCreateNews()));
        }
    }

    public NewsAdapter(android.content.Context Context, ArrayList<News> NewsArrayList) {
        this.Context = Context;
        this.NewsArrayList = NewsArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolderNews onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_news, parent, false);
        return new ViewHolderNews(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News news = NewsArrayList.get(position);
        ((ViewHolderNews) holder).bindViewHolder(news);
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

    public interface NewsClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class NewsRecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private NewsAdapter.NewsClickListener clickListener;

        public NewsRecyclerTouchListener(android.content.Context context, final RecyclerView recyclerView, final NewsAdapter.NewsClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}