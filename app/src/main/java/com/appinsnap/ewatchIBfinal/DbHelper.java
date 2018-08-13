package com.appinsnap.ewatchIBfinal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "reportfirst.db";
    public static final String CONTACTS_TABLE_NAME = "categorydata";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_category = "category";
    public static final String CONTACTS_COLUMN_type = "type";
    public static final String CONTACTS_COLUMN_subject = "subject";
    public static final String CONTACTS_COLUMN_number = "number";
    public static final String CONTACTS_COLUMN_brief = "brief";
    public static final String CONTACTS_COLUMN_image = "image";
    public static final String CONTACTS_COLUMN_video = "video";
    public static final String CONTACTS_COLUMN_audio = "audio";
    private HashMap hp;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table categorydata " +
                        "(id integer primary key, " +
                        "category text," +
                        "type text," +
                        "subject text," +
                        "number text," +
                        "brief text," +
                        "image text," +
                        "video text," +
                        "audio text," +
                        "status text)"

        );

        db.execSQL(
                "create table electiondata " +
                        "(id integer primary key, " +
                        "category text," +
                        "napa text," +
                        "type text," +
                        "subtype text," +
                        "polingstationid text," +
                        "detail text," +
                        "image text," +
                        "video text," +
                        "acdetail text," +
                        "acimage text," +
                        "acvideo text," +
                        "status text," +
                        "creatorid text," +
                        "creationdatetime text," +
                        "issueid text," +
                        "issuestatus text," +
                        "categoryid text," +
                        "PollingStationName text," +
                        "Count text," +
                        "TehsilId text," +
                        "ConstituencyId text" +
                        ")"



        );
        db.execSQL(
                "create table districtdata " +
                        "(id integer primary key, " +
                        "discname text," +
                        "discid text," +
                        "provid text)"

        );

        db.execSQL(
                "create table tehsildata " +
                        "(id integer primary key, " +
                        "tehname text," +
                        "tehid text," +
                        "distid text)"

        );

        db.execSQL(
                "create table consiquincesdata " +
                        "(id integer primary key, " +
                        "consid text," +
                        "consname text," +
                        "tehid text," +
                        "constypeid text)"

        );

        db.execSQL(
                "create table polingstationdata " +
                        "(id integer primary key, " +
                        "Pollingstationname text," +
                        "Pollingstationid text," +
                        "consid text)"

        );

        db.execSQL(
                "create table message " +
                        "(id integer primary key, " +
                        "currentuserid text," +
                        "userid text," +
                        "messagetype text," +
                        "messagetitle text," +
                        "meassagedescription text," +
                        "isread text)"

        );
        db.execSQL(
                "create table user " +
                        "(id integer primary key, " +
                        "userid text," + "username text," +
                        "password text)"

        );
        Log.d("DB", "onCreate: table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS categorydata");
        db.execSQL("DROP TABLE IF EXISTS message");
        onCreate(db);
    }

    public boolean insertElection(String category, String napa, String type, String subtype, String polingstationid, String detail, String image, String video, String acdetail, String acimage, String acvideo, String status,
                                  String creatorid, String creationdatetime, String issueid, String issuestatus, String categoryid, String PollingStationName, String Count,String TehsilId, String ConstituencyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        contentValues.put("napa", napa);
        contentValues.put("type", type);
        contentValues.put("subtype", subtype);
        contentValues.put("polingstationid", polingstationid);
        contentValues.put("detail", detail);
        contentValues.put("image", image);
        contentValues.put("video", video);
        contentValues.put("acdetail", acdetail);
        contentValues.put("acimage", acimage);
        contentValues.put("acvideo", acvideo);
        contentValues.put("status", status);
        contentValues.put("creatorid", creatorid);
        contentValues.put("creationdatetime", creationdatetime);
        contentValues.put("issueid", issueid);
        contentValues.put("issuestatus", issuestatus);
        contentValues.put("categoryid", categoryid);
        contentValues.put("PollingStationName", PollingStationName);
        contentValues.put("Count", Count);
        contentValues.put("TehsilId", TehsilId);
        contentValues.put("ConstituencyId", ConstituencyId);
        db.insert("electiondata", null, contentValues);
        Log.d("DB", ": election inserted");


        return true;
    }

    public Integer deleteElection(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("electiondata",
                "id = " + id,
                null);
    }

    public boolean insertDistrict(String discname, String discid, String provid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("discname", discname);
        contentValues.put("disid", discid);
        contentValues.put("provid", provid);
        db.insert("districtdata", null, contentValues);
        Log.d("DB", ": election inserted");

        return true;
    }

    public boolean insertTehsil(String tehname, String tehid, String distid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tehname", tehname);
        contentValues.put("tehid", tehid);
        contentValues.put("distid", distid);
        db.insert("tehsildata", null, contentValues);
        Log.d("DB", ": election inserted Tehsil");

        return true;
    }

    public boolean insertConsiquences(String consname, String consid, String disid, String contypeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("consname", consname);
        contentValues.put("consid", consid);
        contentValues.put("tehid", disid);
        contentValues.put("constypeid", contypeid);
        db.insert("consiquincesdata", null, contentValues);
        Log.d("DB", ": election inserted  Consiquences");

        return true;
    }

    public boolean insertpolingstationdata(String Pollingstationname, String Pollingstationid, String consid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Pollingstationname", Pollingstationname);
        contentValues.put("Pollingstationid", Pollingstationid);
        contentValues.put("consid", consid);
        db.insert("polingstationdata", null, contentValues);
        Log.d("DB", ": election inserted polingstationdata");

        return true;
    }


    public boolean insertCategory(String category, String type, String subject, String number, String brief, String image, String video, String audio, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        contentValues.put("type", type);
        contentValues.put("subject", subject);
        contentValues.put("number", number);
        contentValues.put("brief", brief);
        contentValues.put("image", image);
        contentValues.put("video", video);
        contentValues.put("audio", audio);
        contentValues.put("status", status);
        db.insert("categorydata", null, contentValues);
        Log.d("DB", ": category inserted");

        return true;
    }

    public Integer deleteCategory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("categorydata",
                "id = " + id,
//                "type =  '"+ type +"' AND "+
//                "subject =  '"+ subject +"' AND "+
//                "number =  '"+ number +"' AND "+
//                "brief =  '"+ brief +"' AND "+
//                "image =  '"+ image +"' AND "+
//                "video =  '"+ video +"' AND "+
//                "audio =  '"+ audio +"' AND "+
//                "status =  '"+ status +"'",
                null);

