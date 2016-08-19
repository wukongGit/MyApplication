package com.suncheng.myapplication.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Properties;

public class TelephonyUtils {
    private static final String TAG = TelephonyUtils.class.toString();

    public static class ChinaOperator {
        public static final String CMCC = "CMCC";
        public static final String CTL = "CTL";
        public static final String UNICOM = "UNICOM";
        public static final String UNKNOWN = "Unknown";
    }

    public static String getSimOperator(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }

    public static String getOperator(Context c) {
        String sim = getSimOperator(c);
        if (BlankUtil.isBlank(sim)) {
            return ChinaOperator.UNKNOWN;
        }
        if (sim.startsWith("46003") || sim.startsWith("46005")) {
            return ChinaOperator.CTL;
        } else if (sim.startsWith("46001") || sim.startsWith("46006")) {
            return ChinaOperator.UNICOM;
        } else if (sim.startsWith("46000") || sim.startsWith("46002")
                || sim.startsWith("46007") || sim.startsWith("46020")) {
            return ChinaOperator.CMCC;
        } else {
            return ChinaOperator.UNKNOWN;
        }
    }

    public static String getPhoneNumber(Context c) {
        TelephonyManager tMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        return phoneNumber;
    }

    public static boolean hasSimCard(Context c) {
        return isSIMCardOK(c);
    }

    private static boolean isSIMCardOK(Context c) {
        TelephonyManager telMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        int s = telMgr.getSimState();

        if (s == TelephonyManager.SIM_STATE_READY) {
            return true;
        }

        boolean isDual = isDualSIM(c);

        if (isDual && s != TelephonyManager.SIM_STATE_ABSENT) {
            return true;
        }

        return false;
    }

    public static boolean isDualSIM(Context c) {
        try {
            Class<android.telephony.SmsManager> smsManagerClass = android.telephony.SmsManager.class;
            Method[] methods = smsManagerClass.getDeclaredMethods();

            int num = 0;
            int publicNum = 0;
            for (Method e : methods) {
                if (e.getName().equals("sendTextMessage")) {
                    ++num;
                    if (Modifier.isPublic(e.getModifiers())) {
                        ++publicNum;
                    }
                }

            }
            return publicNum >= 2;
        } catch (Throwable e) {
        }
        return false;
    }

    public static boolean isMtkDualSIM(Context c) {
        boolean dual = false;
        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // only works for MTK chip.
            method = TelephonyManager.class.getMethod("getSimStateGemini",
                    new Class[]{int.class});
            result_0 = method.invoke(tm, new Object[]{Integer.valueOf(0)});
            result_1 = method.invoke(tm, new Object[]{Integer.valueOf(1)});
            dual = true;
        } catch (Exception e) {
        }
        return dual;
    }

    /**
     * Get dual SIM card states for MTK chip.
     *
     * @param c
     * @param twoOuts Input, two length array, for output. The meaning is dignified
     *                by TelephonyManager.SIM_STATE_XXX.
     * @return True for it is MTK and dual SIM, false otherwise.
     */
    public static boolean getDualSIMStatesForMtk(Context c, int twoOuts[]) {
        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // only works for MTK chip.
            method = TelephonyManager.class.getMethod("getSimStateGemini",
                    new Class[]{int.class});
            result_0 = method.invoke(tm, new Object[]{Integer.valueOf(0)});
            result_1 = method.invoke(tm, new Object[]{Integer.valueOf(1)});

            if (result_0 instanceof Integer) {
                twoOuts[0] = ((Integer) result_0);
            }
            if (result_1 instanceof Integer) {
                twoOuts[1] = ((Integer) result_1);
            }
            return result_0 instanceof Integer && result_1 instanceof Integer;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获得imsi
     *
     * @param context context
     * @return imsi
     */
    public static String getIMSI(Context context) {
        String imsi = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            imsi = telephonyManager.getSubscriberId();
        }
        return imsi == null ? "" : imsi;
    }

    /**
     * 获得imei
     *
     * @param c context
     * @return imei
     */
    public static String getImei(Context c) {
        try {
            TelephonyManager manager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = manager.getDeviceId();
            if (!BlankUtil.isBlank(imei) && !imei.matches("0+") && !imei.equals("004999010640000"))
                return imei;
        } catch (Exception e) {
        }
        return "";

    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() throws IOException {
        InputStream stream = null;
        try {
            Properties properties = new Properties();
            stream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            properties.load(stream);
            return properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final Exception e) {
            return false;
        } finally {
            if (stream != null)
                stream.close();
        }
    }

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 获得 mac 地址
     * @param c context
     * @return mac地址字符串 或者 "YY_FAKE_MAC"
     */
    public static String getMac(Context c){
        try {
            WifiManager wifiMgr = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
            if (wifiMgr != null){
                WifiInfo connectionInfo = wifiMgr.getConnectionInfo();
                if (connectionInfo != null) {
                    String macAddress = connectionInfo.getMacAddress();
                    if (macAddress != null){
                        try {
                            String macStr = URLEncoder.encode(macAddress, "UTF-8");
                            if (macStr != null){
                                return macStr;
                            }
                        } catch (Throwable e1) {
                        }

                    }
                }
            }
        } catch (Throwable throwable) {
        }
        return "YY_FAKE_MAC";
    }
}
