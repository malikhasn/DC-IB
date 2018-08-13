package com.appinsnap.ewatchIBfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SubmitPendingActivity extends AppCompatActivity {


    private ListView listView;
    private ProgressBar progressBar;
    private DbHelper mydb;
    private String UserId;
    private ImageView iv_back;
    private ArrayList<AlectionModel> dataModels;
    private TextView top_txtview;
    private String status;
    private CustomAdapterReport adapter;
    private boolean comefromAclist;
    private ArrayList<AlectionModel> issueList;
    private boolean comefromDclist;
    private ToggleButton tbutton;
    ArrayList<AlectionModel> pendingIssues;
    ArrayList<AlectionModel> resolvedIssues;
    private LinearLayout switch_layout;
    private String userRoleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setupActionBar();

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        top_txtview=(TextView)findViewById(R.id.top_txtview);
        tbutton = (ToggleButton)findViewById(R.id.toggleButton1);

        status=getIntent().getExtras().getString("status");
        if(status.equals("draft")) {
            top_txtview.setText("Pending Reports");
        }else if(status.equalsIgnoreCase("AcList")){
            top_txtview.setText("Reports");
        }else if(status.equalsIgnoreCase("DcList")){
            top_txtview.setText("Reports");
        }else {
            top_txtview.setText("Recent Reports");
        }
        mydb= new DbHelper(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        UserId=   pref.getString("UserId", null); // getting String
        userRoleId=   pref.getString("UserRoleId", "2"); // getting String

//        getMessages();

        iv_back = (ImageView) findViewById(R.id.imgview_backbtn);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userRoleId.equals("1") ){
                    Intent intent = new Intent(SubmitPendingActivity.this, DcMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SubmitPendingActivity.this, AlecMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        comefromAclist= getIntent().getExtras().getBoolean("comefromAclist",false);
        comefromDclist= getIntent().getExtras().getBoolean("comefromDclist",false);
        issueList=new ArrayList<>();
        issueList= (ArrayList <AlectionModel>)getIntent().getExtras().getSerializable("issueList");
        dataModels=issueList;


        listView=(ListView)findViewById(R.id.list);
        switch_layout = (LinearLayout) findViewById(R.id.switch_layout);

//        messageIds=new ArrayList<>();
//        dataModels= new ArrayList<>();
        if(comefromDclist){
            switch_layout.setVisibility(View.VISIBLE);
//            tbutton.setVisibility(View.VISIBLE);
             pendingIssues= new ArrayList<>();
             resolvedIssues= new ArrayList<>();
         for (int i =0;i<issueList.size();i++){
             if((issueList.get(i).getStatus()).equalsIgnoreCase("true")){
                 resolvedIssues.add(issueList.get(i));
             }else {
                 pendingIssues.add(issueList.get(i));
             }
         }


            adapter= new CustomAdapterReport(pendingIssues,SubmitPendingActivity.this);
            listView.setAdapter(adapter);

        }else if(comefromAclist){
            adapter= new CustomAdapterReport(issueList,SubmitPendingActivity.this);
            listView.setAdapter(adapter);

        }else {
             getReportFromDB();
        }
//        adapter= new CustomAdapter(dataModels,getApplicationContext());
//
//        listView.setAdapter(adapter);

        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tbutton.isChecked())
                {
                    tbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending_selected));
                    adapter= new CustomAdapterReport(pendingIssues,SubmitPendingActivity.this);
                    listView.setAdapter(adapter);
                }
                else {
                    tbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.pending_unselected));
                    adapter= new CustomAdapterReport(resolvedIssues,SubmitPendingActivity.this);
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlectionModel dataModel= dataModels.get(position);

                if(comefromDclist) {
                    dataModel=new AlectionModel();
                    if (!tbutton.isChecked()) {
                        dataModel =pendingIssues.get(position);
                    }else {
                        dataModel =resolvedIssues.get(position);
                    }
                }

//                updateMessage(dataModel);
//                Intent intent = new Intent(SubmitPendingActivity.this, MessageDetailActivity.class);
//                intent.putExtra("title", dataModel.getCategory());
//                intent.putExtra("description", dataModel.getSubject());
////                intent.putExtra("type", dataModel.gettype());
//                // Toast.makeText(getApplicationContext(),imageRealPath,Toast.LENGTH_LONG).show();
//                startActivity(intent);
                Intent intent = new Intent(SubmitPendingActivity.this, AlecSubmitActivity.class);
                intent.putExtra("imageRealPath", dataModel.getImage());
                intent.putExtra("videoRealPath", dataModel.getVideo());
                intent.putExtra("model", dataModel);
//                intent.putExtra("audioRealPath", dataModel.getAudio());
//                intent.putExtra("subject", dataModel.getSubject());
//                intent.putExtra("numbers",dataModel.getNumber());
                intent.putExtra("detail", dataModel.getDetail());
                intent.putExtra("napa", dataModel.getNapa());
                intent.putExtra("type", dataModel.getType());
                intent.putExtra("subtype", dataModel.getSubtype());
                intent.putExtra("polid", dataModel.getPolingstationid());
                intent.putExtra("polname", dataModel.getPollingStationName());
                intent.putExtra("category", dataModel.getCategory());
                intent.putExtra("status", dataModel.getStatus());
//                intent.putExtra("comefromlist", true);
                intent.putExtra("recordId", dataModel.getId());
                intent.putExtra("serverrecordId", dataModel.getIssueId());

                intent.putExtra("acDetail", dataModel.getAcdetail());
                intent.putExtra("acimageRealPath", dataModel.getAcimage());
                intent.putExtra("acvideoRealPath", dataModel.getAcvideo());

                if(status.equalsIgnoreCase("AcList")){
                    intent.putExtra("comefromAClist", true);
                }if(status.equalsIgnoreCase("DcList")){
                    intent.putExtra("comefromAClist", true);
                    intent.putExtra("comefromDClist", true);
                }else if(status.equals("draft")){
                    intent.putExtra("comefromlist", true);
                }else {
                    intent.putExtra("comefromSubmitlist", true);
                }

                // Toast.makeText(getApplicationContext(),imageRealPath,Toast.LENGTH_LONG).show();
                startActivity(intent);


//                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
            }
        });


    }

    private void getReportFromDB() {
        dataModels=new ArrayList<>();
        if(status.equals("draft")){
            dataModels=mydb.getAllPendingElection();
        }else {
            dataModels=mydb.getAllSubmittedElection();
        }
        adapter= new CustomAdapterReport(dataModels,SubmitPendingActivity.this);
        listView.setAdapter(adapter);

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
