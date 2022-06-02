package com.payment.arthpay.RechargePlans;

import com.payment.arthpay.RechargePlans.model.DataItem;
import com.payment.arthpay.RechargePlans.model.GenericModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Handler {
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
                        planDataList.add(new DataItem(priceValues, obj.getString("desc"), obj.getString("plan_name"),
                                key, obj.getString("last_update")));
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
                    planDataList.add(new DataItem(recharge_value, recharge_description, recharge_validity, recharge_short_description, last_updated_dt));
                }
            } else {
                try {
                    JSONObject jsonObject = jsonObjectMain.getJSONObject("data");
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        titleList.add(key);
                        JSONArray array = jsonObject.getJSONArray(key);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String rs = "";
                            String desc = "";
                            String validity = "";
                            String last_update = "";
                            rs = obj.getString("rs");
                            if (obj.has("desc"))
                                desc = obj.getString("desc");
                            if (obj.has("validity"))
                                validity = obj.getString("validity");
                            if (obj.has("last_update"))
                                last_update = obj.getString("last_update");
                            planDataList.add(new DataItem(rs, desc, validity, key, last_update));
                        }
                    }
                } catch (Exception e) {
                    JSONArray array = jsonObjectMain.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String rs = "";
                        String desc = "";
                        String validity = "";
                        String last_update = "";
                        rs = obj.getString("rs");
                        if (obj.has("desc"))
                            desc = obj.getString("desc");
                        if (obj.has("validity"))
                            validity = obj.getString("validity");
                        if (obj.has("last_update"))
                            last_update = obj.getString("last_update");
                        planDataList.add(new DataItem(rs, desc, validity, "", last_update));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GenericModel(planDataList, titleList);
    }

}
