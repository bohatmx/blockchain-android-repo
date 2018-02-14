package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Bank implements Data, Serializable, Comparable<Bank> {
    String bankId, name, email, phone;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int compareTo(@NonNull Bank o) {
        return this.name.compareTo(o.name);
    }
}
