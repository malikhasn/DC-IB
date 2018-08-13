package com.appinsnap.ewatchIBfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {
    ImageView iv_back;
    private String UserId;
    private ListView listView;
    private ArrayList<MessagesDataModel> dataModels;
    private CustomAdapter adapter;
    private DbHelper mydb;
ArrayList<String> messageIds;
    private ProgressBar progressBar;
    private TextView txtview_nomsg;
    private String access_token,Webkey;
    private String userRoleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setupActionBar();

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        txtview_nomsg=(TextView)findViewById(R.id.txtview_nomsg);

        mydb= new DbHelper(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        UserId=   pref.getString("UserId", null); // getting String
        userRoleId=   pref.getString("UserRoleId", "2"); // getting String
        access_token=   pref.getString("access_token", null); // getting String
        Webkey=   pref.getString("Webkey", null); // getting String

        getMessages();

        iv_back = (ImageView) findViewById(R.id.imgview_backbtn);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userRoleId.equals("1") ){
                    Intent intent = new Intent(MessagesActivity.this, DcMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MessagesActivity.this, AlecMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        listView=(ListView)findViewById(R.id.list);
        messageIds=new ArrayList<>();
        dataModels= new ArrayList<>();

       /* dataModels.add(new MessagesDataModel("Message title subject", "Message description ", "urgent","userid",false));
        dataModels.add(new MessagesDataModel("Banana Bread", "Android 1.1", "2","February 9, 2009",false));
        dataModels.add(new MessagesDataModel("Cupcake", "Android 1.5", "3","April 27, 2009",false));
        dataModels.add(new MessagesDataModel("Donut","Android 1.6","4","September 15, 2009",false));
        dataModels.add(new MessagesDataModel("Eclair", "Android 2.0", "5","October 26, 2009",false));
        dataModels.add(new MessagesDataModel("Froyo", "Android 2.2", "8","May 20, 2010",false));
        dataModels.add(new MessagesDataModel("Gingerbread", "Android 2.3", "9","December 6, 2010",false));
        dataModels.add(new MessagesDataModel("Honeycomb","Android 3.0","11","February 22, 2011",false));
        dataModels.add(new MessagesDataModel("Ice Cream Sandwich", "Android 4.0", "14","October 18, 2011",false));
        dataModels.add(new MessagesDataModel("Jelly Bean", "Android 4.2", "16","July 9, 2012",false));
        dataModels.add(new MessagesDataModel("Kitkat", "Android 4.4", "19","October 31, 2013",false));
        dataModels.add(new MessagesDataModel("Lollipop","Android 5.0","21","November 12, 2014",false));
        dataModels.add(new MessagesDataModel("Marshmallow", "Android 6.0", "23","October 5, 2015",false));
*/
//        adapter= new CustomAdapter(dataModels,getApplicationContext());
//
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MessagesDataModel dataModel= dataModels.get(position);

                updateMessage(dataModel);
                Intent intent = new Intent(MessagesActivity.this, MessageDetailActivity.class);
                intent.putExtra("title", dataModel.getsubject());
                intent.putExtra("description", dataModel.getdescription());
                intent.putExtra("type", dataModel.gettype());
                 // Toast.makeText(getApplicationContext(),imageRealPath,Toast.LENGTH_LONG).show();
                startActivity(intent);


//                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
            }
        });


    }
    private void getMessages() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/Report/CheckMessages",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (response!=null && !response.equals("")&& !response.equals("null")) {
