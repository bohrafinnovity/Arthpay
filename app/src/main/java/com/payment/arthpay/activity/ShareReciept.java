package com.payment.arthpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.payment.arthpay.R;
import com.payment.arthpay.model.ReciptModel;
import com.payment.arthpay.printer.wd;

import java.util.ArrayList;
import java.util.List;

public class ShareReciept extends AppCompatActivity {

    private ImageView imgCrossFinish;
    private TextView imgTransactionType;
    private ImageView imgShare;
    private ScrollView scrollView;

    private LinearLayout mainInvoice;
    private ImageView imgPrint;

    private LinearLayout layout1;
    private TextView tvFieldName1;
    private TextView tvFieldValue1;
    private List<View> oneField;
    private List mainMap;

    private LinearLayout layout2;
    private TextView tvFieldName2;
    private TextView tvFieldValue2;
    private LinearLayout layout3;
    private TextView tvFieldName3;
    private TextView tvFieldValue3;
    private LinearLayout layout4;
    private TextView tvFieldName4;
    private TextView tvFieldValue4;
    private LinearLayout layout5;
    private TextView tvFieldName5;
    private TextView tvFieldValue5;
    private LinearLayout layout6;
    private TextView tvFieldName6;
    private TextView tvFieldValue6;
    private LinearLayout layout7;
    private TextView tvFieldName7;
    private TextView tvFieldValue7;
    private LinearLayout layout8;
    private TextView tvFieldName8;
    private TextView tvFieldValue8;
    private LinearLayout layout9;
    private TextView tvFieldName9;
    private TextView tvFieldValue9;
    private LinearLayout layout10;
    private TextView tvFieldName10;
    private TextView tvFieldValue10;
    private LinearLayout layout11;
    private TextView tvFieldName11;
    private TextView tvFieldValue11;
    private LinearLayout layout12;
    private TextView tvFieldName12;
    private TextView tvFieldValue12;
    private LinearLayout layout13;
    private TextView tvFieldName13;
    private TextView tvFieldValue13;
    private LinearLayout layout14;
    private TextView tvFieldName14;
    private TextView tvFieldValue14;
    private LinearLayout layout15;
    private TextView tvFieldName15;
    private TextView tvFieldValue15;
    private TextView tvMsgCumStatusCode;
    private ReciptModel reciptModel;

