package com.qoobico.remindme.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qoobico.remindme.R;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.util.MyYAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Winner on 30.03.2016.
 */
public class AnalyticsActivity  extends AppCompatActivity implements OnChartGestureListener {

    private String UserId;
    private ArrayList entries1;
    private ArrayList xVals;
    private PieChart mChart;
    private BarChart bChart;
    private SwipeRefreshLayout swipeRefreshAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_awesomedesign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.analytics);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu);

        bChart = (BarChart) findViewById(R.id.barChart1);

        bChart.setDescription("");
        bChart.setOnChartGestureListener(this);


        bChart.setDrawBarShadow(false);
        bChart.setDrawValueAboveBar(true);
        bChart.setDrawBarShadow(false);
        bChart.setDrawGridBackground(false);
        bChart.setMaxVisibleValueCount(60);


        YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = bChart.getAxisLeft();

        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = bChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = bChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f); // this replaces setStartAtZero(true)

        bChart.getAxisRight().setEnabled(false);

        XAxis xAxis = bChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        bChart.animateY(700, Easing.EasingOption.EaseInCubic);
        setData();


        mChart = (PieChart) findViewById(R.id.pieChart1);


        mChart.setDescription("");
        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);
        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);
        Legend lm = mChart.getLegend();
        lm.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        generatePieDat();

    }

    public void setData() {

        UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        String endPoint = EndPoints.ANALCOST.replace("_ID_", UserId);

        StringRequest AnReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject objCost = new JSONObject(response);

                    // check for error flag
                    if (objCost.getBoolean("error") == false) {


                        ArrayList<String> xVals = new ArrayList<String>();
                        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

                        JSONArray AnalyticsArray = objCost.getJSONArray("cost_hour");
                        for (int i = 0; i < AnalyticsArray.length(); i++) {
                            JSONObject AnalCost = (JSONObject) AnalyticsArray.get(i);
                            xVals.add(AnalCost.getString("month"));
                            yVals1.add(new BarEntry(Float.parseFloat(AnalCost.getString("flight_cost")), i));
                        }

                        BarDataSet set1 = new BarDataSet(yVals1, "Зарплата/Месяц");
                        set1.setBarSpacePercent(35f);

                        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                        dataSets.add(set1);

                        BarData data = new BarData(xVals, dataSets);
                        data.setValueTextSize(10f);

                        bChart.setData(data);

                    } else {
                        // error in fetching chat rooms
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.no_data, Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(AnReq);

    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Самолето\nЧасы");
        s.setSpan(new RelativeSizeSpan(2f), 0, 13, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 13, s.length(), 0);
        return s;
    }

    private void generatePieDat() {

        UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        String endPoint = EndPoints.ANAL.replace("_ID_", UserId);

        StringRequest AnReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        entries1 = new ArrayList();
                        xVals = new ArrayList();

                        JSONArray AnalyticsArray = obj.getJSONArray("flight_hour");
                        for (int i = 0; i < AnalyticsArray.length(); i++) {
                            JSONObject AnalObj = (JSONObject) AnalyticsArray.get(i);
                            entries1.add(new Entry(Float.parseFloat(AnalObj.getString("flight_time")),i));
                            xVals.add(AnalObj.getString("air_model"));
                        }

                        PieDataSet ds1 = new PieDataSet(entries1, "");
                        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                        ds1.setSliceSpace(2f);
                        ds1.setValueTextColor(Color.GRAY);
                        ds1.setValueTextSize(12f);
                        PieData d = new PieData(xVals, ds1);
                        mChart.setData(d);

                    } else {
                        // error in fetching chat rooms
                    }

                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        MyApplication.getInstance().addToRequestQueue(AnReq);

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
