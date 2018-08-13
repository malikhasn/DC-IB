package com.appinsnap.ewatchIBfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.vincent.videocompressor.VideoCompress;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

public class AlecSubmitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks ,GoogleApiClient.OnConnectionFailedListener{
    private ProgressBar progressBar;
    Button btn_save;
    private Button btn_cancel;
    Button switch2;
    private DbHelper mydb;
    Spinner spinner,spinnertwo,pol_spinner;
    ImageView iv_back;
    TextView categorySelect;
    private final int CAMERA_REQUEST = 420;
    ImageView captureLayout, capturevideo_relativlayout, capturevice_relativlayout,dc_image;
    private String imageRealPath = "";
    private File sdImageMainDirectory;
    private Uri outputFileUri;
    private Uri file;
    EditText et_subject, et_numbers, et_brief_facts,et_dc_facts;
    private String spinnerItem;
    private String subspinnerItem;
    public static String audioRealPath;
    private String videoRealPath;
    private ImageView capture_cross,capture_video_cross,capture_voice_cross,dc_vedio;
    ProgressDialog dialog ;
    private Boolean comefromlist;
    private ArrayList<String> Tehsilnames;
    private ArrayList<String> TehsilId;
    private ArrayList<String> realPathList,fileType;
    private String dataId;
    private String UserId;
    private String strCategory;
    private String strnapa,strtype,strsubtype,strdetail,strstatus;
    private int recordId;
    private ArrayList<String> consname;
    private ArrayList<String> consId;
    private ArrayList<String> polname;
    private ArrayList<String> polId;
    private String polingstationId;
    private String strpolid;
    Intent i;
    private boolean comefromAClist;
    AlecMainActivity main;
    String ik;
    String j;
    TextView violationTitle;
    private String NAPA="1";
    private String strpolname;
    private String polingstationName;
    private String strCategoryId;
    private boolean comefromSubmitlist;
    private ToggleButton tbutton;
    private String serverRecordId;
    private String lat;
    private String lng;
    private String spinnerItemId;
    private String subspinnerItemId;
    private AlectionModel dataModel;

    //Location
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final long INTERVAL = 2000;
    private static final long FASTEST_INTERVAL = 1000;
    private static final String TAG = "LocationService";
    private String stracdetail ="";
    private String acimageRealPath="";
    private String acvideoRealPath="";
    private boolean comefromDClist;
    private RelativeLayout dcrelativelayout;


    @Override
    public void onResume() {
        super.onResume();
        Log.e(this.getClass().getSimpleName(), "onResume()");
        mGoogleApiClient.connect();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alec_submit);
        violationTitle = findViewById(R.id.txt_activity_title);
   /*     Intent iin= getIntent();

        String category = iin.getStringExtra("category");
        Log.e("Category",category);
//        Toast.makeText(AlecSubmitActivity.this, "Category" +category, Toast.LENGTH_SHORT).show();
        violationTitle.setText(""+category);
        Bundle b = iin.getExtras();
        Log.e(category, "category" );*/



        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(AlecSubmitActivity.this, "GooglePlayServices Not Available Please Update it", Toast.LENGTH_LONG).show();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                //   .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        setupActionBar();
        categorySelect = (TextView) findViewById(R.id.txt_category);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_numbers = (EditText) findViewById(R.id.et_numbers);
        et_brief_facts = (EditText) findViewById(R.id.et_brief_facts);
        // Spinner element
         spinner = (Spinner) findViewById(R.id.spinner_type);
         spinnertwo = (Spinner) findViewById(R.id.sub_spinner_type);
        pol_spinner = (Spinner) findViewById(R.id.pol_spinner);

