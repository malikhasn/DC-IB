package com.appinsnap.ewatchIBfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DcMainActivity extends AppCompatActivity {
    ImageView imgv1,imgv2,imgv3,imgv4,imgv5,imgv6,imgv7,imgv8,imgv9,imgv10;
    TextView not1,not2,not3,not4,not5,not6,not7,not8,not9,not10;
    String strnot1,strnot2,strnot3,strnot4,strnot5,strnot6,strnot7,strnot8,strnot9,strnot10;
    SharedPreferences pref;
    String userRoleId;
    private ProgressBar progressBar;
    private DbHelper mydb;
    private Drawer drawer;
    ImageView menu;
    String firing = "";
    String displayofweapon = "";
    /* String firing = "";
     String firing = "";
     String firing = "";
     String firing = "";
     String firing = "";
     String firing = "";
     */
    ArrayList<AlectionModel> firingList,
            displayOfWeapon,
            terrorismOrBlast,
            useOfLoudspeaker,
            WallChalking,
            suspiciousPeople,
            oversizeboard,
            others,
            oversizeBannerPoster,
            withoutPermissionMeetingProcession;
    private String UserId;

    private ToggleButton tbutton;
    private String TOGLE="1";
    ImageView imgvt1,imgvt2,imgvt3,imgvt4,imgvt5,imgvt6,imgvt7;
    TextView tnot1,tnot2,tnot3,tnot4,tnot5,tnot6,tnot7;
    String strtnot1,strtnot2,strtnot3,strtnot4,strtnot5,strtnot6,strtnot7;
    ArrayList<AlectionModel> gujarKhan,kahuta,kallarSyedan,kotliSatyan,murree,rawalpindi,taxila;
    private LinearLayout switch_layout;
    private RelativeLayout parent_tehsil;
    private RelativeLayout first_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alec_main);
        init();
        setupActionBar();
        onlcklstr();

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mydb = new DbHelper(this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        userRoleId=   pref.getString("UserRoleId", "2"); // getting String
        UserId=   pref.getString("UserId", ""); // getting String

        setingDrawer();

        switch_layout.setVisibility(View.VISIBLE);

        if (userRoleId.equals("3") || userRoleId.equals("1") ){
            getadmindata();
        }

    }

    private void setingDrawer() {
        //Navigation Drawer
        new DrawerBuilder().withActivity(this).build();

//if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(Html.fromHtml("Ewatch").toString()).withIcon(R.drawable.icon).withTextColor(getResources().getColor(R.color.white));
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Messages").withIcon(R.drawable.messages).withTextColor(getResources().getColor(R.color.white));
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Messages").withIcon(R.drawable.message_icon).withTextColor(getResources().getColor(R.color.white));
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Messages").withIcon(R.drawable.message_icon).withTextColor(getResources().getColor(R.color.white));

//create the drawer and remember the `Drawer` result object

        drawer = new DrawerBuilder()
                .withActivity(this)
//                .withToolbar()
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName("Recent Reports").withIcon(R.drawable.submited_icon).withTextColor(getResources().getColor(R.color.white)),
                        new SecondaryDrawerItem().withName("Pending Reports").withIcon(R.drawable.pending_icon).withTextColor(getResources().getColor(R.color.white)),
                        new SecondaryDrawerItem().withName("Logout").withIcon(R.drawable.logout).withTextColor(getResources().getColor(R.color.white))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (position == 2) {
                            Intent intent = new Intent(DcMainActivity.this, MessagesActivity.class);
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==3){
                            Intent intent = new Intent(DcMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","submit");
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==4){
                            Intent intent = new Intent(DcMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","draft");
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        } else if (position == 5) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("UserId"); // will delete key name
                            editor.commit(); // commit changes

                            Intent intent = new Intent(DcMainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                }).withDrawerGravity(Gravity.RIGHT)
                .withSelectedItem(-1)
                .withSliderBackgroundColor(getResources().getColor(R.color.color_drawer_new))
                .withActionBarDrawerToggle(true)
                .build();






        if(userRoleId.equals("3") || userRoleId.equals("1")) {
            drawer = new DrawerBuilder()
                    .withActivity(this)
//                .withToolbar()
                    .addDrawerItems(
                            item1,
                            new DividerDrawerItem(),
                            item2,
                            new SecondaryDrawerItem().withName("Recent Reports").withIcon(R.drawable.submited_icon).withTextColor(getResources().getColor(R.color.white)),
                            new SecondaryDrawerItem().withName("Pending Reports").withIcon(R.drawable.pending_icon).withTextColor(getResources().getColor(R.color.white)),
                            new SecondaryDrawerItem().withName("Refresh Report List").withIcon(R.drawable.sync).withTextColor(getResources().getColor(R.color.white)).withSelectable(false),
                            new SecondaryDrawerItem().withName("Logout").withIcon(R.drawable.logout).withTextColor(getResources().getColor(R.color.white))
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            // do something with the clicked item :D
                            if (position == 2) {
                                Intent intent = new Intent(DcMainActivity.this, MessagesActivity.class);
                                startActivity(intent);
                            }
                            else if (position == 3) {
                                if(TOGLE.equalsIgnoreCase("1") ){
                                    clearLists();
                                    getadmindata();
                                } else{
                                    clearLists();
                                    getTehsilData();
                                }
                                drawer.closeDrawer();
                                drawer.resetDrawerContent();
                            }

//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                            /*else if(position==3){
                            Intent intent = new Intent(DcMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","submit");
//                            startActivity(intent);
                          Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==4){
                            Intent intent = new Intent(DcMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","draft");
                            startActivity(intent);
                           Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }*/ else if (position == 4) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.remove("UserId"); // will delete key name
                                editor.commit(); // commit changes

                                Intent intent = new Intent(DcMainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    }).withDrawerGravity(Gravity.RIGHT)
                    .withSelectedItem(-1)
                    .withSliderBackgroundColor(getResources().getColor(R.color.color_drawer_new))
                    .withActionBarDrawerToggle(true)
                    .build();
        }
        ////////

        if(userRoleId.equals("3") || userRoleId.equals("1") ) {
            drawer.removeItemByPosition(3);
            drawer.removeItemByPosition(3);
//            drawer.addItem(new SecondaryDrawerItem().withName("Refresh").withIcon(R.drawable.submited_icon).withTextColor(getResources().getColor(R.color.white))
//                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                        @Override
//                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//
//                        }
//                    }));

        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open / close the drawer
                drawer.openDrawer();
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
    private void getadmindata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/ems/IssueCategoyCount",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response!=null && !response.equals("")&& !response.equals("null")) {
//                                dataId = response;

                            //converting response to json object
                            try {
                                JSONArray array = new JSONArray(response);


//                              JSONObject jobj = obj.getJSONObject(0);
//                                JSONObject jobj = new JSONObject(response);
//                                JSONArray array = jobj.getJSONArray("TehsilsList");
                                for (int n = 0; n < array.length(); n++) {
                                    JSONObject parentObject = array.getJSONObject(n);

                                    String objnot=parentObject.getString("IssueCategoyId");
//                                     notstr=objnot.toString();

                                    if(objnot.equals("1")){

                                        String jobj=parentObject.getString("Count");
                                        strnot1=jobj+"";


                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            firingList.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));

                                          /*  mydb.insertElection(
                                                    object.get("IssueCategoryName").toString(),
                                                    object.get("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    "draft",
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()

                                            );*/



                                        }
                                    }

                                    if(objnot.equals("2")){
                                        String jobj=parentObject.getString("Count");
                                        strnot2=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            displayOfWeapon.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("3")){
                                        String jobj=parentObject.getString("Count");
                                        strnot3=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            terrorismOrBlast.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("4")){
                                        String jobj=parentObject.getString("Count");
                                        strnot4=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);



                                            useOfLoudspeaker.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                            Log.i("Isssue data",useOfLoudspeaker.get(i).getAcimage().toString());
                                        }
                                    }
                                    if(objnot.equals("5")){
                                        String jobj=parentObject.getString("Count");
                                        strnot5=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            WallChalking.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("6")){
                                        String jobj=parentObject.getString("Count");
                                        strnot6=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            suspiciousPeople.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("7")){
                                        String jobj=parentObject.getString("Count");
                                        strnot7=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            oversizeboard.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("8")){
                                        String jobj=parentObject.getString("Count");
                                        strnot8=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            others.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("9")){
                                        String jobj=parentObject.getString("Count");
                                        strnot9=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            oversizeBannerPoster.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }
                                    if(objnot.equals("10")){
                                        String jobj=parentObject.getString("Count");
                                        strnot10=jobj+"";
                                        JSONArray jarr=parentObject.getJSONArray("IssuesList");
                                        for (int i = 0; i < jarr.length(); i++) {
                                            JSONObject object = jarr.getJSONObject(i);

                                            withoutPermissionMeetingProcession.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                    object.getString("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                            ));
                                        }
                                    }


                                }



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setNotUi();
//                                        Toast.makeText(getApplicationContext(), "Data is downloaded successfully.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });

//                                Intent intent = new Intent(DcMainActivity.this, DcMainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);


                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }

                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Data cannot download, please try again.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);

                                }
                            });

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                int errcode=error.networkResponse.statusCode;
                                if(errcode==400){
                                    Toast.makeText(getApplicationContext(), "No user Available, Please Enter correct User name. ", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Please try again, there is low internet connectivity", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        //  Toast.makeText(getApplicationContext(), error
                        // .getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

//                params.put("DistrictId", "117");
                params.put("Userid", UserId);
//                params.put("DataFilter", "2");
//                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(DcMainActivity.this).addToRequestQue(stringRequest);
    }

    private void setNotUi() {
        if( strnot1!=null && !strnot1.equals("")) {
            not1.setText(strnot1);
            not1.setVisibility(View.VISIBLE);
        }else {
            not1.setVisibility(View.INVISIBLE);

        }

        if( strnot2!=null && !strnot2.equals("")) {
            not2.setText(strnot2);
            not2.setVisibility(View.VISIBLE);
        }else {
            not2.setVisibility(View.INVISIBLE);

        }
        if( strnot3!=null && !strnot3.equals("")) {
            not3.setText(strnot3);
            not3.setVisibility(View.VISIBLE);
        }else {
            not3.setVisibility(View.INVISIBLE);

        }
        if( strnot4!=null && !strnot4.equals("")) {
            not4.setText(strnot4);
            not4.setVisibility(View.VISIBLE);
        }else {
            not4.setVisibility(View.INVISIBLE);

        }

        if( strnot5!=null && !strnot5.equals("")) {
            not5.setText(strnot5);
            not5.setVisibility(View.VISIBLE);
        }else {
            not5.setVisibility(View.INVISIBLE);

        }

        if( strnot6!=null && !strnot6.equals("")) {
            not6.setText(strnot6);
            not6.setVisibility(View.VISIBLE);
        }else {
            not6.setVisibility(View.INVISIBLE);

        }

        if( strnot7!=null && !strnot7.equals("")) {
            not7.setText(strnot7);
            not7.setVisibility(View.VISIBLE);
        }else {
            not7.setVisibility(View.INVISIBLE);

        }

        if( strnot8!=null && !strnot8.equals("")) {
            not8.setText(strnot8);
            not8.setVisibility(View.VISIBLE);
        }else {
            not8.setVisibility(View.INVISIBLE);

        }

        if( strnot9!=null && !strnot9.equals("")) {
            not9.setText(strnot9);
            not9.setVisibility(View.VISIBLE);
        }else {
            not9.setVisibility(View.INVISIBLE);

        }

        if( strnot10!=null && !strnot10.equals("")) {
            not10.setText(strnot10);
            not10.setVisibility(View.VISIBLE);
        }else {
            not10.setVisibility(View.INVISIBLE);

        }

    }
    private void setTehsilNotUi() {
        if( strtnot1!=null && !strtnot1.equals("")) {
            tnot1.setText(strtnot1);
            tnot1.setVisibility(View.VISIBLE);
        }else {
            tnot1.setVisibility(View.INVISIBLE);

        }

        if( strtnot2!=null && !strtnot2.equals("")) {
            tnot2.setText(strtnot2);
            tnot2.setVisibility(View.VISIBLE);
        }else {
            tnot2.setVisibility(View.INVISIBLE);

        }
        if( strtnot3!=null && !strtnot3.equals("")) {
            tnot3.setText(strtnot3);
            tnot3.setVisibility(View.VISIBLE);
        }else {
            tnot3.setVisibility(View.INVISIBLE);

        }
        if( strtnot4!=null && !strtnot4.equals("")) {
            tnot4.setText(strtnot4);
            tnot4.setVisibility(View.VISIBLE);
        }else {
            tnot4.setVisibility(View.INVISIBLE);

        }

        if( strtnot5!=null && !strtnot5.equals("")) {
            tnot5.setText(strtnot5);
            tnot5.setVisibility(View.VISIBLE);
        }else {
            tnot5.setVisibility(View.INVISIBLE);

        }

        if( strtnot6!=null && !strtnot6.equals("")) {
            tnot6.setText(strtnot6);
            tnot6.setVisibility(View.VISIBLE);
        }else {
            tnot6.setVisibility(View.INVISIBLE);

        }

        if( strtnot7!=null && !strtnot7.equals("")) {
            tnot7.setText(strtnot7);
            tnot7.setVisibility(View.VISIBLE);
        }else {
            tnot7.setVisibility(View.INVISIBLE);

        }



    }

    private void init(){
        imgv1= (ImageView) findViewById(R.id.imgone);
        imgv2= (ImageView) findViewById(R.id.imgtwo);
        imgv3= (ImageView) findViewById(R.id.imgthree);
        imgv4= (ImageView) findViewById(R.id.imgfour);
        imgv5= (ImageView) findViewById(R.id.imgfive);
        imgv6= (ImageView) findViewById(R.id.imgsix);
        imgv7= (ImageView) findViewById(R.id.imgseven);
        imgv8= (ImageView) findViewById(R.id.imgeight);
        imgv9= (ImageView) findViewById(R.id.imgnine);
        imgv10= (ImageView) findViewById(R.id.imgten);
        not1= (TextView) findViewById(R.id.not_one);
        not2= (TextView) findViewById(R.id.not_two);
        not3= (TextView) findViewById(R.id.not_three);
        not4= (TextView) findViewById(R.id.not_four);
        not5= (TextView) findViewById(R.id.not_five);
        not6= (TextView) findViewById(R.id.not_six);
        not7= (TextView) findViewById(R.id.not_seven);
        not8= (TextView) findViewById(R.id.not_eight);
        not9= (TextView) findViewById(R.id.not_nine);
        not10= (TextView) findViewById(R.id.not_ten);

        imgvt1= (ImageView) findViewById(R.id.t_imgone);
        imgvt2= (ImageView) findViewById(R.id.t_imgtwo);
        imgvt3= (ImageView) findViewById(R.id.t_imgthree);
        imgvt4= (ImageView) findViewById(R.id.t_imgfour);
        imgvt5= (ImageView) findViewById(R.id.t_imgfive);
        imgvt6= (ImageView) findViewById(R.id.t_imgsix);
        imgvt7= (ImageView) findViewById(R.id.t_imgseven);
         tnot1= (TextView) findViewById(R.id.not_tone);
        tnot2= (TextView) findViewById(R.id.teh_not_two);
        tnot3= (TextView) findViewById(R.id.teh_not_three);
        tnot4= (TextView) findViewById(R.id.teh_not_four);
        tnot5= (TextView) findViewById(R.id.teh_not_five);
        tnot6= (TextView) findViewById(R.id.teh_not_six);
        tnot7= (TextView) findViewById(R.id.teh_not_ten);

        menu = (ImageView) findViewById(R.id.spinner_menu);


        not1.setVisibility(View.INVISIBLE);
        not1.setText("1");
        firingList=new ArrayList<>();
        displayOfWeapon=new ArrayList<>();
        terrorismOrBlast=new ArrayList<>();
        useOfLoudspeaker=new ArrayList<>();
        WallChalking=new ArrayList<>();
        suspiciousPeople=new ArrayList<>();
        oversizeboard=new ArrayList<>();
        others=new ArrayList<>();

        oversizeBannerPoster=new ArrayList<>();
        withoutPermissionMeetingProcession=new ArrayList<>();

        gujarKhan=new ArrayList<>();
        kahuta=new ArrayList<>();
        kallarSyedan=new ArrayList<>();
        kotliSatyan=new ArrayList<>();
        murree=new ArrayList<>();
        rawalpindi=new ArrayList<>();
        taxila=new ArrayList<>();


        parent_tehsil = (RelativeLayout) findViewById(R.id.parent_tehsil);
        first_parent = (RelativeLayout) findViewById(R.id.first_parent);

        switch_layout = (LinearLayout) findViewById(R.id.switch_layout);
        tbutton = (ToggleButton)findViewById(R.id.toggleButton1);






    }
    private void clearLists(){
        firingList=new ArrayList<>();
        displayOfWeapon=new ArrayList<>();
        terrorismOrBlast=new ArrayList<>();
        useOfLoudspeaker=new ArrayList<>();
        WallChalking=new ArrayList<>();
        suspiciousPeople=new ArrayList<>();
        oversizeboard=new ArrayList<>();
        others=new ArrayList<>();

        oversizeBannerPoster=new ArrayList<>();
        withoutPermissionMeetingProcession=new ArrayList<>();

        gujarKhan=new ArrayList<>();
        kahuta=new ArrayList<>();
        kallarSyedan=new ArrayList<>();
        kotliSatyan=new ArrayList<>();
        murree=new ArrayList<>();
        rawalpindi=new ArrayList<>();
        taxila=new ArrayList<>();

    }
    private void onlcklstr(){
        // UserRoleId UserRole
        //1 Admin    (DC)
        //2 Vigilant  (user)
        //3 Resolver (AC)

        imgv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(userRoleId!=null && userRoleId.equals("1")){


                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Firing");
                    i.putExtra("categoryid", "1");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("status", "DcList");
                    i.putExtra("issueList", firingList);
                    startActivity(i);

                }else  if(userRoleId!=null && userRoleId.equals("2")){
                    firing = "firing";

                    Intent i = new Intent(DcMainActivity.this, AlecSubmitActivity.class);
                    i.putExtra("category", "Firing");
                    i.putExtra("categoryid", "1");
                    i.putExtra("comefromlist", false);
//                    i.putExtra("category", firing);
//                    i.putExtra("firing", firing);
                    startActivity(i);

                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3

//                    getadmindata();


                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Firing");
                    i.putExtra("categoryid", "1");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("status", "AcList");
                    i.putExtra("issueList", firingList);
                    startActivity(i);

                }
            }
        });
        imgv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firing = "";
                displayofweapon = "blast";


                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("status", "DcList");

                    i.putExtra("issueList", displayOfWeapon);

                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
