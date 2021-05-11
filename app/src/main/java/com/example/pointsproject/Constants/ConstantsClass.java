package com.example.pointsproject.Constants;

public class ConstantsClass {
    public static String USERS_BASE_URL = "http://192.168.43.221/AndroidPointsApp/";
    public static final String CHANNEL_ID = "2";
    public static final int NOTIFICATION_ID = 4;

    //payment constants
    public static final int CONNECT_TIMEOUT = 60 * 1000;

    public static final int READ_TIMEOUT = 60 * 1000;

    public static final int WRITE_TIMEOUT = 60 * 1000;

    public static final String BASE_URL = "https://sandbox.safaricom.co.ke/";

    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
    public static final String PARTYB = "174379"; //same as business shortcode above
    public static final String CALLBACKURL = "http://mpesa-requestbin.herokuapp.com/rjiu6vrj";
}
