package com.appinsnap.ewatchIBfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.misc.Utils;
import com.vincent.videocompressor.VideoCompress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btn_save;
    ImageView iv_back;
    TextView categorySelect;
    private final int CAMERA_REQUEST = 420;
    ImageView captureLayout, capturevideo_relativlayout, capturevice_relativlayout;
    private String imageRealPath = "";
    private File sdImageMainDirectory;
    private Uri outputFileUri;
    private Uri file;
    EditText et_subject, et_numbers, et_brief_facts;
    private String spinnerItem;
    public static String audioRealPath;
    private String videoRealPath;
    private ImageView capture_cross,capture_video_cross,capture_voice_cross;
     ProgressDialog  dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setupActionBar();
        categorySelect = (TextView) findViewById(R.id.txt_category);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_numbers = (EditText) findViewById(R.id.et_numbers);
        et_brief_facts = (EditText) findViewById(R.id.et_brief_facts);
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner_type);
        audioRealPath=new String();
        dialog = new ProgressDialog(CategoryActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        captureLayout = (ImageView) findViewById(R.id.capture_relativelayout);
        capturevideo_relativlayout = (ImageView) findViewById(R.id.capturevideo_relativlayout);
        capturevice_relativlayout = (ImageView) findViewById(R.id.capturevice_relativlayout);

        capture_cross = (ImageView) findViewById(R.id.capture_cross);
        capture_video_cross = (ImageView) findViewById(R.id.capture_video_cross);
        capture_voice_cross = (ImageView) findViewById(R.id.capture_voice_cross);

        btn_save = (Button) findViewById(R.id.btn_save);
        iv_back = (ImageView) findViewById(R.id.imgview_backbtn);
        Bundle bundle = getIntent().getExtras();
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


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

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

        // Load ffmpeg lib for audio converter

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
                if(spinnerItem==null || spinnerItem.equals("")){
                    Toast.makeText(getApplicationContext(),"Please select type",Toast.LENGTH_LONG).show();
                return;
                }
                Intent intent = new Intent(CategoryActivity.this, CategoryDataActivity.class);
                intent.putExtra("imageRealPath", imageRealPath);
                intent.putExtra("videoRealPath", videoRealPath);
                intent.putExtra("audioRealPath", audioRealPath);
                intent.putExtra("subject", et_subject.getText().toString());
                intent.putExtra("numbers", et_numbers.getText().toString());
                intent.putExtra("brieffacts", et_brief_facts.getText().toString());
                intent.putExtra("type", spinnerItem);
                intent.putExtra("category", categorySelect.getText().toString());
                intent.putExtra("status", "draft");
                intent.putExtra("comefromlist", false);

                // Toast.makeText(getApplicationContext(),imageRealPath,Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        captureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                    } else if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //                           explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                        ActivityCompat.requestPermissions(CategoryActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                    } else {

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
                    }else*/ if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //                           explain("You need to give mandatory permissions to continue. Do you want to go to app settings?");
                        ActivityCompat.requestPermissions(CategoryActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                2);
                    }else if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CategoryActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                4);
                    } else if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CategoryActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                5);
                    } else if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CategoryActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                3);
                    } else {

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
                    if (ActivityCompat.checkSelfPermission(CategoryActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                builder.setTitle("Do You Want to Delete Audio");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Delete")) {
                            imageRealPath="";
                            capture_cross.setVisibility(View.INVISIBLE);
                            captureLayout.setImageResource(R.drawable.image_icon);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                builder.setTitle("Do You Want to Delete Video");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Delete")) {
                            videoRealPath="";
                            capture_video_cross.setVisibility(View.INVISIBLE);
                            capturevideo_relativlayout.setImageResource(R.drawable.video_icon);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
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

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(MainActivity.this);
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
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 21);//one can be replaced with any action code


//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);//
//                    startActivityForResult(Intent.createChooser(intent, "Select File"),20);

//                    userChoosenTask="Choose from Library";
//                    if(result)
//                        galleryIntent();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setTitle("Rec Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(MainActivity.this);
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
    AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
    builder.setTitle("Rec Audio!");
    builder.setItems(items, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(MainActivity.this);
            if (items[item].equals("Rec Audio")) {
                Intent recordAudio=new Intent(CategoryActivity.this,RecorderActivity.class);
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
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
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
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
            spinnerItem=item;
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(CategoryActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(CategoryActivity.this);
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

                videoRealPath= getRealPathFromURI_API19(CategoryActivity.this,selectedImage);
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
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
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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

              /*  OutputStreamWriter out;
                try {
                    ContextWrapper cw = new ContextWrapper(CategoryActivity.this);


                        File outputFile = new File(Environment.getExternalStorageDirectory(), "reportFirst/sound/"+"sound_" + currentDateFormat() + ".mp3");
                        CategoryActivity.audioRealPath=outputFile.getAbsolutePath();
//                    File path = cw.getDir("myfolder", Context.MODE_PRIVATE);
                    *//*if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        outputFile.mkdir();
                    }*//*
//                    File mypath=new File(path,"myfile.txt");
                    if (!outputFile.exists()) {
                        out = new OutputStreamWriter(openFileOutput( outputFile.getAbsolutePath() , MODE_PRIVATE));
                        out.write("test");
                        out.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
*/

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
//                    dialog.setMessage("Applying Dual Compressation , please wait...");
//                    dialog.show();

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


    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(CameraCapture.PHOTO_TAKEN)) {
            _taken = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(CameraCapture.PHOTO_TAKEN, _taken);
    }
*/
}
