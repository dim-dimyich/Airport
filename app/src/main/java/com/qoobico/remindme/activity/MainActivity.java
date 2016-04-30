package com.qoobico.remindme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.qoobico.remindme.R;
import com.qoobico.remindme.adapter.TabsFragmentAdapter;
import com.qoobico.remindme.app.Config;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.gcm.GcmIntentService;
import com.qoobico.remindme.model.FlightItem;
import com.qoobico.remindme.model.Message;
import com.qoobico.remindme.model.User;
import com.qoobico.remindme.util.CircularNetworkImageView;
import com.qoobico.remindme.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity   {
    private String TAG = MainActivity.class.getSimpleName();
    private static final int LAYOUT = R.layout.activity_main;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String UserId = MyApplication.getInstance().getPrefManager().getUser().getId();
    private String UserName = MyApplication.getInstance().getPrefManager().getUser().getName();
    private String UserEmail = MyApplication.getInstance().getPrefManager().getUser().getEmail();
    private String UserImage = MyApplication.getInstance().getPrefManager().getUser().getImageUser();
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private CircularNetworkImageView UserPfoto;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    TextView drawerUser;
    TextView drawerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        UserCode();
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();
        putDataHeader();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }
    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_logout:
                        MyApplication.getInstance().logout();
                        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.news1,
                R.drawable.personal,
                R.drawable.flights,
                R.drawable.crew

        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);


    }



    private void initNavigationView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        navigationView.addHeaderView(header);

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

         drawerUser = (TextView) header.findViewById(R.id.user_nav_name);
         drawerEmail = (TextView) header.findViewById(R.id.user_nav_email);
        UserPfoto = (CircularNetworkImageView) header
                .findViewById(R.id.user_nav_photo);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.actionstaff:
                        viewPager.setCurrentItem(Constants.TAB_ONE);
                        break;
                    case R.id.actionNotificationItem:
                        viewPager.setCurrentItem(Constants.TAB_TWO);
                        break;
                    case R.id.actionFlights:
                        viewPager.setCurrentItem(Constants.TAB_THREE);
                        break;
                    case R.id.actionnotification:
                        viewPager.setCurrentItem(Constants.TAB_FOR);
                        break;
                    case R.id.calendar:
                        Intent anal = new Intent(getApplicationContext(), AnalyticsActivity.class);
                        startActivity(anal);
                        break;
                    case R.id.tools:
                        Toast.makeText(getApplicationContext(),
                                "Инструменты находятся разработке", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_share:
                        Intent goChat = new Intent(getApplicationContext(),ChatRoomsActivity.class);
                        startActivity(goChat);
                        break;

                    case R.id.nav_send:
                        Toast.makeText(getApplicationContext(),
                                "Отправить находится разработке", Toast.LENGTH_SHORT).show();
                        break;


                }
                return true;
            }
        });
    }


    private void putDataHeader() {
        drawerUser.setText(UserName);
        drawerEmail.setText(UserEmail);
        UserPfoto.setImageUrl(UserImage, imageLoader);


    }

    private void UserCode(){
//        Внимание мега костыль
        String codeIdDef = "0";
        User us = new User();
        us.setCodeId(codeIdDef);
        MyApplication.getInstance().getPrefManager().storeCode(us);
//        Конец мегакостыля
        String endPoint = EndPoints.CODEID.replace("_ID_", UserId);
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
                        JSONArray CardArray = obju.getJSONArray("user_code");
                        for (int i = 0; i < CardArray.length(); i++) {
                            JSONObject CodeOb = (JSONObject) CardArray.get(i);
                            User us = new User();
                            us.setCodeId(CodeOb.getString("code_id"));

                            MyApplication.getInstance().getPrefManager().storeCode(us);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obju.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

}