        et_brief_facts.setSelected(false);
        audioRealPath=new String();
        dialog = new ProgressDialog(AlecSubmitActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        captureLayout = (ImageView) findViewById(R.id.capture_relativelayout);
        capturevideo_relativlayout = (ImageView) findViewById(R.id.capturevideo_relativlayout);
        capturevice_relativlayout = (ImageView) findViewById(R.id.capturevice_relativlayout);

        capture_cross = (ImageView) findViewById(R.id.capture_cross);
        capture_video_cross = (ImageView) findViewById(R.id.capture_video_cross);
        capture_voice_cross = (ImageView) findViewById(R.id.capture_voice_cross);
        mydb = new DbHelper(this);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        iv_back = (ImageView) findViewById(R.id.img_backbtn);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        //////////////////////////// dc

        et_dc_facts=(EditText) findViewById(R.id.dc_details_resolved);
        dc_image=(ImageView) findViewById(R.id.dc_view_img_resolv);
        dc_vedio=(ImageView) findViewById(R.id.dc_view_video_);
        dcrelativelayout = (RelativeLayout)findViewById(R.id.second_layout);

        /////////////////////////ac resource initialization




        tbutton = (ToggleButton)findViewById(R.id.toggleButton1);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        UserId=   pref.getString("UserId", null); // getting String

        dataModel= (AlectionModel)getIntent().getExtras().getSerializable("model");

        comefromlist=getIntent().getExtras().getBoolean("comefromlist",false);
        comefromSubmitlist=getIntent().getExtras().getBoolean("comefromSubmitlist",false);
        comefromAClist=getIntent().getExtras().getBoolean("comefromAClist",false);
        comefromDClist=getIntent().getExtras().getBoolean("comefromDClist",false);
        strCategory=getIntent().getExtras().getString("category","");
        strCategoryId=getIntent().getExtras().getString("categoryid","");
        strnapa=getIntent().getExtras().getString("napa","");
        strtype=getIntent().getExtras().getString("type","");
        strsubtype=getIntent().getExtras().getString("subtype","");
        strpolid=getIntent().getExtras().getString("polid","");
        strpolname=getIntent().getExtras().getString("polname","");
        strdetail=getIntent().getExtras().getString("detail","");

        imageRealPath=getIntent().getExtras().getString("imageRealPath","");
        videoRealPath=getIntent().getExtras().getString("videoRealPath","");
        audioRealPath=getIntent().getExtras().getString("audioRealPath","");

        stracdetail=getIntent().getExtras().getString("acDetail","");
        acimageRealPath=getIntent().getExtras().getString("acimageRealPath","");
        acvideoRealPath=getIntent().getExtras().getString("acvideoRealPath","");


            if(dataModel!=null && dataModel.getVideo()!=null) {
              videoRealPath = new String();
              videoRealPath = dataModel.getVideo();
            }

        violationTitle.setText(strCategory);

            if(strdetail.equalsIgnoreCase("null")){
               strdetail="";
             }

            if(stracdetail.equalsIgnoreCase("null")){
              stracdetail="";
            }


        strstatus=getIntent().getExtras().getString("status","");
        recordId=getIntent().getExtras().getInt("recordId",0);
        serverRecordId=getIntent().getExtras().getString("serverrecordId","");



//        categorySelect.setText(strCategory);

       /* Bundle bundle = getIntent().getExtras();
        String ct = bundle.getString("CT");
        String CI = bundle.getString("CI");
        String political = bundle.getString("political");
        String religious = bundle.getString("religious");
        String securityForeign = bundle.getString("securityForeign");
        String crime = bundle.getString("txt_crime");
        String threatAlert = bundle.getString("threatAlert");
        String misc = bundle.getString("misc");
        if (ct != null && !ct.isEmpty()) {
            categorySelect.setText(ct);
        }
        if (CI != null && !CI.isEmpty()) {
            categorySelect.setText(CI);
        }
        if (political != null && !political.isEmpty()) {
            categorySelect.setText(political);
        }
        if (religious != null && !religious.isEmpty()) {
            categorySelect.setText(religious);
        }
        if (religious != null && !religious.isEmpty()) {
            categorySelect.setText(religious);
        }
        if (securityForeign != null && !securityForeign.isEmpty()) {
            categorySelect.setText(securityForeign);
        }
        if (crime != null && !crime.isEmpty()) {
            categorySelect.setText(crime);
        }
        if (threatAlert != null && !threatAlert.isEmpty()) {
            categorySelect.setText(threatAlert);
        }
        if (misc != null && !misc.isEmpty()) {
            categorySelect.setText(misc);
        }
*/

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinnertwo.setOnItemSelectedListener(this);
        pol_spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("");
        categories.add("Urgent");
        categories.add("Important");
        categories.add("Normal");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        getspinnerdatafromdb();
        // Load ffmpeg lib for audio converter
        tbutton.setChecked(true);

        if(comefromlist || comefromSubmitlist|| comefromAClist){
            setDataInFields();
        }
        if (comefromAClist) {
            btn_save.setText("RESOLVE");
            btn_save.setEnabled(true);
            btn_save.setVisibility(View.VISIBLE);
        }
        if(comefromDClist){
            setDCdata();
        }


        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tbutton.isChecked())
                {
                    NAPA="1";
                    if(Tehsilnames!=null && Tehsilnames.size()>0 && TehsilId !=null && TehsilId.size()>0) {
                       if(spinnerItem!=null && !spinnerItem.equals("")) {
                           int pos = Tehsilnames.indexOf(spinnerItem);
                           String tehsilId = TehsilId.get(pos);
                           getsubspinnerdatafromdb(tehsilId, NAPA);
                           pol_spinner.setSelection(0);
                       }
                    }
//                    Toast.makeText(AlecSubmitActivity.this, "National Assembly", Toast.LENGTH_LONG).show();
                }
                else {
                    NAPA="2";
                    if(Tehsilnames!=null && Tehsilnames.size()>0 && TehsilId !=null && TehsilId.size()>0) {
                        if(spinnerItem!=null && !spinnerItem.equals("")) {
                            int pos = Tehsilnames.indexOf(spinnerItem);
                            String tehsilId = TehsilId.get(pos);
                            getsubspinnerdatafromdb(tehsilId, NAPA);
                            pol_spinner.setSelection(0);
                        }
                    }

//                    Toast.makeText(AlecSubmitActivity.this, "Provincial Assembly", Toast.LENGTH_LONG).show();
                }
            }
        });


        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(Exception error) {
                // FFmpeg is not supported by device
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(comefromAClist){


                   Intent intent = new Intent(AlecSubmitActivity.this, AcResolveActivity.class);
                   intent.putExtra("imageRealPath", imageRealPath);
                   intent.putExtra("videoRealPath", videoRealPath);
//                   intent.putExtra("detail", strdetail);
                   intent.putExtra("napa", strnapa);
                   intent.putExtra("type", strtype);
                   intent.putExtra("subtype", strsubtype);
                   intent.putExtra("polid", strpolid);
                   intent.putExtra("category", strCategory.toString());
                   intent.putExtra("status", strstatus);//draft submit, aclist
                   intent.putExtra("comefromlist", true);
                   intent.putExtra("recordId", recordId);
                   intent.putExtra("serverrecordId", serverRecordId);
                   // Toast.makeText(getApplicationContext(),imageRealPath,Toast.LENGTH_LONG).show();
                   startActivity(intent);
               }else {

                   if (spinnerItem == null || spinnerItem.equals("")) {
                       Toast.makeText(getApplicationContext(), "Please select type", Toast.LENGTH_LONG).show();
                       return;
                   }
                   realPathList = new ArrayList<>();
                   fileType = new ArrayList<>();
                   if (imageRealPath != null && !imageRealPath.equals("")) {
                       realPathList.add(imageRealPath);
                       fileType.add("image");
                   }
                   if (videoRealPath != null && !videoRealPath.equals("")) {
                       realPathList.add(videoRealPath);
                       fileType.add("video");
                   }
                   if (audioRealPath != null && !audioRealPath.equals("")) {
                       realPathList.add(audioRealPath);
                       fileType.add("audio");
                   }

                   submit();
               }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if((comefromlist!=null && comefromlist) ||comefromSubmitlist || comefromAClist){
                    AlecSubmitActivity.super.onBackPressed();

                }else {
                    draftDailog();
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comefromDClist && comefromAClist) {
                    Intent intent = new Intent(AlecSubmitActivity.this, DcMainActivity.class);
                    startActivity(intent);

                }

                else
                {
                    Intent intent = new Intent(AlecSubmitActivity.this, AlecMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        captureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                    } else if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //                           explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                        ActivityCompat.requestPermissions(AlecSubmitActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                    }  else if(comefromAClist) {
                        //   String imageRealPath = "http://ems.appinsnap.com/api/FilesStore/115/Images/photo_20180704_19_49_24.jpg";
                        if (imageRealPath != null && !imageRealPath.equals("")&& !imageRealPath.equalsIgnoreCase("null")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            imageRealPath = Normalizer.normalize(imageRealPath, Normalizer.Form.NFD);
                            imageRealPath = imageRealPath.replaceAll("~", "");
//                            imageRealPath.replace(/~/g,"");
                            Uri data = Uri.parse(getString(R.string.APP_URL)+imageRealPath.toString());
                            intent.setDataAndType(data, "image/*");
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"No Image Attached",Toast.LENGTH_LONG).show();
                        }

                    }else {

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
                        }else {
                            selectImage();
                        }
