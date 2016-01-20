package org.flakor.jpptest;

/**
 * Created by xusq on 2016/1/20.
 */
public class JppTest {
    //#if def{sdk_debugable}
    public static final String SDK_VERSION_NAME = "2.0.0";
    public static final int SDK_VERSION_CODE = 18;
    public static final String SDK_TAG = "SMSSDK";
    public static final String VCODE_TAG = "SMSSDK_VCODE";
    public static String SDK_INIT_URL = "http://init.sms.mob.com/sdk/init";
    //public static String SDK_INIT_URL = "http://upc1299.uz.local:8080/sdk/init";
    //#else
    //#=public static final String SDK_VERSION_NAME = "def{sdk.version.name}";
    //#=public static final int SDK_VERSION_CODE = def{sdk.version.int};
    //#=public static final String SDK_TAG = "SMSSDK";
    //#=public static final String VCODE_TAG = "SMSSDK_VCODE";
    //#=public static String SDK_INIT_URL = "http://init.sms.mob.com/sdk/init";
    //#endif

}
