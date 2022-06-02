package com.payment.arthpay.moneyTransfer.mDMT;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.model.BeneficiaryItems;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Saved_benificiary_Fragment extends Fragment {

    public static Saved_benificiary_Fragment newInstance(String s, String name, String json) {
        Saved_benificiary_Fragment result = new Saved_benificiary_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("sender_number", s);
        bundle.putString("name", name);
        bundle.putString("json", json);
        result.setArguments(bundle);
        return result;
    }

    String sender_number, name = "", json = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        sender_number = bundle.getString("sender_number");
        name = bundle.getString("name");
        json = bundle.getString("json");
    }


    LinearLayout ll_contain_listview;
    LinearLayout ll_contain_norecepient_found;

    RecyclerView recyclerview_benificiary;
    RecyclerView.LayoutManager layoutManager;
    private List<BeneficiaryItems> beneficiaryitems;
    Beneficiry_CardAdapter beneficiry_cardAdapter = null;

    String message, status = "";

    String username, password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dmt_saved_benificiary_fragment, container, false);

        username = SharedPrefs.getValue(getActivity(), SharedPrefs.USER_NAME);
        password = SharedPrefs.getValue(getActivity(), SharedPrefs.PASSWORD);

        beneficiaryitems = new ArrayList<>();

        recyclerview_benificiary = v.findViewById(R.id.recyclerview_plan);
        recyclerview_benificiary.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerview_benificiary.setLayoutManager(layoutManager);

        beneficiry_cardAdapter = new Beneficiry_CardAdapter(getActivity(), beneficiaryitems, name, sender_number);
        recyclerview_benificiary.setAdapter(beneficiry_cardAdapter);

        ll_contain_listview = v.findViewById(R.id.ll_contain_listview);
        ll_contain_norecepient_found = v.findViewById(R.id.ll_contain_norecepient_found);

        new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken=" +
                SharedPrefs.getValue(getActivity(), SharedPrefs.APP_TOKEN) + "&user_id=" +
                SharedPrefs.getValue(getActivity(), SharedPrefs.USER_ID) + "&type=verification&mobile=" + sender_number+ "&device_id=" + Constents.MOBILE_ID);
        return v;
    }

    protected void mShowBeneficieryList(final String json) {
        String status = "";
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("statuscode")) {
                status = jsonObject.getString("statuscode");
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }
            //String status = jsonObject.getString("statuscode");
            if (status.equalsIgnoreCase("UA")) {
                AppManager.getInstance().logoutApp(getActivity());
            } else if (status.equalsIgnoreCase("txn")) {
                JSONArray jsonArray = jsonObject.getJSONArray("beneficiary");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    BeneficiaryItems item = new BeneficiaryItems();
                    item.setBeneid(data.getString("beneid"));
                    item.setBenename(data.getString("benename"));
                    item.setBeneaccount(data.getString("beneaccount"));
                    item.setBenebank(data.getString("benebank"));
                    item.setBeneifsc(data.getString("beneifsc"));
                    item.setBenebankid(data.getString("benebankid"));
                    item.setBenestatus(data.getString("benestatus"));
                    item.setBenemobile(data.getString("benemobile"));
                    item.setSendernumber(sender_number);
                    item.setSendername(name);
                    beneficiaryitems.add(item);
                    beneficiry_cardAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class CallMyRestApi extends CallRestApiForMoneyTransfer {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            mShowBeneficieryList(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.show();
            dialog.setCancelable(false);

        }
    }
}