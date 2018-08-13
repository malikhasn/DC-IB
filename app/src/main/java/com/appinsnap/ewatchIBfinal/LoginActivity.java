package com.appinsnap.ewatchIBfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;/*
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;*/
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {
    EditText et_username,et_password;
    TextView txtview_fp;
    Button btn_Login;
    public int REQUEST_CODE = 007;
    private String TAG = "tag";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static String UserId;
    private ProgressBar progressBar;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    DbHelper db;
    private String access_token;
    private String userRoleId,updatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        txtview_fp = (TextView) findViewById(R.id.txtview_fp);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
         pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
         editor = pref.edit();
        editor.putString("Webkey", "APMMhxh9ebU:APA91bEO7d5IUUIIBBBBNMMDUoa_ui1VMNQWHXxo-w5QcR6xfvX4NTZMYQvPDoXC4O_IJsjgOxKW4M75wIOPVS3Yx0WNe1SAf-3-xisXfbTFgeK3xH8Ym_-DT5ZCAAHKJFGEYR4yjhCdhNKrn4-Mt"); // Storing string
        editor.putString("isFirstTime", "true"); // Storing string
        editor.commit();
         db=new DbHelper(this);
       if( (pref.getString("UserId", null))!=null){
          userRoleId= pref.getString("UserRoleId", "2");
           if(userRoleId.equalsIgnoreCase("1")){
               Intent intent = new Intent(LoginActivity.this, DcMainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
           }else {
               Intent intent = new Intent(LoginActivity.this, AlecMainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
           }


//           Intent intent = new Intent(LoginActivity.this,AlecMainActivity.class);
//           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//           startActivity(intent);
       }
        if (checkAndRequestPermissions()) {
        }
        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_username.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter User name.", Toast.LENGTH_LONG).show();
                }else if( et_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter password.", Toast.LENGTH_LONG).show();
                }else {
                    loginCall();
                }
//                loginCall();
//                Intent intent = new Intent(LoginActivity.this,AlecMainActivity.class);
//                startActivity(intent);
                }
                 });
          txtview_fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!et_username.getText().toString().equals(""))
            {
                forgotPassword();
            }else {
                Toast.makeText(getApplicationContext(), "Please enter Username first", Toast.LENGTH_LONG).show();
            }
                 }
             });
            }
    private void forgotPassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/Report/ForgotPassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "An Email is send to Reset your Password", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
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

                params.put("UserName", et_username.getText().toString());
