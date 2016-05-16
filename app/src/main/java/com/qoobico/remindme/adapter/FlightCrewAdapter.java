package com.qoobico.remindme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qoobico.remindme.R;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.model.FlightCrew;
import com.qoobico.remindme.model.User;
import com.qoobico.remindme.util.CircularNetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Winner on 08.03.2016.
 */
public class FlightCrewAdapter extends RecyclerView.Adapter<FlightCrewAdapter.ViewHolder> {

    private Context Context;
    private ArrayList<User> CrewArrayList;
    private CircularNetworkImageView crewPfoto;
    private String UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
    ImageLoader imageLoaderCrew = MyApplication.getInstance().getImageLoader();
    private TextView userCrew, positioN, initial;
    private static String today;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            imageLoaderCrew = MyApplication.getInstance().getImageLoader();
            crewPfoto = (CircularNetworkImageView) itemView
                    .findViewById(R.id.crew_photo);
            userCrew = (TextView) view.findViewById(R.id.crew_name);
            positioN = (TextView) view.findViewById(R.id.crew_position);
            initial = (TextView) view.findViewById(R.id.intialization_crew);

        }
    }

    public FlightCrewAdapter(Context Context, ArrayList<User> CrewArrayList) {
        this.Context = Context;
        this.CrewArrayList = CrewArrayList;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_crew, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        User flightcrew = CrewArrayList.get(position);
        crewPfoto.setImageUrl(flightcrew.getImageUser(), imageLoaderCrew);
        userCrew.setText(flightcrew.getName());
        positioN.setText(flightcrew.getPosition());
        if(flightcrew.getId().equals(UserId)){
            initial.setVisibility(View.VISIBLE);
        } else {
            initial.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return CrewArrayList.size();
    }

}
