package com.qoobico.remindme.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qoobico.remindme.R;

/**
 * Created by Winner on 29.11.2015.
 */
public class ViewContactActivity extends AppCompatActivity implements View.OnClickListener {
    private String userId;
    private String name;
    private String email;
    private String positionName;
  //  private String cost;
    private String phone;
    private TextView mName;
    private TextView position_name;
   // private TextView costPer;
    private TextView Phone;
    private TextView Email;
    Button BCall;
    Button BEdit;
    Button BBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_staff_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        positionName = intent.getStringExtra("position_name");
       // cost = intent.getStringExtra("cost_per_hour");



        mName = (TextView)findViewById(R.id.TextName);
        position_name = (TextView)findViewById(R.id.TextOffice);
      //  costPer = (TextView)findViewById(R.id.TextDob);
        Phone = (TextView)findViewById(R.id.TextPhone);
        Email = (TextView)findViewById(R.id.TextEmail);


        BCall  = (Button)findViewById(R.id.btn_Call);
        BEdit = (Button)findViewById(R.id.btn_Edit);
        BBack = (Button)findViewById(R.id.btn_Back);

        Bundle extras = getIntent().getExtras();

        mName.setText(extras.getString("name"));
        position_name.setText(extras.getString("position_name"));
      //  costPer.setText(extras.getString("cost_per_hour"));
        Phone.setText(extras.getString("phone"));
        Email.setText(extras.getString("email"));


        BCall.setOnClickListener(this);
        BEdit.setOnClickListener(this);
        BBack.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Call:
                Uri uri = Uri.parse("tel:" + Phone.getText().toString());
                startActivity(new Intent(Intent.ACTION_DIAL, uri));
                break;

            case R.id.btn_Back:
                Uri uri1 = Uri.parse("tel:" + Phone.getText().toString());
                startActivity(new Intent(Intent.ACTION_SENDTO, uri1));
                break;

            case R.id.btn_Edit:
                Uri uri2 = Uri.parse("tel:" + Email.getText().toString());
                startActivity(new Intent(Intent.ACTION_SENDTO, uri2));
                break;
        }
    }}