//                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(photoCaptureIntent, 20);

                  /*      Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        File root = new File(Environment
                                .getExternalStorageDirectory()
                                + File.separator + "Report_first" + File.separator);
                        root.mkdirs();
                        sdImageMainDirectory = new File(root, "myPicName.jpg");
                        outputFileUri = Uri.fromFile(sdImageMainDirectory);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        capturevideo_relativlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   /* if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.CAPTURE_VIDEO_OUTPUT) != PackageManager.PERMISSION_GRANTED) {
                       // explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                        ActivityCompat.requestPermissions(CategoryActivity.this,
                                new String[]{Manifest.permission.CAPTURE_VIDEO_OUTPUT},
                                1);
                    }else*/ if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //                           explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                        ActivityCompat.requestPermissions(AlecSubmitActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                2);
                    }else if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AlecSubmitActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                4);
                    } else if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AlecSubmitActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                5);
                    } else if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AlecSubmitActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                3);
                    } else if(comefromAClist) {
                        //  videoRealPath="http://ems.appinsnap.com/api/FilesStore/109/Videos/vid20180704_07_39_16.mp4";
                        if (videoRealPath != null && !videoRealPath.equals("") && !videoRealPath.equalsIgnoreCase("null")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            videoRealPath = Normalizer.normalize(videoRealPath, Normalizer.Form.NFD);
                            videoRealPath = videoRealPath.replaceAll("~", "");
                            intent.setDataAndType(Uri.parse(getString(R.string.APP_URL)+ videoRealPath.toString()), "video/*");
                            startActivity(Intent.createChooser(intent, "Complete action using"));
                        }else {
                            Toast.makeText(getApplicationContext(),"No Video Attached",Toast.LENGTH_LONG).show();
                        }

                    }else {

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
                        }else {
                            selectVideo();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        capturevice_relativlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(AlecSubmitActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                    } else {

//                        Intent recordAudio=new Intent(CategoryActivity.this,RecorderActivity.class);
//                        startActivity(recordAudio);
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
                        }else {
                            selectAudio();
                        }

//                        File outputFolder= new File(Environment.getExternalStorageDirectory(), "reportFirst/sound");
//                        outputFolder.mkdirs();
//                        File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/sound/"+"sound_" + currentDateFormat() + ".mp3");
//                        audioRealPath=outputFile.getAbsolutePath();
//                        Intent cameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
//                        cameraIntent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
//                        cameraIntent.putExtra("android.speech.extra.GET_AUDIO", true);
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
//
//                        startActivityForResult(cameraIntent, 23);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        capture_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { "Delete",
                        "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
                builder.setTitle("Do You Want to Delete Audio");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Delete")) {
                            imageRealPath="";
                            capture_cross.setVisibility(View.INVISIBLE);
                            captureLayout.setImageResource(R.drawable.image);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });
        capture_video_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { "Delete",
                        "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
                builder.setTitle("Do You Want to Delete Video");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Delete")) {
                            videoRealPath="";
                            capture_video_cross.setVisibility(View.INVISIBLE);
                            capturevideo_relativlayout.setImageResource(R.drawable.video);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        capture_voice_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { "Delete",
                        "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
                builder.setTitle("Do You Want to Delete Audio");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Delete")) {
                            audioRealPath="";
                            capture_voice_cross.setVisibility(View.INVISIBLE);
                            capturevice_relativlayout.setImageResource(R.drawable.audio_icon);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });


        dc_vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (acvideoRealPath != null && !acvideoRealPath.equals("") && !acvideoRealPath.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    acvideoRealPath = Normalizer.normalize(acvideoRealPath, Normalizer.Form.NFD);
                    acvideoRealPath = acvideoRealPath.replaceAll("~", "");
                    intent.setDataAndType(Uri.parse(getString(R.string.APP_URL)+ acvideoRealPath.toString()), "video/*");
                    startActivity(Intent.createChooser(intent, "Complete action using"));
                }else {
                    Toast.makeText(getApplicationContext(),"No Video Attached",Toast.LENGTH_LONG).show();
                }

            }
        });



        dc_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (acimageRealPath != null && !acimageRealPath.equals("")&& !acimageRealPath.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    acimageRealPath = Normalizer.normalize(acimageRealPath, Normalizer.Form.NFD);
                    acimageRealPath = acimageRealPath.replaceAll("~", "");
//                            imageRealPath.replace(/~/g,"");
                    Uri data = Uri.parse(getString(R.string.APP_URL)+acimageRealPath.toString());
                    intent.setDataAndType(data, "image/*");
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"No Image Attached",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void setDCdata() {
//        hidekeyboard();
        ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), InputMethodManager.HIDE_IMPLICIT_ONLY);

        btn_save.setVisibility(View.GONE);
        dcrelativelayout.setVisibility(View.VISIBLE);

        et_dc_facts.setText(stracdetail.toString());
        et_dc_facts.setEnabled(false);

    }

    private void setDataInFields() {
//        switch1.setText(strnapa);


       /* List<String> items1 = new ArrayList<String>();
        items1.add(strtype);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

 List<String> items2 = new ArrayList<String>();
        items2.add(strsubtype);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertwo.setAdapter(dataAdapter2);

 List<String> items3 = new ArrayList<String>();
        items3.add(strpolname);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items3);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pol_spinner.setAdapter(dataAdapter3);
*/



        final Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                int pos=-1;
                pos=Tehsilnames.indexOf(strtype);
                if(pos!=-1){
                    spinner.setSelection(pos+1);
//            getsubspinnerdatafromdb(TehsilId.get(pos));
                }
            }
        }, 50);
