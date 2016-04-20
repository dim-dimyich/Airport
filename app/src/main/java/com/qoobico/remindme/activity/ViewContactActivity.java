package com.qoobico.remindme.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.qoobico.remindme.R;
import com.qoobico.remindme.app.MyApplication;


/**
 * Created by Winner on 29.11.2015.
 */
public class ViewContactActivity extends AppCompatActivity implements View.OnClickListener {

    private String name, email, positionName, imageUrl, phone, create, birthday;

    TextView Email, Position, Phone, Create, Birthday;

    ImageLoader UserImage = MyApplication.getInstance().getImageLoader();
    NetworkImageView ImageUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_staff_layout);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        positionName = intent.getStringExtra("position_name");
        create = intent.getStringExtra("created_at");
        birthday = intent.getStringExtra("birthday");
        imageUrl = intent.getStringExtra("user_image");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.inflateMenu(R.menu.menu);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);

        ImageUser = (NetworkImageView) findViewById(R.id.personImage);
        ImageUser.setImageUrl(imageUrl, UserImage);

        Position = (TextView)findViewById(R.id.position_detail);
        Phone = (TextView)findViewById(R.id.phone_detail);
        Email = (TextView)findViewById(R.id.email_detail);
        Create = (TextView)findViewById(R.id.create_at_detail);
        Birthday = (TextView)findViewById(R.id.birthday_detail);

        final FloatingActionButton call = (FloatingActionButton) findViewById(R.id.phone);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + Phone.getText().toString());
                startActivity(new Intent(Intent.ACTION_DIAL, uri));
            }
        });

        final FloatingActionButton sms = (FloatingActionButton) findViewById(R.id.sms);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri1 = Uri.parse("tel:" + Phone.getText().toString());
                startActivity(new Intent(Intent.ACTION_SENDTO, uri1));
            }
        });

        final FloatingActionButton email = (FloatingActionButton) findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri2 = Uri.parse("tel:" + Email.getText().toString());
               startActivity(new Intent(Intent.ACTION_SENDTO, uri2));
            }
        });

        Bundle extras = getIntent().getExtras();

        Position.setText(extras.getString("position_name"));
        Phone.setText(extras.getString("phone"));
        Email.setText(extras.getString("email"));
        Create.setText(extras.getString("created_at"));
        Birthday.setText(extras.getString("birthday"));

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_Call:

//                break;
//
//            case R.id.btn_Back:

//                break;
//
//            case R.id.btn_Edit:

//                break;
//        }
    }
}

