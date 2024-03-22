package com.phollux.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

public class Payment {
    private String id;
    private String amount;
    private String paymentStatus;
    private String paymentType;

    private String paymentStatusString;

    public Payment() {

    }

    public Payment(String id, String amount, String paymentStatus, String paymentType, String paymentStatusString) {
        this.id = id;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.paymentStatusString = paymentStatusString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatusString() {
        return paymentStatusString;
    }

    public void setPaymentStatusString(String paymentStatusString) {
        this.paymentStatusString = paymentStatusString;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", paymentType=" + paymentType +
                ", paymentStatusString='" + paymentStatusString + '\'' +
                '}';
    }
}
