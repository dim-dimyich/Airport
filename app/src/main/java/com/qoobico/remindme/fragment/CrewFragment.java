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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.qoobico.remindme.R;
import com.qoobico.remindme.adapter.FlightCrewAdapter;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.helper.SimpleDividerItemDecoration;
import com.qoobico.remindme.model.FlightCrew;
import com.qoobico.remindme.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final int LAYOUT = R.layout.crew_layout;
    private View view;
    private String TAG = "cucle";

    private String crewId;
    private String UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
    FloatingActionButton call;

    String Readiness;
    private String Ok = "1";
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<User> FlightCrewArrayList;
    private FlightCrewAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView nameCrew, dataCreate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        Log.d(TAG, "onCreateView");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_crews);
        nameCrew = (TextView) view.findViewById(R.id.number_crew);
        dataCreate = (TextView) view.findViewById(R.id.date_create_crew);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_crew);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.material_blue_100,
                R.color.material_blue_300,
                R.color.material_blue_500,
                R.color.material_blue_700);

        FlightCrewArrayList = new ArrayList<>();
        mAdapter = new FlightCrewAdapter(getActivity(), FlightCrewArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        call = (FloatingActionButton) view.findViewById(R.id.clickon);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                fetchFlightCrew(Readiness);


                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String endPoint = EndPoints.READINESS.replace("_ID_", UserId);
                        Log.e(TAG, "endPoint: " + endPoint);
                        StringRequest strRead = new StringRequest(Request.Method.PUT,
                                endPoint, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "response: " + response);

                                try {
                                    JSONObject obj = new JSONObject(response);

                                    // check for error flag
                                    if (obj.getBoolean("error") == false) {

                                    } else {
                                        // login error - simply toast the message
                                        Toast.makeText(getContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    Log.e(TAG, "json parsing error: " + e.getMessage());
                                    Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                NetworkResponse networkResponse = error.networkResponse;
                                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                                Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Log.d(TAG, "Отправка данных");
                                Map<String, String> params = new HashMap<>();
                                params.put("readiness", Ok);

                                Log.e(TAG, "params: " + params.toString());
                                return params;
                            }
                        };

                        //Adding request to request queue
                        MyApplication.getInstance().addToRequestQueue(strRead);
                        call.setVisibility(View.INVISIBLE);
                    }


                });


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    private void initFloating(){

       if(Readiness!=null) {
           if (Readiness.equals(Ok)) {
               call.setVisibility(View.INVISIBLE);
           } else {
               call.setVisibility(View.VISIBLE);
           }
       }
   }

    private String fetchFlightCrew(String Readeness) {
        swipeRefreshLayout.setRefreshing(true);
        crewId = MyApplication.getInstance().getPrefManager().getUser().getCodeId();
        String endPoint = EndPoints.CREW.replace("_ID_", crewId);
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
                        JSONObject crewObj = obju.getJSONObject("flight_crew");
                        String commentId = crewObj.getString("code_id");
                        String commentText = crewObj.getString("crew_name");
                        String commentcreate = crewObj.getString("created_at");

                        FlightCrew fl = new FlightCrew();
                        fl.setId(commentId);
                        fl.setCodeValue(commentText);
                        fl.setCreate(commentcreate);
                        nameCrew.setText(fl.getCodeValue());
                        dataCreate.setText("Назначена "+fl.getCreate());
                        JSONArray UsersArray = obju.getJSONArray("user");
                        for (int i = 0; i < UsersArray.length(); i++) {
                            JSONObject UsersObj = (JSONObject) UsersArray.get(i);
                            User us = new User();
                            us.setId(UsersObj.getString("user_id"));
                            us.setName(UsersObj.getString("name"));
                            us.setEmail(UsersObj.getString("email"));
                            us.setImageUser(UsersObj.getString("user_image"));
                            us.setPosition(UsersObj.getString("position_name"));
                            us.setReadiness(UsersObj.getString("readiness"));
                            if(us.getId().equals(UserId)){
                                Readiness = (us.getReadiness());

                               }
                            FlightCrewArrayList.add(us);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getContext(), "" + obju.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "initFloating");
                initFloating();
                // subscribing to all chat room topics

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
        return Readiness;
    }

    @Override
    public void onRefresh() {
        FlightCrewArrayList.clear();
        fetchFlightCrew(Readiness);
    }

}