//                "category =? AND "+
//                "type =? AND "+
//                "subject =? AND "+
//                "number =? AND "+
//                "brief =? AND "+
//                "image =? AND "+
//                "video =? AND "+
//                "audio =? AND "+
//                "status =? AND ",
//                new String[] { category,type,subject,number,brief,image,video,audio,status });

//        .delete(DATABASE_TABLE,
//                KEY_DATE + "=? AND " + KEY_GRADE + "=? AND " +
//                        KEY_STYLE + "=? AND " + KEY_PUMPLEVEL + "=?",
//                new String[] {date, grade, style, pumpLevel});
    }

    public boolean insertMessage(String currentuserid, String userid, String messagetype, String messagetitle, String meassagedescription, String isread) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentuserid", currentuserid);
        contentValues.put("userid", userid);
        contentValues.put("messagetype", messagetype);
        contentValues.put("messagetitle", messagetitle);
        contentValues.put("meassagedescription", meassagedescription);
        contentValues.put("isread", isread);
        db.insert("message", null, contentValues);
        return true;
    }

    public boolean insertUser(String userid, String username, String password) {

        String struserid = getUserId(username, password);
        if (struserid == null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("userid", userid);
            contentValues.put("username", username);
            contentValues.put("password", password);
            db.insert("user", null, contentValues);
        }
        return true;
    }

    public boolean updateMessage(String currentuserid, String userid, String messagetype, String messagetitle, String meassagedescription, String isread, Integer recordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentuserid", currentuserid);
        contentValues.put("userid", userid);
        contentValues.put("messagetype", messagetype);
        contentValues.put("messagetitle", messagetitle);
        contentValues.put("meassagedescription", meassagedescription);
        contentValues.put("isread", isread);
        db.update("message", contentValues, "id = ? ", new String[]{recordId.toString()});
        return true;
    }

    public String getUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from user where username = ? AND password = ?", new String[]{username, password}, null);
        res.moveToFirst();
        String struserid = null;
        while (res.isAfterLast() == false) {
            CategoryDataModel CDM = new CategoryDataModel();
            struserid = res.getString(res.getColumnIndex("userid"));
            res.moveToNext();
        }
        res.close();
        return struserid;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categorydata where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(Integer id, String name, String phone, String email, String street, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("message", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("categorydata",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<CategoryDataModel> getAllCategory() {
        ArrayList<CategoryDataModel> array_list = new ArrayList<CategoryDataModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categorydata", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_category)));
            CategoryDataModel CDM = new CategoryDataModel();
            CDM.setCategory(res.getString(res.getColumnIndex("category")));
            CDM.setType(res.getString(res.getColumnIndex("type")));
            CDM.setSubject(res.getString(res.getColumnIndex("subject")));
            CDM.setNumber(res.getString(res.getColumnIndex("number")));
            CDM.setBrief(res.getString(res.getColumnIndex("brief")));
            CDM.setImage(res.getString(res.getColumnIndex("image")));
            CDM.setVideo(res.getString(res.getColumnIndex("video")));
            CDM.setAudio(res.getString(res.getColumnIndex("audio")));
            CDM.setStatus(res.getString(res.getColumnIndex("status")));

            array_list.add(CDM);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<CategoryDataModel> getAllPending() {
        ArrayList<CategoryDataModel> array_list = new ArrayList<CategoryDataModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categorydata where status = ? ", new String[]{"draft"}, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_category)));
            CategoryDataModel CDM = new CategoryDataModel();
            CDM.setCategory(res.getString(res.getColumnIndex("category")));
            CDM.setType(res.getString(res.getColumnIndex("type")));
            CDM.setSubject(res.getString(res.getColumnIndex("subject")));
            CDM.setNumber(res.getString(res.getColumnIndex("number")));
            CDM.setBrief(res.getString(res.getColumnIndex("brief")));
            CDM.setImage(res.getString(res.getColumnIndex("image")));
            CDM.setVideo(res.getString(res.getColumnIndex("video")));
            CDM.setAudio(res.getString(res.getColumnIndex("audio")));
            CDM.setStatus(res.getString(res.getColumnIndex("status")));
            CDM.setId(res.getInt(res.getColumnIndex("id")));

            array_list.add(CDM);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<AlectionModel> getAllPendingElection() {
        ArrayList<AlectionModel> array_list = new ArrayList<AlectionModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from electiondata where status = ? ", new String[]{"draft"}, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_category)));
            AlectionModel CDM = new AlectionModel();
            CDM.setCategory(res.getString(res.getColumnIndex("category")));
            CDM.setNapa(res.getString(res.getColumnIndex("napa")));
            CDM.setType(res.getString(res.getColumnIndex("type")));
            CDM.setSubtype(res.getString(res.getColumnIndex("subtype")));
            CDM.setPolingstationid(res.getString(res.getColumnIndex("polingstationid")));
            CDM.setDetail(res.getString(res.getColumnIndex("detail")));
            CDM.setImage(res.getString(res.getColumnIndex("image")));
            CDM.setVideo(res.getString(res.getColumnIndex("video")));
            CDM.setAcdetail(res.getString(res.getColumnIndex("acdetail")));
            CDM.setAcimage(res.getString(res.getColumnIndex("acimage")));
            CDM.setAcvideo(res.getString(res.getColumnIndex("acvideo")));
            CDM.setStatus(res.getString(res.getColumnIndex("status")));
            CDM.setId(res.getInt(res.getColumnIndex("id")));
            CDM.setCreatorid(res.getString(res.getColumnIndex("creatorid")));
            CDM.setCreationdatetime(res.getString(res.getColumnIndex("creationdatetime")));
            CDM.setIssueId(res.getString(res.getColumnIndex("issueid")));
            CDM.setIssuestatus(res.getString(res.getColumnIndex("issuestatus")));
            CDM.setCategoryid(res.getString(res.getColumnIndex("categoryid")));
            CDM.setPollingStationName(res.getString(res.getColumnIndex("PollingStationName")));
            CDM.setCount(res.getString(res.getColumnIndex("Count")));
            CDM.setCount(res.getString(res.getColumnIndex("TehsilId")));
            CDM.setCount(res.getString(res.getColumnIndex("ConstituencyId")));


            array_list.add(CDM);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<AlectionModel> getAllSubmittedElection() {
        ArrayList<AlectionModel> array_list = new ArrayList<AlectionModel>();
        int i = 0;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from electiondata where status = ? ", new String[]{"submit"}, null);
        res.moveToLast();

        while (res.isBeforeFirst() == false && i < 10) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_category)));
            AlectionModel CDM = new AlectionModel();
            CDM.setCategory(res.getString(res.getColumnIndex("category")));
            CDM.setNapa(res.getString(res.getColumnIndex("napa")));
            CDM.setType(res.getString(res.getColumnIndex("type")));
            CDM.setSubtype(res.getString(res.getColumnIndex("subtype")));
            CDM.setPolingstationid(res.getString(res.getColumnIndex("polingstationid")));
            CDM.setPollingStationName(res.getString(res.getColumnIndex("PollingStationName")));
            CDM.setDetail(res.getString(res.getColumnIndex("detail")));
            CDM.setImage(res.getString(res.getColumnIndex("image")));
            CDM.setVideo(res.getString(res.getColumnIndex("video")));
            CDM.setAcdetail(res.getString(res.getColumnIndex("acdetail")));
            CDM.setAcimage(res.getString(res.getColumnIndex("acimage")));
            CDM.setAcvideo(res.getString(res.getColumnIndex("acvideo")));
            CDM.setStatus(res.getString(res.getColumnIndex("status")));
            CDM.setId(res.getInt(res.getColumnIndex("id")));
            CDM.setCreatorid(res.getString(res.getColumnIndex("creatorid")));
            CDM.setCreationdatetime(res.getString(res.getColumnIndex("creationdatetime")));
            CDM.setIssueId(res.getString(res.getColumnIndex("issueid")));
            CDM.setIssuestatus(res.getString(res.getColumnIndex("issuestatus")));
            CDM.setCategoryid(res.getString(res.getColumnIndex("categoryid")));
            CDM.setPollingStationName(res.getString(res.getColumnIndex("PollingStationName")));
            CDM.setCount(res.getString(res.getColumnIndex("Count")));
            CDM.setCount(res.getString(res.getColumnIndex("TehsilId")));
            CDM.setCount(res.getString(res.getColumnIndex("ConstituencyId")));

            array_list.add(CDM);
            i++;
            res.moveToPrevious();
        }
        return array_list;
    }

    public ArrayList<CategoryDataModel> getAllSubmitted() {
        ArrayList<CategoryDataModel> array_list = new ArrayList<CategoryDataModel>();
        int i = 0;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categorydata where status = ? ", new String[]{"submit"}, null);
        res.moveToLast();

        while (res.isBeforeFirst() == false && i < 10) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_category)));
            CategoryDataModel CDM = new CategoryDataModel();
            CDM.setCategory(res.getString(res.getColumnIndex("category")));
            CDM.setType(res.getString(res.getColumnIndex("type")));
            CDM.setSubject(res.getString(res.getColumnIndex("subject")));
            CDM.setNumber(res.getString(res.getColumnIndex("number")));
            CDM.setBrief(res.getString(res.getColumnIndex("brief")));
            CDM.setImage(res.getString(res.getColumnIndex("image")));
            CDM.setVideo(res.getString(res.getColumnIndex("video")));
            CDM.setAudio(res.getString(res.getColumnIndex("audio")));
            CDM.setStatus(res.getString(res.getColumnIndex("status")));
            CDM.setId(res.getInt(res.getColumnIndex("id")));

            array_list.add(CDM);
            i++;
            res.moveToPrevious();
        }
        return array_list;
    }

    public ArrayList<MessagesDataModel> getAllMessages() {
        ArrayList<MessagesDataModel> array_list = new ArrayList<MessagesDataModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from message", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            MessagesDataModel MDM = new MessagesDataModel(res.getString(res.getColumnIndex("messagetitle")),
                    res.getString(res.getColumnIndex("meassagedescription")),
                    res.getString(res.getColumnIndex("messagetype")),
                    res.getString(res.getColumnIndex("userid")),
                    res.getInt(res.getColumnIndex("id")),
                    Boolean.getBoolean(res.getString(res.getColumnIndex("isread")))
            );
            array_list.add(MDM);
            res.moveToNext();
        }
        return array_list;
    }

    public HashMap getAllDistrict() {
        HashMap array_list = new HashMap();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from districtdata", null);
        res.moveToFirst();
        ArrayList<String> discnames = new ArrayList<>();
        ArrayList<String> discids = new ArrayList<>();


        while (res.isAfterLast() == false) {
//            array_list.put(res.getString(res.getColumnIndex("discname")),res.getString(res.getColumnIndex("discid")));
            discnames.add(res.getString(res.getColumnIndex("discname")));
            discids.add(res.getString(res.getColumnIndex("discid")));
            res.moveToNext();
        }
        array_list.put("descnames", discnames);
        array_list.put("descids", discids);

        return array_list;
    }

    public HashMap gettehsiles() {
        HashMap array_list = new HashMap();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res =  db.query(true, table, projection,null, null, null, null, null, null);
        Cursor res = db.rawQuery("select * from tehsildata", null);
        //res.moveToFirst();
        int aa = res.getCount();
        ArrayList<String> tehnames = new ArrayList<>();
        ArrayList<String> tehids = new ArrayList<>();
        ArrayList<String> disids = new ArrayList<>();
        if (res.moveToFirst()) {
            do {
                tehnames.add(res.getString(res.getColumnIndex("tehname")));
                tehids.add(res.getString(res.getColumnIndex("tehid")));
                disids.add(res.getString(res.getColumnIndex("distid")));
                System.out.println("---------------------------" + res.getString(res.getColumnIndex("tehname")));
                System.out.println("---------------------------" + res.getString(res.getColumnIndex("tehid")));
                System.out.println("---------------------------" + res.getString(res.getColumnIndex("distid")));

            } while (res.moveToNext());
        }


//        while(res != null && res.moveToNext()){
////            array_list.put(res.getString(res.getColumnIndex("discname")),res.getString(res.getColumnIndex("discid")));
//            tehnames.add(res.getString(res.getColumnIndex("tehname")));
//            tehids.add(res.getString(res.getColumnIndex("tehid")));
//            disids.add(res.getString(res.getColumnIndex("disid")));
//            System.out.println("---------------------------" + res.getString(res.getColumnIndex("tehname")));
//            System.out.println("---------------------------" + res.getString(res.getColumnIndex("tehid")));
//            System.out.println("---------------------------" + res.getString(res.getColumnIndex("disid")));
//            res.moveToNext();
//        }
        array_list.put("tehname", tehnames);
        array_list.put("tehid", tehids);
        array_list.put("distid", disids);

        return array_list;
    }