//                i.putExtra("blast", displayofweapon);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
//                AlecSubmitActivity.violationTitle.setText("Voilation : Firing");
                    startActivity(i);



                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("status", "AcList");

                    i.putExtra("issueList", displayOfWeapon);

                    startActivity(i);
                }


            }
        });
        imgv3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Terrorism or Blast");
                    i.putExtra("categoryid", "3");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("status", "DcList");

                    i.putExtra("issueList", terrorismOrBlast);

                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {
                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Terrorism or Blast");
                    i.putExtra("categoryid", "3");

                    startActivity(i);



                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Terrorism or Blast");
                    i.putExtra("categoryid", "3");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("status", "AcList");

                    i.putExtra("issueList", terrorismOrBlast);

                    startActivity(i);
                }


            }
        });
        imgv4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", useOfLoudspeaker);
                    i.putExtra("status", "DcList");


                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", useOfLoudspeaker);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        imgv5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", WallChalking);
                    i.putExtra("status", "DcList");


                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", WallChalking);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        imgv6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", suspiciousPeople);
                    i.putExtra("status", "DcList");


                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", suspiciousPeople);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }

            }
        });
        imgv7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", oversizeboard);
                    i.putExtra("status", "DcList");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", oversizeboard);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        imgv8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Other");
                    i.putExtra("categoryid", "8");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", others);
                    i.putExtra("status", "DcList");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Other");
                    i.putExtra("categoryid", "8");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Other");
                    i.putExtra("categoryid", "8");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", others);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        imgv9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Banner/Poster");
                    i.putExtra("categoryid", "9");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", oversizeBannerPoster);
                    i.putExtra("status", "DcList");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Oversize Banner/Poster");
                    i.putExtra("categoryid", "9");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Banner/Poster");
                    i.putExtra("categoryid", "9");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", oversizeBannerPoster);
                    i.putExtra("status", "AcList");
                    startActivity(i);
                }
            }
        });
        imgv10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "10");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", withoutPermissionMeetingProcession);
                    i.putExtra("status", "DcList");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {
                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Without permission meeting procession");
                    i.putExtra("categoryid", "10");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("3")){
                    //AC =3
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Without permission meeting procession");
                    i.putExtra("categoryid", "10");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", withoutPermissionMeetingProcession);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });

        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tbutton.isChecked())
                {
                    TOGLE="1";
                    clearLists();
                    getadmindata();
                   first_parent.setVisibility(View.VISIBLE);
                    parent_tehsil.setVisibility(View.GONE);

                    tbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.voilation_selected));
