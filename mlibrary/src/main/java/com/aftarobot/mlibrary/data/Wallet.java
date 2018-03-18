package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Wallet implements Data, Serializable, Comparable<Wallet> {
    private String walletID, accountID, seed,
            sourceAccountID, sourceSeed = "SDZGFXWT5PHQZ6U6KHMBFFQ346I34B4LB7N524DHY4A22XHEEKHNKXCJ",
            fcmToken, name, email, uid, sequenceNumber, stringDate;
    private long date;
    private boolean debug;
    private HashMap<String, Photo> photos;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    public HashMap<String, Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, Photo> photos) {
        this.photos = photos;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getSourceAccountID() {
        return sourceAccountID;
    }

    public void setSourceAccountID(String sourceAccountID) {
        this.sourceAccountID = sourceAccountID;
    }

    public String getSourceSeed() {
        return sourceSeed;
    }

    public void setSourceSeed(String sourceSeed) {
        this.sourceSeed = sourceSeed;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWalletID() {
        return walletID;
    }

    public void setWalletID(String walletID) {
        this.walletID = walletID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        this.stringDate = sdf.format(new Date(date));
    }

    @Override
    public int compareTo(@NonNull Wallet o) {
        return this.name.compareTo(o.name);
    }
}
