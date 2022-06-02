package com.payment.arthpay.utill;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.payment.arthpay.R;
import com.payment.arthpay.activity.Login;
import com.payment.arthpay.app.AppController;
import com.payment.arthpay.app.Constents;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by piyush on 9/24/15.
 */
public class AppManager {

    private static int formArraySize = 0;
    private static int imageArraySize = 0;

    private static Activity activity;
    private static Context context;
    //private static DBAdapter db;
    private static ProgressDialog loading;

    private static AppManager appManager;

    public String lossType;
    public int tabPosition = 0;

    public ArrayList<String> filteredForm;
    //For Summary
    int listviewPosition = -1;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (appManager == null)
            appManager = new AppManager();
        return appManager;
    }

    public static boolean isOnline(Context context) {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(context.CONNECTIVITY_SERVICE);
            isConnected = cm.getActiveNetworkInfo().isConnected();
        } catch (Exception ex) {
            isConnected = false;
        }
        return isConnected;
    }

    public static String createVideoPath(String surveynName) {
        String videoPath = Environment.getExternalStorageDirectory() + "/SURVEY/" + surveynName + "/" + "VIDEO";
        File imagefile = new File(videoPath);
        if (!imagefile.exists()) {
            imagefile.mkdirs();
        }
        return videoPath + "/";
    }

    public static String getImei(Context context) {
        String deviceId = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < 29) {
                deviceId = telephonyManager.getImei(0);
            } else if (Build.VERSION.SDK_INT >= 29) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                deviceId = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static String createVideoPathForCCE(String observationName) {
        String videoPath = Environment.getExternalStorageDirectory() + "/CCE/" + observationName + "/" + "VIDEO";
        File imagefile = new File(videoPath);
        if (!imagefile.exists()) {
            imagefile.mkdirs();
        }
        return videoPath + "/";
    }

    public static String createAudioPath(String name, String routeFolder) {
        String audioPath = Environment.getExternalStorageDirectory() + File.separator + routeFolder + File.separator + name + "/" + "AUDIO";
        File imagefile = new File(audioPath);
        if (!imagefile.exists()) {
            imagefile.mkdirs();
        }
        return audioPath + "/";
    }

    public static String screenShotPath(String surveyId) {
        String audioPath = Environment.getExternalStorageDirectory() + File.separator + "SURVEY" + File.separator + surveyId;
        File imagefile = new File(audioPath);
        if (!imagefile.exists()) {
            imagefile.mkdirs();
        }
        return audioPath + "/";
    }


    public static String newAudioPath(String audioPath, String audioName) {
        return audioPath + audioName + ".mp3";
    }

    public static String newVideosPath(String videoPath, String resulation, String videoName) {
        return videoPath + videoName + "_" + resulation + ".mp4";
    }

    public Bitmap resizeBitmap(String highImagePath) {
        Boolean value = FolderManager.deleteLastFromDCIM();

        ResizeBitmap rb = new ResizeBitmap();
        return rb.decodeSampledBitmapFromUri(highImagePath, Constents.IMAGE_HIEGHT, Constents.IMAGE_WIDTH);
    }

    /**
     * Creating this alternative method for some particular image quality changes in witness &
     * form 2 images in reporting module
     *
     * @param highImagePath: String
     * @return image bitmap
     */
    public Bitmap resizeParticularBitmap(String highImagePath) {
        Boolean value = FolderManager.deleteLastFromDCIM();

        ResizeBitmap rb = new ResizeBitmap();
        return rb.decodeFormTwoBitmapFromUri(highImagePath, Constents.IMAGE_HIEGHT, Constents.IMAGE_WIDTH);
    }

    public void compressImage(Bitmap bmp, String imagePath) {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    }

    public void compressFormTwoImage(Bitmap bmp, String imagePath) {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    }

    public Uri createURI(String path, Context context) {
        File file = new File(path);
        Uri outputFileUri = null;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
            outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            outputFileUri = Uri.fromFile(file);
        }
        return outputFileUri;
    }


    public String getDefaultCamera(Context context) {
        String defaultCameraPackage = null;
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (int n = 0; n < list.size(); n++) {
            if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                if (list.get(n).loadLabel(pm).toString().equalsIgnoreCase("Camera")) {
                    defaultCameraPackage = list.get(n).packageName;
                    break;
                }
            }
        }
        return defaultCameraPackage;

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePictureIntent.setPackage(defaultCameraPackage);
//        startActivityForResult(takePictureIntent, 101);
    }

    public static String getBase64FromPath(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        String base64String = null;
        if (bitmap != null) {
            if (bitmap.getWidth() > 1600) {
                options.inSampleSize = 8;
                //System.out.println("option " + 8);
            } else if (bitmap.getWidth() > 1280) {
                options.inSampleSize = 2;
                //System.out.println("option " + 2);
            } else {
                options.inSampleSize = 1;
                //System.out.println("option " + 1);
            }
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            //System.out.println("Image Width " + options.outWidth);
            //System.out.println("Image Hieght " + options.outHeight);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            base64String = Base64.encodeBytes(ba);
        }
        return base64String;
    }


    public String getCurrentYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        return currentYear;
    }

    public void logoutApp(Activity context) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Authentication!");
            builder.setMessage("Your session has been expired.");
            builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    //Toast.makeText(context, "Thanks", Toast.LENGTH_SHORT).show();
                    logoutFromServer(context);
                }
            });

            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        } catch (Exception e) {
            Toast.makeText(context, "Getting exception in logout process", Toast.LENGTH_SHORT).show();
            AppManager.appendLog("Logout Error : " + Constents.SIMPLE_DATE_FORMAT.format(new Date()));
            AppManager.appendLog(e);
            e.printStackTrace();
        }
    }

    public void logoutFromServer(Activity context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.show();
        dialog.setCancelable(false);
        String token = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
        String id = SharedPrefs.getValue(context, SharedPrefs.USER_ID);
        String apiUrl = Constents.URL.baseUrl + "api/android/auth/logout?apptoken=" + token + "&user_id=" + id;

        new PrefLoginManager(context).setFarmerLoginRes("");
        SharedPrefs.clearAllPreferences(context);

        //Log.e("Log", "url" + apiUrl);
        StringRequest sendRequest = new StringRequest(
                Request.Method.GET,
                apiUrl,
                result -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Print.P("Volley Form Response : " + result);
                    try {
                        Toast.makeText(context, "session cleared successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(context, "Exception in session clearing process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    //Toast.makeText(context, "Getting error in logout api ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(i);
                    volleyError.printStackTrace();
                    Print.P("Not able to connect with server");
                });
        AppController.getInstance().addToRequestQueue(sendRequest);
    }

    public static void openDialog(final Context context, String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Permission Deny");
        alertDialogBuilder.setMessage("Application unable to run while " + str + " permission deny! Open app setting and enable permission.");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                dialog.cancel();
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                context.startActivity(intent);
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void appendLog(String text) {
        // Create an log file name with date
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String FileName = "AppLog_" + timeStamp + ".txt";
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/SECURE_PAYMENT/LOG/");

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        File logFile = new File(Environment.getExternalStorageDirectory(), "/SECURE_PAYMENT/LOG/" + FileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void appendLog(Exception e) {
        // Create an log file name with date
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String FileName = "AppLog_" + timeStamp + ".txt";
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/SECURE_PAYMENT/LOG/");

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        File logFile = new File(Environment.getExternalStorageDirectory(), "/SECURE_PAYMENT/LOG/" + FileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            PrintStream ps = new PrintStream(new FileOutputStream(logFile, true));
            e.printStackTrace(ps);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void loadImage(final Context context, ImageView imageView, String url) {
        if (context == null || ((Activity) context).isDestroyed()) return;
        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(1000);
                fadeAnim.start();
            }
        };

        RequestOptions options = new RequestOptions()
                .placeholder(context.getResources().getDrawable(R.drawable.loading_bg))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .timeout(60000)
                .error(context.getResources().getDrawable(R.drawable.image1));


        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(options)
                .into(imageView);
    }

    public static String cL(String status) {
        try {
            return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
        } catch (Exception e) {
            return status;
        }
    }
}

