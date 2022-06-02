package com.payment.arthpay.utill;

import android.content.Context;
import android.content.SharedPreferences;

import com.payment.arthpay.security.EncryptionUtils;

public class SharedPrefs {

    public static final String NEWS = "news";
    private static SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "PREFS";

    public static String ABOUT = "appAbout";
    public static String LANGUAGE_TYPE = "set_lang";
    public static String Mobile = "set_lang";
    public static final String FIREBASE_TOKEN = "firebase_token";
    public static String USER_ID = "user_id";
    public static String USER_NAME = "user_name";
    public static String USER_CONTACT = "user_contact";
    public static String USER_EMAIL = "user_email";
    public static String OTP_VERIFY = "otp_verify";
    public static String MAIN_WALLET = "main_wallet";
    public static String NSDL_WALLET = "nsdl_wallet";
    public static String APES_BALANCE = "apes_balance";
    public static String LOCKED_AMOUNT = "locked_amount";
    public static String ROLE_ID = "role_id";
    public static String PARENT_ID = "parent_id";
    public static String COMPANY_ID = "company_id";
    public static String STATUS = "status";
    public static String ADDRESS = "address";
    public static String SHOP_NAME = "shop_name";
    public static String GSTIN = "gstin";
    public static String CITY = "city";
    public static String STATE = "state";
    public static String PINCODE = "pincode";
    public static String AADHAR_CARD = "aadhar_card";
    public static String PANCARD = "pan_card";
    public static String KYC = "kyc";

    public static String ACCOUNT = "account";
    public static String BANK = "bank";
    public static String IFSC = "ifsc";

    public static String ACCOUNT2 = "account2";
    public static String BANK2 = "bank2";
    public static String IFSC2 = "ifsc2";

    public static String ACCOUNT3 = "account3";
    public static String BANK3 = "bank3";
    public static String IFSC3 = "ifsc3";

    public static String APP_TOKEN = "app_token";
    public static String TOKEN_AMOUNT = "token_amount";
    public static String UTI_ID = "uti_id";
    public static String MICRO_ATM_BALANCE = "MATM_BALANCE";

    // ROLE OBJECT
    public static String ROLE_NAME = "role_name";
    public static String ROLE_SLUG = "role_slug";

    // COMPANY OBJECT
    public static String COMPANY_NAME = "company_name";
    public static String SHORT_NAME = "short_name";
    public static String WEBSITE = "website";
    public static String LOGO = "logo";

    public static String LOGIN_ID = "login_id";
    public static String PASSWORD = "password";
    public static String BANNERARRAY = "bannerArray";
    public static String SUPPORT_EMAIL = "support_email";
    public static String SUPPORT_NUMBER = "support_number";
    public static String SUPPORT_ADDRESS = "support_address";
    public static String FILTER_DATE_FROM = "from_filter_date";
    public static String FILTER_DATE_TO = "to_filter_date";
    public static String REPORT_SEARCH_TEXT = "search_text";
    public static String FILTER_STATUS = "filter_status";


    public SharedPrefs() {
        super();
    }

    public static void setValue(Context context, String PREF_KEY, String PREF_VALUE) {
        String encryptedValue = EncryptionUtils.encrypt(context, PREF_VALUE);
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit(); //2
        editor.putString(PREF_KEY, encryptedValue); //3
        editor.apply(); //4
    }

    public static String getValue(Context context, String PREF_KEY) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //Print.P(PREF_KEY + " Encrypted Value : " + sharedPreferences.getString(PREF_KEY, null));
        //Print.P(PREF_KEY + " Decrypted Value : " + EncryptionUtils.decrypt(context, sharedPreferences.getString(PREF_KEY, null)));
        return EncryptionUtils.decrypt(context, sharedPreferences.getString(PREF_KEY, null));
    }

    public static void clearAllPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}
