package com.payment.arthpay.utill;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ExtensionFunction {

    public static void checkValidation(EditText editText,String msg){
        editText.requestFocus();
        editText.setError(msg);
    }

    public static void showToast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void datePicker(Context context, TextView textView){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month<9){
                    textView.setText(dayOfMonth+"-"+ "0"+(month+1)+"-"+year);
                }else{
                    textView.setText(dayOfMonth+"-"+ (month+1)+"-"+year);
                }

            }
        },year,month,day);

        datePickerDialog.show();


    }

    public static <T> void startActivity(Context context, Class<T> destanationActivity) {
        context.startActivity(new Intent(context, destanationActivity));
    }
}
