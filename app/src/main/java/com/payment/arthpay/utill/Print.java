package com.payment.arthpay.utill;

import com.payment.arthpay.BuildConfig;

import timber.log.Timber;

public class Print {

    static int count = 0;

    Print() {

    }

    public static void P(String sb) {
        if (BuildConfig.DEBUG)
            Timber.d("" + sb);
    }

    public static void printFormUploadParameter(String key, String value) {
        //System.out.println(key+" = "+value+" "+count++);
    }
}
