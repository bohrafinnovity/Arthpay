package com.payment.arthpay.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.payment.arthpay.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class TransactionReciept extends AppCompatActivity {
    LinearLayout ll_title_background;
    CardView cardview_receipt;
    TextView textview_message;
    ImageView mysuccess, myfailed;

    TextView textview_transaction_id, textview_transaction_type, textview_operator, textview_number, textview_price, textview_rrn;

    ImageView imageview_icon;
    String status = "", message = "", transactionid = "", transaction_type = "", operator = "", number = "", price = "", transactionrrn = "";
    File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_transaction_reciept);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ll_title_background = findViewById(R.id.ll_title_background);
        //cardview_receipt=findViewById(R.id.cardview_receipt);
        textview_message = findViewById(R.id.textview_message);
        textview_transaction_id = findViewById(R.id.textview_transaction_id);
        textview_transaction_type = findViewById(R.id.textview_transaction_type);
        textview_operator = findViewById(R.id.textview_operator);
        textview_number = findViewById(R.id.textview_number);
        textview_price = findViewById(R.id.textview_price);
        textview_rrn = findViewById(R.id.textview_rrn);

        mysuccess = findViewById(R.id.mysuccess);
        myfailed = findViewById(R.id.myfailed);

        status = getIntent().getStringExtra("status");
        message = getIntent().getStringExtra("message");
        transactionid = getIntent().getStringExtra("transactionid");
        transactionrrn = getIntent().getStringExtra("transactionrrn");
        transaction_type = getIntent().getStringExtra("transaction_type");
        operator = getIntent().getStringExtra("operator");
        number = getIntent().getStringExtra("number");
        price = getIntent().getStringExtra("price");

        textview_transaction_id.setText(transactionid);
        textview_rrn.setText(transactionrrn);
        textview_transaction_type.setText(transaction_type);
        textview_operator.setText(operator);
        textview_number.setText(number);
        textview_price.setText("\u20B9 " + price);

        textview_message.setText(message);
        if (status.equalsIgnoreCase("txn")) {
            mysuccess.setVisibility(View.VISIBLE);
            //ll_title_background.setBackgroundColor(getResources().getColor(R.color.green));
            // new UpdateBillService(this);
        } else if (status.equalsIgnoreCase("err")) {
            myfailed.setVisibility(View.VISIBLE);
            //ll_title_background.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            // ll_title_background.setBackgroundColor(getResources().getColor(R.color.orange));
        }
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

    protected void mSaveFile(Bitmap bm) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        file = new File(myDir, fname);
        //Log.i("Freelinfing", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
