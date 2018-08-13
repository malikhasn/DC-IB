package com.appinsnap.ewatchIBfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexbbb.uploadservice.UploadService;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;/*
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;*/
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CategoryDataActivity extends AppCompatActivity {

    ImageView imageview_backbtn,imageview1;
    TextView tv_subject,tv_numbers,tv_brieffacts,tv_type,category_txt;
    Button btn_ok;
    private String imageRealPath;
    private String encodedImage;
    private Bitmap bitmap;
    private String strsubject;
    private String strnumbers;
    private String strbrieffacts;
    private String dataId,strtype,strcategory;
    private ProgressBar progressBar;
    private String UserId;
ArrayList<String> realPathList;
ArrayList<String> fileType;
    private String videoRealPath;
    private String audioRealPath;
    private DbHelper mydb;
    private ImageView vid_imageview;
    private RelativeLayout audio_relativelayout;
    private String strstatus;
    private Button btn_cancel;
    private RelativeLayout btn_next_relativelayout;
    private boolean comefromlist;
    private Integer recordId;
    private String access_token,Webkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_data);
        setupActionBar();
        imageRealPath=getIntent().getExtras().getString("imageRealPath");
        videoRealPath=getIntent().getExtras().getString("videoRealPath");
        audioRealPath=getIntent().getExtras().getString("audioRealPath");
        strsubject=getIntent().getExtras().getString("subject");
        strnumbers=getIntent().getExtras().getString("numbers");
        strbrieffacts=getIntent().getExtras().getString("brieffacts");
        strtype=getIntent().getExtras().getString("type");
        strcategory=getIntent().getExtras().getString("category");
        strstatus=getIntent().getExtras().getString("status");
        comefromlist=getIntent().getExtras().getBoolean("comefromlist",false);
        recordId=getIntent().getExtras().getInt("recordId",0);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        System.setProperty("http.keepAlive", "false");

        mydb = new DbHelper(this);


       /* File outputFolder= new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress/vid"+ currentDateFormat() +".mp4");//"+"photo_" + currentDate + ".jpg");

        if(videoRealPath!=null && !videoRealPath.equals("")) {
            VideoCompress.compressVideoLow(videoRealPath, outputFolder.getAbsolutePath().toString(), new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    //Start Compress
                    Toast.makeText(getApplicationContext(), "start copression", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                    //Finish successfully
                    Toast.makeText(getApplicationContext(), " copression Finish successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFail() {
                    //Failed
                    Toast.makeText(getApplicationContext(), " copression Failed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProgress(float percent) {
                    //Progress

                }
            });
        }
*/


        // Toast.makeText(getApplicationContext(),imageRealPath,Toast.LENGTH_LONG).show();
        imageview1=(ImageView)findViewById(R.id.imageview);
        vid_imageview=(ImageView)findViewById(R.id.vid_imageview);
        audio_relativelayout=(RelativeLayout)findViewById(R.id.audio_relativelayout);
        btn_next_relativelayout=(RelativeLayout)findViewById(R.id.btn_next_relativelayout);

        category_txt=(TextView) findViewById(R.id.category_txt);
        tv_type=(TextView) findViewById(R.id.tv_type);
        tv_subject=(TextView) findViewById(R.id.tv_subject);
        tv_numbers=(TextView) findViewById(R.id.tv_numbers_display);
        tv_brieffacts=(TextView) findViewById(R.id.tv_brieffacts_display);

        category_txt.setText(strcategory.toString());
        tv_type.setText(strtype.toString());
        tv_subject.setText(strsubject.toString());
        tv_numbers.setText(strnumbers.toString());
        tv_brieffacts.setText(strbrieffacts.toString());

        //hide layout buttons in case of submitted reports
        if(strstatus!=null && strstatus.equals("submit"))
        {
            btn_next_relativelayout.setVisibility(View.GONE);
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        UserId=   pref.getString("UserId", null); // getting String
        access_token=   pref.getString("access_token", null); // getting String
        Webkey=   pref.getString("Webkey", null); // getting String


        realPathList=new ArrayList<>();
        fileType=new ArrayList<>();
        if(imageRealPath!=null&&!imageRealPath.equals("")) {
            realPathList.add(imageRealPath);
            fileType.add("image");
        }
        if(videoRealPath!=null&&!videoRealPath.equals("")) {
            realPathList.add(videoRealPath);
            fileType.add("video");
        }
        if(audioRealPath!=null&&!audioRealPath.equals("")) {
            realPathList.add(audioRealPath);
            fileType.add("audio");
        }



        getpermissions();
        if(imageRealPath!=null && !imageRealPath.equals(""))
        {
            try {
                ExifInterface ei = new ExifInterface(imageRealPath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                Bitmap bitmap = BitmapFactory.decodeFile(imageRealPath);
                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap =  rotate(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotate(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotate(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }

                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(rotatedBitmap,
                        170, 170);
                imageview1.setImageBitmap(ThumbImage);

            } catch (Exception e)
            {
                e.printStackTrace();
            }

//            Bitmap thumbimage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageRealPath),170, 170);
//            imageview1.setImageBitmap(thumbimage);

//                    bitmap= getImageFileFromSDCard(imageRealPath);
//            Bitmap bitmap = BitmapFactory.co(getResources(), R.mipmap.hqimage);
//            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 75 ,null);
        }
        if(videoRealPath!=null && !videoRealPath.equals("")){
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoRealPath, MediaStore.Video.Thumbnails.MICRO_KIND);
            vid_imageview.setImageBitmap(bMap);
        }
        if(audioRealPath!=null && !audioRealPath.equals("")){
            audio_relativelayout.setVisibility(View.VISIBLE);
        }


        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
//                startActivity(intent);
//                  uploadImage();


                uploadData();
//                uploadImage();


            }
        });
        imageview_backbtn = (ImageView) findViewById(R.id.imgview_backbtn);
        imageview_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                draftDailog();
                CategoryDataActivity.super.onBackPressed();