//                   setadminUI;
//                    Toast.makeText(AlecSubmitActivity.this, "National Assembly", Toast.LENGTH_LONG).show();
                }
                else {
                    TOGLE="2";
                    tbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.voilation_unselected));
                    parent_tehsil.setVisibility(View.VISIBLE);
                    first_parent.setVisibility(View.GONE);
                    clearLists();
                    getTehsilData();

//                    Toast.makeText(AlecSubmitActivity.this, "Provincial Assembly", Toast.LENGTH_LONG).show();
                }
            }
        });

//          TEHSIL BUTTON ONCLICK

        imgvt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(userRoleId!=null && userRoleId.equals("1")){


                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Firing");
                    i.putExtra("categoryid", "1");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("status", "DcList");
                    i.putExtra("issueList", gujarKhan);
                    startActivity(i);

                }else  if(userRoleId!=null && userRoleId.equals("2")){
                    firing = "firing";

                    Intent i = new Intent(DcMainActivity.this, AlecSubmitActivity.class);
                    i.putExtra("category", "Firing");
                    i.putExtra("categoryid", "1");
                    i.putExtra("comefromlist", false);
//                    i.putExtra("category", firing);
//                    i.putExtra("firing", firing);
                    startActivity(i);

                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3

//                    getadmindata();


                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Firing");
                    i.putExtra("categoryid", "1");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("status", "AcList");
                    i.putExtra("issueList", firingList);
                    startActivity(i);

                }
            }
        });
        imgvt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firing = "";
                displayofweapon = "blast";


                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("status", "DcList");

                    i.putExtra("issueList", kahuta);

                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
