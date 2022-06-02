package com.payment.arthpay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.utill.SharedPrefs;

public class ContactUs extends AppCompatActivity {

    private Button loginButton;
    private ImageView imgKyc;

    private void init() {
        //loginButton = findViewById(R.id.loginButton);
        //imgKyc = findViewById(R.id.kycImage);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        init();

        TextView tvContact = findViewById(R.id.tvContact);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvAddress = findViewById(R.id.tvAddress);
        String email = SharedPrefs.getValue(this, SharedPrefs.SUPPORT_EMAIL);
        String contact = SharedPrefs.getValue(this, SharedPrefs.SUPPORT_NUMBER);
        String address = SharedPrefs.getValue(this, SharedPrefs.SUPPORT_ADDRESS);

        /*if (email != null && email.length() > 0)
            tvEmail.setText(email);
        if (contact != null && contact.length() > 0)
            tvContact.setText(contact);
        if (address != null && address.length() > 0)
            tvAddress.setText(address);*/

        try {
            ImageView img = findViewById(R.id.imgCall);
            img.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact));
                startActivity(intent);
            });

            ImageView imgLoc = findViewById(R.id.imgLoc);
            imgLoc.setOnClickListener(v -> {
               // openMap(17.442399596085796f, 78.34723768187571f);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
