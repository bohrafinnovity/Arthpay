package com.payment.arthpay.moneyTransfer.mDMT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.moneyTransfer.mDMT.operastor.MOperatorList;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class Add_benificiary_Fragment extends Fragment {
    String mobileValue, nameValue, beneaccountValue, ifscValue, type = "";

    AlertDialog alertDialog;
    public static AlertDialog alertDialog_for_bank;

    public static Add_benificiary_Fragment newInstance(String mobile, String name) {
        Add_benificiary_Fragment result = new Add_benificiary_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        bundle.putString("name", name);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mobileValue = bundle.getString("mobile");
        nameValue = bundle.getString("name");
    }

    public static LinearLayout ll_bank_detail;
    Button button_add_beneficiary;
    EditText beneaccount, benename, benemobile;
    ProgressDialog dialog;
    Button button_verify_account;

    public static EditText etBank;
    public static EditText ifscCode;
    public static String benebankValue;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_m_ben, container, false);
        beneaccount = v.findViewById(R.id.beneaccount);
        benename = v.findViewById(R.id.benename);
        benemobile = v.findViewById(R.id.benemobile);
        ifscCode = v.findViewById(R.id.ifscCode);

        v.findViewById(R.id.toolbar).setVisibility(View.GONE);

        beneaccount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (beneaccount.getRight() - beneaccount.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (AppManager.isOnline(getContext())) {
                            if (beneaccount.getText().toString().equals("")) {
                                Toast.makeText(getContext(), "Please enter account number", Toast.LENGTH_SHORT).show();
                            } else if (benemobile.getText().toString().equals("")) {
                                Toast.makeText(getContext(), "Please enter beneficiary mobile", Toast.LENGTH_SHORT).show();
                            } else if (ifscCode.getText().toString().equals("")) {
                                Toast.makeText(getContext(), "Please enter ifsc code", Toast.LENGTH_SHORT).show();
                            } else if (benebankValue.equals("")) {
                                Toast.makeText(getContext(), "Please select bank", Toast.LENGTH_SHORT).show();
                            } else {
                                beneaccountValue = beneaccount.getText().toString();
                                ifscValue = ifscCode.getText().toString();
                                new CallMyRestApiForVerify().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken=" + SharedPrefs.getValue(getActivity(), SharedPrefs.APP_TOKEN)
                                        + "&user_id=" + SharedPrefs.getValue(getActivity(), SharedPrefs.USER_ID) + "&type=accountverification&mobile=" + mobileValue + "&benemobile=" + benemobile.getText().toString() + "&benebank=" + benebankValue + "&beneifsc=" + ifscValue + "&beneaccount="
                                        + beneaccountValue + "&name=" + nameValue + "&benename=" + benename.getText().toString()+ "&device_id=" + Constents.MOBILE_ID);
                            }
                        } else {
                            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                }

                return false;
            }
        });


        etBank = v.findViewById(R.id.etBank);
        etBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MOperatorList.class);
                i.putExtra("type", "bank");
                startActivityForResult(i, 100);
            }
        });

        button_add_beneficiary = v.findViewById(R.id.button_add_benificiary);
        ll_bank_detail = v.findViewById(R.id.ll_bank_detail);

        button_add_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppManager.isOnline(getActivity())) {
                    if (benebankValue == null || benebankValue.equals("")) {
                        Toast.makeText(getContext(), "Select Bank name", Toast.LENGTH_SHORT).show();
                    } else if (beneaccount.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Enter bank account number..", Toast.LENGTH_SHORT).show();
                    } else if (benename.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Enter beneficiary name", Toast.LENGTH_SHORT).show();
                    } else if (benemobile.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Enter beneficiary mobile", Toast.LENGTH_SHORT).show();
                    } else if (ifscCode.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Please enter ifsc code", Toast.LENGTH_SHORT).show();
                    } else {
                        type = "addbeneficiary";
                        beneaccountValue = beneaccount.getText().toString();
                        new CallMyRestApi().execute(Constents.URL.baseUrl
                                + "api/android/dmt/transaction?apptoken=" + SharedPrefs.getValue(getActivity(), SharedPrefs.APP_TOKEN)
                                + "&user_id=" + SharedPrefs.getValue(getActivity(), SharedPrefs.USER_ID) + "&type=" + type
                                + "&mobile=" + mobileValue + "&benemobile=" + benemobile.getText().toString() + "&benebank=" + benebankValue + "&beneifsc=" + ifscCode.getText().toString() + "&beneaccount="
                                + beneaccountValue + "&benename=" + benename.getText().toString() + "&name=" + nameValue+ "&device_id=" + Constents.MOBILE_ID);
                    }
                } else {
                    Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    class CallMyRestApi extends CallRestApiForMoneyTransfer {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            //Log.e("data", s);

            if (type.equalsIgnoreCase("addBeneficiary")) {
                mShowAddBeneStatus(s);
            }
        }
    }

    protected void mShowAddBeneStatus(final String myJSON) {
        String status = "";
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(myJSON);

            if (jsonObject.has("statuscode")) {
                status = jsonObject.getString("statuscode");
            }

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String status = jsonObject.getString("statuscode");
        if (status.equalsIgnoreCase("UA")) {
            AppManager.getInstance().logoutApp(getActivity());
        } else if (status.equalsIgnoreCase("txn")) {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(message);

            builder1.setCancelable(false)
                    .setTitle("Success")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            if (AppManager.isOnline(getActivity())) {
                                ((SenderDetailActivity) getActivity()).Update();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            AlertDialog alert = builder1.create();
            alert.show();
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    class CallMyRestApiForVerify extends CallRestApiForMoneyTransfer {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            String myresult = "";
            myresult = s;
            //Log.e("data", myresult);
            if (!myresult.equals("")) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(myresult);

                    if (jsonObject.has("statuscode")) {
                        status = jsonObject.getString("statuscode");
                    }

                    //String status = jsonObject.getString("statuscode");
                    if (status.equalsIgnoreCase("UA")) {
                        AppManager.getInstance().logoutApp(getActivity());
                    } else if (status.equalsIgnoreCase("txn")) {
                        if (jsonObject.has("benename")) {
                            benename.setText(jsonObject.getString("benename"));
                        }

                        if (jsonObject.has("message")) {
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else if (!status.equals("") && !status.equalsIgnoreCase("txn")) {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 100:
                    String id, name, bankId, ifsc;
                    id = data.getStringExtra("id");
                    name = data.getStringExtra("name");
                    bankId = data.getStringExtra("bankId");
                    ifsc = data.getStringExtra("ifsc");
                    etBank.setText(name);
                    ifscCode.setText(ifsc);
                    benebankValue = bankId;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}