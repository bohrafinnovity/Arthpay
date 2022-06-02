package com.payment.arthpay.moneyTransfer.mDMT;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class CallRestApiForMoneyTransfer extends AsyncTask<String, String, String> {


    HttpURLConnection urlConnection;
    @Override
    protected String doInBackground(String... strings) {

        StringBuilder result = new StringBuilder();

        try {
            //Log.e("url", strings[0]);
            URL url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }
}