//                i.putExtra("blast", displayofweapon);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
//                AlecSubmitActivity.violationTitle.setText("Voilation : Firing");
                    startActivity(i);



                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("status", "AcList");

                    i.putExtra("issueList", displayOfWeapon);

                    startActivity(i);
                }


            }
        });
        imgvt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Terrorism or Blast");
                    i.putExtra("categoryid", "3");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("status", "DcList");

                    i.putExtra("issueList", kallarSyedan);

                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {
                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Terrorism or Blast");
                    i.putExtra("categoryid", "3");

                    startActivity(i);



                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Terrorism or Blast");
                    i.putExtra("categoryid", "3");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("status", "AcList");

                    i.putExtra("issueList", terrorismOrBlast);

                    startActivity(i);
                }


            }
        });
        imgvt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", kotliSatyan);
                    i.putExtra("status", "DcList");


                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", useOfLoudspeaker);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        imgvt5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", murree);
                    i.putExtra("status", "DcList");


                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", WallChalking);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        imgvt6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){

                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", rawalpindi);
                    i.putExtra("status", "DcList");


                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", suspiciousPeople);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }

            }
        });
        imgvt7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userRoleId!=null && userRoleId.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromDclist", true);
                    i.putExtra("issueList", taxila);
                    i.putExtra("status", "DcList");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", oversizeboard);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });


    }

    private void getTehsilData() {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/ems/IssueCategoyCount",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            if (response != null && !response.equals("") && !response.equals("null")) {
//                                dataId = response;

                                //converting response to json object
                                try {
                                    JSONArray array = new JSONArray(response);


//                              JSONObject jobj = obj.getJSONObject(0);
//                                JSONObject jobj = new JSONObject(response);
//                                JSONArray array = jobj.getJSONArray("TehsilsList");
                                    for (int n = 0; n < array.length(); n++) {
                                        JSONObject parentObject = array.getJSONObject(n);

                                        String objnot = parentObject.getString("Tehsilid");
//                                     notstr=objnot.toString();

                                        if (objnot.equals("1")) {

                                            String jobj = parentObject.getString("Count");
                                            strtnot1 = jobj + "";


                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                gujarKhan.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));

                                          /*  mydb.insertElection(
                                                    object.get("IssueCategoryName").toString(),
                                                    object.get("napa").toString(),
                                                    object.get("TehsilName").toString(),
                                                    object.get("ConstituencyName").toString(),
                                                    object.get("PollingStationId").toString(),
                                                    object.get("IssueDetails").toString(),
                                                    object.get("ImageURL").toString(),
                                                    object.get("VideoURL").toString(),
                                                    object.get("ACDetail").toString(),
                                                    object.get("ACImageURL").toString(),
                                                    object.get("ACVideoURL").toString(),
                                                    "draft",
                                                    object.get("CreatorId").toString(),
                                                    object.get("CreationDateTime").toString(),
                                                    object.get("IssueId").toString(),
                                                    object.get("IssueStatus").toString(),
                                                    object.get("IssueCategoryId").toString(),
                                                    object.get("PollingStationName").toString(),
                                                    object.get("Count").toString(),
                                                    "",""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()

                                            );*/


                                            }
                                        }

                                        if (objnot.equals("2")) {
                                            String jobj = parentObject.getString("Count");
                                            strtnot2 = jobj + "";
                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                kahuta.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));
                                            }
                                        }
                                        if (objnot.equals("3")) {
                                            String jobj = parentObject.getString("Count");
                                            strtnot3 = jobj + "";
                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                kallarSyedan.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));
                                            }
                                        }
                                        if (objnot.equals("4")) {
                                            String jobj = parentObject.getString("Count");
                                            strtnot4 = jobj + "";
                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                kotliSatyan.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));
                                            }
                                        }
                                        if (objnot.equals("5")) {
                                            String jobj = parentObject.getString("Count");
                                            strtnot5 = jobj + "";
                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                murree.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));
                                            }
                                        }
                                        if (objnot.equals("6")) {
                                            String jobj = parentObject.getString("Count");
                                            strtnot6 = jobj + "";
                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                rawalpindi.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));
                                            }
                                        }
                                        if (objnot.equals("7")) {
                                            String jobj = parentObject.getString("Count");
                                            strtnot7 = jobj + "";
                                            JSONArray jarr = parentObject.getJSONArray("IssuesList");
                                            for (int i = 0; i < jarr.length(); i++) {
                                                JSONObject object = jarr.getJSONObject(i);

                                                taxila.add(new AlectionModel(object.getString("IssueCategoryName"),
                                                        object.getString("napa").toString(),
                                                        object.get("TehsilName").toString(),
                                                        object.get("ConstituencyName").toString(),
                                                        object.get("IssueDetails").toString(),
                                                        object.get("ImageURL").toString(),
                                                        object.get("VideoURL").toString(),
                                                        object.get("ACDetail").toString(),
                                                        object.get("ACImageURL").toString(),
                                                        object.get("ACVideoURL").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("PollingStationId").toString(),
                                                        object.get("PollingStationName").toString(),
                                                        object.get("IssueStatus").toString(),
                                                        object.get("IssueId").toString(),
                                                        object.get("CreatorId").toString(),
                                                        object.get("CreationDateTime").toString(),
                                                        object.get("IssueCategoryId").toString(),
                                                        object.get("Count").toString(),
                                                        "", ""
//                                                    object.get("TehsilId").toString(),
//                                                    object.get("ConstituencyId").toString()
                                                ));
                                            }
                                        }




                                    }


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setTehsilNotUi();
//                                        Toast.makeText(getApplicationContext(), "Data is downloaded successfully.", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);

                                        }
                                    });

//                                Intent intent = new Intent(DcMainActivity.this, DcMainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Data cannot download, please try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    int errcode = error.networkResponse.statusCode;
                                    if (errcode == 400) {
                                        Toast.makeText(getApplicationContext(), "No user Available, Please Enter correct User name. ", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please try again, there is low internet connectivity", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            //  Toast.makeText(getApplicationContext(), error
                            // .getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

//                params.put("DistrictId", "117");
                    params.put("Userid", UserId);
                    params.put("DataFilter", "2");
//                params.put("password", password);
                    return params;
                }
            };

            VolleySingleton.getInstance(DcMainActivity.this).addToRequestQue(stringRequest);

    }

}