    public void init() {
        imgCrossFinish = (ImageView) findViewById(R.id.imgCrossFinish);
        imgTransactionType =  findViewById(R.id.imgTransactionType);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mainInvoice = (LinearLayout) findViewById(R.id.mainInvoice);
        imgPrint = (ImageView) findViewById(R.id.imgPrint);
        mainMap = new ArrayList();
        oneField = new ArrayList<>();

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        tvFieldName1 = (TextView) findViewById(R.id.tvFieldName1);
        tvFieldValue1 = (TextView) findViewById(R.id.tvFieldValue1);
        oneField.add(layout1);
        oneField.add(tvFieldName1);
        oneField.add(tvFieldValue1);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout2 = (LinearLayout) findViewById(R.id.layout2);
        tvFieldName2 = (TextView) findViewById(R.id.tvFieldName2);
        tvFieldValue2 = (TextView) findViewById(R.id.tvFieldValue2);
        oneField.add(layout2);
        oneField.add(tvFieldName2);
        oneField.add(tvFieldValue2);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout3 = (LinearLayout) findViewById(R.id.layout3);
        tvFieldName3 = (TextView) findViewById(R.id.tvFieldName3);
        tvFieldValue3 = (TextView) findViewById(R.id.tvFieldValue3);
        oneField.add(layout3);
        oneField.add(tvFieldName3);
        oneField.add(tvFieldValue3);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout4 = (LinearLayout) findViewById(R.id.layout4);
        tvFieldName4 = (TextView) findViewById(R.id.tvFieldName4);
        tvFieldValue4 = (TextView) findViewById(R.id.tvFieldValue4);
        oneField.add(layout4);
        oneField.add(tvFieldName4);
        oneField.add(tvFieldValue4);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout5 = (LinearLayout) findViewById(R.id.layout5);
        tvFieldName5 = (TextView) findViewById(R.id.tvFieldName5);
        tvFieldValue5 = (TextView) findViewById(R.id.tvFieldValue5);
        oneField.add(layout5);
        oneField.add(tvFieldName5);
        oneField.add(tvFieldValue5);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout6 = (LinearLayout) findViewById(R.id.layout6);
        tvFieldName6 = (TextView) findViewById(R.id.tvFieldName6);
        tvFieldValue6 = (TextView) findViewById(R.id.tvFieldValue6);
        oneField.add(layout6);
        oneField.add(tvFieldName6);
        oneField.add(tvFieldValue6);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout7 = (LinearLayout) findViewById(R.id.layout7);
        tvFieldName7 = (TextView) findViewById(R.id.tvFieldName7);
        tvFieldValue7 = (TextView) findViewById(R.id.tvFieldValue7);
        oneField.add(layout7);
        oneField.add(tvFieldName7);
        oneField.add(tvFieldValue7);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout8 = (LinearLayout) findViewById(R.id.layout8);
        tvFieldName8 = (TextView) findViewById(R.id.tvFieldName8);
        tvFieldValue8 = (TextView) findViewById(R.id.tvFieldValue8);
        oneField.add(layout8);
        oneField.add(tvFieldName8);
        oneField.add(tvFieldValue8);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout9 = (LinearLayout) findViewById(R.id.layout9);
        tvFieldName9 = (TextView) findViewById(R.id.tvFieldName9);
        tvFieldValue9 = (TextView) findViewById(R.id.tvFieldValue9);
        oneField.add(layout9);
        oneField.add(tvFieldName9);
        oneField.add(tvFieldValue9);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout10 = (LinearLayout) findViewById(R.id.layout10);
        tvFieldName10 = (TextView) findViewById(R.id.tvFieldName10);
        tvFieldValue10 = (TextView) findViewById(R.id.tvFieldValue10);
        oneField.add(layout10);
        oneField.add(tvFieldName10);
        oneField.add(tvFieldValue10);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout11 = (LinearLayout) findViewById(R.id.layout11);
        tvFieldName11 = (TextView) findViewById(R.id.tvFieldName11);
        tvFieldValue11 = (TextView) findViewById(R.id.tvFieldValue11);
        oneField.add(layout11);
        oneField.add(tvFieldName11);
        oneField.add(tvFieldValue11);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout12 = (LinearLayout) findViewById(R.id.layout12);
        tvFieldName12 = (TextView) findViewById(R.id.tvFieldName12);
        tvFieldValue12 = (TextView) findViewById(R.id.tvFieldValue12);
        oneField.add(layout12);
        oneField.add(tvFieldName12);
        oneField.add(tvFieldValue12);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout13 = (LinearLayout) findViewById(R.id.layout13);
        tvFieldName13 = (TextView) findViewById(R.id.tvFieldName13);
        tvFieldValue13 = (TextView) findViewById(R.id.tvFieldValue13);
        oneField.add(layout13);
        oneField.add(tvFieldName13);
        oneField.add(tvFieldValue13);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout14 = (LinearLayout) findViewById(R.id.layout14);
        tvFieldName14 = (TextView) findViewById(R.id.tvFieldName14);
        tvFieldValue14 = (TextView) findViewById(R.id.tvFieldValue14);
        oneField.add(layout14);
        oneField.add(tvFieldName14);
        oneField.add(tvFieldValue14);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        layout15 = (LinearLayout) findViewById(R.id.layout15);
        tvFieldName15 = (TextView) findViewById(R.id.tvFieldName15);
        tvFieldValue15 = (TextView) findViewById(R.id.tvFieldValue15);
        oneField.add(layout15);
        oneField.add(tvFieldName15);
        oneField.add(tvFieldValue15);

        mainMap.add(oneField);
        oneField = new ArrayList<>();

        tvMsgCumStatusCode = (TextView) findViewById(R.id.tvMsgCumStatusCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciept_share);

        init();
        int dataCount = getIntent().getIntExtra("dataCount",0);
        String from = getIntent().getStringExtra("from");
        imgTransactionType.setText(from);
        if(dataCount!=0){
            reciptModel  =getIntent().getParcelableExtra("dataModel");
            if(reciptModel!=null){
                setInitial(reciptModel,dataCount);
            }
        }

        imgCrossFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l();
            }
        });
    }

    public final void l() {
        new wd().NUL(this.scrollView, this);
    }
    private void setInitial(ReciptModel reciptModel,int dataCount){

        List<String> modelData = getData(reciptModel);
        int count = -1;
        for(int i =0; i<dataCount; i++){
            try{
                count++;
                ((View)((List) mainMap.get(i)).get(0)).setVisibility(View.VISIBLE);
                ((TextView)((List) mainMap.get(i)).get(1)).setText(modelData.get(count));
                ((TextView)((List) mainMap.get(i)).get(2)).setText(modelData.get(++count));
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
    private List getData(ReciptModel reciptModel){

        List<String> result = new ArrayList();

        if(reciptModel.getField1()!=null){
            result.add(reciptModel.getField1());
            result.add(reciptModel.getValue1());

        }else{
            return result;
        }
        if(reciptModel.getField2()!=null){
            result.add(reciptModel.getField2());
            result.add(reciptModel.getValue2());

        }else{
            return result;
        }
        if(reciptModel.getField3()!=null){
            result.add(reciptModel.getField3());
            result.add(reciptModel.getValue3());

        }else{
            return result;
        }
        if(reciptModel.getField4()!=null){
            result.add(reciptModel.getField4());
            result.add(reciptModel.getValue4());

        }else{
            return result;
        }
        if(reciptModel.getField5()!=null){
            result.add(reciptModel.getField5());
            result.add(reciptModel.getValue5());

        }else{
            return result;
        }
        if(reciptModel.getField6()!=null){
            result.add(reciptModel.getField6());
            result.add(reciptModel.getValue6());

        }else{
            return result;
        }
        if(reciptModel.getField7()!=null){
            result.add(reciptModel.getField7());
            result.add(reciptModel.getValue7());

        }else{
            return result;
        }
        if(reciptModel.getField8()!=null){
            result.add(reciptModel.getField8());
            result.add(reciptModel.getValue8());

        }else{
            return result;
        }
        if(reciptModel.getField9()!=null){
            result.add(reciptModel.getField9());
            result.add(reciptModel.getValue9());

        }else{
            return result;
        }
        if(reciptModel.getField10()!=null){
            result.add(reciptModel.getField10());
            result.add(reciptModel.getValue10());

        }else{
            return result;
        }
        if(reciptModel.getField11()!=null){
            result.add(reciptModel.getField11());
            result.add(reciptModel.getValue11());

        }else{
            return result;
        }
        if(reciptModel.getField12()!=null){
            result.add(reciptModel.getField12());
            result.add(reciptModel.getValue12());

        }else{
            return result;
        }
        if(reciptModel.getField13()!=null){
            result.add(reciptModel.getField13());
            result.add(reciptModel.getValue13());

        }else{
            return result;
        }
        if(reciptModel.getField14()!=null){
            result.add(reciptModel.getField14());
            result.add(reciptModel.getValue14());

        }else{
            return result;
        }
        if(reciptModel.getField15()!=null){
            result.add(reciptModel.getField15());
            result.add(reciptModel.getValue15());

        }else{
            return result;
        }

        return result;


    }
}