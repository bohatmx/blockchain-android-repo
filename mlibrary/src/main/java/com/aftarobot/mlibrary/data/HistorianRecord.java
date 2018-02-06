package com.aftarobot.mlibrary.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class HistorianRecord implements Data, Serializable{

    private String $class,transactionInvoked, identityUsed;
    private String transactionId, transactionType, participantInvoking;
    private ArrayList<String> eventsEmitted;
    private Date transactionTimestamp;
    public String getClassz() { return this.$class; }

    public Date getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(Date transactionTimestamp) {
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

    public ArrayList<String> getEventsEmitted() {
        return eventsEmitted;
    }

    public void setEventsEmitted(ArrayList<String> eventsEmitted) {
        this.eventsEmitted = eventsEmitted;
    }
}
