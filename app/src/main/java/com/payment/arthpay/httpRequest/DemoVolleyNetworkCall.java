package com.payment.arthpay.httpRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Iterator;


public class DemoVolleyNetworkCall {

    private RequestResponseLis listener;
    private Context context;
    private String apiUrl;

    public DemoVolleyNetworkCall(RequestResponseLis listener, Context context, String apiUrl) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
    }

    public interface RequestResponseLis {
        void onSuccessRequest(String JSonResponse);

        void onFailRequest(String msg);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    public void netWorkCall(java.util.Map<String, String> map) {
        // lets create the post params
        StringBuilder postParamsBuffer = new StringBuilder();

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry pair = (java.util.Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            postParamsBuffer.append(concatParams(pair.getKey().toString(), pair.getValue().toString()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                java.net.URL url = new java.net.URL(apiUrl);

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                java.io.InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }
                JSONObject response = new JSONObject(responseStringBuffer.toString());
                int statusCode = conn.getResponseCode();
                switch (statusCode) {
                    case 200:
                        merchantHash = response.toString();
                        break;
                    case 400:
                        merchantHash = "{\"status\":\"failed\",\"message\":\"Bad Request\"}";
                        break;
                    case 401:
                        merchantHash = "{\"status\":\"failed\",\"message\":\"Unauthorised Request\"}";
                        break;
                    case 422:
                        merchantHash = "{\"status\":\"failed\",\"message\":\"Unauthorised Request\"}";
                        break;
                    default:
                        merchantHash = "{\"status\":\"failed\",\"message\":\"An Error Occur\"}";
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                merchantHash = "";
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
           // Log.e("Demo", "Res: " + merchantHash);
            super.onPostExecute(merchantHash);
            progressDialog.dismiss();
            try{
                if (merchantHash != null && merchantHash.length() > 0) {
                    listener.onSuccessRequest(merchantHash);
                } else {
                    listener.onFailRequest("An error occur please try after some time");
                }
            }catch (Exception e){
                listener.onFailRequest("Unhandled exception!");
            }
        }
    }
}