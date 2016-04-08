package com.qoobico.remindme.fragment;

import android.content.Intent;
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
import com.qoobico.remindme.activity.ViewContactActivity;
import com.qoobico.remindme.adapter.UsersAdapter;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.helper.SimpleDividerItemDecoration;
import com.qoobico.remindme.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipeRefreshLayout;


    private static final int LAYOUT = R.layout.users_layout;
    private View view;
    private String TAG = MainActivity.class.getSimpleName();


    private ArrayList<User> UsersArrayList;
    private UsersAdapter uAdapter;
    private RecyclerView UrecyclerView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        UrecyclerView = (RecyclerView) view.findViewById(R.id.recycler_users);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_user);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.material_blue_100,
                R.color.material_blue_300,
                R.color.material_blue_500,
                R.color.material_blue_700);

        UsersArrayList = new ArrayList<>();
        uAdapter = new UsersAdapter(getActivity(), UsersArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        UrecyclerView.setLayoutManager(layoutManager);
        UrecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));
        UrecyclerView.setItemAnimator(new DefaultItemAnimator());
        UrecyclerView.setAdapter(uAdapter);


        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                fetchUsers();
            }
        });

       // fetchUsers();
        UrecyclerView.addOnItemTouchListener(new UsersAdapter.RecyclerTouchListener(getContext(), UrecyclerView, new UsersAdapter.UserClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                User user = UsersArrayList.get(position);
                Intent intent = new Intent(getActivity(), ViewContactActivity.class);
                intent.putExtra("user_id", user.getId());
                intent.putExtra("name", user.getName());
                intent.putExtra("email", user.getEmail());
                intent.putExtra("phone", user.getPhone());
//                intent.putExtra("user_image", user.getImage());
                intent.putExtra("position_name", user.getPosition());
                intent.putExtra("cost_per_hour", user.getCost());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    public void onRefresh() {
        UsersArrayList.clear();
        fetchUsers();
    }

    private void fetchUsers() {

        swipeRefreshLayout.setRefreshing(true);
        StringRequest strReq1 = new StringRequest(Request.Method.GET,
                EndPoints.USERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obju = new JSONObject(response);

                    // check for error flag
                    if (obju.getBoolean("error") == false) {
                        JSONArray UsersArray = obju.getJSONArray("users");
                        for (int i = 0; i < UsersArray.length(); i++) {
                            JSONObject UsersObj = (JSONObject) UsersArray.get(i);
                            User us = new User();
                            us.setId(UsersObj.getString("user_id"));
                            us.setName(UsersObj.getString("name"));
                            us.setEmail(UsersObj.getString("email"));
                            us.setPhone(UsersObj.getString("phone"));
//                            us.setImage(UsersObj.getString("user_image"));
                            us.setPosition(UsersObj.getString("position_name"));
                                UsersArrayList.add(us);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getContext(), "" + obju.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                uAdapter.notifyDataSetChanged();


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
              swipeRefreshLayout.setRefreshing(false);
            }
        });


        MyApplication.getInstance().addToRequestQueue(strReq1);




    }

}
