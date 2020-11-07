package com.example.mobileassignment2;

public class Friends {

    private Integer fid;
    private String fusername;
    private String imgurl;

    public Friends(int fid, String fusername, String imgurl) {
        this.fid = fid;
        this.fusername = fusername;
        this.imgurl=imgurl;
    }



    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFusername() {
        return fusername;
    }

    public void setFusername(String fusername) {
        this.fusername = fusername;
    }

}