//                Intent intent = new Intent(CategoryDataActivity.this,CategoryActivity.class);
//                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comefromlist){
                    CategoryDataActivity.super.onBackPressed();

                }else {
                    draftDailog();
                }
//                CategoryDataActivity.super.onBackPressed();
//                Intent intent = new Intent(CategoryDataActivity.this,CategoryActivity.class);
//                startActivity(intent);
            }
        });
        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        if(imageRealPath!=null && !imageRealPath.equals("")){
                            File imagefile=new File(imageRealPath);
                            try {
                                if (!imagefile.exists()) {
                                    imagefile.createNewFile();
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            //  startActivity(new Intent(Intent.ACTION_VIEW,  Uri.fromFile(imagefile))); /** replace with your own uri */
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(imagefile), "image/*");
                            startActivity(intent);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        vid_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        if(videoRealPath!=null && !videoRealPath.equals("")){
                            File vidfile=new File(videoRealPath);
                            try {
                                if (!vidfile.exists()) {
                                    vidfile.createNewFile();
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(vidfile), "video/*");
                            startActivity(intent);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        audio_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        if(audioRealPath!=null && !audioRealPath.equals("")){
                            File audfile=new File(audioRealPath);
                            try {
                                if (!audfile.exists()) {
                                    audfile.createNewFile();
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(audfile), "audio/*");
                            startActivity(intent);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private void draftDailog() {
        {
            final CharSequence[] items = { "Save As Draft",
                    "Discard" };
            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryDataActivity.this);
            builder.setTitle("Are you want Save Report ?");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(MainActivity.this);
                    if (items[item].equals("Save As Draft")) {
                        mydb.insertCategory(strcategory.toString(),strtype.toString(),strsubject.toString(),strnumbers.toString(),strbrieffacts.toString(),
                                imageRealPath,videoRealPath,audioRealPath,"draft");
                        Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }  else if (items[item].equals("Discard")) {
//                        CategoryDataActivity.super.onBackPressed();
                        Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                }
            });
            builder.show();
        }
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    void getpermissions(){

        ActivityCompat.requestPermissions(CategoryDataActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

//        if (ActivityCompat.checkSelfPermission(CategoryDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
//        } else {
//            bitmap = getImageFileFromSDCard(imageRealPath);
//            imageview1.setImageBitmap(bitmap);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 786);
//        } else {
//            bitmap = getImageFileFromSDCard(imageRealPath);
//            imageview1.setImageBitmap(bitmap);
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CategoryDataActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void explain(String msg) {
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(CategoryDataActivity.this);
        dialog.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

//                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("com.example.reportfirst"));
//                        startActivityForResult(intent, CAMERA_REQUEST);
                    }
                });
        dialog.show();
    }
    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(filename);
        imageRealPath=imageFile.getAbsolutePath();
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private void uploadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/SaveData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (!response.equals("")) {
                                dataId = response;
                                if(comefromlist) {
                                    //delete//

                                    mydb.deleteCategory(recordId);
                                    //add row
                                    mydb.insertCategory(strcategory.toString(), strtype.toString(), strsubject.toString(), strnumbers.toString(), strbrieffacts.toString(),
                                            imageRealPath, videoRealPath, audioRealPath, "submit" );
                                }else {
                                    mydb.insertCategory(strcategory.toString(), strtype.toString(), strsubject.toString(), strnumbers.toString(), strbrieffacts.toString(),
                                            imageRealPath, videoRealPath, audioRealPath, "submit" );
                                }

                            //converting response to json object
                            // JSONObject obj = new JSONObject(response);

                            //if no error in response
//                                        json_response = j_obj.getString("message");
                           /* if (response!= null) {
//                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                String json_response = obj.getString("message");
//                                ShowToastMessage(json_response);
                                //getting the user from the response
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }*/
//                           if(imageRealPath!=null && !imageRealPath.equals("")) {
                           if(realPathList!=null && realPathList.size()>0) {
//                               uploadImage();
                               new UploadFiles(getApplicationContext(),realPathList,fileType,dataId,access_token,Webkey, false).execute();
//                              uploadVideo();
//                                uploadAudio();
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       progressBar.setVisibility(View.GONE);
                                   }
                               });
                               Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                               Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                           }else {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       progressBar.setVisibility(View.GONE);
                                   }
                               });
                               Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                               Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                           }

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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int errcode=error.networkResponse.statusCode;
                        if(errcode==300){
                            Toast.makeText(getApplicationContext(), "Invalid Access, Please try again", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection Available, Tap On Cancel Button To Save Report As Draft", Toast.LENGTH_SHORT).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }) {
           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
//                String credentials = "username:password";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
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
                params.put("Id", UserId);
                params.put("Subject", strsubject);
                params.put("Numbers", strnumbers);
                params.put("BriefFacts", strbrieffacts);
                params.put("Category", strcategory);
                params.put("ThreatType", strtype);
                params.put("DateCreated", date);
                params.put("Webkey", Webkey);
//                params.put("password", password);
//                params.put("imgtype", "jpg");
                return params;
            }
        };

        VolleySingleton.getInstance(CategoryDataActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view


    }
    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    private void uploadImage() {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageRealPath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);




