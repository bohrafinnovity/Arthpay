package com.payment.arthpay.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReciptModel implements Parcelable {

    private String type;
    private String Field1;
    private String Value1;
    private String Field2;
    private String Value2;
    private String Field3;
    private String Value3;
    private String Field4;
    private String Value4;
    private String Field5;
    private String Value5;
    private String Field6;
    private String Value6;
    private String Field7;
    private String Value7;
    private String Field8;
    private String Value8;
    private String Field9;
    private String Value9;
    private String Field10;
    private String Value10;
    private String Field11;
    private String Value11;
    private String Field12;
    private String Value12;

    private String Field13;
    private String Value13;
    private String Field14;
    private String Value14;
    private String Field15;
    private String Value15;

    protected ReciptModel(Parcel in) {
        type = in.readString();
        Field1 = in.readString();
        Value1 = in.readString();
        Field2 = in.readString();
        Value2 = in.readString();
        Field3 = in.readString();
        Value3 = in.readString();
        Field4 = in.readString();
        Value4 = in.readString();
        Field5 = in.readString();
        Value5 = in.readString();
        Field6 = in.readString();
        Value6 = in.readString();
        Field7 = in.readString();
        Value7 = in.readString();
        Field8 = in.readString();
        Value8 = in.readString();
        Field9 = in.readString();
        Value9 = in.readString();
        Field10 = in.readString();
        Value10 = in.readString();
        Field11 = in.readString();
        Value11 = in.readString();
        Field12 = in.readString();
        Value12 = in.readString();
        Field13 = in.readString();
        Value13 = in.readString();
        Field14 = in.readString();
        Value14 = in.readString();
        Field15 = in.readString();
        Value15 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(Field1);
        dest.writeString(Value1);
        dest.writeString(Field2);
        dest.writeString(Value2);
        dest.writeString(Field3);
        dest.writeString(Value3);
        dest.writeString(Field4);
        dest.writeString(Value4);
        dest.writeString(Field5);
        dest.writeString(Value5);
        dest.writeString(Field6);
        dest.writeString(Value6);
        dest.writeString(Field7);
        dest.writeString(Value7);
        dest.writeString(Field8);
        dest.writeString(Value8);
        dest.writeString(Field9);
        dest.writeString(Value9);
        dest.writeString(Field10);
        dest.writeString(Value10);
        dest.writeString(Field11);
        dest.writeString(Value11);
        dest.writeString(Field12);
        dest.writeString(Value12);
        dest.writeString(Field13);
        dest.writeString(Value13);
        dest.writeString(Field14);
        dest.writeString(Value14);
        dest.writeString(Field15);
        dest.writeString(Value15);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReciptModel> CREATOR = new Creator<ReciptModel>() {
        @Override
        public ReciptModel createFromParcel(Parcel in) {
            return new ReciptModel(in);
        }

        @Override
        public ReciptModel[] newArray(int size) {
            return new ReciptModel[size];
        }
    };

    public String getValue14() {
        return Value14;
    }

    public void setValue14(String value14) {
        Value14 = value14;
    }

    public String getValue15() {
        return Value15;
    }

    public void setValue15(String value15) {
        Value15 = value15;
    }

    public String getField11() {
        return Field11;
    }

    public void setField11(String field11) {
        Field11 = field11;
    }

    public String getValue11() {
        return Value11;
    }

    public void setValue11(String value11) {
        Value11 = value11;
    }

    public String getField12() {
        return Field12;
    }

    public void setField12(String field12) {
        Field12 = field12;
    }

    public String getValue12() {
        return Value12;
    }

    public void setValue12(String value12) {
        Value12 = value12;
    }

    public String getField13() {
        return Field13;
    }

    public void setField13(String field13) {
        Field13 = field13;
    }

    public String getValue13() {
        return Value13;
    }

    public void setValue13(String value13) {
        Value13 = value13;
    }



    public ReciptModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField1() {
        return Field1;
    }

    public void setField1(String field1) {
        Field1 = field1;
    }

    public String getValue1() {
        return Value1;
    }

    public void setValue1(String value1) {
        Value1 = value1;
    }

    public String getField2() {
        return Field2;
    }

    public void setField2(String field2) {
        Field2 = field2;
    }

    public String getValue2() {
        return Value2;
    }

    public void setValue2(String value2) {
        Value2 = value2;
    }

    public String getField3() {
        return Field3;
    }

    public void setField3(String field3) {
        Field3 = field3;
    }

    public String getValue3() {
        return Value3;
    }

    public void setValue3(String value3) {
        Value3 = value3;
    }

    public String getField4() {
        return Field4;
    }

    public void setField4(String field4) {
        Field4 = field4;
    }

    public String getValue4() {
        return Value4;
    }

    public void setValue4(String value4) {
        Value4 = value4;
    }

    public String getField5() {
        return Field5;
    }

    public void setField5(String field5) {
        Field5 = field5;
    }

    public String getValue5() {
        return Value5;
    }

    public void setValue5(String value5) {
        Value5 = value5;
    }

    public String getField6() {
        return Field6;
    }

    public void setField6(String field6) {
        Field6 = field6;
    }

    public String getValue6() {
        return Value6;
    }

    public void setValue6(String value6) {
        Value6 = value6;
    }

    public String getField7() {
        return Field7;
    }

    public void setField7(String field7) {
        Field7 = field7;
    }

    public String getValue7() {
        return Value7;
    }

    public void setValue7(String value7) {
        Value7 = value7;
    }

    public String getField8() {
        return Field8;
    }

    public void setField8(String field8) {
        Field8 = field8;
    }

    public String getValue8() {
        return Value8;
    }

    public void setValue8(String value8) {
        Value8 = value8;
    }

    public String getField9() {
        return Field9;
    }

    public void setField9(String field9) {
        Field9 = field9;
    }

    public String getValue9() {
        return Value9;
    }

    public void setValue9(String value9) {
        Value9 = value9;
    }

    public String getField10() {
        return Field10;
    }

    public void setField10(String field10) {
        Field10 = field10;
    }

    public String getValue10() {
        return Value10;
    }

    public void setValue10(String value10) {
        Value10 = value10;
    }

    public String getField14() {
        return Field14;
    }

    public void setField14(String field14) {
        Field14 = field14;
    }

    public String getField15() {
        return Field15;
    }

    public void setField15(String field15) {
        Field15 = field15;
    }
}
