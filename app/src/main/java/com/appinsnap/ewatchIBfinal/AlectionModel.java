package com.appinsnap.ewatchIBfinal;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AlectionModel  implements Serializable{

    private String category;
    private String napa;
    private String type;
    private String subtype;
    private String detail;
    private String image;
    private String video;
    private String acdetail;
    private String acimage;
    private String acvideo;
    private String status;
    private String polingstationid;

    private Integer id;

    private String issueId;
    private String PollingStationName,creatorid,creationdatetime,issuestatus,categoryid;
    private String Count;
    String TehsilId;
    String ConstituencyId;

    public String getTehsilId() {
        return TehsilId;
    }

    public void setTehsilId(String tehsilId) {
        TehsilId = tehsilId;
    }

    public String getConstituencyId() {
        return ConstituencyId;
    }

    public void setConstituencyId(String constituencyId) {
        ConstituencyId = constituencyId;
    }

    public String getPollingStationName() {
        return PollingStationName;
    }

    public void setPollingStationName(String pollingStationName) {
        PollingStationName = pollingStationName;
    }

    public String getIssueId() {
        return issueId;
    }



    public AlectionModel(){}
    public AlectionModel(String category, String napa, String type , String subtype, String detail, String image, String video, String acdetail, String acimage, String acvideo, String status, String polingstationid,
                         String  PollingStationName, String issuestatus, String issueId,String creatorid,String creationdatetime,String categoryid,String Count,String TehsilId,String ConstituencyId){
        this.category=category;
        this.napa= napa;
        this.type= type;
        this.subtype=subtype;
        this.detail= detail;
        this.image= image;
        this.video= video;
        this.acdetail=acdetail;
        this.acimage=acimage;
        this. acvideo= acvideo;
        this.status=status;
        this. polingstationid =polingstationid;
        this. issuestatus =issuestatus;
        this.PollingStationName= PollingStationName;
        this.issueId=issueId;
        this.creatorid=creatorid;
        this.creationdatetime=creationdatetime;
        this.categoryid=categoryid;
        this.Count=Count;
        this.TehsilId=TehsilId;
        this.ConstituencyId=ConstituencyId;

    }


    public String getCategory() {
        return category;
    }




    public String getPolingstationid() {
        return polingstationid;
    }

    public void setPolingstationid(String polingstationid) {
        this.polingstationid = polingstationid;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getNapa() {
        return napa;
    }

    public void setNapa(String napa) {
        this.napa = napa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAcdetail() {
        return acdetail;
    }

    public void setAcdetail(String acdetail) {
        this.acdetail = acdetail;
    }

    public String getAcimage() {
        return acimage;
    }

    public void setAcimage(String acimage) {
        this.acimage = acimage;
    }

    public String getAcvideo() {
        return acvideo;
    }

    public void setAcvideo(String acvideo) {
        this.acvideo = acvideo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getCreationdatetime() {
        return creationdatetime;
    }

    public void setCreationdatetime(String creationdatetime) {
        this.creationdatetime = creationdatetime;
    }

    public String getIssuestatus() {
        return issuestatus;
    }

    public void setIssuestatus(String issuestatus) {
        this.issuestatus = issuestatus;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }


}
