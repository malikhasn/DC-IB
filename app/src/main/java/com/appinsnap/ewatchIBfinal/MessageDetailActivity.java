package com.appinsnap.ewatchIBfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageDetailActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_type,tv_subject,tv_description_display;
    private String title,description,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_detail);
        setupActionBar();

        iv_back = (ImageView) findViewById(R.id.imgview_backbtn);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_subject = (TextView) findViewById(R.id.tv_subject);
        tv_description_display = (TextView) findViewById(R.id.tv_description_display);

        title=getIntent().getExtras().getString("title");
        description=getIntent().getExtras().getString("description");
        type=getIntent().getExtras().getString("type");

        tv_subject.setText(title.toString());
        tv_type.setText(type.toString());
        tv_description_display.setText(description.toString());



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MessagesActivity.this, MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.custom_toolbarr);
            actionBar.setShowHideAnimationEnabled(true);
            //  setListenerForActionBarCustomView(actionBarView);
        }
    }
    }
