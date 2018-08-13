package com.appinsnap.ewatchIBfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class AlecMainActivity extends AppCompatActivity {
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
                            Intent intent = new Intent(AlecMainActivity.this, MessagesActivity.class);
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==3){
                            Intent intent = new Intent(AlecMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","submit");
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==4){
                            Intent intent = new Intent(AlecMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","draft");
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        } else if (position == 5) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("UserId"); // will delete key name
                            editor.commit(); // commit changes

                            Intent intent = new Intent(AlecMainActivity.this, LoginActivity.class);
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






        if(userRoleId.equals("3") ) {
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
                                Intent intent = new Intent(AlecMainActivity.this, MessagesActivity.class);
                                startActivity(intent);
                            }
                             else if (position == 3) {
                                getadmindata();
                                drawer.closeDrawer();
                                drawer.resetDrawerContent();
                                }

//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                            /*else if(position==3){
                            Intent intent = new Intent(AlecMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","submit");
//                            startActivity(intent);
                          Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==4){
                            Intent intent = new Intent(AlecMainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","draft");
                            startActivity(intent);
                           Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }*/ else if (position == 4) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.remove("UserId"); // will delete key name
                                editor.commit(); // commit changes

                                Intent intent = new Intent(AlecMainActivity.this, LoginActivity.class);
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

        if(userRoleId.equals("3") ) {
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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
                                                    "Aclist",
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

//                                Intent intent = new Intent(AlecMainActivity.this, AlecMainActivity.class);
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
//                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(AlecMainActivity.this).addToRequestQue(stringRequest);
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


    }
    private void onlcklstr(){
        // UserRoleId UserRole
        //1 Admin    (DC)
        //2 Vigilant  (user)
        //3 Resolver (AC)

        imgv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(userRoleId!=null && userRoleId.equals("1")){

                }else  if(userRoleId!=null && userRoleId.equals("2")){
                    firing = "firing";

                    Intent i = new Intent(AlecMainActivity.this, AlecSubmitActivity.class);
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

                }else  if(userRoleId!=null && userRoleId.equals("2")) {
                     Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                     i.putExtra("category", "Terrorism or Blast");
                     i.putExtra("categoryid", "3");

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

                     i.putExtra("issueList", terrorismOrBlast);

                    startActivity(i);
                }


            }
        });
        imgv4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(userRoleId!=null && userRoleId.equals("1")){

                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Use of Loadspeaker");
                    i.putExtra("categoryid", "4");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
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

                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Wall Chalking");
                    i.putExtra("categoryid", "5");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
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

                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Suspicious People");
                    i.putExtra("categoryid", "6");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
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

                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Oversize Board");
                    i.putExtra("categoryid", "7");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
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

                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                     Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                     i.putExtra("category", "Other");
                     i.putExtra("categoryid", "8");

                     startActivity(i);


                 }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
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

                }else  if(userRoleId!=null && userRoleId.equals("2")) {

                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Oversize Banner/Poster");
                    i.putExtra("categoryid", "9");

                    startActivity(i);


                }else  if(userRoleId!=null && userRoleId.equals("3")){

                    //AC =3
//
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
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
                }else  if(userRoleId!=null && userRoleId.equals("2")) {
                    Intent i = new Intent(getApplicationContext(), AlecSubmitActivity.class);
                    i.putExtra("category", "Without permission meeting procession");
                    i.putExtra("categoryid", "10");
                    startActivity(i);
                }else  if(userRoleId!=null && userRoleId.equals("3")){
                    //AC =3
                    Intent i = new Intent(getApplicationContext(), SubmitPendingActivity.class);
                    i.putExtra("category", "Display of Weapon");
                    i.putExtra("categoryid", "2");
                    i.putExtra("comefromlist", false);
                    i.putExtra("comefromAclist", true);
                    i.putExtra("issueList", withoutPermissionMeetingProcession);
                    i.putExtra("status", "AcList");


                    startActivity(i);
                }
            }
        });
        }
        }