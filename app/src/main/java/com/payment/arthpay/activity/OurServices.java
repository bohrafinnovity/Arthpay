package com.payment.arthpay.activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.adapter.ServicesListAdapter;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.bppsServices.OperatorListNew;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.model.MicroATMModel;
import com.payment.arthpay.model.ProfileMenuModel;
import com.payment.arthpay.moneyTransfer.mDMT.MSenderActivity;
import com.payment.arthpay.printer.Invoice;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.egram.aepslib.DashboardActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OurServices extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private ListView listView;
    private String user_id;
    private int REQUEST_TYPE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.our_services);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i;
            switch (position) {
                case 0:
                    startActivity(new Intent(OurServices.this, MobileRecharge.class));
                    break;
                case 1:
                    startActivity(new Intent(OurServices.this, DTHRecharge.class));
                    break;
                case 2:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Electric");
                    i.putExtra("type", "electricity");
                    i.putExtra("hint", "Bill Number");
                    i.putExtra("descInput", "Please enter valid Bill Number");
                    startActivity(i);
                    break;
                case 3:
                    startActivity(new Intent(OurServices.this, MSenderActivity.class));
                    //startActivity(new Intent(OurServices.this, Echo_SenderActivity.class));
                    break;
                case 4:
                   /* user_id = SharedPrefs.getValue(OurServices.this, SharedPrefs.USER_ID);
                    REQUEST_TYPE = 1;
                    volleyNetworkCall(Constents.URL.baseUrl + "api/android/aeps/initiate?apptoken=" +
                            SharedPrefs.getValue(OurServices.this, SharedPrefs.APP_TOKEN) + "&user_id=" + user_id + "&device_id=" + Constents.MOBILE_ID);
                    break;*/
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Postpaid");
                    i.putExtra("type", "postpaid");
                    i.putExtra("hint", "Customer Id");
                    i.putExtra("descInput", "Please enter valid Customer Id");
                    startActivity(i);
                    break;
                case 5:
                    user_id = SharedPrefs.getValue(OurServices.this, SharedPrefs.USER_ID);
                    REQUEST_TYPE = 2;
                    volleyNetworkCall(Constents.URL.baseUrl + "api/android/secure/microatm/initiate?apptoken=" +
                            SharedPrefs.getValue(OurServices.this, SharedPrefs.APP_TOKEN) + "&user_id=" + user_id + "&device_id=" + Constents.MOBILE_ID);
                    break;
                case 6:
                    //startActivity(new Intent(OurServices.this, PanCard.class));
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Housing Society");
                    i.putExtra("type", "housing");
                    i.putExtra("hint", "Apartment Number");
                    i.putExtra("descInput", "Please enter Apartment Number with special symbol without space");
                    startActivity(i);
                    break;
                case 7:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "LPG GAS");
                    i.putExtra("type", "lpggas");
                    i.putExtra("hint", "Customer ID");
                    i.putExtra("descInput", "Please enter your 10 digit numeric Customer ID");
                    startActivity(i);
                    break;
                case 8:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Landline");
                    i.putExtra("type", "landline");
                    i.putExtra("hint", "Telephone Number");
                    i.putExtra("descInput", "Please enter your Telephone Number");
                    startActivity(i);
                    break;
                case 9:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Broadband");
                    i.putExtra("type", "broadband");
                    i.putExtra("hint", "Account Number");
                    i.putExtra("descInput", "Please enter valid Account Number");
                    startActivity(i);
                    break;
                case 10:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Water");
                    i.putExtra("type", "water");
                    i.putExtra("hint", "Customer ID");
                    i.putExtra("descInput", "Please enter valid Customer ID");
                    startActivity(i);
                    break;
                case 11:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Loan Repayment");
                    i.putExtra("type", "loanrepay");
                    i.putExtra("hint", "Loan Account Number");
                    i.putExtra("descInput", "Please enter valid Account Number");
                    startActivity(i);
                    break;
                case 12:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Life Insurance");
                    i.putExtra("type", "lifeinsurance");
                    i.putExtra("hint", "Policy Number");
                    i.putExtra("descInput", "Please enter valid Policy Number");
                    startActivity(i);
                    break;
                case 13:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "FASTag");
                    i.putExtra("type", "fasttag");
                    i.putExtra("hint", "Vehicle Number");
                    i.putExtra("descInput", "Please enter valid Vehicle Number");
                    startActivity(i);
                    break;
                case 14:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Loan");
                    i.putExtra("type", "loanrepay");
                    i.putExtra("hint", "Loan Account Number");
                    i.putExtra("descInput", "Please enter valid Account Number");
                    startActivity(i);
                    break;
                case 15:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Cable TV");
                    i.putExtra("type", "cable");
                    i.putExtra("hint", "Account Number");
                    i.putExtra("descInput", "Please enter valid Account Number.");
                    startActivity(i);
                    break;
                case 17:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Education Fees");
                    i.putExtra("type", "schoolfees");
                    i.putExtra("hint", "Date of Birth(dd/mm/yyyy)");
                    i.putExtra("descInput", "Please fill up student's date of birth");
                    startActivity(i);
                    break;
                case 16:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Health Insurance");
                    i.putExtra("type", "insurance");
                    i.putExtra("hint", "Policy Number");
                    i.putExtra("descInput", "Please enter valid Policy Number");
                    startActivity(i);
                    break;
                case 18:
                    i = new Intent(OurServices.this, OperatorListNew.class);
                    i.putExtra("title", "Municipal Taxes");
                    i.putExtra("type", "muncipal");
                    i.putExtra("hint", "Property ID");
                    i.putExtra("descInput", "Please enter valid Property ID");
                    startActivity(i);
                    break;


                default:
                    Toast.makeText(getApplicationContext(), "Available Soon", Toast.LENGTH_SHORT).show();
            }
        });

        initialiseMenuList();
    }

    private void volleyNetworkCall(String url) {
        if (AppManager.isOnline(OurServices.this)) {
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, OurServices.this, url).netWorkCall();
        } else {
            Toast.makeText(OurServices.this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void initialiseMenuList() {
        List<ProfileMenuModel> list = new ArrayList<>();
        list.add(new ProfileMenuModel(R.drawable.ic_mobile_icon_new, "Mobile"));
        list.add(new ProfileMenuModel(R.drawable.dth_icon_new, "DTH"));
        list.add(new ProfileMenuModel(R.drawable.ic_electricity_icon_new, "Electricity"));
        list.add(new ProfileMenuModel(R.drawable.dmt_icon_new, "DMT"));
        list.add(new ProfileMenuModel(R.drawable.ic_landline_icon_new, "Postpaid"));
        list.add(new ProfileMenuModel(R.drawable.ic_matm, "mATM"));
       // list.add(new ProfileMenuModel(R.drawable.pancard_icon_new, "Utipan"));
        list.add(new ProfileMenuModel(R.drawable.ic_house_icon, "Housing Society"));
        list.add(new ProfileMenuModel(R.drawable.ic_gas_cylinder_icon_new, "LPG GAS"));
        list.add(new ProfileMenuModel(R.drawable.ic_landline_icon_new, "Landline")); // => Holidays
        list.add(new ProfileMenuModel(R.drawable.broad_bank_icon_new, "Broadband")); // => Cash back
        list.add(new ProfileMenuModel(R.drawable.ic_water_icon_new, "Water"));
        list.add(new ProfileMenuModel(R.drawable.ic_loan_payment_icon_new, "Loan Repayment"));
        list.add(new ProfileMenuModel(R.drawable.ic_health_insurance_icon_new, "Life Insurance"));
        list.add(new ProfileMenuModel(R.drawable.ic_fastag_icon_new, "FASTag"));
        list.add(new ProfileMenuModel(R.drawable.ic_loan_payment_icon_new, "Loan"));
        list.add(new ProfileMenuModel(R.drawable.ic_tv_icon_new, "Cable TV"));
        list.add(new ProfileMenuModel(R.drawable.ic_health_insurance_icon_new, "Health Insurance"));
        list.add(new ProfileMenuModel(R.drawable.ic_fee_icon_new, "Education Fees"));
        list.add(new ProfileMenuModel(R.drawable.ic_municipal_icon_new, "Municipal Taxes"));
//        list.add(new ProfileMenuModel(R.drawable.ic_landline_icon_new, "Postpaid"));
       // list.add(new ProfileMenuModel(R.drawable.ic_house_icon, "Housing Society"));

        ServicesListAdapter adapter = new ServicesListAdapter(OurServices.this, list);
        listView.setAdapter(adapter);
    }

    private void initiateMicroAtm(String url) {
        if (AppManager.isOnline(OurServices.this)) {
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, OurServices.this, url).netWorkCall();
        } else {
            Toast.makeText(OurServices.this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    //    private void callAeps(final String BcId, final String userId, final String
//            emailId, final String phone, final String cpid) {
//        new SecurePayAuthCall(this,
//                "NxHj9giJ3OrGa2DpN1z1ol4DyU1swWO35Q1LT5QW",
//                BcId,
//                userId,
//                emailId,
//                phone,
//                cpid).netWorkCall();
//    }
    private void callAeps(final String saltKey, final String secretKey, final String BcId, final String userId, final String emailId, final String phone, final String cpid) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("saltKey", saltKey);
        intent.putExtra("secretKey", secretKey);
        intent.putExtra("BcId", BcId);
        intent.putExtra("UserId", userId);
        intent.putExtra("bcEmailId", emailId);
        intent.putExtra("Phone1", phone);
        intent.putExtra("cpid", cpid);//(If any)
        startActivityForResult(intent, 1);
    }

    private android.app.AlertDialog loaderDialog;

    private void showLoader(String loaderMsg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OurServices.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.android_loader, null);
        builder.setView(view);
        builder.create();
        loaderDialog = builder.show();
        loaderDialog.setCancelable(false);
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    public static boolean isPackageInstalled(String packagename, PackageManager
            packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void mCallMicroATMIntent(final String saltKey, final String secretKey, final String BcId,
                                     final String userId, final String emailId, final String phone, final String cpid, String rId) {

        PackageManager packageManager = getPackageManager();
        if (isPackageInstalled("org.egram.microatm", packageManager)) {
            Intent intent = new Intent();
            intent.setComponent(new
                    ComponentName("org.egram.microatm", "org.egram.microatm.BluetoothMacSearchActivity"));
            intent.putExtra("saltkey", saltKey);
            intent.putExtra("secretkey", secretKey);
            intent.putExtra("bcid", BcId);
            intent.putExtra("userid", userId);
            intent.putExtra("bcemailid", emailId);
            intent.putExtra("phone1", phone);
            intent.putExtra("clientrefid", rId);
            intent.putExtra("vendorid", "");
            intent.putExtra("udf1", "");
            intent.putExtra("udf2", "");
            intent.putExtra("udf3", "");
            intent.putExtra("udf4", "");
            startActivityForResult(intent, 123);
            //finish();
        } else {
            final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OurServices.this);
            alertDialog.setTitle("Get Service");
            alertDialog.setMessage("MicroATM Service not installed. Click OK to download .");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String appPackageName = "org.egram.microatm";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    MicroATMModel model;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                Toast.makeText(this, data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message"), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                String respCode = data.getStringExtra("respcode");
                if (respCode.equals("999")) {//Response code from bank(999 for pending transactions 00 for success)
                    String requesttxn = data.getStringExtra("requesttxn ");//Type of transaction
                    String refstan = data.getStringExtra("refstan");// Mahagram Stan Numbe
                    String txnamount = data.getStringExtra("txnamount");//EcoTransaction amount (0 in case of balanceinquiry and transaction amount in cash withdrawal)
                    String mid = data.getStringExtra("mid");//Mid
                    String tid = data.getStringExtra("tid");//Tid
                    String clientrefid = data.getStringExtra("clientrefid");//Your reference Id
                    String vendorid = data.getStringExtra("vendorid");//Your define value
                    String udf1 = data.getStringExtra("udf1");//Your define value
                    String udf2 = data.getStringExtra("udf2");//Your define value
                    String udf3 = data.getStringExtra("udf3");//Your define value
                    String udf4 = data.getStringExtra("udf4");//Your define value
                    String date = data.getStringExtra("date");//Date of transaction
                    Toast.makeText(this, "txnamount " + txnamount, Toast.LENGTH_LONG).show();

                    // public MicroATMModel(String requesttxn, String refstan, String txnamount, String mid, String tid, String clientrefid, String vendorid, String udf1, String udf2, String udf3, String udf4, String date, String bankremarks, String cardno, String amount, String invoicenumber, String rrn, String statuscode, String message) {
                    model = new MicroATMModel(requesttxn, refstan, txnamount, mid, tid, clientrefid, vendorid, udf1, udf2, udf3, udf4, date, "", "", "", "", "", respCode, "");
                    uplaodResponse(model);
                } else {
                    String requesttxn = data.getStringExtra("requesttxn ");//Type of transaction
                    String bankremarks = data.getStringExtra("msg");//Bank message
                    String refstan = data.getStringExtra("refstan");// Mahagram Stan Number
                    String cardno = data.getStringExtra("cardno");//Atm card number
                    String date = data.getStringExtra("date");//Date of transaction
                    String amount = data.getStringExtra("amount");//Account Balance
                    String invoicenumber = data.getStringExtra("invoicenumber");//Invoice Number
                    String mid = data.getStringExtra("mid");//Mid
                    String tid = data.getStringExtra("tid");//Tid
                    String clientrefid = data.getStringExtra("clientrefid");//Your reference Id
                    String vendorid = data.getStringExtra("vendorid");//Your define value
                    String udf1 = data.getStringExtra("udf1");//Your define value
                    String udf2 = data.getStringExtra("udf2");//Your define value
                    String udf3 = data.getStringExtra("udf3");//Your define value
                    String udf4 = data.getStringExtra("udf4");//Your define value
                    String txnamount = data.getStringExtra("txnamount");//EcoTransaction amount (0 in case of balanceinquiry and transaction amount in cash withdrawal)
                    String rrn = data.getStringExtra("rrn");//Bank RRN number
                    Toast.makeText(this, "txnamount " + txnamount, Toast.LENGTH_LONG).show();

                    model = new MicroATMModel(requesttxn, refstan, txnamount, mid, tid, clientrefid, vendorid, udf1, udf2, udf3, udf4, date, bankremarks, cardno, amount, invoicenumber, rrn, respCode, "");
                    uplaodResponse(model);
                }
            } else {
                try {
                    data.getStringExtra("statuscode"); //to get status code
                    data.getStringExtra("message"); //to get response message
                    if (data.getStringExtra("statuscode").equals("111")) {
                        sdk();
                    } else {
                        Toast.makeText(this, "Status " + data.getStringExtra("statuscode") + "\nMessage " + data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message"), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uplaodResponse(MicroATMModel model) {
        Intent intent = new Intent(this, Invoice.class);
        intent.putExtra("data", model);
        startActivity(intent);
    }

    AlertDialog alert;

    private void sdk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Information")
                .setMessage("MAtm sdk version is outdated do you want to update ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        alert.dismiss();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=org.egram.microatm")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        alert.dismiss();
                    }
                });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onSuccessRequest(String result) {
        closeLoader();
        if (!result.equals("")) {
            try {
                String status = "";
                String saltKey = "";
                String secretKey = "";
                String BcId = "";
                String UserId = "";
                String bcEmailId = "";
                String Phone1 = "";

                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("txn")) {
                    if (REQUEST_TYPE == 3) {
                        JSONObject userObject = new JSONObject(jsonObject.getString("userdata"));
                        if (userObject.has("mainwallet"))
                            SharedPrefs.setValue(OurServices.this, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
                        else
                            SharedPrefs.setValue(OurServices.this, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));

                        if (userObject.has("nsdlwallet")) {
                            SharedPrefs.setValue(OurServices.this, SharedPrefs.NSDL_WALLET, userObject.getString("nsdlwallet"));
                        } else {
                            SharedPrefs.setValue(OurServices.this, SharedPrefs.NSDL_WALLET, userObject.getString("nsdlbalance"));
                        }
                    } else {
                        JSONObject data = jsonObject.getJSONObject("data");
                        saltKey = data.getString("saltKey");
                        secretKey = data.getString("secretKey");
                        BcId = data.getString("BcId");
                        UserId = data.getString("UserId");
                        bcEmailId = data.getString("bcEmailId");
                        Phone1 = data.getString("Phone1");


                        if (REQUEST_TYPE == 1) {
                            callAeps(saltKey, secretKey, BcId, UserId, bcEmailId, Phone1, user_id);
                        } else {
                            String clientrefid = data.getString("clientrefid");
                            mCallMicroATMIntent(saltKey, secretKey, BcId, UserId, bcEmailId, Phone1, user_id, clientrefid);
                        }
                    }
                } else {
                    if (jsonObject.has("message")) {
                        Toast.makeText(OurServices.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(OurServices.this, "Something went wrong unable to initiate Payment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(OurServices.this, msg, Toast.LENGTH_SHORT).show();
    }

}