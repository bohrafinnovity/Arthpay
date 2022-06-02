package com.payment.arthpay.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.payment.arthpay.R;
import com.payment.arthpay.activity.AepsWalletRequest;
import com.payment.arthpay.activity.DTHRecharge;
import com.payment.arthpay.activity.MicoAtmWalletRequest;
import com.payment.arthpay.activity.MobileRecharge;
import com.payment.arthpay.activity.OurServices;
import com.payment.arthpay.activity.WalletRequest;
import com.payment.arthpay.adapter.HomeCenterGridAdapter;
import com.payment.arthpay.adapter.HomeFragOfferSliderAdapter;
import com.payment.arthpay.adapter.Offer_Pager_Adapter;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.bppsServices.OperatorListNew;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.model.CenterGridModel;
import com.payment.arthpay.model.MicroATMModel;
import com.payment.arthpay.moneyTransfer.mDMT.MSenderActivity;
import com.payment.arthpay.on_boarding.OnBoardingActivity;
import com.payment.arthpay.printer.Invoice;
import com.payment.arthpay.utill.AppHandler;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.GridSpacingItemDecoration;
import com.payment.arthpay.utill.MarqueeTextView;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.egram.aepslib.DashboardActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class HomeFragment extends Fragment implements HomeCenterGridAdapter.OnItemClick, VolleyGetNetworkCall.RequestResponseLis {
    MicroATMModel model;
    AlertDialog alert;

    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    TextView tvMicroAtmBalance;
    Timer timer;
    ArrayList<String> bannerImages;
    CarouselView carouselView;
    LinearLayout mainCon1;
    private RecyclerView centerOptionsGrid;
    private List<Integer> offerDataList;
    private TextView tvBalance, tvName, tvNumber, tvAeps, tvAccount, tvBank, tvIFSC;
    // private RelativeLayout walletContainer;
    private LinearLayout mainCon, secondCon;
    private HomeFragOfferSliderAdapter offerSliderAdapter;
    private LinearLayout menuRechargeReport, menuBillReport, menuAepsReport, menuAllReport;
    private ImageView imgSync;
    private String user_id;
    private int REQUEST_TYPE = 0;
    private List<String> banners;
    private List<Integer> drawables;
    private int bannerAvail = 0;
    private Offer_Pager_Adapter offer_pager_adapter;
    private int currentPage = 0;
    private View rootView ;
    private ImageListener imageListener = (position, imageView) -> {
        if (bannerImages.size() > 0) {
            //carouselView.setPageCount(bannerImages.size());
            String url = bannerImages.get(position);
            AppManager.loadImage(getActivity(), imageView, url);
        } else {
            imageView.setImageResource(offerDataList.get(position));
        }
    };
    private AlertDialog loaderDialog;

    public HomeFragment() {
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

    private void init(View view) {
        mainCon1 = view.findViewById(R.id.mainCon1);
        centerOptionsGrid = view.findViewById(R.id.centerOptionsGrid);
        //tvWallet = view.findViewById(R.id.tvWallet);
        menuRechargeReport = view.findViewById(R.id.menuRechargeReport);
        menuBillReport = view.findViewById(R.id.menuBillReport);
        menuAepsReport = view.findViewById(R.id.menuAepsReport);
        menuAllReport = view.findViewById(R.id.menuAllReport);
        tvMicroAtmBalance = view.findViewById(R.id.tvMicroAtmBalance);
        imgSync = view.findViewById(R.id.imgSync);
        mainCon = view.findViewById(R.id.mainCon);
        secondCon = view.findViewById(R.id.secondCon);
        //walletContainer = view.findViewById(R.id.walletContainer);
        tvAeps = view.findViewById(R.id.tvAeps);
        tvBalance = view.findViewById(R.id.tvBalance);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        init(rootView);
        prepareOfferData();
        initializeList();
        mAtmControlller();
        slider(rootView);
        initMarquee();

        mainCon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WalletRequest.class);
            startActivity(intent);
        });

        secondCon.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AepsWalletRequest.class);
            startActivity(i);
        });


        imgSync.setOnClickListener(v -> new UpdateBillService(getActivity(), tvBalance, tvAeps, tvMicroAtmBalance));

        return rootView;
    }

    private void prepareOfferData() {
        drawables = new ArrayList<>();
        banners = new ArrayList<>();

        String is_avail_array = SharedPrefs.getValue(getActivity(), SharedPrefs.BANNERARRAY);
        try {
            if (is_avail_array != null && is_avail_array.length() > 0) {
                bannerAvail = 1;
                try {
                    JSONArray bannersJson = new JSONArray(is_avail_array);
                    for (int i = 0; i < bannersJson.length(); i++) {
                        JSONObject obj = bannersJson.getJSONObject(i);
                        String url = Constents.URL.baseUrl + "/public/" + obj.getString("value");
                        banners.add(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                bannerAvail = 0;
                drawables.add(R.drawable.image1);
                drawables.add(R.drawable.image2);
                drawables.add(R.drawable.image3);
                drawables.add(R.drawable.image4);
                drawables.add(R.drawable.image5);
            }
        } catch (Exception e) {
            bannerAvail = 0;
            drawables.add(R.drawable.image1);
            drawables.add(R.drawable.image2);
            drawables.add(R.drawable.image3);
            drawables.add(R.drawable.image4);
            drawables.add(R.drawable.image5);
        }
    }

    private void slider(View view) {
        bannerImages = new ArrayList<String>();

        offerDataList = new ArrayList<>();
        offerDataList.add(R.drawable.image1);
        offerDataList.add(R.drawable.image2);
        offerDataList.add(R.drawable.image3);
        offerDataList.add(R.drawable.image4);
        offerDataList.add(R.drawable.image5);

        String is_avail_array = SharedPrefs.getValue(getActivity(), SharedPrefs.BANNERARRAY);
        try {
            if (is_avail_array != null && is_avail_array.length() > 0) {
                try {
                    JSONArray bannersJson = new JSONArray(is_avail_array);
                    for (int i = 0; i < bannersJson.length(); i++) {
                        JSONObject obj = bannersJson.getJSONObject(i);
                        String url = Constents.URL.baseUrl + "/public/" + obj.getString("value");
                        bannerImages.add(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        bannerImages.add("https://besthqwallpapers.com/Uploads/24-3-2018/45534/thumb2-online-shopping-4k-modern-technology-payment-online-e-commerce.jpg");
//        bannerImages.add("https://miro.medium.com/max/1200/1*BuFLVvsj1c4wHkrzHv8wNg.jpeg");
//        bannerImages.add("https://cdn.dribbble.com/users/1810601/screenshots/9344342/save-money_4x.png");
//        bannerImages.add("https://www.teahub.io/photos/full/279-2797644_online-financial-services.jpg");

        carouselView = view.findViewById(R.id.carouselView);
        if (bannerImages.size() > 0) {
            carouselView.setPageCount(bannerImages.size());
        } else {
            carouselView.setPageCount(offerDataList.size());
        }
        carouselView.setImageListener(imageListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitial();
    }

    @Override
    public void onResume() {
        super.onResume();
        new UpdateBillService(getActivity(), tvBalance, tvAeps, tvMicroAtmBalance);
        mAtmControlller();
    }

    private void setInitial() {
        String balance = getString(R.string.rupee) + " " + SharedPrefs.getValue(getActivity(), SharedPrefs.MAIN_WALLET);
        String aeps = getString(R.string.rupee) + " " + SharedPrefs.getValue(getActivity(), SharedPrefs.APES_BALANCE);
        String MaMOUNT = getString(R.string.rupee) + " " + SharedPrefs.getValue(getActivity(), SharedPrefs.MICRO_ATM_BALANCE);
        tvMicroAtmBalance.setText(MaMOUNT);
        tvBalance.setText(balance);
        tvAeps.setText(aeps);
    }

    private void mAtmControlller() {
        if (mainCon1 != null) {
            String mAtm = SharedPrefs.getValue(getActivity(), SharedPrefs.MICRO_ATM_BALANCE);
            if (mAtm != null && mAtm.equalsIgnoreCase("no")) {
                mainCon1.setVisibility(View.GONE);
            } else {
                mainCon1.setVisibility(View.VISIBLE);
                mainCon1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), MicoAtmWalletRequest.class);
                        startActivity(i);
                    }
                });
            }
        }
    }

    private void initializeList() {
        ArrayList<CenterGridModel> list = new ArrayList<>();
        list.add(new CenterGridModel("available", R.drawable.ic_iphone, "Mobile"));
        list.add(new CenterGridModel("available", R.drawable.dth_icon_new, "DTH"));
        list.add(new CenterGridModel("available", R.drawable.ic_electricity_icon_new, "Electricity"));
        list.add(new CenterGridModel("available", R.drawable.dmt_icon_new, "DMT"));
        list.add(new CenterGridModel("available", R.drawable.ic_finger, "AEPS"));
        list.add(new CenterGridModel("available", R.drawable.ic_matm, "mATM"));
        //list.add(new CenterGridModel("available", R.drawable.pancard_icon_new, "Utipan"));
        list.add(new CenterGridModel("available", R.drawable.ic_fastag_icon_new, "FASTag"));
        list.add(new CenterGridModel("available", R.drawable.ic_gas_cylinder_icon_new, "LPG GAS"));
        list.add(new CenterGridModel("available", R.drawable.ic_landline_icon_new, "Landline")); // => Holidays
        list.add(new CenterGridModel("available", R.drawable.broad_bank_icon_new, "Broadband")); // => Cash back
        list.add(new CenterGridModel("available", R.drawable.ic_water_icon_new, "Water"));
        list.add(new CenterGridModel("available", R.drawable.menu_ic_80, "More"));

        HomeCenterGridAdapter adapter = new HomeCenterGridAdapter(getActivity(), list, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);
        centerOptionsGrid.setLayoutManager(mLayoutManager);
        centerOptionsGrid.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(1), true));
        centerOptionsGrid.setItemAnimator(new DefaultItemAnimator());
        centerOptionsGrid.setAdapter(adapter);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onItemClick(int position) {
        Intent i;
        switch (position) {

            case 0:
                startActivity(new Intent(getActivity(), MobileRecharge.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), DTHRecharge.class));
                break;
            case 2:
                i = new Intent(getActivity(), OperatorListNew.class);
                i.putExtra("title", "Electric");
                i.putExtra("type", "electricity");
                i.putExtra("hint", "Bill Number");
                i.putExtra("descInput", "Please enter valid Bill Number");
                startActivity(i);
                break;
            case 3:
                //startActivity(new Intent(getActivity(), DMT.class));
                startActivity(new Intent(getActivity(), MSenderActivity.class));
                break;
            case 4:
                user_id = SharedPrefs.getValue(getActivity(), SharedPrefs.USER_ID);
                REQUEST_TYPE = 1;
                volleyNetworkCall(Constents.URL.baseUrl + "api/android/aeps/initiate?apptoken=" +
                        SharedPrefs.getValue(getActivity(), SharedPrefs.APP_TOKEN) + "&user_id=" + user_id);
//                String s = ""+(Constents.URL.baseUrl + "api/android/aeps/initiate?apptoken=" +
//                        SharedPrefs.getValue(getActivity(), SharedPrefs.APP_TOKEN) + "&user_id=" + user_id);
//                Log.e("io",""+s);
                break;
            case 5:
                user_id = SharedPrefs.getValue(getActivity(), SharedPrefs.USER_ID);
                REQUEST_TYPE = 2;
                volleyNetworkCall(Constents.URL.baseUrl + "api/android/secure/microatm/initiate?apptoken=" +
                        SharedPrefs.getValue(getActivity(), SharedPrefs.APP_TOKEN) + "&user_id=" + user_id);
                break;
            case 6:
                //startActivity(new Intent(getActivity(), PanCard.class));
                i = new Intent(getActivity(), OperatorListNew.class);
                i.putExtra("title", "FASTag");
                i.putExtra("type", "fasttag");
                i.putExtra("hint", "Vehicle Number");
                i.putExtra("descInput", "Please enter valid Vehicle Number");
                startActivity(i);
                break;
            case 7:
                i = new Intent(getActivity(), OperatorListNew.class);
                i.putExtra("title", "LPG GAS");
                i.putExtra("type", "lpggas");
                i.putExtra("hint", "Customer ID");
                i.putExtra("descInput", "Please enter your 10 digit numeric Customer ID");
                startActivity(i);
                break;
            case 8:
                i = new Intent(getActivity(), OperatorListNew.class);
                i.putExtra("title", "Landline");
                i.putExtra("type", "landline");
                i.putExtra("hint", "Telephone Number");
                i.putExtra("descInput", "Please enter your Telephone Number");
                startActivity(i);
                break;
            case 9:
                i = new Intent(getActivity(), OperatorListNew.class);
                i.putExtra("title", "Broadband");
                i.putExtra("type", "broadband");
                i.putExtra("hint", "Account Number");
                i.putExtra("descInput", "Please enter valid Account Number");
                startActivity(i);
                break;
            case 10:
                i = new Intent(getActivity(), OperatorListNew.class);
                i.putExtra("title", "Water");
                i.putExtra("type", "water");
                i.putExtra("hint", "Customer ID");
                i.putExtra("descInput", "Please enter valid Customer ID");
                startActivity(i);
                break;
            case 11:
                startActivity(new Intent(getActivity(), OurServices.class));
                break;
            default:
                Toast.makeText(getActivity(), "Available Soon", Toast.LENGTH_SHORT).show();
        }
    }

    private void volleyNetworkCall(String url) {
        if (AppManager.isOnline(getActivity())) {
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, getActivity(), url).netWorkCall();
        } else {
            Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void callAeps(final String saltKey, final String secretKey, final String BcId, final String userId, final String emailId, final String phone, final String cpid) {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.putExtra("saltKey", saltKey);
        intent.putExtra("secretKey", secretKey);
        intent.putExtra("BcId", BcId);
        intent.putExtra("UserId", userId);
        intent.putExtra("bcEmailId", emailId);
        intent.putExtra("Phone1", phone);
        intent.putExtra("cpid", cpid);//(If any)
        startActivityForResult(intent, 1);
    }

    private void showLoader(String loaderMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void mCallMicroATMIntent(final String saltKey, final String secretKey, final String BcId,
                                     final String userId, final String emailId, final String phone, final String cpid, String rId) {

        PackageManager packageManager = getActivity().getPackageManager();
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
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Get Service");
            alertDialog.setMessage("MicroATM Service not installed. Click OK to download .");
            alertDialog.setPositiveButton("OK", (dialogInterface, i) -> {
                String appPackageName = "org.egram.microatm";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            });
            alertDialog.setNegativeButton("Cancel", (dialog, i) -> dialog.dismiss());
            alertDialog.show();
        }
    }
    String clientrefid = "";
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
                            SharedPrefs.setValue(getActivity(), SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
                        else
                            SharedPrefs.setValue(getActivity(), SharedPrefs.MAIN_WALLET, userObject.getString("balance"));

                        if (userObject.has("nsdlwallet")) {
                            SharedPrefs.setValue(getActivity(), SharedPrefs.NSDL_WALLET, userObject.getString("nsdlwallet"));
                        } else {
                            SharedPrefs.setValue(getActivity(), SharedPrefs.NSDL_WALLET, userObject.getString("nsdlbalance"));
                        }
                        setInitial();
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
                            clientrefid = data.getString("clientrefid");
                            mCallMicroATMIntent(saltKey, secretKey, BcId, UserId, bcEmailId, Phone1, user_id, clientrefid);
                        }
                    }
                } else {
                    String message = AppHandler.getMessage(result);
                    if (message.equalsIgnoreCase("Aeps Registration Pending")) {
                        startActivity(new Intent(getActivity(), OnBoardingActivity.class));
                        //getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong unable to initiate Payment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                String msg = data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message");
                Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
            } else if (requestCode == 123) {
                if (resultCode == -1) {
                    String respCode = data.getStringExtra("respcode");
                    if (respCode.equals("999")) {//Response code from bank(999 for pending transactions 00 for success)
                        String requesttxn = data.getStringExtra("requesttxn");//Type of transaction
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
                        Toast.makeText(getActivity(), "txnamount " + txnamount, Toast.LENGTH_LONG).show();
                        model = new MicroATMModel(requesttxn, refstan, txnamount, mid, tid, clientrefid, vendorid, udf1, udf2, udf3, udf4, date, "", "", "", "", "", respCode, "");
                        uplaodResponse(model);
                    } else {
                        String requesttxn = data.getStringExtra("requesttxn");//Type of transaction
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
                        Toast.makeText(getActivity(), "txnamount " + txnamount, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity(), "Status " + data.getStringExtra("statuscode") + "\nMessage " +
                                    data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
                        }
                        sendBackSdkRes(data.getStringExtra("statuscode"), data.getStringExtra("message"));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message"), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                try {
                    if (data.getStringExtra("statuscode").equals("111")) {
                        sdk();
                    } else {
                        Toast.makeText(getActivity(), "Status " + data.getStringExtra("statuscode") + "\nMessage " +
                                data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
                        Print.P("" + "Status " + data.getStringExtra("statuscode") + "\nMessage " +
                                data.getStringExtra("message"));
                    }
                } catch (Exception e) {
                    Print.P("" + "Status " + data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message"));
                }
            }
        } catch (Exception e) {

        }
    }

    private void sdk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Information")
                .setMessage("MAtm sdk version is outdated do you want to update ?")
                .setPositiveButton("Yes", (dialog, whichButton) -> {
                    alert.dismiss();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=org.egram.microatm")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> alert.dismiss());
        alert = builder.create();
        alert.show();
    }


    private void uplaodResponse(MicroATMModel model) {
        Intent intent = new Intent(getActivity(), Invoice.class);
        intent.putExtra("data", model);
        startActivity(intent);
    }

    private void initMarquee() {
        String t = "";
        rootView.findViewById(R.id.secNews).setVisibility(View.VISIBLE);
        MarqueeTextView tvMarquee = rootView.findViewById(R.id.tvMarquee);
        t = SharedPrefs.getValue(getActivity(), SharedPrefs.NEWS);
        if (!isNN(t) || t.equalsIgnoreCase("no"))
            t = "Welcome to " + getString(R.string.app_name);
        tvMarquee.setText(t);
    }

    public static boolean isNN(String str) {
        boolean flag = false;
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            flag = true;
        }
        return flag;
    }

    private void sendBackSdkRes(String statusCode, String msg) {
        String url = Constents.URL.baseUrl + "api/android/secure/microatm/update";
        new VolleyPostNetworkCall(new VolleyPostNetworkCall.RequestResponseLis() {
            @Override
            public void onSuccessRequest(String JSonResponse) { }
            @Override
            public void onFailRequest(String msg) { }
        }, getActivity(), url, false).netWorkCall(matmParam(msg, statusCode));
    }

    private Map<String, String> matmParam(String msg, String code) {
        Map<String, String> map = new HashMap<>();
        model = new MicroATMModel("", "", "", "", "",
                clientrefid, "", "", "", "", "", "",
                "", "", "", "", "", code, msg);
        map.put("response", new GsonBuilder().create().toJson(model));
        map.put("res", new GsonBuilder().create().toJson(model));
        return map;
    }
}