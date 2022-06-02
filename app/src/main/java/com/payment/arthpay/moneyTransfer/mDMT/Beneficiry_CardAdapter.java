package com.payment.arthpay.moneyTransfer.mDMT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.model.BeneficiaryItems;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Beneficiry_CardAdapter extends RecyclerView.Adapter<Beneficiry_CardAdapter.ViewHolder> {
    List<BeneficiaryItems> beneficiary_items;
    private Context context;
    AlertDialog alertDialog = null;
    AlertDialog dialog;

    String type = "", sender_number, account_number, otp, benemobile;

    private String sName, sNumber ;
    public Beneficiry_CardAdapter(Context context, List<BeneficiaryItems> beneficiary_items, String sName, String sNumber) {
        super();
        this.context = context;
        this.sName = sName;
        this.sNumber = sNumber;
        this.beneficiary_items = beneficiary_items;
    }



    @Override
    public int getItemCount() {
        return beneficiary_items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_beneficiary_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BeneficiaryItems items = beneficiary_items.get(position);
        holder.textView_beneficiary_name.setText(items.getBenename());
        holder.textView_acountno.setText(items.getBeneaccount());
        holder.textView_bankname.setText(items.getBenebank());
        holder.textView_ifsc.setText(items.getBeneifsc());
        holder.textView_recepient_id.setText(items.getBeneid());

        if (items.getBenestatus().equalsIgnoreCase("NV")) {
            holder.button_verify.setVisibility(View.VISIBLE);
            holder.button_transfer.setVisibility(View.GONE);
        } else {
            holder.button_verify.setVisibility(View.GONE);
            holder.button_transfer.setVisibility(View.VISIBLE);
        }

        holder.button_transfer.setOnClickListener(view -> {
            /*Intent intent = new Intent(context, DMTTransaction.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("DATA", beneficiary_items.get(position));
            intent.putExtras(bundle);
            context.startActivity(intent);*/
            Intent i = new Intent(context, DMTTransaction.class);
            i.putExtra("data", items);
            i.putExtra("sender_name", sName);
            i.putExtra("sender_number", sNumber);
            i.putExtra("url", Constents.URL.DMT_TRANSACTION);
            context.startActivity(i);
        });

        holder.button_verify.setOnClickListener(view -> {
            sender_number = items.getSendernumber();
            account_number = items.getBeneaccount();
            benemobile = items.getBenemobile();
            type = "otp";
            new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken=" +
                    SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN) + "&user_id=" +
                    SharedPrefs.getValue(context, SharedPrefs.USER_ID) + "&type=" + type + "&mobile=" + sender_number+ "&device_id=" + Constents.MOBILE_ID);
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_beneficiary_name;
        public TextView textView_acountno;
        public TextView textView_bankname;
        public TextView textView_ifsc;
        public TextView textView_recepient_id;
        Button button_transfer, button_verify;

        public ViewHolder(View view) {
            super(view);
            textView_beneficiary_name = view.findViewById(R.id.textview_beneficiary_name);
            textView_acountno = view.findViewById(R.id.textview_acountno);
            textView_bankname = view.findViewById(R.id.textview_bankname);
            textView_ifsc = view.findViewById(R.id.textView_ifsc);
            textView_recepient_id = view.findViewById(R.id.textView_recepient_id);
            button_transfer = view.findViewById(R.id.button_transfer);
            button_verify = view.findViewById(R.id.button_verify);
        }
    }

    class CallMyRestApi extends CallRestApiForMoneyTransfer {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
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
            String statuscode = "";
            String message = "";

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.has("statuscode")) {
                    statuscode = jsonObject.getString("statuscode");
                } else {
                    statuscode = "ERR";
                }

                if (jsonObject.has("message")) {
                    message = jsonObject.getString("message");
                } else {
                    message = "Something went wrong";
                }

                if (statuscode.equalsIgnoreCase("txn")) {
                    if (type.equals("otp")) {
                        final EditText otpedittext = new EditText(context);
                        otpedittext.setSingleLine();
                        otpedittext.setHint("Enter otp");
                        FrameLayout container = new FrameLayout(context);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 30;
                        params.leftMargin = 50;
                        params.rightMargin = 50;
                        params.bottomMargin = 30;

                        otpedittext.setLayoutParams(params);
                        container.addView(otpedittext);

                        mShowOTPDialog();
//
//                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                         builder1.setCancelable(false)
//                                .setTitle("Otp Verification")
//                                .setView(container)
//                                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int i) {
//                                        if (AppManager.isOnline(context)) {
//                                            type = "beneverify";
//                                            otp = otpedittext.getText().toString();
//                                            new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?" +
//                                                    "apptoken=" + SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN) +
//                                                    "&user_id=" + SharedPrefs.getValue(context, SharedPrefs.USER_ID) + "&type=" + type + "&mobile=" + sender_number + "&benemobile=" + benemobile + "&beneaccount=" + account_number + "&otp=" + otp);
//                                            dialog.dismiss();
//                                        } else {
//                                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                })
//                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        AlertDialog alert = builder1.create();
//                        alert.show();

                    } else {
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage(message);

                        builder1.setCancelable(false)
                                .setTitle("Success")
                                .setPositiveButton("Ok", (dialog, i) -> {
                                    if (AppManager.isOnline(context)) {
                                        ((SenderDetailActivity) context).Update();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alert = builder1.create();
                        alert.show();
                    }
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    protected void mShowOTPDialog() {
//        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = LayoutInflater.from(context).inflate(R.layout.custome_alert_dialog_for_enter_otp, null);
//        View v2 = inflater2.inflate(R.layout.custome_alert_dialog_for_enter_otp, null);

        final EditText edittext_otp = v.findViewById(R.id.edittext_otp);
        final EditText edittext_new_password = v.findViewById(R.id.edittext_new_password);
        edittext_new_password.setVisibility(View.GONE);
        TextView textview_resend_otp = v.findViewById(R.id.textview_resend_otp);
        Button button_submit = v.findViewById(R.id.button_submit);
        Button button_cancel = v.findViewById(R.id.button_cancel);

        button_submit.setOnClickListener(v12 -> {
            if (AppManager.isOnline(context)) {
                alertDialog.dismiss();
                type = "beneverify";
                otp = edittext_otp.getText().toString();
                new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?" +
                        "apptoken=" + SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN) +
                        "&user_id=" + SharedPrefs.getValue(context, SharedPrefs.USER_ID) + "&type=" + type +
                        "&mobile=" + sender_number + "&benemobile=" + benemobile + "&beneaccount=" +
                        account_number + "&otp=" + otp+ "&device_id=" + Constents.MOBILE_ID);
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        textview_resend_otp.setOnClickListener(v1 -> {
            if (AppManager.isOnline(context)) {
                alertDialog.dismiss();
                type = "otp";
                new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken=" +
                        SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN) + "&user_id=" +
                        SharedPrefs.getValue(context, SharedPrefs.USER_ID) + "&type=" + type + "&mobile=" + sender_number+ "&device_id=" +
                        Constents.MOBILE_ID);
            } else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });


        final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setCancelable(false);
        builder2.setView(v);
        alertDialog = builder2.create();
        button_cancel.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.show();
    }
}
