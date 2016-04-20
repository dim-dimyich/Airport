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
import com.qoobico.remindme.util.CircularNetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Winner on 30.11.2015.
 */
public class FlightsAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private Context Context;
    private ArrayList<FlightItem> FlightsArrayList;

    private static String today;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        @Bind(R.id.flight_status)
        TextView flightStatus;

        @Bind(R.id.flight_to)
        TextView flightTo;

        @Bind(R.id.flight_from)
        TextView flightFrom;

        @Bind(R.id.date_to)
        TextView dateTo;

        @Bind(R.id.date_from)
        TextView dateFrom;

        @Bind(R.id.flight_image)
        NetworkImageView thumbNail;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
        public void bindViewHolder(FlightItem flightItem) {

            thumbNail.setImageUrl(flightItem.getImage(), imageLoader);

            flightStatus.setText(flightItem.getStatus());
            flightTo.setText(flightItem.getToFlight());
            flightFrom.setText(flightItem.getFromFlight());
            dateTo.setText(getTimeStamp(flightItem.getDepTime()));
            dateFrom.setText(getTimeStamp(flightItem.getArTime()));
        }

    }
    //---------------------------------------------------
    public FlightsAdapter(Context Context, ArrayList<FlightItem> FlightsArrayList) {
        this.Context = Context;
        this.FlightsArrayList = FlightsArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View FlightView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_flight, parent, false);
        return new ViewHolder(FlightView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FlightItem flightItem = FlightsArrayList.get(position);
        ((ViewHolder) holder).bindViewHolder(flightItem);
    }

    @Override
    public int getItemCount() {
        return FlightsArrayList.size();
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
