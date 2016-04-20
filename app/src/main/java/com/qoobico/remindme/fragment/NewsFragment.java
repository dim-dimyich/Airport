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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.qoobico.remindme.R;
import com.qoobico.remindme.activity.MainActivity;
import com.qoobico.remindme.adapter.FlightsAdapter;
import com.qoobico.remindme.adapter.NewsAdapter;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.helper.SimpleDividerItemDecoration;
import com.qoobico.remindme.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Winner on 28.02.2016.
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int LAYOUT = R.layout.news_layout;

    private View view;
    private String TAG = MainActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<News> NewsArrayList;
    private NewsAdapter mAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_news);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.material_blue_100,
                R.color.material_blue_300,
                R.color.material_blue_500,
                R.color.material_blue_700);

        NewsArrayList = new ArrayList<>();
        mAdapter = new NewsAdapter(getActivity(), NewsArrayList);
        LinearLayoutManager layoutManagerNews = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerNews);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //recyclerView.setHasFixedSize(false);

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

                fetchNewsItem();
            }
        });

        recyclerView.addOnItemTouchListener(new NewsAdapter.NewsRecyclerTouchListener(getContext(), recyclerView, new NewsAdapter.NewsClickListener() {


            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), "Позиция news"+ position , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onRefresh() {
        NewsArrayList.clear();
        fetchNewsItem();
    }

    private void fetchNewsItem() {
        swipeRefreshLayout.setRefreshing(true);
        NewsArrayList.clear();
        StringRequest strReqNews = new StringRequest(Request.Method.GET,
                EndPoints.NEWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obju = new JSONObject(response);

                    // check for error flag
                    if (obju.getBoolean("error") == false) {
                        JSONArray NewsArray = obju.getJSONArray("news");
                        for (int i = 0; i < NewsArray.length(); i++) {
                            JSONObject NewsObj = (JSONObject) NewsArray.get(i);
                            News item = new News();
                            item.setId(NewsObj.getString("news_id"));
                            item.setNewsTitle(NewsObj.getString("title"));
                            item.setNewsDescription(NewsObj.getString("description"));
                            item.setNews_image(NewsObj.getString("news_image"));
                            item.setCreateNews(NewsObj.getString("created_at"));

                            NewsArrayList.add(item);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getContext(), "" + obju.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                Collections.sort(NewsArrayList, new Comparator<News>(){
                    public int compare(News e1, News e2) {
                        return e2.getId().compareToIgnoreCase(e1.getId());
                    }
                });
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReqNews);
    }



}
