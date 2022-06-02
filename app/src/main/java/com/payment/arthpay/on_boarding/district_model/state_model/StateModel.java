package com.payment.arthpay.on_boarding.district_model.state_model;

import java.util.Comparator;

public class StateModel {
    private String stateid;
    private String statename;
    private String statecode;

    public static Comparator<StateModel> comparator = (s1, s2) -> {
        String name1 = s1.getStatename().toUpperCase();
        String name2 = s2.getStatename().toUpperCase();
        return name1.compareTo(name2);
    };

    public StateModel(String stateid, String statename, String statecode) {
        this.stateid = stateid;
        this.statename = statename;
        this.statecode = statecode;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }


    public static Comparator<StateModel> getComparator() {
        return comparator;
    }

    public static void setComparator(Comparator<StateModel> comparator) {
        StateModel.comparator = comparator;
    }
}
