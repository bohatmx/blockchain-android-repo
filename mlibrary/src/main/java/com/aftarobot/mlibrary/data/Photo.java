package com.aftarobot.mlibrary.data;

import java.io.Serializable;

public class Photo implements Data, Serializable {
    private String photoID, uid, stringDateTaken, walletID;
    private long dateTaken, width, height, bytes;
    private boolean defaultPhoto = false;
    private String  url, filePath,  caption;
    private int photoType;
    public static final int PROFILE = 1,
            MERCHANDISE = 2, PROOF = 3;

    public String getWalletID() {
        return walletID;
    }

    public void setWalletID(String walletID) {
        this.walletID = walletID;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStringDateTaken() {
        return stringDateTaken;
    }

    public void setStringDateTaken(String stringDateTaken) {
        this.stringDateTaken = stringDateTaken;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public boolean isDefaultPhoto() {
        return defaultPhoto;
    }

    public void setDefaultPhoto(boolean defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getPhotoType() {
        return photoType;
    }

    public void setPhotoType(int photoType) {
        this.photoType = photoType;
    }
}
