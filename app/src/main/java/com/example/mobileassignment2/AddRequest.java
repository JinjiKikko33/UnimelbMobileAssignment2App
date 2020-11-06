package com.example.mobileassignment2;

public class AddRequest {
    private String requestid;
    private String uid;
    private String fid;
    private String fname;
    private String imgurl;


    public AddRequest(String requestid, String uid, String fid, String fname, String imgurl) {
        this.requestid = requestid;
        this.uid = uid;
        this.fid = fid;
        this.fname = fname;
        this.imgurl=imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }


}
