package com.payment.arthpay.member;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.payment.arthpay.R;
import com.payment.arthpay.member.model.AppMember;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtil {




    public static void removeStatusBar(Activity activity) {
        try {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setGlideImage(String url, ImageView imageView, Context context) {
        if (context == null || ((Activity) context).isDestroyed()) return;
        ViewPropertyTransition.Animator animationObject = view -> {
            view.setAlpha(0f);
            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeAnim.setDuration(1000);
            fadeAnim.start();
        };

        RequestOptions options = new RequestOptions()
                .error(context.getResources().getDrawable(R.drawable.not_found_error))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .timeout(60000)
                .centerCrop();
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(options)
                .into(imageView);
    }



    public static String format(Object value) {
        double m = Double.parseDouble(String.valueOf(value));
        return new DecimalFormat("0.00").format(m);
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
    public static void explodeAnimation(Activity activity) {
        activity.getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

    }

    public static boolean isNN(String str) {
        boolean flag = false;
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            flag = true;
        }
        return flag;
    }

    public static boolean isNN_ET(EditText strView) {
        String str = strView.getText().toString();
        boolean flag = false;
        if (str != null) {
            flag = true;
        }
        return flag;
    }


    public static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static List<AppMember> parseMemberRecord(Activity activity, JSONArray jsonArray) {
        List<AppMember> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AppMember>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public static String getMessage(String json) {
        String status = "";
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("message")) {
                status = obj.getString("message");
            } else if (obj.has("msg")) {
                status = obj.getString("msg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    public static String parseExceptionMsg(Exception e) {
        String m = "Some thing went wrong please try again after some time";
        try {
            if (e.getMessage() != null) {
                m += "\nError : " + e.getMessage();
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
        return m;
    }

}
