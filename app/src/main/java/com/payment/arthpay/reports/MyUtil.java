package com.payment.arthpay.reports;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.payment.arthpay.R;

import java.text.DecimalFormat;

public class MyUtil {

    public static void transparentStatusBar(Activity activity) {
        try {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void whiteStatusBar(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(R.color.white_card));
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean isNN(String str) {
        boolean flag = false;
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            flag = true;
        }
        return flag;
    }

    public static String formatWithRupee(Context context, Object value) {
        if (value != null) {
            String rupee = context.getResources().getString(R.string.rupee);
            double m = Double.parseDouble(String.valueOf(value));
            return rupee + " " + new DecimalFormat("0.00").format(m);
        } else {
            return "Not Available";
        }
    }
}
