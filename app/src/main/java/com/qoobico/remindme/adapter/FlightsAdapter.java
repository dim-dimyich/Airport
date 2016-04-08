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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Winner on 30.11.2015.
 */
public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {

    private Context Context;
    private ArrayList<FlightItem> CardArrayList;
    private NetworkImageView thumbNail;
    public TextView flightStatus;
    public TextView flightTo;
    public TextView flightFrom;
    public TextView dateTo;
    public TextView dateFrom;

    private static String today;


    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            imageLoader = MyApplication.getInstance().getImageLoader();
            thumbNail = (NetworkImageView) itemView
                    .findViewById(R.id.flight_image);
            flightStatus = (TextView) itemView.findViewById(R.id.flight_status);
            flightTo = (TextView) itemView.findViewById(R.id.flight_to);
            flightFrom = (TextView) itemView.findViewById(R.id.flight_from);
            dateTo = (TextView) itemView.findViewById(R.id.date_to);
            dateFrom = (TextView) itemView.findViewById(R.id.date_from);

        }

    }

    public FlightsAdapter(Context Context, ArrayList<FlightItem> CardArrayList) {
        this.Context = Context;
        this.CardArrayList = CardArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_flight, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        FlightItem flightItem = CardArrayList.get(position);

        thumbNail.setImageUrl(flightItem.getImage(), imageLoader);

        flightStatus.setText(flightItem.getStatus());
        flightTo.setText(flightItem.getToFlight());
        flightFrom.setText(flightItem.getFromFlight());
        dateTo.setText(getTimeStamp(flightItem.getDepTime()));
        dateFrom.setText(getTimeStamp(flightItem.getArTime()));

    }

    public interface FlightClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class FlightsRecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FlightsAdapter.FlightClickListener clickListener;

        public FlightsRecyclerTouchListener(Context context, final RecyclerView recyclerView, final FlightsAdapter.FlightClickListener clickListener) {
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

    @Override
    public int getItemCount() {
        return CardArrayList.size();
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