//        categorySelect.setText(strCategory);



        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(strnapa.equalsIgnoreCase("na")){
                    tbutton.setChecked(true);
                    tbutton.callOnClick();
                    NAPA="1";
                }else {
                    tbutton.setChecked(false);
                    tbutton.callOnClick();

                    NAPA="2";

                }
            }
        }, 200);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                int pos2=-1;
                if(consname!=null)
                pos2=consname.indexOf(strsubtype);
                if(pos2!=-1){
                    spinnertwo.setSelection(pos2+1);
//            getpolingdatafromdb(consId.get(pos2));
                }
            }
        }, 600);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                int pos3=-1;
                if(polId!=null)
                    pos3=polId.indexOf(strpolid);
                if(pos3!=-1){
                    pol_spinner.setSelection(pos3+1);
                }
            }
        }, 800);
        et_brief_facts.setText(strdetail);
        if(imageRealPath!=null && !imageRealPath.equals("")&& !imageRealPath.equalsIgnoreCase("null")) {

            try {

                ExifInterface ei = new ExifInterface(imageRealPath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                Bitmap bitmap = BitmapFactory.decodeFile(imageRealPath);
                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotate(bitmap, 90);
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
                captureLayout.setImageBitmap(ThumbImage);
                capture_cross.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

        }

        if(videoRealPath!=null && !videoRealPath.equals("")&& !videoRealPath.equalsIgnoreCase("null")){
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoRealPath, MediaStore.Video.Thumbnails.MICRO_KIND);
            capturevideo_relativlayout.setImageBitmap(bMap);
            capture_video_cross.setVisibility(View.VISIBLE);

        }else {

        }








        if(comefromSubmitlist || comefromAClist){
            //disable all fields
            tbutton.setEnabled(false);

            spinner.setEnabled(false);
            spinnertwo.setEnabled(false);
            pol_spinner.setEnabled(false);

            et_brief_facts.setEnabled(false);


            captureLayout.setEnabled(false);
            if(imageRealPath!=null && !imageRealPath.equals("")) {
                captureLayout.setEnabled(true);
            }
            capture_cross.setVisibility(View.INVISIBLE);


            capturevideo_relativlayout.setEnabled(false);
            if(videoRealPath!=null && !videoRealPath.equals("")) {
                capturevideo_relativlayout.setEnabled(true);
            }
            capture_video_cross.setVisibility(View.INVISIBLE);

            btn_save.setEnabled(false);
            btn_save.setVisibility(View.GONE);

        }

        if(comefromAClist){
            if(imageRealPath!=null && !imageRealPath.equals("")&& !imageRealPath.equalsIgnoreCase("null")) {
                captureLayout.setEnabled(true);
                captureLayout.setBackground(getResources().getDrawable(R.drawable.image));
                capture_cross.setVisibility(View.INVISIBLE);

            }

            if(videoRealPath!=null && !videoRealPath.equals("")&& !videoRealPath.equalsIgnoreCase("null")) {
                capturevideo_relativlayout.setEnabled(true);
                capturevideo_relativlayout.setBackground(getResources().getDrawable(R.drawable.video));
                capture_video_cross.setVisibility(View.INVISIBLE);
            }
        }




    }

    private void submit() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.APP_URL)+"/api/ems/saveissue",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (!response.equals("")) {
                                JSONObject obj = new JSONObject(response);
                                String date = new SimpleDateFormat("yyyy MM dd hh:mm a", Locale.getDefault()).format(new Date());

                                dataId = obj.getString("Message");;
//                                dataId = response;
                               if(NAPA.equals("1")){
                                   strnapa="na";
                               }else {
                                   strnapa="pa";

                               }
                                if(comefromlist ||comefromSubmitlist) {
                                    //delete//

                                    mydb.deleteElection(recordId);
                                    //add row
                                    mydb.insertElection(strCategory.toString(), strnapa, spinnerItem, subspinnerItem,polingstationId, et_brief_facts.getText().toString(),
                                            imageRealPath, videoRealPath, "", "", "","submit" ,"",date,"","","",polingstationName,"","","");
                                }else {
                                    mydb.insertElection(strCategory.toString(), strnapa, spinnerItem, subspinnerItem,polingstationId, et_brief_facts.getText().toString(),
                                            imageRealPath, videoRealPath, "", "", "","submit" ,"",date,"","","",polingstationName,"","","");

                                     }

                                //converting response to json object
                                // JSONObject obj = new JSONObject(response);

                                //if no error in response
//                                        json_response = j_obj.getString("message");
                                if (response!= null) {
//                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                String json_response = obj.getString("message");
//                                ShowToastMessage(json_response);
                                //getting the user from the response
                            } else {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

//                           if(imageRealPath!=null && !imageRealPath.equals("")) {
                                if(realPathList!=null && realPathList.size()>0) {
//                               uploadImage();
                                    new UploadFiles(getApplicationContext(),realPathList,fileType,dataId,"","", false).execute();
//                              uploadVideo();
//                                uploadAudio();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(AlecSubmitActivity.this,AlecMainActivity.class);
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
                                    Intent intent = new Intent(AlecSubmitActivity.this,AlecMainActivity.class);
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
 @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 Map<String, String> headers = new HashMap<>();
 //                String credentials = "username:password";
 //                String auth = "Basic "
 //                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
 //                headers.put("Content-Type", "application/json");
//                 headers.put("Content-Type", "application/json; charset=UTF-8");
//                 headers.put("Authorization", "bearer "+access_token);
                 return headers;
             }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                String date = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault()).format(new Date());
                String date = new SimpleDateFormat("yyyy MM dd hh:mm a", Locale.getDefault()).format(new Date());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                Map<String, String> params = new HashMap<>();
                params.put("CreatorId", UserId);
                params.put("PollingStationId", polingstationId);
//                params.put("PollingStationId", "2");
//                params.put("PollingStationId", "2");
                params.put("IssueDetails", et_brief_facts.getText().toString());
                params.put("CreationDateTime", date);
                params.put("IssueCategoryId", strCategoryId);
                params.put("TehsilId", spinnerItemId);
                params.put("ConstituencyId", subspinnerItemId);

                params.put("TehsilName", spinnerItem);
                params.put("ConstituencyName", subspinnerItem);
                params.put("PollingStationName", polingstationName);
                params.put("lat", lat);
                params.put("lng", lng);

               /* params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
                params.put("CreationDateTime", date);
*/

//                strCategory.toString(), switch1.getText().toString(), spinnerItem, subspinnerItem,polingstationId, et_brief_facts.getText().toString(),
//                        imageRealPath, videoRealPath, "", "", "","submit" ,"",date.toString(),"","","","","");


//                params.put("Webkey", Webkey);
//                params.put("password", password);
//                params.put("imgtype", "jpg");
                return params;
            }
        };

        VolleySingleton.getInstance(AlecSubmitActivity.this).addToRequestQue(stringRequest);
        //Display the image to the image view


    }
    private void getspinnerdatafromdb(){
        Tehsilnames=new ArrayList<>();
        TehsilId=new ArrayList<>();

        // Spinner Drop down elements
        HashMap hm=mydb.gettehsiles();
        Tehsilnames=(ArrayList<String>) hm.get("tehname");
        TehsilId=(ArrayList<String>) hm.get("tehid");

        System.out.println("---------------------------" + hm.toString());

        List<String> spinnerdata = new ArrayList<String>();
        spinnerdata.add("");
        spinnerdata.addAll(Tehsilnames);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerdata);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

