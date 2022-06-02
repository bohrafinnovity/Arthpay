package com.payment.arthpay.utill;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.payment.arthpay.RechargePlans.model.DataItem;
import com.payment.arthpay.RechargePlans.model.GenericModel;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.on_boarding.district_model.DistrictModel;
import com.payment.arthpay.on_boarding.district_model.state_model.StateModel;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.reports.invoice.model.InvoiceModel;
import com.payment.arthpay.reports.model.AEPSFundReqReportModel;
import com.payment.arthpay.reports.model.AEPSReportModel;
import com.payment.arthpay.reports.model.AEPSWalletModel;
import com.payment.arthpay.reports.model.BillRechargeModel;
import com.payment.arthpay.reports.model.DMTModel;
import com.payment.arthpay.reports.model.FundRequestModel;
import com.payment.arthpay.reports.model.WalletBankModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppHandler {
    public static void initAepsTransactionReportData(AEPSReportModel model, Context context) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Txnid", model.getTxnid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Terminal Id", model.getTerminalid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Bcid", model.getAadhar()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Commission", MyUtil.formatWithRupee(context, model.getCharge())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constents.INVOICE_DATA.add(new InvoiceModel("GST", MyUtil.formatWithRupee(context, model.getGst())));
        Constents.INVOICE_DATA.add(new InvoiceModel("TDS", MyUtil.formatWithRupee(context, model.getTds())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Type", model.getType()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Aeps Type", model.getAepstype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTranstype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    static String selectedType = "";
    public static List<StateModel> parseStateList(Activity activity, JSONArray jsonArray){
        List<StateModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<StateModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static String getStatusFromObj(JSONObject obj) {
        String status = "";
        try {
            if (obj.has("status")) {
                status = obj.getString("status");
            } else if (obj.has("statuscode")) {
                status = obj.getString("statuscode");
            }else{
                status = "NA";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static void initWalletStatement(WalletBankModel model, Context context, double closing, double amount) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Txnid", model.getTxnid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Provider ID", model.getProviderId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Opening Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Closing Balance", MyUtil.formatWithRupee(context, closing)));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, amount)));
        Constents.INVOICE_DATA.add(new InvoiceModel("ST Type", model.getRtype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initAEPSWalletStatement(AEPSWalletModel model, Context context) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", "" + model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Txnid", model.getTxnid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBank()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Ref NO", model.getRefno()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Opening Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Closing Balance", MyUtil.formatWithRupee(context, model.getClosingAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getShowAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("ST Type", model.getRtype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Tran Type", model.getTranstype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("AEPS Type", model.getAepstype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initAepsFundRequestReportReportData(AEPSFundReqReportModel model, Context context) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Account", model.getAccount()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBank()));
        Constents.INVOICE_DATA.add(new InvoiceModel("IFSC", model.getIfsc()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Type", model.getType()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Request Type", model.getRequesttype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Pay Type", model.getPayType()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Payout Id", model.getPayoutid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));

        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initBillReportData(BillRechargeModel model, Context context) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Txn Id", model.getTxnid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Ref No", model.getRefno()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Commission", MyUtil.formatWithRupee(context, model.getCharge())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Profit", MyUtil.formatWithRupee(context, model.getProfit())));
        Constents.INVOICE_DATA.add(new InvoiceModel("R Type", model.getRtype()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Via", model.getVia()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTransType()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Provider Name", model.getProvidername()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initWalletFundReqReportData(FundRequestModel model, Context context) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Type", model.getType().toUpperCase()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Fundbank ID", model.getFundbankId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Pay Mode", model.getPaymode()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Actual Amount", MyUtil.formatWithRupee(context, model.getActualAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Ref Number", model.getRefNo()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Pay Data", model.getPaydate()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initDMTReportData(DMTModel model, Context context) {
        Constents.INVOICE_DATA = new ArrayList<>();
        Constents.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Txn Id", model.getTxnid()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Ref No", model.getRefno()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Charge", MyUtil.formatWithRupee(context, model.getCharge())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Profit", MyUtil.formatWithRupee(context, model.getProfit())));
        Constents.INVOICE_DATA.add(new InvoiceModel("GST", MyUtil.formatWithRupee(context, model.getGst())));
        Constents.INVOICE_DATA.add(new InvoiceModel("TDS", MyUtil.formatWithRupee(context, model.getTds())));
        Constents.INVOICE_DATA.add(new InvoiceModel("Description", model.getDescription()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Via", model.getVia()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTransType()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Bank", model.getOption3()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }


    public static List<AEPSReportModel> parseAepsTransaction(Activity activity, JSONArray jsonArray) {
        List<AEPSReportModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AEPSReportModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<WalletBankModel> parsewalletTransaction(Activity activity, JSONArray jsonArray) {
        List<WalletBankModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<WalletBankModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<AEPSWalletModel> parseAepsWalletTransaction(Activity activity, JSONArray jsonArray) {
        List<AEPSWalletModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AEPSWalletModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BillRechargeModel> parseBillRechargeData(Activity activity, JSONArray jsonArray) {
        List<BillRechargeModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<BillRechargeModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BillRechargeModel> parseRecentBillRechargeData(Activity activity, JSONArray jsonArray) {
        List<BillRechargeModel> data = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().create();
            int size = 5;
            if (jsonArray.length() < size)
                size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String operator = obj.getString("providername");
                if (isMobileProvider(operator)) {
                    BillRechargeModel model = gson.fromJson(obj.toString(), BillRechargeModel.class);
                    data.add(model);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<DistrictModel> parseDistrictList(Activity activity, JSONArray jsonArray){
        List<DistrictModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<DistrictModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    private static boolean isMobileProvider(String str) {
        switch (str) {
            case "VODAFONE":
            case "BSNL":
            case "BSNL SPECIAL":
            case "JIORECH":
            case "Idea":
            case "Airtel":
                return true;
            default:
                return false;
        }
    }

    public static List<AEPSFundReqReportModel> parseAepsFundReqTransaction(Activity activity, JSONArray jsonArray) {
        List<AEPSFundReqReportModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AEPSFundReqReportModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static List<FundRequestModel> parseWalletFundReqTransaction(Activity activity, JSONArray jsonArray) {
        List<FundRequestModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<FundRequestModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<DMTModel> parseDMTReport(Activity activity, JSONArray jsonArray) {
        List<DMTModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<DMTModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static GenericModel parse(JSONObject jsonObjectMain) {

        List<DataItem> planDataList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        String s = jsonObjectMain.toString();
        try {
            if (s.contains("Plan") && s.contains("MONTHS") && s.contains("rs")) {
                JSONObject jsonObject = jsonObjectMain.getJSONObject("data");
                JSONArray jsonArray = jsonObject.getJSONArray("Plan");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    JSONObject rsObj = obj.getJSONObject("rs");
                    Iterator<String> rsKeys = rsObj.keys();
                    while (rsKeys.hasNext()) {
                        String key = rsKeys.next();
                        String priceValues = rsObj.getString(key);
                        if (!titleList.contains(key))
                            titleList.add(key);
                        planDataList.add(new DataItem(priceValues, obj.getString("desc"), obj.getString("plan_name"), key, obj.getString("last_update")));
                    }
                }
            } else if (s.contains("recharge_short_description") || s.contains("recharge_description")) {
                JSONArray array = jsonObjectMain.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String recharge_value = obj.getString("recharge_value");
                    String recharge_validity = obj.getString("recharge_validity");
                    String recharge_short_description = obj.getString("recharge_short_description");
                    String recharge_description = obj.getString("recharge_description");
                    String last_updated_dt = obj.getString("last_updated_dt");
                    if (!titleList.contains(recharge_short_description))
                        titleList.add(recharge_short_description);
                    planDataList.add(new DataItem(recharge_value, recharge_description,
                            recharge_validity, recharge_short_description, last_updated_dt));
                }
            } else {
                JSONObject jsonObject = jsonObjectMain.getJSONObject("data");
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    titleList.add(key);
                    JSONArray array = jsonObject.getJSONArray(key);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        planDataList.add(new DataItem(obj.getString("rs"), obj.getString("desc"),
                                obj.getString("validity"), key, obj.getString("last_update")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GenericModel(planDataList, titleList);
    }

    public static boolean editTextRequiredValidation(EditText[] data) {
        boolean flag = true;
        for (EditText et : data) {
            if (et.getText().toString() == null || et.getText().toString().length() == 0) {
                et.setError("This is field is required");
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean checkStatus(String json, Activity context) {
        boolean flag = false;
        try {
            JSONObject obj = new JSONObject(json);
            String status = "";
            if (obj.has("status")) {
                status = obj.getString("status");
            } else if (obj.has("statuscode")) {
                status = obj.getString("statuscode");
            }
            if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("TXN") ||
                    status.equalsIgnoreCase("RNF") || status.equalsIgnoreCase("OTP")) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getStatus(String json) {
        String status = "";
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("status")) {
                status = obj.getString("status");
            } else if (obj.has("statuscode")) {
                status = obj.getString("statuscode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String getMessage(String json) {
        String status = "";
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("message")) {
                status = obj.getString("message");
            } else if (obj.has("msg")) {
                status = obj.getString("msg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String parseExceptionMsg(Exception e) {
        String m = "Some thing went wrong please try again after some time";
        try {
            if (e.getMessage() != null) {
                m += "\nError : " + e.getMessage();
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
        return m;
    }

    //static String selectedType = "";

}
