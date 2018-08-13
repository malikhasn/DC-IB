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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String text_one = "";
    TextView txt_reportfirst;
    TextView cT,cI,political,religious,securityForeign,threatAlert,txt_misc,txt_crime;
    String categoryText;
    Intent intent;
    boolean isfirstTime=true;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();
//        Spinner spinner = (Spinner) findViewById(R.id.spinner_menu);
        ImageView menu = (ImageView) findViewById(R.id.spinner_menu);

        text_one = "<font color=#bfa264>Report</font> <font color=#FEFDFD>First</font>";
        txt_reportfirst = (TextView) findViewById(R.id.txt_reportFirst);
        txt_reportfirst.setText(Html.fromHtml(text_one));
        cT = (TextView) findViewById(R.id.txt_ct);
        cI = (TextView) findViewById(R.id.txt_ci);
        political = (TextView) findViewById(R.id.txt_political);
        religious = (TextView) findViewById(R.id.txt_religious);
        securityForeign = (TextView) findViewById(R.id.txt_security);
        threatAlert = (TextView) findViewById(R.id.txt_threat_alert);
        txt_crime = (TextView) findViewById(R.id.txt_crime);
        txt_misc = (TextView) findViewById(R.id.txt_misc);
        cT.setOnClickListener(this);
        cI.setOnClickListener(this);
        political.setOnClickListener(this);
        religious.setOnClickListener(this);
        securityForeign.setOnClickListener(this);
        txt_crime.setOnClickListener(this);
        threatAlert.setOnClickListener(this);
        txt_misc.setOnClickListener(this);

        //Navigation Drawer
        new DrawerBuilder().withActivity(this).build();

//if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(Html.fromHtml("ReportFirst").toString()).withIcon(R.drawable.ib_logo_new).withTextColor(getResources().getColor(R.color.white));
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Messages").withIcon(R.drawable.message_icon).withTextColor(getResources().getColor(R.color.white));
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
                        new SecondaryDrawerItem().withName("Logout").withIcon(R.drawable.logout_icon).withTextColor(getResources().getColor(R.color.white))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if(position==2){
                            Intent intent = new Intent(MainActivity.this,MessagesActivity.class);
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==3){
                            Intent intent = new Intent(MainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","submit");
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==4){
                            Intent intent = new Intent(MainActivity.this,SubmitPendingActivity.class);
                            intent.putExtra("status","draft");
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Messages",Toast.LENGTH_SHORT).show();
                        }else if(position==5){
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.remove("UserId"); // will delete key name
                            editor.commit(); // commit changes

                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                }).withDrawerGravity(Gravity.RIGHT)
                 .withSelectedItem(-1)
                 .withSliderBackgroundColor(getResources().getColor(R.color.color_report_drawer))
                 .withActionBarDrawerToggle(true)
                 .build();
        ////////

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open / close the drawer
                drawer.openDrawer();
            }
        });

//        setupActionBar();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        // Spinner click listener
        /*spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("");
        categories.add("Messages");
        categories.add("Logout");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
*/


    }
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        if(position==0 && !isfirstTime){
//            Intent intent = new Intent(MainActivity.this,MessagesActivity.class);
//            startActivity(intent);

        }
//        isfirstTime=false;

        if(position==1){
            Intent intent = new Intent(MainActivity.this,MessagesActivity.class);
            startActivity(intent);

        }
        if(position==2){

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("UserId"); // will delete key name
            editor.commit(); // commit changes

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();


        }

//        spinnerItem=item;
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }*/

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
        Bundle bundle = new Bundle();
        int id = v.getId();
        switch (id){
            case R.id.txt_ct:
                categoryText = cT.getText().toString();
                bundle.putString("CT",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.txt_ci:
                categoryText = cI.getText().toString();
                bundle.putString("CI",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.txt_political:
                categoryText = political.getText().toString();
                bundle.putString("political",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.txt_religious:
                categoryText = religious.getText().toString();
                bundle.putString("religious",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.txt_security:
                categoryText = securityForeign.getText().toString();
                bundle.putString("securityForeign",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.txt_crime:
                categoryText = txt_crime.getText().toString();
                bundle.putString("txt_crime",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
                case R.id.txt_threat_alert:
                categoryText = threatAlert.getText().toString();
                bundle.putString("threatAlert",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.txt_misc:
                categoryText = txt_misc.getText().toString();
                bundle.putString("misc",categoryText);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

}