//                                dataId = response;

                                dataModels.clear();
                                //converting response to json object
                                JSONArray obj = new JSONArray(response);

                                JSONObject jobj =obj.getJSONObject(0);
                                JSONArray array =jobj.getJSONArray("messagelist");
                                for(int n = 0; n < array.length(); n++)
                                {
                                    JSONObject object = array.getJSONObject(n);
                                    dataModels.add(new MessagesDataModel(object.get("MessageTitle").toString(),
                                            object.get("MessageDescription").toString(), object.get("MessageType").toString(),
                                            object.get("Id").toString(),n,object.getBoolean("IsRead")));

                                    messageIds.add(object.get("Id").toString());
                                }

                                insertMessageInDB();
                                showMessagesFromDB();
                                sendMessageIds();

                            }
                            else {
                                //get from db
                                showMessagesFromDB();
                               // sendMessageIds();


                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int errcode=error.networkResponse.statusCode;
                        if(errcode==300){
                            Toast.makeText(getApplicationContext(), "Invalid Access, Please try again", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Please retry in a while", Toast.LENGTH_SHORT).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }) {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
//                String credentials = "username:password";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                access_token = Base64.encodeToString(access_token.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "bearer "+access_token);
                return headers;
            }*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                Map<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
//                params.put("Authorization", "bearer "+access_token);
                params.put("Webkey", Webkey);

                return params;
            }
        };


        VolleySingleton.getInstance(MessagesActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view


    }

    private void sendMessageIds() {

       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/SendMessageIds",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (response!=null && !response.equals("")&& !response.equals("null")) {
//                                dataId = response;


                                    // do some stuff....
                                }


                            else {
                                //get from db
//                                showMessagesFromDB();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Please retry in a while", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                Map<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("MessageIds", messageIds.toString());

//                params.put("Numbers", strnumbers);
//                params.put("BriefFacts", strbrieffacts);
//                params.put("Category", strcategory);
//                params.put("ThreatType", strtype);
//                params.put("DateCreated", date);
//                params.put("password", password);
//                params.put("imgtype", "jpg");
                return params;
            }
        };


        VolleySingleton.getInstance(MessagesActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view*/

        JSONObject jobj=new JSONObject();
//        messageIds.add("2");
//        messageIds.add("3");


        JSONArray array=new JSONArray();

        for(int i=0;i<messageIds.size();i++){
            JSONObject obj=new JSONObject();
            try {
                obj.put("messageId",messageIds.get(i));
//                obj.put("typeName","CAT_ID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
//        jobj.optJSONArray(array);
       try {
           jobj.put("UserId", UserId);
           jobj.put("Webkey", Webkey);
           jobj.put("MessageIds", array);
       }catch (JSONException e){
            e.printStackTrace();
       }

//        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/Report/SendMessageIds", jobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int errcode=error.networkResponse.statusCode;
                        if(errcode==300){
                            Toast.makeText(getApplicationContext(), "Invalid Access, Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
        }
                )
        {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
//                String credentials = "username:password";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                headers.put("Content-Type", "application/json");

                headers.put("Authorization","bearer "+ access_token);
                return headers;
            }*/
        };
        VolleySingleton.getInstance(MessagesActivity.this).addToRequestQue(jobReq);

//        queue.add(jobReq);



    }

    private void showMessagesFromDB() {
        dataModels.clear();
      ArrayList<MessagesDataModel> tempModel= mydb.getAllMessages();
        if(tempModel!=null && tempModel.size()>0) {

          dataModels.addAll(tempModel);
            adapter = new CustomAdapter(dataModels, getApplicationContext());
            listView.setAdapter(adapter);
            txtview_nomsg.setVisibility(View.INVISIBLE);
        }
        else {
            txtview_nomsg.setVisibility(View.VISIBLE);
        }

    }

    void updateMessage(MessagesDataModel dataModel){

        mydb.updateMessage(UserId,dataModel.getuserid(),dataModel.gettype(),dataModel.getsubject(),dataModel.getdescription(),"true",dataModel.getRecordId());

    }
    void insertMessageInDB(){
        for (int i=0;i<dataModels.size();i++){
            mydb.insertMessage(UserId,dataModels.get(i).getuserid(),dataModels.get(i).gettype(),dataModels.get(i).getsubject(),dataModels.get(i).getdescription(),String.valueOf(dataModels.get(i).getisread()));
        }

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
