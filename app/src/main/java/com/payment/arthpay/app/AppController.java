package com.payment.arthpay.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.payment.arthpay.BuildConfig;
import com.payment.arthpay.R;
import com.payment.arthpay.activity.LogoutConfirmation;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

import timber.log.Timber;

@ReportsCrashes(formKey = "", // will not be used
        mailTo = "sumitkumarx95@gmail.com",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE,
                ReportField.USER_CRASH_DATE,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.DISPLAY,
                ReportField.PHONE_MODEL,
                ReportField.USER_APP_START_DATE
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.errorLog)

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Handler handler;
    private Runnable runnable;
    private String state;
    public static AppController myAutoLogoutApp;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        Log.d("AppController: ", "Application Created!");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                ////System.out.println("-->Logout");
                if (!getApplicationContext().getClass().getName().equals(BuildConfig.APPLICATION_ID + ".activity.Login")) {
                    Intent i = new Intent(getApplicationContext(), LogoutConfirmation.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        };

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                try {
                    ////System.out.println("-->Created");
                    state = "Created";
                    handler.removeCallbacks(runnable);
                    //stopRepeatingTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ////System.out.println("-->Started");
                state = "Started";
                handler.removeCallbacks(runnable);
                //stopRepeatingTask();
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ////System.out.println("-->Resumed");
                state = "Resumed";
                handler.removeCallbacks(runnable);
                //stopRepeatingTask();
            }

            @Override
            public void onActivityPaused(Activity activity) {
                ////System.out.println("-->PAUSED");
                state = "PAUSED";
                startTrackingPauseTime();
                //startRepeatingTask();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ////System.out.println("-->Stop");
                state = "Stop";
                startTrackingPauseTime();
                //startRepeatingTask();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                ////System.out.println("-->SaveInstanceState");
                state = "SaveInstanceState";
                //handler.removeCallbacks(runnable);
                //stopRepeatingTask();
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ////System.out.println("-->Destroyed " + activity.getClass().getName());
                if (!activity.getClass().getName().equals(BuildConfig.APPLICATION_ID + ".activity.SplashScreen") ||
                        !activity.getClass().getName().equals(BuildConfig.APPLICATION_ID + ".activity.Login"))
                    if (!state.equals("Stop"))
                        handler.removeCallbacks(runnable);
                //stopRepeatingTask();
            }
        });

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // free your memory, clean cache for example
        Toast.makeText(getApplicationContext(), "Application on Low memory.", Toast.LENGTH_LONG).show();
        Log.d("AppController: ", "Application on Low memory.");
        AppManager.appendLog("Low memory error");
    }


    private void startTrackingPauseTime() {
        // if (handler != null) return;
        //handler.postDelayed(runnable, 1000 * 60 * 20);
        handler.postDelayed(runnable, 1000 * 60 * 15);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void setLang(String code, Activity activity) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
        SharedPrefs.setValue(activity, SharedPrefs.LANGUAGE_TYPE, code);
        //activity.startActivity(new Intent(activity, SplashScreen.class));
        //activity.finish();
    }

    // Alternate code to change language
    public static void changeLanguage(Context context, String languageCode) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

}