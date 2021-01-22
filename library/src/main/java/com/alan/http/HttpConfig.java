package com.alan.http;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * @author Alan
 * 时 间：2020-03-27
 * 简 述：<功能简述>
 */
public class HttpConfig {

    /**
     * mdiatype 这个需要和服务端保持一致
     */
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_APPLICATION = MediaType.parse("application/x-www-form-urlencoded");

    public static IHttpConfig iHttpConfig;

    public static void regist(IHttpConfig iHttpConfig) {
        HttpConfig.iHttpConfig = iHttpConfig;
    }

    public static String getHost() {
        return iHttpConfig == null ? "" : iHttpConfig.host();
    }

    public static OkHttpClient getOkHttpClient() {
        if (null == HttpConfig.iHttpConfig || HttpConfig.iHttpConfig.getOkHttpClient() == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS);
            builder.writeTimeout(1000, TimeUnit.MILLISECONDS);
            builder.readTimeout(10 * 1000, TimeUnit.MILLISECONDS);
            return builder.build();
        }
        return HttpConfig.iHttpConfig.getOkHttpClient();
    }

    public static boolean isPrintLog() {
        return null != iHttpConfig && iHttpConfig.isPrintLog();
    }

    public static boolean isEncoding() {
        return null == iHttpConfig || iHttpConfig.isEncoding();
    }

    public static MediaType getMediaType() {
        return null == iHttpConfig || iHttpConfig.getMediaType() == null ? MEDIA_TYPE_JSON : iHttpConfig.getMediaType();
    }

    public static HashMap<String, String> getCommonParams() {
        return null == iHttpConfig || null == iHttpConfig.getCommonParams() ? new HashMap<String, String>() : iHttpConfig.getCommonParams();
    }

    public static HashMap<String, String> getHeadParams() {
        return null == iHttpConfig || null == iHttpConfig.getHeadParams() ? new HashMap<String, String>() : iHttpConfig.getHeadParams();
    }

    public static Handler handler() {
        return null == iHttpConfig ? null : iHttpConfig.handler();
    }

    public static IParseStrategy getParseStrategy() {
        return null == iHttpConfig ? null : iHttpConfig.getParseStrategy();
    }

    public static Context getContext() {
        return iHttpConfig.getContent();
    }

}
