package com.payment.arthpay.printer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.payment.arthpay.R;


import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Bd
{
    public static boolean NUL(final String s, final PackageManager packageManager) {
        final int n = 0;
        try {
            packageManager.getPackageInfo(s, n);
            return true;
        }
        catch (PackageManager.NameNotFoundException ex) {
            return false;
        }
    }
    
    public Dialog c(final Context context) {
        final Dialog dialog = new Dialog(context);
        final Dialog dialog2 = dialog;
        new Dialog(context);
        final View inflate = LayoutInflater.from(context).inflate(R.layout.progress_bar, (ViewGroup)null);
        final Dialog dialog3 = dialog2;
        dialog3.requestWindowFeature(1);
        dialog3.getWindow().setBackgroundDrawableResource(R.color.transpD);
        dialog.setContentView(inflate);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
    
    public void NUL(final View view, final AppCompatActivity appCompatActivity) {
        ((InputMethodManager)appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    public void NUL(final View view, final String str, final int n) {
        final Snackbar make;
        final View view3;
        final View view2 = view3 = (make = Snackbar.make(view, (CharSequence)("" + str), Snackbar.LENGTH_SHORT)).getView();
        final Animation nul = new Bd().NUL(view.getContext());
        final Snackbar snackbar = make;
        final View view4 = view3;
        view3.setAnimation(nul);
        view4.setBackgroundColor(ContextCompat.getColor(view.getContext(), n));
        snackbar.show();
        view2.startAnimation(nul);
    }
    
    public Animation NUL(final Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.right_to_left_slide);
    }
    
    public void NUL(final Activity activity) {
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
    
    public String getTimeStamp() {
        //return a.getInstance("yyyy-MM-dd'T'HH:mm:ssZZZ").format(new Date());
        return ""+System.currentTimeMillis();
    }
    
    public String b(final Context context) {
        final NetworkInfo activeNetworkInfo;
        //cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((activeNetworkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()) != null) {
            if (activeNetworkInfo.getType() == 1) {
                return Formatter.formatIpAddress(((WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
            }
            if (activeNetworkInfo.getType() == 0) {
                try {
                    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (networkInterfaces.hasMoreElements()) {
                        final Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            final InetAddress inetAddress;
                            if (!(inetAddress = inetAddresses.nextElement()).isLoopbackAddress() && !(inetAddress instanceof Inet6Address)) {
                                return InetAddress.getByAddress(inetAddress.getAddress()).getHostAddress();
                            }
                        }
                    }
                    return "0.0.0.0";
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "0.0.0.0";
    }
    
    public boolean a(final Context context) {
        final GoogleApiAvailability instance;
        final int googlePlayServicesAvailable;
        if ((googlePlayServicesAvailable = (instance = GoogleApiAvailability.getInstance()).isGooglePlayServicesAvailable(context)) != 0) {
            if (instance.isUserResolvableError(googlePlayServicesAvailable)) {
                try {
                    instance.getErrorDialog((Activity)context, googlePlayServicesAvailable, 9000).show();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        }
        return true;
    }
    
    public Dialog NUL(final Context context, final String str, final int n, final Class clazz) {
        final Dialog dialog = new Dialog(context);
        final View inflate = LayoutInflater.from(context).inflate(R.layout.aeps_server_pending, (ViewGroup)null);
        final Dialog dialog2 = dialog;
        final Dialog dialog3 = dialog;
        dialog3.requestWindowFeature(1);
        dialog3.getWindow().setBackgroundDrawableResource(R.color.transpD);
        dialog2.setContentView(inflate);
        final TextView textView = (TextView)inflate.findViewById(R.id.flash_title);
        final RelativeLayout relativeLayout = (RelativeLayout)inflate.findViewById(R.id.flashCloseLayout);
        ((TextView)inflate.findViewById(R.id.flash_text_1)).setText((CharSequence)(str + ""));
        if (n != 1) {
            if (n == 2) {
                textView.setText((CharSequence)"Rejected!");
            }
        }
        else {
            textView.setText((CharSequence)"Pending!");
        }
        final Dialog dialog4 = dialog;
        final RelativeLayout relativeLayout2 = relativeLayout;
        final Dialog dialog5 = dialog;
        dialog5.setCancelable(false);
        dialog5.show();
        //relativeLayout2.setOnClickListener((View.OnClickListener)new yd(this, clazz, context, dialog));
        return dialog4;
    }
    
    public void a(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        final Dialog dialog3;
        final Dialog dialog2 = dialog3 = dialog;
        new Dialog((Context)activity, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialog3.setCancelable(false);
        dialog3.getWindow().requestFeature(1);
        dialog3.getWindow().setFlags(1024, 256);
        dialog.setContentView(LayoutInflater.from((Context)activity).inflate(R.layout.confirmation_dialog, (ViewGroup)null));
        dialog.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        final TextView textView = (TextView)dialog.findViewById(R.id.btn_yes);
        final TextView textView2 = (TextView)dialog.findViewById(R.id.btn_no);
        //textView.setOnClickListener((View.OnClickListener)new zd(this, dialog2, activity));
        //textView2.setOnClickListener((View.OnClickListener)new Ad(this, dialog2));
        dialog.show();
    }
}
