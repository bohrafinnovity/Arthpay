package com.payment.arthpay.reports.invoice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class wd {
    public static Bitmap NUL(final Bitmap bitmap, final Bitmap bitmap2) {
        final Bitmap bitmap3 = Bitmap.createBitmap(bitmap.getWidth(), bitmap2.getHeight() + bitmap.getHeight(), bitmap.getConfig());
        final Canvas canvas = new Canvas();
        new Canvas(bitmap3);
        final Canvas canvas2 = new Canvas();
        canvas2.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, 0.0f, (float) bitmap.getHeight(), (Paint) null);
        return bitmap3;
    }

    private static void NUL(final Bitmap finalBitmap, final Context context) {
        final String child = "Report.png";
        //String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Screenshots/");
        myDir.mkdirs();
        String fname = "MG.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            final StringBuilder append = new StringBuilder().append(Environment.getExternalStorageDirectory().getAbsolutePath());
            final String str = "/Screenshots/MG.jpg";
            try {
                NUL(new File(append.append(str).toString()), context);
            } catch (Exception ex) {
                Toast.makeText(context, (CharSequence) "Something went wrong.Please try again later.", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NULL(final Bitmap bitmap, final Context context) {
        final String child = "MG.png";
        File myDirectory = new File(Environment.getExternalStorageDirectory() + "/Screenshots/");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        try {
            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Screenshots/MG.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
            final StringBuilder append = new StringBuilder().append(Environment.getExternalStorageDirectory().getAbsolutePath());
            final String str = "/Screenshots/MG.png";
            try {
                NUL(new File(append.append(str).toString()), context);
            } catch (Exception ex) {
                Toast.makeText(context, (CharSequence) "Something went wrong.Please try again later.", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //final File file = new File(string);
//        final File file2 = new File(string, child);
//        if (!file2.exists()) {
//            file2.mkdirs();
//        }


//        try {
//            //final FileOutputStream fileOutputStream = new FileOutputStream(file);
//            final FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
//            final Bitmap.CompressFormat png = Bitmap.CompressFormat.PNG;
//            final int n = 85;
//            try {
//                bitmap.compress(png, n, (OutputStream) fileOutputStream2);
//                //fileOutputStream.flush();
//                //fileOutputStream.close();
//                final StringBuilder append = new StringBuilder().append(Environment.getExternalStorageDirectory().getAbsolutePath());
//                final String str = "/Screenshots/MG.png";
//                try {
//                    NUL(new File(append.append(str).toString()), context);
//                } catch (Exception ex) {
//                    Toast.makeText(context, (CharSequence) "Something went wrong.Please try again later.", Toast.LENGTH_LONG).show();
//                    ex.printStackTrace();
//                }
//            } catch (Exception ex2) {
//                ex2.printStackTrace();
//            }
//        } catch (Exception ex3) {
//            ex3.printStackTrace();
//        }
    }

    public static void NUL(final File file, final Context context) {
        //final Uri uriForFile = FileProvider.getUriForFile(context, "org.egram.aepslib", file);
        Uri uriForFile = null;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
            if (file.exists())
                uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".files", file);
        } else {
            if (file.exists()) {
                uriForFile = Uri.fromFile(file);
            }
        }

        final Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.SUBJECT", "");
        intent.putExtra("android.intent.extra.TEXT", "");
        intent.putExtra("android.intent.extra.STREAM", (Parcelable) uriForFile);
        final String s = "Share Screenshot";
        try {
            context.startActivity(Intent.createChooser(intent, (CharSequence) s));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, (CharSequence) "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void NUL(final ScrollView scrollView, final Context context) {
        scrollView.setDrawingCacheEnabled(true);
        final Activity activity;
        int n;
        if ((activity = (Activity) context).getWindow().getDecorView().getHeight() > scrollView.getChildAt(0).getHeight()) {
            n = activity.getWindow().getDecorView().getHeight();
        } else {
            n = scrollView.getChildAt(0).getHeight();
        }
//        final Bitmap nul = NUL(this.NUL(view, view.getHeight(), view.getWidth()), this.NUL((View) scrollView, n, scrollView.getWidth()));
        scrollView.setDrawingCacheEnabled(false);
        //NUL(nul, context);

        int totalHeight = scrollView.getChildAt(0).getHeight();
        int totalWidth = scrollView.getChildAt(0).getWidth();

        Bitmap b = getBitmapFromView(scrollView, totalHeight, totalWidth);
        NUL(b, context);
    }

    public void NUL(final NestedScrollView scrollView, final Context context) {
        scrollView.setDrawingCacheEnabled(true);
        final Activity activity;
        scrollView.setDrawingCacheEnabled(false);
        int totalHeight = scrollView.getChildAt(0).getHeight();
        int totalWidth = scrollView.getChildAt(0).getWidth();
        Bitmap b = getBitmapFromView(scrollView, totalHeight, totalWidth);
        NUL(b, context);
    }

    public void NUL(final View scrollView, final Context context) {
        scrollView.setDrawingCacheEnabled(true);
        final Activity activity;
        scrollView.setDrawingCacheEnabled(false);
        int totalHeight = scrollView.getHeight();
        int totalWidth = scrollView.getWidth();
        Bitmap b = getBitmapFromView(scrollView, totalHeight, totalWidth);
        NUL(b, context);
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    public final Bitmap NUL(final View view, final int n, final int n2) {
        final Bitmap bitmap = Bitmap.createBitmap(n2, n, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Drawable background;
        if ((background = view.getBackground()) != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        final Bitmap bitmap2 = bitmap;
        view.draw(canvas);
        return bitmap2;
    }
}
