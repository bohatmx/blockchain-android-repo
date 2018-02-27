package com.aftarobot.mlibrary.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistorianRecord implements Data, Serializable, Comparable<HistorianRecord>{

    private String $class,transactionInvoked, identityUsed;
    private String transactionId, transactionType, participantInvoking;
    private List<Object> eventsEmitted;
    private String transactionTimestamp;
    public String getClassz() { return this.$class; }

    public List<Object> getEventsEmitted() {
        return eventsEmitted;
    }

    public void setEventsEmitted(List<Object> eventsEmitted) {
        this.eventsEmitted = eventsEmitted;
    }

    public String getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(String transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public void setClass(String $class) { this.$class = $class; }


    public String getTransactionInvoked() {
        return transactionInvoked;
    }

    public void setTransactionInvoked(String transactionInvoked) {
        this.transactionInvoked = transactionInvoked;
    }

    public String getIdentityUsed() {
        return identityUsed;
    }

    public void setIdentityUsed(String identityUsed) {
        this.identityUsed = identityUsed;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getParticipantInvoking() {
        return participantInvoking;
    }

    public void setParticipantInvoking(String participantInvoking) {
        this.participantInvoking = participantInvoking;
    }


    @Override
    public int compareTo(@NonNull HistorianRecord o) {
        return this.transactionTimestamp.compareTo(o.transactionTimestamp) * -1;
    }
}
