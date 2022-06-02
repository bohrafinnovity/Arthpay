package com.payment.arthpay.reports.invoice.model;

public class PrintDataModel {
    public String head;
    public String value;

    public PrintDataModel() {
    }

    public PrintDataModel(String var1, String var2) {
        this.head = var1;
        this.value = var2;
    }

    public String getHead() {
        return this.head;
    }

    public void setHead(String var1) {
        this.head = var1;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String var1) {
        this.value = var1;
    }
}
