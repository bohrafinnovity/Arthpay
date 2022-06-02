package com.payment.arthpay.moneyTransfer.model;

public class LocalInvoiceModel {
    private String rrn;
    private String message;
    private String status;

    public LocalInvoiceModel(String rrn, String message, String status) {
        this.rrn = rrn;
        this.message = message;
        this.status = status;
    }

    public String getRrn() {
        return rrn;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}