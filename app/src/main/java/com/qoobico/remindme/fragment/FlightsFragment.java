package com.qoobico.remindme.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.qoobico.remindme.R;
import com.qoobico.remindme.activity.MainActivity;
import com.qoobico.remindme.adapter.FlightsAdapter;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.helper.SimpleDividerItemDecoration;
import com.qoobico.remindme.model.FlightItem;
import com.qoobico.remindme.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlightsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LAYOUT = R.layout.flights_layout;

    private View view;
    private String TAG = MainActivity.class.getSimpleName();
    private String crewId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<FlightItem> FlightArrayList;
    private FlightsAdapter mAdapter;
    private RecyclerView recyclerView;
    private String noFlight = "У Вас нет рейсов";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_flight);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_flights);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.material_blue_100,
                R.color.material_blue_300,
                R.color.material_blue_500,
                R.color.material_blue_700);

        FlightArrayList = new ArrayList<>();
        mAdapter = new FlightsAdapter(getActivity(), FlightArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        final MainActivity mainActF = (MainActivity)getActivity();

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
//                mainActF.UserCode();
                fetchFlightItem();
            }
        });

        recyclerView.addOnItemTouchListener(new FlightsAdapter.FlightsRecyclerTouchListener(getContext(), recyclerView, new FlightsAdapter.FlightClickListener() {


            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), "Позиция"+ position , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void fetchFlightItem() {
        swipeRefreshLayout.setRefreshing(true);

        crewId = MyApplication.getInstance().getPrefManager().getCodeUser().getCodeId();
        String endPoint = EndPoints.FLIGHTS.replace("_ID_", crewId);
        Log.e(TAG, "endPoint: " + endPoint);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obju = new JSONObject(response);

                    // check for error flag
                        if (obju.getBoolean("error") == false) {
                            JSONArray CardArray = obju.getJSONArray("flight_info");
                            for (int i = 0; i < CardArray.length(); i++) {
                                JSONObject CardObj = (JSONObject) CardArray.get(i);
                                FlightItem item = new FlightItem();
                                item.setId(CardObj.getString("flight_id"));
                                item.setNumber(CardObj.getString("flight_number"));
                                item.setStatus(CardObj.getString("flight_status"));
                                item.setFromFlight(CardObj.getString("from_flight"));
                                item.setFromAirport(CardObj.getString("airport_from"));
                                item.setToFlight(CardObj.getString("to_flight"));
                                item.setToAirport(CardObj.getString("airport_to"));
                                item.setDepTime(CardObj.getString("departure_datetime"));
                               // item.setImage(CardObj.getString("flight_image"));
                                item.setArTime(CardObj.getString("arrival_datatime"));
                                item.setFlightTime(CardObj.getString("flight_time"));

                                FlightArrayList.add(item);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getContext(), "сюда" + obju.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_LONG).show();
                }
                Collections.sort(FlightArrayList, new Comparator<FlightItem>(){
                    public int compare(FlightItem em1, FlightItem em2) {
                        return em2.getDepTime().compareTo(em1.getDepTime());
                    }
                });
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    @Override
    public void onRefresh() {
        FlightArrayList.clear();
//        MainActivity mainAct = (MainActivity)getActivity();
//        mainAct.UserCode();
        fetchFlightItem();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putParcelableArrayList("mList", (ArrayList<? extends Parcelable>) NewsArrayList);
    }
}