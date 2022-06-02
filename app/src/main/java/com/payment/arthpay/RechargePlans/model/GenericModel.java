package com.payment.arthpay.RechargePlans.model;

import java.util.List;

public class GenericModel {
    private List<DataItem> dataList;
    private List<String> keyList;

    public GenericModel(List<DataItem> dataList, List<String> keyList) {
        this.dataList = dataList;
        this.keyList = keyList;
    }

    public List<DataItem> getDataList() {
        return dataList;
    }

    public List<String> getKeyList() {
        return keyList;
    }
}