//                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view

    }

    private void loginCall() {

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/CheckLogin",
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/ems/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONArray arrayobj = new JSONArray(response);
                            JSONObject obj = arrayobj.getJSONObject(0);
                            //if no error in response
                           if(  obj!=null)   {
                                   //json_response = j_obj.getString("message");
//                            if (!response.equals("")) {
//                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                //String json_response = obj.getString("message");
//                                 UserId = response;//obj.getString("Id");
                                 UserId = obj.getString("Userid");
                               userRoleId = obj.getString("UserRoleId");
//                               access_token = obj.getString("access_token");
                               editor = pref.edit();
                               editor.putString("UserId", UserId); // Storing string
                               editor.putString("UserRoleId", userRoleId); // Storing string
//                               editor.putString("access_token", access_token); // Storing string
//                               editor.putString("Webkey", "APMMhxh9ebU:APA91bEO7d5IUUIIBBBBNMMDUoa_ui1VMNQWHXxo-w5QcR6xfvX4NTZMYQvPDoXC4O_IJsjgOxKW4M75wIOPVS3Yx0WNe1SAf-3-xisXfbTFgeK3xH8Ym_-DT5ZCAAHKJFGEYR4yjhCdhNKrn4-Mt"); // Storing string
                               editor.commit(); // commit changes
                               db.insertUser(UserId,et_username.getText().toString(),et_password.getText().toString());
                               String isfirsttime=   pref.getString("isFirstTime", null); // getting String
                               if(isfirsttime!=null && isfirsttime.equals("true")){
                                   gettehselData();
                               }else {

                                   Intent intent = new Intent(LoginActivity.this, AlecMainActivity.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);
                               }
                                //Toast.makeText(getApplicationContext(), "userId = "+response, Toast.LENGTH_SHORT).show();

//                                ShowToastMessage(json_response);
                                //getting the user from the response
                            } else {
                               // Toast.makeText(getApplicationContext(), "userId = "+response, Toast.LENGTH_SHORT).show();
                               UserId =db.getUserId(et_username.getText().toString(),et_password.getText().toString());
                               if(UserId!=null && !UserId.equals("")){
                                   editor.putString("UserId", UserId); // Storing string
                                   editor.commit(); // commit changes
                                   Intent intent = new Intent(LoginActivity.this,AlecMainActivity.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);
                               }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } catch (JSONException e) {
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
                    public void onErrorResponse(final VolleyError error) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                               error.getc

                                int errcode=error.networkResponse.statusCode;
                                if(errcode==403){
                                    Toast.makeText(getApplicationContext(), "Invalid Credencials, Please try again", Toast.LENGTH_LONG).show();
                                }else if(errcode==300){
                                    Toast.makeText(getApplicationContext(), "Invalid Access, Please try again", Toast.LENGTH_LONG).show();
                                }else {
                                    UserId = db.getUserId(et_username.getText().toString(),et_password.getText().toString());
                                    if (UserId != null && !UserId.equals("" )) {
                                        editor.putString("UserId", UserId); // Storing string
                                        editor.commit(); // commit changes
                                        Intent intent = new Intent(LoginActivity.this, AlecMainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please try again, there is low internet connectivity", Toast.LENGTH_LONG).show();
                                    }
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

                params.put("Username", et_username.getText().toString());
                params.put("Password", et_password.getText().toString());
//                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view

    }

    private void gettehselData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/ems/GetUerDisplay",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response!=null && !response.equals("")&& !response.equals("null")) {
//                                dataId = response;

                            //converting response to json object
                          try {
//                              JSONArray obj = new JSONArray(response);


//                              JSONObject jobj = obj.getJSONObject(0);
                              JSONObject jobj = new JSONObject(response);
                              JSONArray array = jobj.getJSONArray("TehsilsList");
                              for (int n = 0; n < array.length(); n++) {
                                  JSONObject object = array.getJSONObject(n);
//                                  dataModels.add(new MessagesDataModel(object.get("MessageTitle").toString(),
//                                          object.get("MessageDescription").toString(), object.get("MessageType").toString(),
//                                          object.get("Id").toString(), n, object.getBoolean("IsRead")));
                                  db.insertTehsil(object.get("TehsilName").toString(),object.get("Tehsilid").toString(),object.get("DistrictId").toString());

                              }
                              JSONArray array2 = jobj.getJSONArray("ConstituenciesList");
                              for (int n = 0; n < array2.length(); n++) {
                                  JSONObject object = array2.getJSONObject(n);
                                  db.insertConsiquences(object.get("ConstituencyNo").toString(),object.get("Constid").toString(),object.get("ThesilId").toString(), object.get("ConstituencyTypeid").toString());
                              }
                              JSONArray array3 = jobj.getJSONArray("PollingStationList");
                              for (int n = 0; n < array3.length(); n++) {
                                  JSONObject object = array3.getJSONObject(n);
                                    db.insertpolingstationdata(object.get("Pollingstationname").toString(),object.get("Pollingstationid").toString()
                                            ,object.get("Constituencyid").toString());
                              }

                              editor = pref.edit();
                              editor.putString("isFirstTime", "false"); // Storing string
                              String date = new SimpleDateFormat("yyyy-MM-dd HH:mm ", Locale.getDefault()).format(new Date());
                              editor.putString("updatedDate", date); // Storing string
                              editor.commit();


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                              Toast.makeText(getApplicationContext(), "Data is downloaded successfully.", Toast.LENGTH_LONG).show();
                              progressBar.setVisibility(View.GONE);
                                    }
                                });

                                if(userRoleId.equalsIgnoreCase("1")){
                                    Intent intent = new Intent(LoginActivity.this, DcMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(LoginActivity.this, AlecMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

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
                            Toast.makeText(getApplicationContext(), "Data cannot download, please try again.", Toast.LENGTH_LONG).show();

                            progressBar.setVisibility(View.GONE);

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
                updatedDate = pref.getString("updatedDate", ""); // getting String

                params.put("DistrictId", "117");
                params.put("UserRoleId", userRoleId);
                params.put("UpdateDate", updatedDate);

//                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQue(stringRequest);
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    // Method to request and check for permissions from  user.
    private boolean checkAndRequestPermissions() {
        try {
            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int permissionexternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }if (permissionexternalStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }

        }catch (Exception e){

        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e(TAG, "Permission callBack Called");
        try{


            switch (requestCode) {
                case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                    Map<String, Integer> permission = new HashMap<>();
                    permission.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                    permission.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++)
                            permission.put(permissions[i], grantResults[i]);
                        //check for all permission
                        if (permission.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && permission.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e(TAG, "Permission are not granted");
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA )) {
                                showDialogOK("We Need Camera Permission for Taking the Picture", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
//                                        case DialogInterface.BUTTON_NEGATIVE:
//                                            finish();
//                                            break;
                                        }
                                    }
                                });
                            }
                            else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                                showDialogOK("We Need Record Permission for Recording the Voice", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
//                                        case DialogInterface.BUTTON_NEGATIVE:
//                                            finish();
                                        }
                                    }
                                });
                            }
                            else {
                                explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.e(TAG, "onRequestPermissionsResult: ",e );
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && checkAndRequestPermissions()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            checkAndRequestPermissions();
            //Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:jsbankagent.management.application"));
                        startActivityForResult(intent, REQUEST_CODE);
                        //startActivityForResult(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.studio.barefoot.freeedrivebeacononlyapp")));
                    }
                });
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        finish();
//                    }
//                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        finish();
    }
}