//    public ArrayList<HashMap<String, String>> gettehsiles(){
//        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();
//        SQLiteDatabase db = getReadableDatabase();
//        String query = "select * from tehsildata";
//        Cursor c = db.rawQuery(query, null);
//        if(c.moveToFirst()) {
//            do {
//                // you are creating map here but not adding this map to list
//                HashMap<String, String> map = new HashMap<String, String>();
//                for(int i=0; i<c.getColumnCount();i++) {
//                    map.put(c.getColumnName(i), c.getString(i));
//                }
//                // so do add it here
//                maplist.add(map);
//            } while (c.moveToNext());
//        }
//        db.close();
//        return maplist;
//    }


    public HashMap getconsiqueces(String tehsilid, String napa) {
        //public HashMap getconsiqueces(String tehsilid) {
        HashMap array_list = new HashMap();

        //hp = new HashMap();
        //String napa="1";
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor res =  db.rawQuery( "select * from consiquincesdata where tehid="+tehid+" AND constypeid="+napa+"", null );
        Cursor res = db.rawQuery("select * from consiquincesdata where tehid='" + tehsilid.toString() + "' AND constypeid='" + napa.toString() + "' ", null);
        res.moveToFirst();
        ArrayList<String> consnames = new ArrayList<>();
        ArrayList<String> consids = new ArrayList<>();
        ArrayList<String> tehids = new ArrayList<>();
        int t = res.getCount();
//        while(res.isAfterLast() == false){
//            array_list.put(res.getString(res.getColumnIndex("discname")),res.getString(res.getColumnIndex("discid")));
        if (res.moveToFirst()) {
            do {
                consnames.add(res.getString(res.getColumnIndex("consname")));
                consids.add(res.getString(res.getColumnIndex("consid")));
                tehids.add(res.getString(res.getColumnIndex("tehid")));
                System.out.println("---------------------------" + res.getString(res.getColumnIndex("consname")));
                System.out.println("---------------------------" + res.getString(res.getColumnIndex("consid")));
                System.out.println("---------------------------" + res.getString(res.getColumnIndex("tehid")));
            }
            while (res.moveToNext());
        }
        array_list.put("consname", consnames);
        array_list.put("consid", consids);
        array_list.put("tehid", tehids);

        return array_list;
    }

    public HashMap getpolingstation(String consid) {
        HashMap array_list = new HashMap();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from polingstationdata where consid=" + consid.toString() + "", null);
        res.moveToFirst();
        int rrr = res.getCount();
        ArrayList<String> pollnames = new ArrayList<>();
        ArrayList<String> pollids = new ArrayList<>();
        ArrayList<String> consids = new ArrayList<>();

        while (res.isAfterLast() == false) {
            pollnames.add(res.getString(res.getColumnIndex("Pollingstationname")));
            pollids.add(res.getString(res.getColumnIndex("Pollingstationid")));
            consids.add(res.getString(res.getColumnIndex("consid")));
            System.out.println("---------------------------" + res.getString(res.getColumnIndex("Pollingstationname")));
            System.out.println("---------------------------" + res.getString(res.getColumnIndex("Pollingstationid")));
            System.out.println("---------------------------" + res.getString(res.getColumnIndex("consid")));
            res.moveToNext();
        }
        array_list.put("polname", pollnames);
        array_list.put("polid", pollids);
        array_list.put("consid", consids);

//        array_list.keySet();

        return array_list;
    }
}