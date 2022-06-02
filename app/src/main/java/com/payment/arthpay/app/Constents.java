package com.payment.arthpay.app;

import com.payment.arthpay.bppsServices.model.BillPayModel;
import com.payment.arthpay.commission.gsonModel.CommissionModel;
import com.payment.arthpay.reports.invoice.model.InvoiceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Constents {

    public static final double MILLISECOND_CONVERTER = 2.777777777777778e-7; //To conver miliseconds in to hourse
    public static final String TRUE = "true";
    public static final int IMAGE_HIEGHT = 400;
    public static final int IMAGE_WIDTH = 400;

    public static final int MIN_MEMORY_FOR_IMAGE_CAPTURE = 50;
    public static final int MIN_MEMORY_FOR_APP = 150;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SHOWING_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat COMMON_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MMMM");
    public static class URL {
        public static String baseUrl = "https://arthpay.arthdigital.net/";
        //public static String baseUrl = "http://adspayssbi.com/";1
        public static final String DMT_TRANSACTION = baseUrl + "api/android/dmt/transaction";
        public static final String REPORT_CHECK_STATUS = baseUrl + "api/android/transaction/status";
        public static final String REPORT = baseUrl + "api/android/transaction";
        public static final String COMMISION = baseUrl + "api/android/comission";
        public static final String CREATE_MEMBER = baseUrl + "api/android/member/create";
        public static final String ALL_MEMBERS = baseUrl + "api/android/member/list";
        public static final String FUND_REQUEST = baseUrl + "api/android/fundrequest";
        public static final String AEPS_REGISTRATION = baseUrl + "api/android/aepsregistration";
        public static final String STATE_LIST = baseUrl + "api/android/GetState";
        public static final String DISTRICT_LIST = baseUrl + "api/android/GetDistrictByState";
    }

    public static String isBack = "1";
    public static String MOBILE_ID;
    public static int GLOBAL_POSITION = -1;
    public static BillPayModel BILL_MODEL;
    public static boolean IS_TPIN_ACTIVE = true;
    public static List<InvoiceModel> INVOICE_DATA;
    public static boolean IS_RELOAD_REQUEST = false;
    public static ArrayList<CommissionModel> commissionDataList;
}