//        list=new ArrayList<HashMap<String,String>>();
//        ArrayList<HashMap<String, String>> maplist=mydb.gettehsiles();
//        CustomAdapter cp = new CustomAdapter(maplist,this);
//        spinner.setAdapter(cp);
    }

    private void getsubspinnerdatafromdb(String tehsilId, String NAPA){

        consname=new ArrayList<>();
        consId=new ArrayList<>();

        // Spinner Drop down elements
        HashMap hm=mydb.getconsiqueces(tehsilId,NAPA);
        consname=(ArrayList<String>) hm.get("consname");
        consId=(ArrayList<String>) hm.get("consid");

        List<String> spinnerdata = new ArrayList<String>();
        spinnerdata.add("");
        spinnerdata.addAll(consname);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerdata);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnertwo.setAdapter(dataAdapter);


    }
    private void getpolingdatafromdb(String consid){

        polname=new ArrayList<>();
        polId=new ArrayList<>();

        // Spinner Drop down elements
        HashMap hm=mydb.getpolingstation(consid);
        polname=(ArrayList<String>) hm.get("polname");
        polId=(ArrayList<String>) hm.get("polid");

        List<String> spinnerdata = new ArrayList<String>();
        spinnerdata.add("");
        spinnerdata.addAll(polname);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerdata);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        pol_spinner.setAdapter(dataAdapter);


    }
    private void draftDailog() {
        {
            final CharSequence[] items = { "Save As Draft",
                    "Discard" };
            AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
            builder.setTitle("Are you want Save Report ?");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(AlecMainActivity.this);
                    if (items[item].equals("Save As Draft")) {

                        String date = new SimpleDateFormat("yyyy MM dd hh:mm a", Locale.getDefault()).format(new Date());
                        if(NAPA.equals("1")){
                            strnapa="na";
                        }else {
                            strnapa="pa";

                        }
                        mydb.insertElection(strCategory.toString(), strnapa, spinnerItem, subspinnerItem,polingstationId, et_brief_facts.getText().toString(),
                                imageRealPath, videoRealPath, "", "", "","draft","",date.toString(),"","","",polingstationName,"" ,"","");
                        Intent intent = new Intent(AlecSubmitActivity.this,AlecMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }  else if (items[item].equals("Discard")) {
//                        CategoryDataActivity.super.onBackPressed();
                        Intent intent = new Intent(AlecSubmitActivity.this,AlecMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                }
            });
            builder.show();
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(AlecMainActivity.this);
                if (items[item].equals("Take Photo")) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File outputFolder= new File(Environment.getExternalStorageDirectory(), "reportFirst/images");//"+"photo_" + currentDate + ".jpg");
                    outputFolder.mkdirs();
                    File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/images/"+"/photo_" + currentDateFormat() + ".jpg");
                    try {
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    imageRealPath=outputFile.getAbsolutePath();



                    Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));

                    startActivityForResult(photoCaptureIntent, 20);

//                    userChoosenTask="Take Photo";
//                    if(result)
//                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 21);//one can be replaced with any action code


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectVideo() {
        final CharSequence[] items = { "Rec Video", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
        builder.setTitle("Rec Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(AlecMainActivity.this);
                if (items[item].equals("Rec Video")) {

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File outputFolder= new File(Environment.getExternalStorageDirectory(), "reportFirst/videos");//"+"photo_" + currentDate + ".jpg");
                    outputFolder.mkdirs();
                    File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/"+"vid_" + currentDateFormat() + ".mp4");
                    try {
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
//                       outputFolder.mkdir();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    videoRealPath=outputFile.getAbsolutePath();

                    Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
                    // set the video image quality to high
                    cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(cameraIntent, 22);

                } else if (items[item].equals("Choose from Library")) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto , 21);//one can be replaced with any action code
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.putExtra("return-data", true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Video"),23);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    void selectAudio(){
        final CharSequence[] items = { "Rec Audio", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AlecSubmitActivity.this);
        builder.setTitle("Rec Audio!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(AlecMainActivity.this);
                if (items[item].equals("Rec Audio")) {
                    Intent recordAudio=new Intent(AlecSubmitActivity.this,RecorderActivity.class);
                    startActivity(recordAudio);
                    capture_voice_cross.setVisibility(View.VISIBLE);
                    capturevice_relativlayout.setImageResource(R.drawable.audio_play_icon);
                } else if (items[item].equals("Choose from Library")) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto , 21);//one can be replaced with any action code

//                Intent intent = new Intent();
//                intent.setType("audio/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Audio"),24);
//                capture_voice_cross.setVisibility(View.VISIBLE);

                    Intent audioIntent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(audioIntent, "Select Audio"), 24);


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

       if(parent.getId()==R.id.spinner_type) {
           // On selecting a spinner item
           String item = parent.getItemAtPosition(position).toString();
           if(((TextView)parent.getChildAt(0))!=null)
               ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);

           spinnerItem = item;


           if(position!=0) {
              getsubspinnerdatafromdb(TehsilId.get(position - 1),NAPA);
               spinnerItemId=TehsilId.get(position - 1);

           }
           // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
       }else if(parent.getId()==R.id.sub_spinner_type){

           String item = parent.getItemAtPosition(position).toString();
           if(((TextView)parent.getChildAt(0))!=null)
           ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);

           subspinnerItem = item;

           if(position!=0) {
               getpolingdatafromdb(consId.get(position - 1));
               subspinnerItemId=consId.get(position - 1);

           }

       }else if(parent.getId()==R.id.pol_spinner){

           String item = parent.getItemAtPosition(position).toString();
           if(((TextView)parent.getChildAt(0))!=null)
               ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);

//           polspinnerItem = item;
           if(position!=0) {
               polingstationId = polId.get(position - 1);
               polingstationName=polname.get(position-1);
           }
       }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
                    Toast.makeText(AlecSubmitActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){

        File outputFolder= new File(Environment.getExternalStorageDirectory(), "reportFirst/images");//"+"photo_" + currentDate + ".jpg");
        outputFolder.mkdirs();
        File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/images/"+"/photo_" + currentDate + ".jpg");
        if(!outputFile.exists()){
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageRealPath=outputFile.getAbsolutePath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
//        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        File imageFile = new File( filename);

        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
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

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AlecSubmitActivity.this);
        dialog.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("com.example.reportfirst"));
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }
                });
        dialog.show();
    }

    //for getting stored file URI and Path
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20 && resultCode == RESULT_OK){

            try
            {
//               float angle= getExifAngle(CategoryActivity.this,getUriFromPath(CategoryActivity.this,imageRealPath));
//                Bitmap bitmap = BitmapFactory.decodeFile(imageRealPath);
//
//                Bitmap rotatedimg=rotate(bitmap,(int)angle);
//                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(rotatedimg,
//                        170, 170);
//                captureLayout.setImageBitmap(ThumbImage);
//                capture_cross.setVisibility(View.VISIBLE);

                if(imageRealPath!=null && !imageRealPath.equals("")) {


                    ExifInterface ei = new ExifInterface(imageRealPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inSampleSize=8;

                    Bitmap rotatedBitmap = null;
                    Bitmap bitmap = BitmapFactory.decodeFile(imageRealPath,options);
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotate(bitmap, 90);
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
                    captureLayout.setImageBitmap(ThumbImage);
                    capture_cross.setVisibility(View.VISIBLE);
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }

            //  Bitmap bitmap = (Bitmap)data.getExtras().get("data");

//            String partFilename = currentDateFormat();
//            storeCameraPhotoInSDCard(bitmap, partFilename);

            // display the image from SD Card to ImageView Control
            //   String storeFilename = "photo_" + partFilename + ".jpg";
//            Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
//            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            if(imageRealPath!=null && !imageRealPath.equals("")) {
//              Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageRealPath),
//            170, 170);
//                 captureLayout.setImageBitmap(ThumbImage);
//                    capture_cross.setVisibility(View.VISIBLE);
            }
        }else if (requestCode == 21 && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            imageRealPath= getRealPathFromURI(selectedImage);
//            Bitmap mBitmap =getImageFileFromSDCard(imageRealPath);
//            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, 100, 100, true);
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageRealPath),
                    170, 170);
            captureLayout.setImageBitmap(ThumbImage);
            capture_cross.setVisibility(View.VISIBLE);
            //onCaptureImageResult(data);

        }
        else if (requestCode == 22 && resultCode == RESULT_OK){
//            video file save directly on provider URI
//            Uri selectedImage = data.getData();
//            videoRealPath= getRealPathVidFromURI(selectedImage);

            if(videoRealPath!=null && !videoRealPath.equals("")) {
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoRealPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                capturevideo_relativlayout.setImageBitmap(bMap);
                capture_video_cross.setVisibility(View.VISIBLE);
                File outputFolder = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress");//"+"photo_" + currentDate + ".jpg");
                outputFolder.mkdirs();
                File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress/vid" + currentDateFormat() + ".mp4");//"+"photo_" + currentDate + ".jpg");
                try {
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
//                       outputFolder.mkdir();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                CompressVideo(videoRealPath, outputFile.getAbsolutePath().toString());
            }

        }
        else if (requestCode == 23 && resultCode == RESULT_OK) {
            try{
                Uri selectedImage =  data.getData();

                videoRealPath= getRealPathFromURI_API19(AlecSubmitActivity.this,selectedImage);
            }catch (Exception e){
                e.printStackTrace();
            }


            if(videoRealPath!=null && !videoRealPath.equals("")) {
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoRealPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                capturevideo_relativlayout.setImageBitmap(bMap);
                capture_video_cross.setVisibility(View.VISIBLE);

                File outputFolder = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress");//"+"photo_" + currentDate + ".jpg");
                outputFolder.mkdirs();
                File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress/vid" + currentDateFormat() + ".mp4");//"+"photo_" + currentDate + ".jpg");
                try {
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
//                       outputFolder.mkdir();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                CompressVideo(videoRealPath, outputFile.getAbsolutePath().toString());
            }


        } else if (requestCode == 24 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            audioRealPath= getAudioPath(selectedImage);
            capture_voice_cross.setVisibility(View.VISIBLE);
            capturevice_relativlayout.setImageResource(R.drawable.audio_play_icon);
            convertaudiotoMp3(audioRealPath);


        }

    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    public Uri getUriFromPath(Context context, String destination) {
        File file =  new File(destination);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    @Nullable
    public ExifInterface getExifInterface(Context context, Uri uri) {
        try {
            String path = uri.toString();
            if (path.startsWith("file://")) {
                return new ExifInterface(path);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (path.startsWith("content://")) {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    return new ExifInterface(inputStream);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public float getExifAngle(Context context, Uri uri) {
        try {
            ExifInterface exifInterface = getExifInterface(context, uri);
            if(exifInterface == null) {
                return -1f;
            }

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90f;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180f;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270f;
                case ExifInterface.ORIENTATION_NORMAL:
                    return 0f;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    return -1f;
                default:
                    return -1f;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1f;
        }
    }
    private void convertaudiotoMp3(String audioRealPath) {
//        File outputFile = new File(Environment.getExternalStorageDirectory(), "my_audio.flac");
//        File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/sound/"+"sound_" + currentDateFormat() + ".mp3");

        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                // So fast? Love it!
//                convertedFile.
//                /storage/emulated/0/reportFirst/sound/sound_20180403_19_02_05.3gp
                CategoryActivity.audioRealPath= convertedFile.getAbsolutePath();



            }
            @Override
            public void onFailure(Exception error) {
                // Oops! Something went wrong
            }
        };
        AndroidAudioConverter.with(this)
                // Your current audio file
                .setFile(new File(audioRealPath))

                // Your desired audio format
                .setFormat(AudioFormat.MP3)

                // An callback to know when conversion is finished
                .setCallback(callback)

                // Start conversion
                .convert();
    }

    private void CompressVideo(String inputVidPath, final String outputVidPath) {
        if(videoRealPath!=null && !videoRealPath.equals("")) {


//            final ProgressDialog  dialog = new ProgressDialog(getApplicationContext());
//            dialog.setCanceledOnTouchOutside(false);
            VideoCompress.compressVideoLow(inputVidPath, outputVidPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    //Start Compress
                    dialog.setMessage("Compressing Video, please wait...");
                    dialog.show();

//                    Toast.makeText(getApplicationContext(), "start copression", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                    //Finish successfully
//                    Toast.makeText(getApplicationContext(), " compression Finish successfully", Toast.LENGTH_SHORT).show();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    videoRealPath=outputVidPath;
                    if(videoRealPath!=null && !videoRealPath.equals("")) {
                        File outputFolder = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress");//"+"photo_" + currentDate + ".jpg");
                        outputFolder.mkdirs();
                        File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/videos/compress/vid" + currentDateFormat() + ".mp4");//"+"photo_" + currentDate + ".jpg");
                        try {
                            if (!outputFile.exists()) {
                                outputFile.createNewFile();
//                       outputFolder.mkdir();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        dualCompression(videoRealPath, outputFile.getAbsolutePath().toString());
                    }

                }

                @Override
                public void onFail() {
                    //Failed
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), " compression Failed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProgress(float percent) {
                    //Progress
//                    dialog.setMessage("Compressing Video, please wait... "+String.format("%.0f", percent) +"%");


                    dialog.setMessage("Compressing Video, please wait..."+String.format("%.0f", percent) +"%");

                }
            });
        }

    }

    private void dualCompression(String inputVidPath, final String outputVidPath) {
        if(inputVidPath!=null && !inputVidPath.equals("")) {


            VideoCompress.compressVideoLow(inputVidPath, outputVidPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    //Start Compress
                    dialog.setMessage("Applying Dual Compressation , please wait...");
                    dialog.show();

//                    Toast.makeText(getApplicationContext(), "start compression", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess() {
                    //Finish successfully
                    Toast.makeText(getApplicationContext(), " compression Finish successfully", Toast.LENGTH_SHORT).show();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    videoRealPath=outputVidPath;
                }

                @Override
                public void onFail() {
                    //Failed
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), " compression Failed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProgress(float percent) {
                    //Progress
                    dialog.setMessage("Applying Dual Compressation , please wait..."+String.format("%.0f", percent) +"%");

                }
            });
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ivImage.setImageBitmap(thumbnail);
    }
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    public String getOriginalImagePath() {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private String getRealPathVidFromURI(Uri uri) {
        String[] data = { MediaStore.Video.Media.DATA };

        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
/*
        Cursor cursor = CategoryActivity.this.getContentResolver().query(uri, null, null, null, null); cursor.moveToFirst(); int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA); return cursor.getString(idx);
*/
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        if (null != mCurrentLocation) {
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lng = String.valueOf(mCurrentLocation.getLongitude());
            //String speed = String.valueOf(mCurrentLocation.getSpeed());
            Log.e("Latitude :", lat);
            Log.e("Longitude :", lng);
//            Toast.makeText(getApplicationContext(),""+lat,Toast.LENGTH_LONG).show();
            // sendBroadcastMessage(location);
            //currentSpeed = mCurrentLocation.getSpeed() * 3.6f;
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(AlecSubmitActivity.this);
        /*int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);*/
        if (ConnectionResult.SUCCESS == code) {
            return true;
        } else {
            //GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.e(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
     /*   Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 500, pendingIntent );*/
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getApplicationContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        Log.e(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution() && getApplicationContext() instanceof Activity) {
            try {
                Activity activity = (Activity) getApplicationContext();
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

}

