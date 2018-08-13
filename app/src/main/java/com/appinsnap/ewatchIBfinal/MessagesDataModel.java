package com.appinsnap.ewatchIBfinal;

public class MessagesDataModel {
    String subject;
    String description;
    String type;
    String userid;
    boolean isread;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    Integer recordId;
    public MessagesDataModel(String subject, String description, String type, String userid,Integer recordId,boolean isread ) {
        this.subject=subject;
        this.description=description;
        this.type=type;
        this.userid=userid;
        this.isread=isread;
        this.recordId=recordId;

    }

    public String getsubject() {
        return subject;
    }

    public String getdescription() {
        return description;
    }

    public String gettype() {
        return type;
    }

    public String getuserid() {
        return userid;
    }

    public boolean getisread() {
        return isread;
    }

}