//        ((ImageView) findViewById(R.id.cam_prev)).setImageBitmap(bitmap);
       // imageview1.setImageBitmap(bitmap);
        //Toast.makeText(getApplicationContext(),encodedImage,Toast.LENGTH_LONG).show();
        SimpleMultiPartRequest stringRequest = new SimpleMultiPartRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/UpdateData",

        //StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/UpdateData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
//                            Intent intent = new Intent(CategoryDataActivity.this,CategoryActivity.class);
//                            startActivity(intent);
                            //converting response to json object
                           // JSONObject obj = new JSONObject(response);

                            //if no error in response
//                                        json_response = j_obj.getString("message");
                            if (videoRealPath!=null &&!videoRealPath.equals("") ) {

                               uploadVideo();
                                //Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_SHORT).show();

//                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                String json_response = obj.getString("message");
//                                ShowToastMessage(json_response);
                                //getting the user from the response

                            } else {
                                Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                                // Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Please try again, there is low internet connectivity", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }) {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                int percentage = (int) ((transferredBytes /  ((float)totalSize)) * 100);
                // updating progress bar value
                progressBar.setProgress(percentage);
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());
//
                Map<String, String> params = new HashMap<>();
//                params.put("Id", dataId);
//                params.put("IsImageAttached", "True");
//                params.put("Imagename", imageRealPath);
//                params.put("Image", encodedImage);
//                params.put("DateUpdated", date);
//                params.put("FileUpdatedBy", UserId);

