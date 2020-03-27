package com.alan.http;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Mouse on 2018/10/15.
 */
public class LogUtil {

    private static final String TAG = "xm_http_tag";


    private static String getTag() {
        return TAG;
    }

    public static void i(String msg) {
        println(Log.INFO, getTag(), msg);
    }

    public static void e(String msg) {
        println(Log.ERROR, getTag(), msg);
    }

    public static void e(String tag, String msg) {
        println(Log.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        println(tag, msg, e);
    }

    public static void w(String msg) {
        println(Log.WARN, getTag(), msg);
    }

    public static void w(String tag, String msg) {
        println(Log.WARN, tag, msg);
    }

    public static void i(String tag, String msg) {
        println(Log.INFO, tag, msg);
    }

    public static void v(String tag, String s) {
        println(Log.VERBOSE, tag, s);
    }

    public static void d(String tag, String s) {
        println(Log.DEBUG, tag, s);
    }

    public static void d(String s) {
        println(Log.DEBUG, getTag(), s);
    }

    private static void println(int priority, String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg) || !isDebug())
            return;

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String info = "";
        if (trace != null && trace.length >= 4) {
            try {
                StackTraceElement element = trace[4];
                info = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
            } catch (NoClassDefFoundError e1) {
            }
        }
//        Log.println(priority, tag, info + msg);
        printlnLog(priority, info, tag, msg);
    }

    private static void println(String tag, String msg, Throwable e) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg) || !isDebug())
            return;

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String info = "";
        if (trace != null && trace.length >= 4) {
            try {
                StackTraceElement element = trace[4];
                info = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
            } catch (NoClassDefFoundError e1) {
            }
        }
        Log.e(tag, info + msg, e);
    }

    private static void printlnLog(int priority, String info, String tag, String msg) {
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.println(priority, tag, info + msg);
        } else {
            int segmentIndex = 1;
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.substring(segmentSize, msg.length());
                Log.println(priority, tag, info + "segmentIndex:" + segmentIndex++ + ":" + logContent);
            }
            if (!TextUtils.isEmpty(msg))
                Log.println(priority, tag, info + "segmentIndex:" + segmentIndex + ":" + msg);// 打印剩余日志
        }
    }

    public static boolean isDebug() {
        return null!=HttpConfig.iHttpConfig&&HttpConfig.iHttpConfig.isPrintLog();
//        return ResourceTools.getResourceBool(R.bool.is_debug);
    }

    public static void error(Throwable e) {
        LogUtil.e(TAG, Log.getStackTraceString(e));
    }
}
