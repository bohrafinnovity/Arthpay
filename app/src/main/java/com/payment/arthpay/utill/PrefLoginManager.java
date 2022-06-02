package com.payment.arthpay.utill;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefLoginManager {
    // Shared preferences file name
    private static final String PREF_NAME = "ebanker_login_pref";
    private static final String USER_LOGIN_RES = "user_login_res";


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    public PrefLoginManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFarmerLoginRes(String a) {
        editor.putString(USER_LOGIN_RES, a);
        editor.commit();
    }

    public String getFarmerLoginRes() {
        return pref.getString(USER_LOGIN_RES, "");
    }

    public void clearFarmerLoginRes() {
        editor.putString(USER_LOGIN_RES, "");
        editor.commit();
    }
}