//                params.put("password", password);
//                params.put("imgtype", "jpg");
                return params;
            }
        };
        String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());

        stringRequest.addFile("Image", imageRealPath);
        stringRequest.addStringParam("Id", dataId);
        stringRequest.addStringParam("IsImageAttached", "True");
        stringRequest.addStringParam("Imagename", imageRealPath);
        stringRequest.addStringParam("DateUpdated", date);
        stringRequest.addStringParam("FileUpdatedBy", UserId);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(CategoryDataActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view


    }

    private void uploadVideo() {

        //Toast.makeText(getApplicationContext(),encodedImage,Toast.LENGTH_LONG).show();
               SimpleMultiPartRequest stringRequest = new SimpleMultiPartRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/UpdateData",

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/UpdateData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
//                            Intent intent = new Intent(CategoryDataActivity.this,CategoryActivity.class);
//                            startActivity(intent);
                            //converting response to json object
                            // JSONObject obj = new JSONObject(response);

                            //if no error in response
//                                        json_response = j_obj.getString("message");
                            if (audioRealPath!=null &&!audioRealPath.equals("") ) {
                               // Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                                uploadAudio();
//                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                String json_response = obj.getString("message");
//                                ShowToastMessage(json_response);
                                //getting the user from the response

                            } else {
                                Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                                // Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Please try again, there is low internet connectivity", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }) {
                   @Override
                   public void onProgress(long transferredBytes, long totalSize) {
                       int percentage = (int) ((transferredBytes /  ((float)totalSize)) * 100);
                       // updating progress bar value
                       progressBar.setProgress(percentage);
                   }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());

                String encodedvid = getStringFile(new File(videoRealPath));
                Map<String, String> params = new HashMap<>();
//                params.put("Id", dataId);
//                params.put("IsVedioAttached", "True");
//                params.put("Vedioname", videoRealPath);
//                params.put("DateUpdated", date);
//                params.put("FileUpdatedBy", UserId);
//                params.put("Vedio", encodedvid);

//                params.put("password", password);
//                params.put("imgtype", "jpg");
                return params;
            }
        };

        String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());

        stringRequest.addFile("Vedio", videoRealPath);
        stringRequest.addStringParam("Id", dataId);
        stringRequest.addStringParam("IsVedioAttached", "True");
        stringRequest.addStringParam("Vedioname", videoRealPath);
        stringRequest.addStringParam("DateUpdated", date);
        stringRequest.addStringParam("FileUpdatedBy", UserId);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.markDelivered();
        VolleySingleton.getInstance(CategoryDataActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view


    }

    private void uploadAudio() {

            //Toast.makeText(getApplicationContext(),encodedImage,Toast.LENGTH_LONG).show();
            SimpleMultiPartRequest stringRequest = new SimpleMultiPartRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/UpdateData",

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://reportfirst.appinsnap.com/api/Report/UpdateData",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
//                            Intent intent = new Intent(CategoryDataActivity.this,CategoryActivity.class);
//                            startActivity(intent);
                                //converting response to json object
                                // JSONObject obj = new JSONObject(response);

                                //if no error in response
//                                        json_response = j_obj.getString("message");
                                if (response!=null &&!response.equals("") ) {
                                     Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                                   // uploadAudio();
//                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                String json_response = obj.getString("message");
//                                ShowToastMessage(json_response);
                                    //getting the user from the response

                                } else {
                                    Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                    Intent intent = new Intent(CategoryDataActivity.this,MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Please try again, there is low internet connectivity", Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }) {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    int percentage = (int) ((transferredBytes /  ((float)totalSize)) * 100);
                    // updating progress bar value
                    progressBar.setProgress(percentage);
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());

                    String encodedvid = getStringFile(new File(videoRealPath));
                    Map<String, String> params = new HashMap<>();
//                params.put("Id", dataId);
//                params.put("IsVedioAttached", "True");
//                params.put("Vedioname", videoRealPath);
//                params.put("DateUpdated", date);
//                params.put("FileUpdatedBy", UserId);
//                params.put("Vedio", encodedvid);

//                params.put("password", password);
//                params.put("imgtype", "jpg");
                    return params;
                }
            };

            String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());

            stringRequest.addFile("Audio", audioRealPath);
            stringRequest.addStringParam("Id", dataId);
            stringRequest.addStringParam("IsAudioAttached", "True");
            stringRequest.addStringParam("Audioname", audioRealPath);
            stringRequest.addStringParam("DateUpdated", date);
            stringRequest.addStringParam("FileUpdatedBy", UserId);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(CategoryDataActivity.this).addToRequestQue(stringRequest);
            //Display the image to the image view




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
